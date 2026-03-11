package com.example.myadermoshop;

import android.content.Intent;
import android.os.Bundle;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanIntentResult;
import com.journeyapps.barcodescanner.ScanOptions;

public class BarcodeScanActivity extends AppCompatActivity {

    private final ActivityResultLauncher<ScanOptions> barLauncher =
            registerForActivityResult(new ScanContract(), this::onScanResult);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        startBarcodeScanner();
    }

    private void startBarcodeScanner() {
        ScanOptions options = new ScanOptions();
        options.setPrompt("Volume up to flash on");
        options.setBeepEnabled(true);
        options.setOrientationLocked(true);
        options.setCaptureActivity(CaptureAct.class);
        barLauncher.launch(options);
    }

    private void onScanResult(ScanIntentResult result) {
        if (result.getContents() != null) {
            Intent intent = new Intent();
            intent.putExtra("scanned_data", result.getContents());
            setResult(RESULT_OK, intent);
            finish();
        }
        // If contents is null the user cancelled — activity just stays open
        // for ZXing to handle, or finishes naturally.
    }
}