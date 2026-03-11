package com.example.myadermoshop;

import android.app.AlertDialog;
import android.app.KeyguardManager;
import android.bluetooth.BluetoothDevice;
import android.content.DialogInterface;
import android.content.Intent;
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
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;
import com.example.myadermoshop.PrinterConnectionFragment;
import com.google.android.material.navigation.NavigationView;
import java.io.File;

/* loaded from: classes.dex */
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, PrinterConnectionFragment.OnPrinterSelectedListener {
    private static final int REQUEST_CODE_CONFIRM_DEVICE_CREDENTIALS = 1;
    private static final String TAG = "MainActivity";
    private ClosingSummary closingSummaryToUpload;
    private DrawerLayout drawer;

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        this.drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, this.drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        this.drawer.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        View headerView = navigationView.getHeaderView(0);
        TextView textView = headerView.findViewById(R.id.textViewEmployeeName);
        TextView textView2 = headerView.findViewById(R.id.textViewEmployeeEmail);
        ImageView imageView = headerView.findViewById(R.id.imageViewEmployee);
        String string = getSharedPreferences("MyApp", 0).getString("employeeID", "");
        Employee employeeByID = new DatabaseHelper(this).getEmployeeByID(string);
        if (employeeByID != null) {
            textView.setText(employeeByID.getEmployeeFirstName() + " " + employeeByID.getEmployeeLastName());
            textView2.setText(employeeByID.getEmployeeEmail());
            File file = new File(getFilesDir(), "employee_pictures/" + string + ".jpg");
            if (file.exists()) {
                imageView.setImageBitmap(BitmapFactory.decodeFile(file.getAbsolutePath()));
            }
        }
        if (bundle == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.container, new WorkspaceFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_workspace);
        }
    }

    @Override // androidx.activity.ComponentActivity, android.app.Activity
    public void onBackPressed() {
        if (this.drawer.isDrawerOpen(GravityCompat.START)) {
            this.drawer.closeDrawer(GravityCompat.START);
        } else {
            showExitConfirmationDialog();
        }
    }

    private void showExitConfirmationDialog() {
        new AlertDialog.Builder(this).setTitle("Quitter l'application").setMessage("Êtes-vous sûr de vouloir quitter l'application ?").setPositiveButton("Oui", new DialogInterface.OnClickListener() { // from class: com.example.myadermoshop.MainActivity.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                MainActivity.super.onBackPressed();
            }
        }).setNegativeButton("Non", null).show();
    }

    @Override // com.google.android.material.navigation.NavigationView.OnNavigationItemSelectedListener
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        FragmentTransaction fragmentTransactionBeginTransaction = getSupportFragmentManager().beginTransaction();
        switch (menuItem.getItemId()) {
            case R.id.nav_barcode_pdfs /* 2131231126 */:
                fragmentTransactionBeginTransaction.replace(R.id.container, new PdfListFragment()).commit();
                break;
            case R.id.nav_change_password /* 2131231127 */:
                fragmentTransactionBeginTransaction.replace(R.id.container, new ChangePasswordFragment()).commit();
                break;
            case R.id.nav_connect_printer /* 2131231128 */:
                fragmentTransactionBeginTransaction.replace(R.id.container, new PrinterConnectionFragment()).commit();
                break;
            case R.id.nav_dispenses /* 2131231129 */:
                fragmentTransactionBeginTransaction.replace(R.id.container, new DispensesFragment()).commit();
                break;
            case R.id.nav_low_stock /* 2131231130 */:
                fragmentTransactionBeginTransaction.replace(R.id.container, new LowStockFragment()).commit();
                break;
            case R.id.nav_physical_controls /* 2131231131 */:
                fragmentTransactionBeginTransaction.replace(R.id.container, new PhysicalControlsFragment()).commit();
                break;
            case R.id.nav_purchase /* 2131231132 */:
                fragmentTransactionBeginTransaction.replace(R.id.container, new PurchaseFragment()).commit();
                break;
            case R.id.nav_reportitems /* 2131231133 */:
                fragmentTransactionBeginTransaction.replace(R.id.container, new ReportItemsTabFragment()).commit();
                break;
            case R.id.nav_settings /* 2131231134 */:
                fragmentTransactionBeginTransaction.replace(R.id.container, new SettingsFragment()).commit();
                break;
            case R.id.nav_stock /* 2131231135 */:
                fragmentTransactionBeginTransaction.replace(R.id.container, new StockFragment()).commit();
                break;
            case R.id.nav_versements /* 2131231136 */:
                fragmentTransactionBeginTransaction.replace(R.id.container, new VersementsFragment()).commit();
                break;
            case R.id.nav_workspace /* 2131231138 */:
                fragmentTransactionBeginTransaction.replace(R.id.container, new WorkspaceFragment()).commit();
                break;
        }
        this.drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override // com.example.myadermoshop.PrinterConnectionFragment.OnPrinterSelectedListener
    public void onPrinterSelected(BluetoothDevice bluetoothDevice) {
        Log.d(TAG, "Printer selected: " + bluetoothDevice.getName());
    }

    public void authenticateUserForClosing(ClosingSummary closingSummary) {
        Intent intentCreateConfirmDeviceCredentialIntent = ((KeyguardManager) getSystemService("keyguard")).createConfirmDeviceCredentialIntent("Authentication Required", "Please confirm your screen lock pattern, PIN, or password to continue.");
        if (intentCreateConfirmDeviceCredentialIntent != null) {
            this.closingSummaryToUpload = closingSummary;
            startActivityForResult(intentCreateConfirmDeviceCredentialIntent, 1);
        } else {
            Log.e(TAG, "No lock screen security setup found.");
        }
    }

    public void resendClosing(ClosingSummary closingSummary) {
        Log.d(TAG, "Resending closure data for date: " + closingSummary.getDate());
        ProgressFragment.newInstance(closingSummary.getDate(), closingSummary).show(getSupportFragmentManager(), "progressFragment");
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 1) {
            if (i2 == -1) {
                Log.d(TAG, "Authentication successful. Starting data upload...");
                ClosingSummary closingSummary = this.closingSummaryToUpload;
                if (closingSummary != null) {
                    ProgressFragment.newInstance(closingSummary.getDate(), this.closingSummaryToUpload).show(getSupportFragmentManager(), "progressFragment");
                    return;
                }
                return;
            }
            Log.e(TAG, "Authentication failed.");
        }
    }
}