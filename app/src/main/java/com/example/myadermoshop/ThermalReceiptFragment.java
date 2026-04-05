package com.example.myadermoshop;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public class ThermalReceiptFragment extends Fragment {

    private static final UUID   PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG          = "ThermalReceiptFragment";

    // ── Views ─────────────────────────────────────────────────────────────────
    private TextView     textViewCartID;
    private TextView     textViewTime;
    private TextView     textViewCurrency;
    private TextView     textViewAmount;
    private TextView     textViewEmployeeID;
    private TextView     textViewEmployeeName;
    private TextView     textViewEmployeeEmail;
    private LinearLayout linearLayoutItems;
    private Button       buttonPrint;

    // ── State ─────────────────────────────────────────────────────────────────
    private String          employeeTel;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private OutputStream    outputStream;
    private DatabaseHelper  databaseHelper;
    private final Handler   handler = new Handler(Looper.getMainLooper());

    // ── Lifecycle ─────────────────────────────────────────────────────────────

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_thermal_receipt, container, false);

        textViewCartID       = root.findViewById(R.id.textViewCartID);
        textViewTime         = root.findViewById(R.id.textViewTime);
        textViewCurrency     = root.findViewById(R.id.textViewCurrency);
        textViewAmount       = root.findViewById(R.id.textViewAmount);
        textViewEmployeeID   = root.findViewById(R.id.textViewEmployeeID);
        textViewEmployeeName = root.findViewById(R.id.textViewEmployeeName);
        textViewEmployeeEmail= root.findViewById(R.id.textViewEmployeeEmail);
        linearLayoutItems    = root.findViewById(R.id.linearLayoutItems);
        buttonPrint          = root.findViewById(R.id.buttonPrint);

        databaseHelper = new DatabaseHelper(getContext());

        loadCartData();

        buttonPrint.setOnClickListener(v -> {
            try { closeConnection(); } catch (IOException ignored) {}
            Toast.makeText(getContext(), "Bouton d'impression cliqué", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "Bouton d'impression cliqué");
            showPairedDevicesDialog();
        });

        return root;
    }

    // ── Cart data ─────────────────────────────────────────────────────────────

    private void loadCartData() {
        if (getArguments() == null) {
            Log.e(TAG, "Aucun argument trouvé.");
            return;
        }
        String cartID = getArguments().getString(DatabaseHelper.COLUMN_CART_ID);
        if (cartID == null) {
            Log.e(TAG, "Le cartID est nul.");
            return;
        }
        Cart cart = databaseHelper.getCartByID(cartID);
        if (cart == null) {
            Log.e(TAG, "Le panier est nul.");
            return;
        }

        List<CartItem> items = databaseHelper.getCartItemsByCartID(cartID);
        double total = databaseHelper.calculateTotalAmount(items);
        Employee employee = databaseHelper.getEmployeeByID(cart.getEmployeeID());

        textViewCartID.setText(cart.getCartID());
        textViewTime.setText(cart.getTimestamp());
        textViewCurrency.setText("BIF");
        textViewAmount.setText(String.valueOf(total));

        if (employee != null) {
            textViewEmployeeID.setText(employee.getEmployeeID());
            textViewEmployeeName.setText(
                    employee.getEmployeeFirstName() + " " + employee.getEmployeeLastName());
            textViewEmployeeEmail.setText(employee.getEmployeeEmail());
            employeeTel = employee.getEmployeeTel();
        } else {
            textViewEmployeeID.setText("N/A");
            textViewEmployeeName.setText("N/A");
            textViewEmployeeEmail.setText("N/A");
            employeeTel = "N/A";
        }

        // Build item rows programmatically (used both for display and printing)
        linearLayoutItems.removeAllViews();
        for (CartItem item : items) {
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);

            TextView tvName  = makeItemTextView(item.getProductName());
            TextView tvQty   = makeItemTextView(String.valueOf(item.getQuantity()));
            TextView tvPrice = makeItemTextView(String.valueOf(item.getUnitPrice()));
            TextView tvTotal = makeItemTextView(
                    String.valueOf(item.getQuantity() * item.getUnitPrice()));

            row.addView(tvName);
            row.addView(tvQty);
            row.addView(tvPrice);
            row.addView(tvTotal);
            linearLayoutItems.addView(row);
        }
    }

    private TextView makeItemTextView(String text) {
        TextView tv = new TextView(getContext());
        tv.setText(text);
        tv.setLayoutParams(new LinearLayout.LayoutParams(0,
                LinearLayout.LayoutParams.WRAP_CONTENT, 1.0f));
        return tv;
    }

    // ── Bluetooth ─────────────────────────────────────────────────────────────

    private void showPairedDevicesDialog() {
        BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
        if (adapter == null || !adapter.isEnabled()) {
            showAlert("Erreur", "Le Bluetooth n'est pas activé.");
            return;
        }
        Set<BluetoothDevice> bonded = adapter.getBondedDevices();
        List<String>         names  = new ArrayList<>();
        List<BluetoothDevice> devices = new ArrayList<>();
        for (BluetoothDevice device : bonded) {
            names.add(device.getName());
            devices.add(device);
        }
        new AlertDialog.Builder(getContext())
                .setTitle("Sélectionner l'imprimante")
                .setAdapter(new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_list_item_1, names),
                        (dialog, which) -> {
                            bluetoothDevice = devices.get(which);
                            connectToPrinterAndPrint();
                        })
                .setNegativeButton("Annuler", (dialog, which) -> dialog.dismiss())
                .create()
                .show();
    }

    private void connectToPrinterAndPrint() {
        new Thread(() -> {
            try {
                if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
                    closeConnection();
                }
                Log.d(TAG, "Création d'un nouveau socket RFcomm...");
                bluetoothSocket = bluetoothDevice
                        .createRfcommSocketToServiceRecord(PRINTER_UUID);
                bluetoothSocket.connect();
                outputStream = bluetoothSocket.getOutputStream();
                handler.post(() -> Toast.makeText(
                        getContext(), "Connecté à l'imprimante",
                        Toast.LENGTH_SHORT).show());
                printReceipt();
            } catch (IOException e) {
                Log.e(TAG, "Erreur de connexion à l'imprimante", e);
                handler.post(() -> showAlert("Erreur",
                        "Échec de la connexion à l'imprimante"));
            }
        }).start();
    }

    // ── Printing ──────────────────────────────────────────────────────────────

    private void printReceipt() {
        try {
            Log.d(TAG, "Impression du reçu...");
            StringBuilder sb = new StringBuilder();
            sb.append("ADERMO\n");
            sb.append("LOCALISATION: A 400 METRES DU ROND POINT RN1\n");
            sb.append("EMAIL: adermoburundi@gmail.com\n");
            sb.append("NIF: 4002736603\n");
            sb.append("NUMERO RC: 0059270/24\n");
            sb.append("==============================\n");
            sb.append("Panier :\n");
            sb.append("ID: ").append(textViewCartID.getText()).append("\n");
            sb.append("Heure: ").append(textViewTime.getText()).append("\n");
            sb.append("Devise: ").append(textViewCurrency.getText()).append("\n");
            sb.append("Montant Total: ").append(textViewAmount.getText()).append("\n");
            sb.append("==============================\n");
            sb.append(String.format("%-10s %-4s %-7s %-8s\n",
                    "ARTICLE", "QTE", "UNITE", "TOTAL"));
            sb.append("------------------------------\n");

            for (int i = 0; i < linearLayoutItems.getChildCount(); i++) {
                LinearLayout row = (LinearLayout) linearLayoutItems.getChildAt(i);
                TextView tvName  = (TextView) row.getChildAt(0);
                TextView tvQty   = (TextView) row.getChildAt(1);
                TextView tvPrice = (TextView) row.getChildAt(2);
                TextView tvTotal = (TextView) row.getChildAt(3);
                if (tvName != null && tvQty != null && tvPrice != null && tvTotal != null) {
                    sb.append(String.format("%-10s %-4s %-7s %-8s\n",
                            truncate(tvName.getText().toString(), 10),
                            tvQty.getText(),
                            tvPrice.getText(),
                            tvTotal.getText()));
                }
            }

            sb.append("==============================\n");
            sb.append("Merci pour votre achat !\n\n");
            sb.append("Employee :\n");
            sb.append("Nom: ").append(textViewEmployeeName.getText()).append("\n");
            sb.append("Tel: ").append(employeeTel).append("\n");
            sb.append("==============================\n\n\n\n");

            Log.d(TAG, "Contenu du reçu:\n" + sb);
            outputStream.write(sb.toString().getBytes());
            outputStream.flush();

            handler.post(() -> {
                Toast.makeText(getContext(), "Reçu imprimé",
                        Toast.LENGTH_SHORT).show();
                Log.d(TAG, "Reçu imprimé");
            });
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de l'impression du reçu", e);
            handler.post(() -> showAlert("Erreur",
                    "Échec de l'impression du reçu"));
        } finally {
            try { closeConnection(); } catch (IOException ignored) {}
        }
    }

    // ── Helpers ───────────────────────────────────────────────────────────────

    private void closeConnection() throws IOException {
        try {
            if (outputStream != null) {
                outputStream.close();
                outputStream = null;
            }
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
                bluetoothSocket = null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de la fermeture de la connexion", e);
        }
    }

    private void showAlert(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                .show();
    }

    private String truncate(String text, int maxLen) {
        return text.length() > maxLen ? text.substring(0, maxLen - 1) + "." : text;
    }
}