package com.example.myadermoshop;

import android.Manifest;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import com.google.android.material.button.MaterialButton;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class PrinterConnectionFragment extends Fragment {

    private static final String TAG = "PrinterConnectionFragment";
    private static final UUID MY_UUID =
            UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_LOCATION_PERMISSIONS  = 1;
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 2;
    private static final int REQUEST_ENABLE_LOCATION       = 3;

    private ArrayAdapter<String> adapter;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothSocket bluetoothSocket;
    private ArrayList<BluetoothDevice> deviceList;
    private Handler handler;
    private ListView listViewPrinters;
    private OnPrinterSelectedListener listener;

    // ── Interface ──
    public interface OnPrinterSelectedListener {
        void onPrinterSelected(BluetoothDevice device);
    }

    // ── Broadcast receiver: device found + discovery finished ──
    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(TAG, "Broadcast received: " + action);

            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(
                        BluetoothDevice.EXTRA_DEVICE);
                if (device == null || device.getName() == null) return;
                Log.d(TAG, "Device discovered: " + device.getName()
                        + " [" + device.getAddress() + "]");
                deviceList.add(device);
                adapter.add(device.getName());
                adapter.notifyDataSetChanged();

            } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                Log.d(TAG, "Discovery finished. Total: " + deviceList.size());
                if (adapter.getCount() == 0) {
                    showAlert("Aucun appareil",
                            "Aucun appareil Bluetooth n'a été trouvé.");
                }
            }
        }
    };

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "Fragment attached");
        if (context instanceof OnPrinterSelectedListener) {
            listener = (OnPrinterSelectedListener) context;
        } else {
            throw new RuntimeException(context
                    + " must implement OnPrinterSelectedListener");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "Fragment created");
        deviceList = new ArrayList<>();
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView called");
        View view = inflater.inflate(
                R.layout.fragment_printer_connection, container, false);

        listViewPrinters = view.findViewById(R.id.listViewPrinters);
        MaterialButton buttonScan = view.findViewById(R.id.buttonScanPrinters);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        handler = new Handler();

        adapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_list_item_1, new ArrayList<>());
        listViewPrinters.setAdapter(adapter);

        // ── Item click: pair selected device ──
        listViewPrinters.setOnItemClickListener((parent, v, position, id) -> {
            BluetoothDevice device = deviceList.get(position);
            Log.d(TAG, "Device selected: " + device.getName());
            pairDevice(device);
            if (listener != null) {
                listener.onPrinterSelected(device);
            }
        });

        // ── Scan button: start discovery ──
        buttonScan.setOnClickListener(v ->
                checkLocationServicesAndPermissions());

        Log.d(TAG, "Fragment view created");
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(receiver,
                new IntentFilter(BluetoothDevice.ACTION_FOUND));
        getContext().registerReceiver(receiver,
                new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED));
        checkLocationServicesAndPermissions();
    }

    @RequiresPermission(Manifest.permission.BLUETOOTH_SCAN)
    @Override
    public void onStop() {
        super.onStop();
        if (getContext() != null) {
            getContext().unregisterReceiver(receiver);
        }
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    // ── Check location services first ──
    private void checkLocationServicesAndPermissions() {
        LocationManager lm = (LocationManager)
                getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean locationEnabled = lm.isProviderEnabled(
                LocationManager.GPS_PROVIDER)
                || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        Log.d(TAG, "Location enabled: " + locationEnabled);

        if (!locationEnabled) {
            new AlertDialog.Builder(getContext())
                    .setTitle("Services de localisation requis")
                    .setMessage("La localisation est nécessaire pour la "
                            + "découverte Bluetooth. Veuillez l'activer.")
                    .setPositiveButton("Activer", (dialog, which) ->
                            startActivityForResult(new Intent(
                                            android.provider.Settings
                                                    .ACTION_LOCATION_SOURCE_SETTINGS),
                                    REQUEST_ENABLE_LOCATION))
                    .setNegativeButton("Annuler", (dialog, which) ->
                            showAlert("Erreur",
                                    "La localisation est requise pour "
                                            + "découvrir les appareils Bluetooth."))
                    .show();
        } else {
            checkPermissions();
        }
    }

    // ── Check location permissions ──
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 31) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_FINE_LOCATION) != 0
                    || ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.ACCESS_COARSE_LOCATION) != 0) {
                Log.d(TAG, "Requesting location permissions");
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{
                                android.Manifest.permission.ACCESS_FINE_LOCATION,
                                android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        REQUEST_LOCATION_PERMISSIONS);
                return;
            }
        }
        checkBluetoothPermissions();
    }

    // ── Check Bluetooth permissions ──
    private void checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= 31) {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.BLUETOOTH_CONNECT) != 0
                    || ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.BLUETOOTH_SCAN) != 0) {
                Log.d(TAG, "Requesting Bluetooth permissions (Android 12+)");
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{
                                android.Manifest.permission.BLUETOOTH_CONNECT,
                                android.Manifest.permission.BLUETOOTH_SCAN},
                        REQUEST_BLUETOOTH_PERMISSIONS);
            } else {
                Log.d(TAG, "Bluetooth permissions granted (Android 12+). Starting discovery.");
                startDiscovery();
            }
        } else {
            if (ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.BLUETOOTH) != 0
                    || ActivityCompat.checkSelfPermission(getContext(),
                    android.Manifest.permission.BLUETOOTH_ADMIN) != 0) {
                Log.d(TAG, "Requesting Bluetooth permissions (below Android 12)");
                ActivityCompat.requestPermissions(requireActivity(),
                        new String[]{
                                android.Manifest.permission.BLUETOOTH,
                                android.Manifest.permission.BLUETOOTH_ADMIN},
                        REQUEST_BLUETOOTH_PERMISSIONS);
            } else {
                Log.d(TAG, "Bluetooth permissions granted. Starting discovery.");
                startDiscovery();
            }
        }
    }

    // ── Start Bluetooth discovery ──
    private void startDiscovery() {
        Log.d(TAG, "Starting discovery...");
        boolean hasPermission =
                ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.BLUETOOTH) == 0
                        || (Build.VERSION.SDK_INT >= 31
                        && ActivityCompat.checkSelfPermission(getContext(),
                        android.Manifest.permission.BLUETOOTH_CONNECT) == 0);

        if (!hasPermission) {
            Log.d(TAG, "Bluetooth permission not granted.");
            showAlert("Permission refusée",
                    "La permission Bluetooth est requise pour scanner.");
            return;
        }

        if (bluetoothAdapter.isDiscovering()) {
            Log.d(TAG, "Discovery already running, canceling...");
            bluetoothAdapter.cancelDiscovery();
        }

        deviceList.clear();
        adapter.clear();

        if (!bluetoothAdapter.startDiscovery()) {
            Log.e(TAG, "Discovery failed to start.");
            showAlert("Erreur",
                    "Impossible de démarrer la recherche. "
                            + "Vérifiez que le Bluetooth est activé.");
        } else {
            Log.d(TAG, "Discovery started successfully.");
        }
    }

    // ── Pair with selected device in background thread ──
    @RequiresPermission(Manifest.permission.BLUETOOTH_CONNECT)
    private void pairDevice(final BluetoothDevice device) {
        new Thread(() -> {
            Log.d(TAG, "Pairing with: " + device.getName());
            try {
                bluetoothSocket =
                        device.createRfcommSocketToServiceRecord(MY_UUID);
                bluetoothSocket.connect();
                Log.d(TAG, "Paired successfully.");
                handler.post(() -> showAlert("Succès",
                        "Appareil couplé avec succès."));
            } catch (IOException e) {
                Log.e(TAG, "Pairing error: " + e.getMessage());
                handler.post(() -> showAlert("Erreur",
                        "Échec du couplage. Veuillez réessayer."));
            }
        }).start();
    }

    // ── Alert helper ──
    private void showAlert(String title, String message) {
        new AlertDialog.Builder(getContext())
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK",
                        (dialog, which) -> dialog.dismiss())
                .show();
    }

    // ── Permission results ──
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions,
                                           int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_LOCATION_PERMISSIONS) {
            for (int result : grantResults) {
                if (result != 0) {
                    Log.d(TAG, "Location permissions denied.");
                    showAlert("Permission refusée",
                            "La localisation est requise pour "
                                    + "découvrir les appareils Bluetooth.");
                    return;
                }
            }
            Log.d(TAG, "Location permissions granted.");
            checkBluetoothPermissions();

        } else if (requestCode == REQUEST_BLUETOOTH_PERMISSIONS) {
            for (int result : grantResults) {
                if (result != 0) {
                    Log.d(TAG, "Bluetooth permissions denied.");
                    showAlert("Permission refusée",
                            "La permission Bluetooth est requise pour scanner.");
                    return;
                }
            }
            Log.d(TAG, "Bluetooth permissions granted. Starting discovery.");
            startDiscovery();
        }
    }

    // ── Activity result: location settings returned ──
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_ENABLE_LOCATION) {
            LocationManager lm = (LocationManager)
                    getContext().getSystemService(Context.LOCATION_SERVICE);
            if (lm.isProviderEnabled(LocationManager.GPS_PROVIDER)
                    || lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
                checkPermissions();
            } else {
                showAlert("Erreur",
                        "La localisation est requise pour "
                                + "découvrir les appareils Bluetooth.");
            }
        }
    }
}