package com.example.myadermoshop;

import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
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

/* loaded from: classes.dex */
public class ThermalReceiptFragment extends Fragment {
    private static final UUID PRINTER_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final String TAG = "ThermalReceiptFragment";
    private String EmployeeTel;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private Button buttonPrint;
    private DatabaseHelper databaseHelper;
    private Handler handler;
    private LinearLayout linearLayoutItems;
    private OutputStream outputStream;
    private TextView textViewAmount;
    private TextView textViewCartID;
    private TextView textViewCurrency;
    private TextView textViewEmployeeEmail;
    private TextView textViewEmployeeID;
    private TextView textViewEmployeeName;
    private TextView textViewTime;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_thermal_receipt, viewGroup, false);
        this.textViewCartID = viewInflate.findViewById(R.id.textViewCartID);
        this.textViewTime = viewInflate.findViewById(R.id.textViewTime);
        this.textViewCurrency = viewInflate.findViewById(R.id.textViewCurrency);
        this.textViewAmount = viewInflate.findViewById(R.id.textViewAmount);
        this.textViewEmployeeID = viewInflate.findViewById(R.id.textViewEmployeeID);
        this.textViewEmployeeName = viewInflate.findViewById(R.id.textViewEmployeeName);
        this.textViewEmployeeEmail = viewInflate.findViewById(R.id.textViewEmployeeEmail);
        this.linearLayoutItems = viewInflate.findViewById(R.id.linearLayoutItems);
        this.buttonPrint = viewInflate.findViewById(R.id.buttonPrint);
        this.handler = new Handler();
        this.databaseHelper = new DatabaseHelper(getContext());
        if (getArguments() != null) {
            String string = getArguments().getString(DatabaseHelper.COLUMN_CART_ID);
            if (string != null) {
                Cart cartByID = this.databaseHelper.getCartByID(string);
                if (cartByID != null) {
                    List<CartItem> cartItemsByCartID = this.databaseHelper.getCartItemsByCartID(string);
                    double dCalculateTotalAmount = this.databaseHelper.calculateTotalAmount(cartItemsByCartID);
                    Employee employeeByID = this.databaseHelper.getEmployeeByID(cartByID.getEmployeeID());
                    this.textViewCartID.setText(cartByID.getCartID());
                    this.textViewTime.setText(cartByID.getTimestamp());
                    this.textViewCurrency.setText("BIF");
                    this.textViewAmount.setText(String.valueOf(dCalculateTotalAmount));
                    if (employeeByID != null) {
                        this.textViewEmployeeID.setText(employeeByID.getEmployeeID());
                        this.textViewEmployeeName.setText(employeeByID.getEmployeeFirstName() + " " + employeeByID.getEmployeeLastName());
                        this.textViewEmployeeEmail.setText(employeeByID.getEmployeeEmail());
                        this.EmployeeTel = employeeByID.getEmployeeTel();
                    } else {
                        this.textViewEmployeeID.setText("N/A");
                        this.textViewEmployeeName.setText("N/A");
                        this.textViewEmployeeEmail.setText("N/A");
                        this.EmployeeTel = "N/A";
                    }
                    this.linearLayoutItems.removeAllViews();
                    for (CartItem cartItem : cartItemsByCartID) {
                        LinearLayout linearLayout = new LinearLayout(getContext());
                        linearLayout.setOrientation(0);
                        TextView textView = new TextView(getContext());
                        textView.setText(cartItem.getProductName());
                        textView.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
                        linearLayout.addView(textView);
                        TextView textView2 = new TextView(getContext());
                        textView2.setText(String.valueOf(cartItem.getQuantity()));
                        textView2.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
                        linearLayout.addView(textView2);
                        TextView textView3 = new TextView(getContext());
                        textView3.setText(String.valueOf(cartItem.getUnitPrice()));
                        textView3.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
                        linearLayout.addView(textView3);
                        TextView textView4 = new TextView(getContext());
                        textView4.setText(String.valueOf(cartItem.getQuantity() * cartItem.getUnitPrice()));
                        textView4.setLayoutParams(new LinearLayout.LayoutParams(0, -2, 1.0f));
                        linearLayout.addView(textView4);
                        this.linearLayoutItems.addView(linearLayout);
                    }
                } else {
                    Log.e(TAG, "Le panier est nul.");
                }
            } else {
                Log.e(TAG, "Le cartID est nul.");
            }
        } else {
            Log.e(TAG, "Aucun argument trouvé.");
        }
        this.buttonPrint.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.ThermalReceiptFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) throws IOException {
                this.f$0.m134x587d7000(view);
            }
        });
        return viewInflate;
    }

    /* renamed from: lambda$onCreateView$0$com-example-myadermoshop-ThermalReceiptFragment, reason: not valid java name */
    /* synthetic */ void m134x587d7000(View view) throws IOException {
        closeConnection();
        Toast.makeText(getContext(), "Bouton d'impression cliqué", 0).show();
        Log.d(TAG, "Bouton d'impression cliqué");
        showPairedDevicesDialog();
    }

    private void showPairedDevicesDialog() {
        BluetoothAdapter defaultAdapter = BluetoothAdapter.getDefaultAdapter();
        if (defaultAdapter == null || !defaultAdapter.isEnabled()) {
            showAlert("Erreur", "Le Bluetooth n'est pas activé.");
            return;
        }
        Set<BluetoothDevice> bondedDevices = defaultAdapter.getBondedDevices();
        ArrayList arrayList = new ArrayList();
        final ArrayList arrayList2 = new ArrayList();
        for (BluetoothDevice bluetoothDevice : bondedDevices) {
            arrayList.add(bluetoothDevice.getName());
            arrayList2.add(bluetoothDevice);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Sélectionner l'imprimante");
        builder.setAdapter(new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, arrayList), new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.ThermalReceiptFragment$$ExternalSyntheticLambda7
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                this.f$0.m137x71dda7f6(arrayList2, dialogInterface, i);
            }
        });
        builder.setNegativeButton("Annuler", new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.ThermalReceiptFragment$$ExternalSyntheticLambda8
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    /* renamed from: lambda$showPairedDevicesDialog$1$com-example-myadermoshop-ThermalReceiptFragment, reason: not valid java name */
    /* synthetic */ void m137x71dda7f6(List list, DialogInterface dialogInterface, int i) {
        this.bluetoothDevice = (BluetoothDevice) list.get(i);
        connectToPrinterAndPrint();
    }

    private void connectToPrinterAndPrint() {
        new Thread(new Runnable() { // from class: com.example.myadermoshop.ThermalReceiptFragment$$ExternalSyntheticLambda6
            @Override // java.lang.Runnable
            public void run() throws IOException {
                this.f$0.m133xdc8b3a50();
            }
        }).start();
    }

    /* renamed from: lambda$connectToPrinterAndPrint$5$com-example-myadermoshop-ThermalReceiptFragment, reason: not valid java name */
    /* synthetic */ void m133xdc8b3a50() throws IOException {
        try {
            BluetoothSocket bluetoothSocket = this.bluetoothSocket;
            if (bluetoothSocket != null && bluetoothSocket.isConnected()) {
                closeConnection();
            }
            Log.d(TAG, "Création d'un nouveau socket RFcomm...");
            BluetoothSocket bluetoothSocketCreateRfcommSocketToServiceRecord = this.bluetoothDevice.createRfcommSocketToServiceRecord(PRINTER_UUID);
            this.bluetoothSocket = bluetoothSocketCreateRfcommSocketToServiceRecord;
            bluetoothSocketCreateRfcommSocketToServiceRecord.connect();
            this.outputStream = this.bluetoothSocket.getOutputStream();
            this.handler.post(new Runnable() { // from class: com.example.myadermoshop.ThermalReceiptFragment$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public void run() {
                    this.f$0.m131xdd78064e();
                }
            });
            printReceipt();
        } catch (IOException e) {
            Log.e(TAG, "Erreur de connexion à l'imprimante", e);
            this.handler.post(new Runnable() { // from class: com.example.myadermoshop.ThermalReceiptFragment$$ExternalSyntheticLambda5
                @Override // java.lang.Runnable
                public void run() {
                    this.f$0.m132xdd01a04f();
                }
            });
        }
    }

    /* renamed from: lambda$connectToPrinterAndPrint$3$com-example-myadermoshop-ThermalReceiptFragment, reason: not valid java name */
    /* synthetic */ void m131xdd78064e() {
        Toast.makeText(getContext(), "Connecté à l'imprimante", 0).show();
    }

    /* renamed from: lambda$connectToPrinterAndPrint$4$com-example-myadermoshop-ThermalReceiptFragment, reason: not valid java name */
    /* synthetic */ void m132xdd01a04f() {
        showAlert("Erreur", "Échec de la connexion à l'imprimante");
    }

    private void printReceipt() throws IOException {
        try {
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
                sb.append("ID: ").append(this.textViewCartID.getText().toString()).append("\n");
                sb.append("Heure: ").append(this.textViewTime.getText().toString()).append("\n");
                sb.append("Devise: ").append(this.textViewCurrency.getText().toString()).append("\n");
                sb.append("Montant Total: ").append(this.textViewAmount.getText().toString()).append("\n");
                sb.append("==============================\n");
                sb.append(String.format("%-10s %-4s %-7s %-8s\n", "ARTICLE", "QTE", "UNITE", "TOTAL"));
                sb.append("------------------------------\n");
                for (int i = 0; i < this.linearLayoutItems.getChildCount(); i++) {
                    LinearLayout linearLayout = (LinearLayout) this.linearLayoutItems.getChildAt(i);
                    TextView textView = (TextView) linearLayout.getChildAt(0);
                    TextView textView2 = (TextView) linearLayout.getChildAt(1);
                    TextView textView3 = (TextView) linearLayout.getChildAt(2);
                    TextView textView4 = (TextView) linearLayout.getChildAt(3);
                    if (textView != null && textView2 != null && textView3 != null && textView4 != null) {
                        sb.append(String.format("%-10s %-4s %-7s %-8s\n", truncate(textView.getText().toString(), 10), textView2.getText().toString(), textView3.getText().toString(), textView4.getText().toString()));
                    }
                }
                sb.append("==============================\n");
                sb.append("Merci pour votre achat !\n");
                sb.append("\n");
                sb.append("Employee :\n");
                sb.append("Nom: ").append(this.textViewEmployeeName.getText().toString()).append("\n");
                sb.append("Tel: ").append(this.EmployeeTel).append("\n");
                sb.append("==============================\n\n\n\n");
                Log.d(TAG, "Contenu du reçu: \n" + sb);
                this.outputStream.write(sb.toString().getBytes());
                this.outputStream.flush();
                this.handler.post(new Runnable() { // from class: com.example.myadermoshop.ThermalReceiptFragment$$ExternalSyntheticLambda0
                    @Override // java.lang.Runnable
                    public void run() {
                        this.f$0.m135xd42efd11();
                    }
                });
            } catch (IOException e) {
                Log.e(TAG, "Erreur lors de l'impression du reçu", e);
                this.handler.post(new Runnable() { // from class: com.example.myadermoshop.ThermalReceiptFragment$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public void run() {
                        this.f$0.m136xd3b89712();
                    }
                });
            }
        } finally {
            closeConnection();
        }
    }

    /* renamed from: lambda$printReceipt$6$com-example-myadermoshop-ThermalReceiptFragment, reason: not valid java name */
    /* synthetic */ void m135xd42efd11() {
        Toast.makeText(getContext(), "Reçu imprimé", 0).show();
        Log.d(TAG, "Reçu imprimé");
    }

    /* renamed from: lambda$printReceipt$7$com-example-myadermoshop-ThermalReceiptFragment, reason: not valid java name */
    /* synthetic */ void m136xd3b89712() {
        showAlert("Erreur", "Échec de l'impression du reçu");
    }

    private void closeConnection() throws IOException {
        try {
            OutputStream outputStream = this.outputStream;
            if (outputStream != null) {
                outputStream.close();
                this.outputStream = null;
            }
            BluetoothSocket bluetoothSocket = this.bluetoothSocket;
            if (bluetoothSocket != null) {
                bluetoothSocket.close();
                this.bluetoothSocket = null;
            }
        } catch (IOException e) {
            Log.e(TAG, "Erreur lors de la fermeture de la connexion", e);
        }
    }

    private void showAlert(String str, String str2) {
        new AlertDialog.Builder(getContext()).setTitle(str).setMessage(str2).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.ThermalReceiptFragment$$ExternalSyntheticLambda3
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    private String truncate(String str, int i) {
        return str.length() > i ? str.substring(0, i - 1) + FileUtils.HIDDEN_PREFIX : str;
    }
}