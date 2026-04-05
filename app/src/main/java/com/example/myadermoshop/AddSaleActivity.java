package com.example.myadermoshop;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.itextpdf.kernel.pdf.tagging.StandardRoles;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;
import java.util.Set;
import java.util.UUID;

public class AddSaleActivity extends AppCompatActivity {

    private DatabaseHelper dbHelper;
    private Set<String> instanceIDs;
    private ProductOnCartAdapter productAdapter;
    private ArrayList<Product> productList;
    private HashMap<String, Double> productQuantities;
    private RecyclerView recyclerViewCart;
    private TextView textViewTotalPrice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        dbHelper = new DatabaseHelper(this);

        // ── LinearLayout rows instead of FloatingActionButtons ──
        LinearLayout fabScanBarcode   = findViewById(R.id.fabScanBarcode);
        LinearLayout fabSelectProduct = findViewById(R.id.fabSelectProduct);

        recyclerViewCart    = findViewById(R.id.recyclerViewCart);
        textViewTotalPrice  = findViewById(R.id.textViewTotalPrice);
        Button buttonCompleteSale = findViewById(R.id.buttonCompleteSale);

        instanceIDs       = new HashSet<>();
        productQuantities = new HashMap<>();
        productList       = new ArrayList<>();

        recyclerViewCart.setLayoutManager(new LinearLayoutManager(this));
        productAdapter = new ProductOnCartAdapter(
                productList, this, productQuantities, instanceIDs);
        recyclerViewCart.setAdapter(productAdapter);

