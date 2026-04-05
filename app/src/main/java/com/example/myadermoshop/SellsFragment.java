package com.example.myadermoshop;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class SellsFragment extends Fragment {
    private AllSalesAdapter allSalesAdapter;
    private Button buttonFilter;
    private List<Cart> cartList;
    private final SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private DatabaseHelper dbHelper;
    private EditText editTextEndDate;
    private EditText editTextStartDate;
    private RecyclerView recyclerViewSells;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_sells, viewGroup, false);
        
        this.recyclerViewSells = viewInflate.findViewById(R.id.recyclerViewSells);
        this.recyclerViewSells.setLayoutManager(new LinearLayoutManager(getContext()));
        
        this.cartList = new ArrayList<>();
        this.allSalesAdapter = new AllSalesAdapter(getContext(), this.cartList);
        this.recyclerViewSells.setAdapter(this.allSalesAdapter);
        
        this.dbHelper = new DatabaseHelper(getContext());
        this.editTextStartDate = viewInflate.findViewById(R.id.editTextStartDate);
        this.editTextEndDate = viewInflate.findViewById(R.id.editTextEndDate);
        this.buttonFilter = viewInflate.findViewById(R.id.buttonFilter);
        
        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, -15);
        Date fifteenDaysAgo = calendar.getTime();
        
        this.editTextStartDate.setText(this.dbFormat.format(fifteenDaysAgo));
        this.editTextEndDate.setText(this.dbFormat.format(today));
        
        loadCartsBetween(fifteenDaysAgo, today);
        
        setUpDatePicker(this.editTextStartDate);
        setUpDatePicker(this.editTextEndDate);
        
        this.buttonFilter.setOnClickListener(view -> {
            try {
                loadCartsBetween(this.dbFormat.parse(this.editTextStartDate.getText().toString()), 
                                 this.dbFormat.parse(this.editTextEndDate.getText().toString()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        
        return viewInflate;
    }

    private void loadCartsBetween(Date start, Date end) {
        if (start != null && end != null) {
            this.cartList.clear();
            this.cartList.addAll(this.dbHelper.getCartsBetweenDates(this.dbFormat.format(start), this.dbFormat.format(end)));
            this.allSalesAdapter.notifyDataSetChanged();
        }
    }

    private void setUpDatePicker(final EditText editText) {
        editText.setOnClickListener(view -> {
            Calendar calendar = Calendar.getInstance();
            new DatePickerDialog(getContext(), (datePicker, year, month, day) -> {
                Calendar selected = Calendar.getInstance();
                selected.set(year, month, day);
                editText.setText(this.dbFormat.format(selected.getTime()));
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
        });
    }
}
