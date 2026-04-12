package com.example.myadermoshop;

import android.Manifest;
import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import java.io.File;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        PrinterConnectionFragment.OnPrinterSelectedListener {

    private static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 1;
    private static final String TAG = "MainActivity";
    private ClosingSummary closingSummaryToUpload;
    private DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        // Populate nav header
        View headerView = navigationView.getHeaderView(0);
        TextView tvName    = headerView.findViewById(R.id.textViewEmployeeName);
        TextView tvEmail   = headerView.findViewById(R.id.textViewEmployeeEmail);
        ImageView ivAvatar = headerView.findViewById(R.id.imageViewEmployee);

        String employeeID = getSharedPreferences("MyApp", 0)
                .getString("employeeID", "");
        Employee employee = new DatabaseHelper(this).getEmployeeByID(employeeID);
        if (employee != null) {
            tvName.setText(employee.getEmployeeFirstName()
                    + " " + employee.getEmployeeLastName());
            tvEmail.setText(employee.getEmployeeEmail());
            File photo = new File(getFilesDir(),
                    "employee_pictures/" + employeeID + ".jpg");
            if (photo.exists()) {
                ivAvatar.setImageBitmap(
                        BitmapFactory.decodeFile(photo.getAbsolutePath()));
            }
        }

        if (bundle == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, new WorkspaceFragment())
                    .commit();
            navigationView.setCheckedItem(R.id.nav_workspace);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            showExitConfirmationDialog();
        }
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Quitter l'application")
                .setMessage("Êtes-vous sûr de vouloir quitter l'application ?")
                .setPositiveButton("Oui", (dialog, which) ->
                        MainActivity.super.onBackPressed())
                .setNegativeButton("Non", null)
                .show();
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        int id = menuItem.getItemId();

        if (id == R.id.nav_workspace) {
            ft.replace(R.id.container, new WorkspaceFragment());
        } else if (id == R.id.nav_purchase) {
            ft.replace(R.id.container, new PurchaseFragment());
        } else if (id == R.id.nav_stock) {
            ft.replace(R.id.container, new StockFragment());
        } else if (id == R.id.nav_low_stock) {
            ft.replace(R.id.container, new LowStockFragment());
        } else if (id == R.id.nav_reportitems) {
            ft.replace(R.id.container, new ReportItemsTabFragment());
        } else if (id == R.id.nav_dispenses) {
            ft.replace(R.id.container, new DispensesFragment());
        } else if (id == R.id.nav_versements) {
            ft.replace(R.id.container, new VersementsFragment());
        } else if (id == R.id.nav_physical_controls) {
            ft.replace(R.id.container, new PhysicalControlsFragment());
        } else if (id == R.id.nav_change_password) {
            ft.replace(R.id.container, new ChangePasswordFragment());
        } else if (id == R.id.nav_settings) {
            ft.replace(R.id.container, new SettingsFragment());
        } else if (id == R.id.nav_connect_printer) {
            ft.replace(R.id.container, new PrinterConnectionFragment());
        } else if (id == R.id.nav_barcode_pdfs) {
            ft.replace(R.id.container, new PdfListFragment());
        }

        ft.commit();
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onPrinterSelected(BluetoothDevice bluetoothDevice) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Log.d(TAG, "Printer selected: " + bluetoothDevice.getName());
    }

    public void authenticateUserForClosing(ClosingSummary closingSummary) {
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        Intent intent = km.createConfirmDeviceCredentialIntent(
                "Authentication Required",
                "Please confirm your screen lock pattern, PIN, or password to continue.");
        if (intent != null) {
            closingSummaryToUpload = closingSummary;
            startActivityForResult(intent, REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS);
        } else {
            Log.e(TAG, "No lock screen security setup found.");
        }
    }

    public void resendClosing(ClosingSummary closingSummary) {
        Log.d(TAG, "Resending closure data for date: " + closingSummary.getDate());
        ProgressFragment.newInstance(closingSummary.getDate(), closingSummary)
                .show(getSupportFragmentManager(), "progressFragment");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS) {
            if (resultCode == RESULT_OK) {
                Log.d(TAG, "Authentication successful. Starting data upload...");
                if (closingSummaryToUpload != null) {
                    ProgressFragment.newInstance(
                                    closingSummaryToUpload.getDate(),
                                    closingSummaryToUpload)
                            .show(getSupportFragmentManager(), "progressFragment");
                }
            } else {
                Log.e(TAG, "Authentication failed.");
            }
        }
    }
}