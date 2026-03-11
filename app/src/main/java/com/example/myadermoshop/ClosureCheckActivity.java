package com.example.myadermoshop;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/* loaded from: classes.dex */
public class ClosureCheckActivity extends AppCompatActivity {
    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_closure_check);
        findViewById(R.id.buttonOk).setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.ClosureCheckActivity.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                ClosureCheckActivity.this.finish();
            }
        });
    }
}