        fabScanBarcode.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(this, BarcodeScanActivity.class), 1));

        fabSelectProduct.setOnClickListener(v ->
                startActivityForResult(
                        new Intent(this, ProductSelectionActivity.class), 2));

        buttonCompleteSale.setOnClickListener(v -> showPaymentTypeDialog());

        calculateTotalPrice();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data == null || data.getStringExtra("scanned_data") == null) return;
            addProductWithInstanceToCart(data.getStringExtra("scanned_data"));
            return;
        }

        if (requestCode == 2 && resultCode == RESULT_OK) {
            if (data == null || data.getStringExtra("selected_product_id") == null) return;
            addProductWithoutInstanceToCart(
                    data.getStringExtra("selected_product_id"),
                    data.getDoubleExtra("selected_product_quantity", 1.0d));
        }
    }

    private void addProductWithInstanceToCart(String instanceID) {
        Log.d("AddSaleActivity", "Ajout du produit avec ID d'instance: " + instanceID);
        if (instanceIDs.contains(instanceID) || dbHelper.isInstanceSold(instanceID)) {
            showAlert("Instance invalide",
                    "Le produit avec l'ID d'instance " + instanceID
                            + " est soit déjà dans le panier, soit déjà vendu.");
            return;
        }
        ProductInfo info = dbHelper.getProductInfoByInstance(instanceID);
        if (info != null) {
            instanceIDs.add(instanceID);
            Product product = new Product();
            product.setProductID(instanceID);
            product.setProductName(info.getProductName());
            product.setProductPhotoName(info.getProductPhotoName());
            product.setProductManufacture(info.getProductManufacture());
            product.setProductPrice(info.getProductPrice());
            product.setPricecaseID(info.getPricecaseID());
            productList.add(product);
            productAdapter.notifyDataSetChanged();
            calculateTotalPrice();
        } else {
            showAlert("Produit non trouvé",
                    "Le produit avec l'ID d'instance " + instanceID
                            + " n'a pas été trouvé dans la base de données.");
        }
    }

    private void addProductWithoutInstanceToCart(String productID, double quantity) {
        Log.d("AddSaleActivity", "Ajout du produit sans ID d'instance: " + productID);
        Product product = dbHelper.getProductObjectDetailsById(productID);
        if (product == null) {
            showAlert("Produit non trouvé",
                    "Le produit avec l'ID " + productID
                            + " n'a pas été trouvé dans la base de données.");
            return;
        }
        if (productQuantities.containsKey(productID)) {
            productQuantities.put(productID,
                    productQuantities.get(productID) + quantity);
        } else {
            productQuantities.put(productID, quantity);
        }
        productList.add(product);
        Log.d("AddSaleActivity", "Produit ajouté sans instance: "
                + productID + " avec quantité: " + quantity);
        productAdapter.notifyDataSetChanged();
        calculateTotalPrice();
    }

    private void showPaymentTypeDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Sélectionner le mode de paiement");

        Cursor cursor = dbHelper.getAllPaymentTypes();
        ArrayList<String> paymentTypes = new ArrayList<>();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int col = cursor.getColumnIndex(DatabaseHelper.COLUMN_PAYMENT_METHOD);
                if (col == -1) {
                    Log.d("AddSaleActivity", "Colonne 'paymentMethod' non trouvée");
                } else {
                    paymentTypes.add(cursor.getString(col));
                }
            } while (cursor.moveToNext());
            cursor.close();
        } else {
            Log.d("AddSaleActivity",
                    "Échec de la récupération des types de paiement ou curseur vide");
        }

        final String[] typesArray = paymentTypes.toArray(new String[0]);
        builder.setItems(typesArray, (dialog, which) -> completeSale(typesArray[which]));
        builder.setNegativeButton("Annuler", (dialog, which) -> dialog.cancel());
        builder.show();
    }

    private void completeSale(String paymentMethod) {
        String employeeID  = getSharedPreferences("MyApp", 0).getString("employeeID", "");
        String cartID      = generateUniqueCartID();
        String timestamp   = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                Locale.getDefault()).format(new Date());
        String paymentID   = generateUniquePaymentID();

        dbHelper.addCart(cartID, timestamp, employeeID);

        // ── Items with instance (scanned) ──
        for (String instanceID : instanceIDs) {
            dbHelper.addCartItemWithInstance(new CartItemWithInstance(
                    generateUniqueCartItemID(),
                    cartID,
                    instanceID,
                    dbHelper.getPricecaseIDByInstance(instanceID)));
            dbHelper.updateInstanceState(instanceID, "sold");
        }

        // ── Items without instance (selected) ──
        for (Iterator<String> it = productQuantities.keySet().iterator(); it.hasNext(); ) {
            String pid        = it.next();
            double qty        = productQuantities.get(pid);
            String cartItemID = generateUniqueCartItemID();
            int pricecaseID   = dbHelper.getPricecaseIDByProductID(pid);
            Log.d("AddSaleActivity", "ID du prix pour le produit ID " + pid + ": " + pricecaseID);
            dbHelper.addCartItemWithoutInstance(
                    new CartItemWithoutInstance(cartItemID, qty, cartID, pid, pricecaseID));
            Log.d("AddSaleActivity", "Produit sans instance sauvegardé: "
                    + pid + " avec quantité: " + qty);
        }

        dbHelper.addPayment(new Payment(
                paymentID, cartID,
                dbHelper.getPaymentTypeID(paymentMethod),
                employeeID));

        new AlertDialog.Builder(this)
                .setTitle("Vente terminée")
                .setMessage("La vente a été réalisée avec succès.")
                .setPositiveButton("OK", (dialog, which) -> {
                    resetCart();
                    dialog.dismiss();
                })
                .show();
    }

    private void resetCart() {
        productList.clear();
        instanceIDs.clear();
        productQuantities.clear();
        productAdapter.notifyDataSetChanged();
        calculateTotalPrice();
    }

    public void calculateTotalPrice() {
        double total = 0.0d;
        for (Product p : productList) {
            double qty = productQuantities.containsKey(p.getProductID())
                    ? productQuantities.get(p.getProductID()) : 1.0d;
            total += p.getProductPrice() * qty;
        }
        textViewTotalPrice.setText(
                String.format(Locale.getDefault(), "%.2f BIF", total));
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(this)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private String generateUniqueCartID() {
        return "CT" + new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date())
                + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }

    private String generateUniqueCartItemID() {
        return generateUniqueID("CI");
    }

    private String generateUniquePaymentID() {
        return generateUniqueID(StandardRoles.P);
    }

    private String generateUniqueID(String prefix) {
        return prefix + new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date())
                + "_" + UUID.randomUUID().toString().replace("-", "").substring(0, 8);
    }
}