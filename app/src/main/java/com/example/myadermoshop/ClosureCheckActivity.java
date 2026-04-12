package com.example.myadermoshop;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class ClosureCheckActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_closure_check);

        // ── RESTORED: buttonOk matches XML ID ──
        findViewById(R.id.buttonOk).setOnClickListener(v -> finish());
    }
}