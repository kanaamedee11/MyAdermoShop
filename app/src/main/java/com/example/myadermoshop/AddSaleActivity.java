package com.example.myadermoshop;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddSaleActivity extends AppCompatActivity {
    private EditText editTextAmount;
    private EditText editTextDate;
    private EditText editTextNote;
    private TextView tvSelectedType;
    private MaterialButton buttonSave;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_sale);

        dbHelper = new DatabaseHelper(this);
        initViews();

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDatePicker();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSale();
            }
        });
    }

    private void initViews() {
        editTextAmount = findViewById(R.id.editTextAmount);
        editTextDate = findViewById(R.id.editTextDate);
        editTextNote = findViewById(R.id.editTextNote);
        tvSelectedType = findViewById(R.id.tvSelectedType);
        buttonSave = findViewById(R.id.buttonSave);
    }

    private void showDatePicker() {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                String date = String.format(Locale.getDefault(), "%d/%02d/%02d", year, month + 1, dayOfMonth);
                editTextDate.setText(date);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public void calculateTotalPrice() {
        // Implementation for ProductOnCartAdapter reference
    }

    private void saveSale() {
        String amount = editTextAmount.getText().toString().trim();
        if (amount.isEmpty()) {
            Toast.makeText(this, "Veuillez entrer un montant", Toast.LENGTH_SHORT).show();
            return;
        }
        // Logic to save sale
        Toast.makeText(this, "Vente enregistrée", Toast.LENGTH_SHORT).show();
        finish();
    }
}
