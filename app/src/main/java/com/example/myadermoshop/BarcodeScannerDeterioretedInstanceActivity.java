package com.example.myadermoshop;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class BarcodeScannerDeterioretedInstanceActivity extends AppCompatActivity {
    private final ActivityResultLauncher<ScanOptions> barLauncher = registerForActivityResult(new ScanContract(), new ActivityResultCallback<ScanIntentResult>() {
        @Override
        public void onActivityResult(ScanIntentResult result) {
            if (result.getContents() != null) {
                Intent intent = new Intent();
                intent.putExtra("scanned_data", result.getContents());
                setResult(-1, intent);
                finish();
            }
        }
    });

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        startBarcodeScanner();
    }

    private void startBarcodeScanner() {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setPrompt("Volume up to flash on");
        scanOptions.setBeepEnabled(true);
        scanOptions.setOrientationLocked(true);
        scanOptions.setCaptureActivity(CaptureAct.class);
        this.barLauncher.launch(scanOptions);
    }
}