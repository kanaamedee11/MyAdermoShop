package com.example.myadermoshop;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

/* loaded from: classes.dex */
public class PrinterConnectionFragment extends Fragment {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB");
    private static final int REQUEST_BLUETOOTH_PERMISSIONS = 2;
    private static final int REQUEST_ENABLE_LOCATION = 3;
    private static final int REQUEST_LOCATION_PERMISSIONS = 1;
    private static final String TAG = "PrinterConnectionFragment";
    private ArrayAdapter<String> adapter;
    private BluetoothAdapter bluetoothAdapter;
    private BluetoothDevice bluetoothDevice;
    private BluetoothSocket bluetoothSocket;
    private ArrayList<BluetoothDevice> deviceList;
    private Handler handler;
    private ListView listViewPrinters;
    private OnPrinterSelectedListener listener;
    private final BroadcastReceiver receiver = new BroadcastReceiver() { // from class: com.example.myadermoshop.PrinterConnectionFragment.1
        @Override // android.content.BroadcastReceiver
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Log.d(PrinterConnectionFragment.TAG, "Broadcast received: " + action);
            if ("android.bluetooth.device.action.FOUND".equals(action)) {
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra("android.bluetooth.device.extra.DEVICE");
                if (bluetoothDevice == null || bluetoothDevice.getName() == null) {
                    return;
                }
                Log.d(PrinterConnectionFragment.TAG, "Device discovered: " + bluetoothDevice.getName() + " [" + bluetoothDevice.getAddress() + "]");
                PrinterConnectionFragment.this.deviceList.add(bluetoothDevice);
                PrinterConnectionFragment.this.adapter.add(bluetoothDevice.getName());
                PrinterConnectionFragment.this.adapter.notifyDataSetChanged();
                return;
            }
            if ("android.bluetooth.adapter.action.DISCOVERY_FINISHED".equals(action)) {
                Log.d(PrinterConnectionFragment.TAG, "Discovery finished. Total devices found: " + PrinterConnectionFragment.this.deviceList.size());
                if (PrinterConnectionFragment.this.adapter.getCount() == 0) {
                    PrinterConnectionFragment.this.showAlert("No devices found", "No Bluetooth devices were found");
                }
            }
        }
    };

    public interface OnPrinterSelectedListener {
        void onPrinterSelected(BluetoothDevice bluetoothDevice);
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // androidx.fragment.app.Fragment
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.d(TAG, "Fragment attached");
        if (context instanceof OnPrinterSelectedListener) {
            this.listener = (OnPrinterSelectedListener) context;
            return;
        }
        throw new RuntimeException(context + " must implement OnPrinterSelectedListener");
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Log.d(TAG, "Fragment created");
        this.deviceList = new ArrayList<>();
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Log.d(TAG, "onCreateView called");
        View viewInflate = layoutInflater.inflate(R.layout.fragment_printer_connection, viewGroup, false);
        this.listViewPrinters = viewInflate.findViewById(R.id.listViewPrinters);
        this.bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        this.handler = new Handler();
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, new ArrayList());
        this.adapter = arrayAdapter;
        this.listViewPrinters.setAdapter(arrayAdapter);
        Log.d(TAG, "Fragment view created");
        this.listViewPrinters.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.example.myadermoshop.PrinterConnectionFragment$$ExternalSyntheticLambda6
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView adapterView, View view, int i, long j) {
                this.f$0.m112xc7146eb9(adapterView, view, i, j);
            }
        });
        return viewInflate;
    }

    /* renamed from: lambda$onCreateView$0$com-example-myadermoshop-PrinterConnectionFragment, reason: not valid java name */
    /* synthetic */ void m112xc7146eb9(AdapterView adapterView, View view, int i, long j) {
        BluetoothDevice bluetoothDevice = this.deviceList.get(i);
        Log.d(TAG, "Device selected: " + bluetoothDevice.getName());
        pairDevice(bluetoothDevice);
        OnPrinterSelectedListener onPrinterSelectedListener = this.listener;
        if (onPrinterSelectedListener != null) {
            onPrinterSelectedListener.onPrinterSelected(bluetoothDevice);
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onStart() {
        super.onStart();
        getContext().registerReceiver(this.receiver, new IntentFilter("android.bluetooth.device.action.FOUND"));
        getContext().registerReceiver(this.receiver, new IntentFilter("android.bluetooth.adapter.action.DISCOVERY_FINISHED"));
        checkLocationServicesAndPermissions();
    }

    @Override // androidx.fragment.app.Fragment
    public void onStop() {
        super.onStop();
        if (getContext() != null && this.receiver != null) {
            getContext().unregisterReceiver(this.receiver);
        }
        BluetoothAdapter bluetoothAdapter = this.bluetoothAdapter;
        if (bluetoothAdapter != null) {
            bluetoothAdapter.cancelDiscovery();
        }
    }

    private void checkLocationServicesAndPermissions() {
        LocationManager locationManager = (LocationManager) getContext().getSystemService(DatabaseHelper.COLUMN_LOCATION);
        boolean z = locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network");
        Log.d(TAG, "Location enabled: " + z);
        if (!z) {
            new AlertDialog.Builder(getContext()).setTitle("Location Services Required").setMessage("Location services are required for Bluetooth discovery. Please enable location services.").setPositiveButton("Enable", new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.PrinterConnectionFragment$$ExternalSyntheticLambda1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    this.f$0.m110xfb7e978a(dialogInterface, i);
                }
            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.PrinterConnectionFragment$$ExternalSyntheticLambda2
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    this.f$0.m111x295731e9(dialogInterface, i);
                }
            }).show();
        } else {
            checkPermissions();
        }
    }

    /* renamed from: lambda$checkLocationServicesAndPermissions$1$com-example-myadermoshop-PrinterConnectionFragment, reason: not valid java name */
    /* synthetic */ void m110xfb7e978a(DialogInterface dialogInterface, int i) {
        startActivityForResult(new Intent("android.settings.LOCATION_SOURCE_SETTINGS"), 3);
    }

    /* renamed from: lambda$checkLocationServicesAndPermissions$2$com-example-myadermoshop-PrinterConnectionFragment, reason: not valid java name */
    /* synthetic */ void m111x295731e9(DialogInterface dialogInterface, int i) {
        showAlert("Error", "Location services are required to discover Bluetooth devices.");
    }

    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= 31) {
            if (ActivityCompat.checkSelfPermission(getContext(), "android.permission.ACCESS_FINE_LOCATION") != 0 || ActivityCompat.checkSelfPermission(getContext(), "android.permission.ACCESS_COARSE_LOCATION") != 0) {
                Log.d(TAG, "Requesting location permissions");
                ActivityCompat.requestPermissions(requireActivity(), new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.ACCESS_COARSE_LOCATION"}, 1);
                return;
            } else {
                checkBluetoothPermissions();
                return;
            }
        }
        checkBluetoothPermissions();
    }

    private void checkBluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= 31) {
            if (ActivityCompat.checkSelfPermission(getContext(), "android.permission.BLUETOOTH_CONNECT") != 0 || ActivityCompat.checkSelfPermission(getContext(), "android.permission.BLUETOOTH_SCAN") != 0) {
                Log.d(TAG, "Requesting Bluetooth permissions for Android 12 and above");
                ActivityCompat.requestPermissions(requireActivity(), new String[]{"android.permission.BLUETOOTH_CONNECT", "android.permission.BLUETOOTH_SCAN"}, 2);
                return;
            } else {
                Log.d(TAG, "Bluetooth permissions already granted for Android 12 and above. Starting discovery.");
                startDiscovery();
                return;
            }
        }
        if (ActivityCompat.checkSelfPermission(getContext(), "android.permission.BLUETOOTH") != 0 || ActivityCompat.checkSelfPermission(getContext(), "android.permission.BLUETOOTH_ADMIN") != 0) {
            Log.d(TAG, "Requesting Bluetooth permissions for Android versions below 12");
            ActivityCompat.requestPermissions(requireActivity(), new String[]{"android.permission.BLUETOOTH", "android.permission.BLUETOOTH_ADMIN"}, 2);
        } else {
            Log.d(TAG, "Bluetooth permissions already granted for Android versions below 12. Starting discovery.");
            startDiscovery();
        }
    }

    private void startDiscovery() {
        Log.d(TAG, "Starting discovery...");
        if (ActivityCompat.checkSelfPermission(getContext(), "android.permission.BLUETOOTH") == 0 || (Build.VERSION.SDK_INT >= 31 && ActivityCompat.checkSelfPermission(getContext(), "android.permission.BLUETOOTH_CONNECT") == 0)) {
            if (this.bluetoothAdapter.isDiscovering()) {
                Log.d(TAG, "Bluetooth discovery is already running, canceling...");
                this.bluetoothAdapter.cancelDiscovery();
            }
            Log.d(TAG, "Clearing device list and starting discovery...");
            this.deviceList.clear();
            this.adapter.clear();
            if (!this.bluetoothAdapter.startDiscovery()) {
                Log.e(TAG, "Discovery did not start. Check Bluetooth state and permissions.");
                showAlert("Error", "Failed to start Bluetooth discovery. Please ensure Bluetooth is enabled and try again.");
                return;
            } else {
                Log.d(TAG, "Discovery started successfully.");
                return;
            }
        }
        Log.d(TAG, "Bluetooth permission not granted.");
        showAlert("Permission Denied", "Bluetooth permission is required to scan for devices.");
    }

    private void pairDevice(final BluetoothDevice bluetoothDevice) {
        new Thread(new Runnable() { // from class: com.example.myadermoshop.PrinterConnectionFragment$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public void run() throws IOException {
                this.f$0.m115x46070084(bluetoothDevice);
            }
        }).start();
    }

    /* renamed from: lambda$pairDevice$5$com-example-myadermoshop-PrinterConnectionFragment, reason: not valid java name */
    /* synthetic */ void m115x46070084(BluetoothDevice bluetoothDevice) throws IOException {
        Log.d(TAG, "Pairing with device: " + bluetoothDevice.getName());
        try {
            BluetoothSocket bluetoothSocketCreateRfcommSocketToServiceRecord = bluetoothDevice.createRfcommSocketToServiceRecord(MY_UUID);
            this.bluetoothSocket = bluetoothSocketCreateRfcommSocketToServiceRecord;
            bluetoothSocketCreateRfcommSocketToServiceRecord.connect();
            Log.d(TAG, "Device paired successfully");
            this.handler.post(new Runnable() { // from class: com.example.myadermoshop.PrinterConnectionFragment$$ExternalSyntheticLambda3
                @Override // java.lang.Runnable
                public void run() {
                    this.f$0.m113xea55cbc6();
                }
            });
        } catch (IOException e) {
            Log.e(TAG, "Error pairing with device: " + e.getMessage());
            this.handler.post(new Runnable() { // from class: com.example.myadermoshop.PrinterConnectionFragment$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public void run() {
                    this.f$0.m114x182e6625();
                }
            });
        }
    }

    /* renamed from: lambda$pairDevice$3$com-example-myadermoshop-PrinterConnectionFragment, reason: not valid java name */
    /* synthetic */ void m113xea55cbc6() {
        showAlert("Success", "Device paired successfully and connection is maintained.");
    }

    /* renamed from: lambda$pairDevice$4$com-example-myadermoshop-PrinterConnectionFragment, reason: not valid java name */
    /* synthetic */ void m114x182e6625() {
        showAlert("Error", "Failed to pair with device. Please try again.");
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void showAlert(String str, String str2) {
        new AlertDialog.Builder(getContext()).setTitle(str).setMessage(str2).setPositiveButton("OK", new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.PrinterConnectionFragment$$ExternalSyntheticLambda0
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).show();
    }

    @Override // androidx.fragment.app.Fragment
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        super.onRequestPermissionsResult(i, strArr, iArr);
        int i2 = 0;
        if (i == 1) {
            if (iArr.length > 0) {
                int length = iArr.length;
                while (i2 < length) {
                    if (iArr[i2] != 0) {
                        Log.d(TAG, "Location permissions denied.");
                        showAlert("Permission Denied", "Location permission is required to discover Bluetooth devices.");
                        return;
                    }
                    i2++;
                }
                Log.d(TAG, "Location permissions granted. Checking Bluetooth permissions.");
                checkBluetoothPermissions();
                return;
            }
            Log.d(TAG, "Location permissions denied.");
            showAlert("Permission Denied", "Location permission is required to discover Bluetooth devices.");
            return;
        }
        if (i == 2) {
            if (iArr.length > 0) {
                int length2 = iArr.length;
                while (i2 < length2) {
                    if (iArr[i2] != 0) {
                        Log.d(TAG, "Bluetooth permissions denied.");
                        showAlert("Permission Denied", "Bluetooth permission is required to scan for devices.");
                        return;
                    }
                    i2++;
                }
                Log.d(TAG, "Bluetooth permissions granted. Starting discovery.");
                startDiscovery();
                return;
            }
            Log.d(TAG, "Bluetooth permissions denied.");
            showAlert("Permission Denied", "Bluetooth permission is required to scan for devices.");
        }
    }

    @Override // androidx.fragment.app.Fragment
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 3) {
            LocationManager locationManager = (LocationManager) getContext().getSystemService(DatabaseHelper.COLUMN_LOCATION);
            if (locationManager.isProviderEnabled("gps") || locationManager.isProviderEnabled("network")) {
                checkPermissions();
            } else {
                showAlert("Error", "Location services are required to discover Bluetooth devices.");
            }
        }
    }
}