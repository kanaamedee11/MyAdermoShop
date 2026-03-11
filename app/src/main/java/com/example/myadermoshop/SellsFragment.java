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

/* loaded from: classes.dex */
public class SellsFragment extends Fragment {
    private AllSalesAdapter allSalesAdapter;
    private Button buttonFilter;
    private List<Cart> cartList;
    private final SimpleDateFormat dbFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    private DatabaseHelper dbHelper;
    private EditText editTextEndDate;
    private EditText editTextStartDate;
    private RecyclerView recyclerViewSells;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_sells, viewGroup, false);
        RecyclerView recyclerView = viewInflate.findViewById(R.id.recyclerViewSells);
        this.recyclerViewSells = recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.cartList = new ArrayList();
        AllSalesAdapter allSalesAdapter = new AllSalesAdapter(getContext(), this.cartList);
        this.allSalesAdapter = allSalesAdapter;
        this.recyclerViewSells.setAdapter(allSalesAdapter);
        this.dbHelper = new DatabaseHelper(getContext());
        this.editTextStartDate = viewInflate.findViewById(R.id.editTextStartDate);
        this.editTextEndDate = viewInflate.findViewById(R.id.editTextEndDate);
        this.buttonFilter = viewInflate.findViewById(R.id.buttonFilter);
        Calendar calendar = Calendar.getInstance();
        Date time = calendar.getTime();
        calendar.add(6, -15);
        Date time2 = calendar.getTime();
        this.editTextStartDate.setText(this.dbFormat.format(time2));
        this.editTextEndDate.setText(this.dbFormat.format(time));
        loadCartsBetween(time2, time);
        setUpDatePicker(this.editTextStartDate);
        setUpDatePicker(this.editTextEndDate);
        this.buttonFilter.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.SellsFragment$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.f$0.m123lambda$onCreateView$0$comexamplemyadermoshopSellsFragment(view);
            }
        });
        return viewInflate;
    }

    /* renamed from: lambda$onCreateView$0$com-example-myadermoshop-SellsFragment, reason: not valid java name */
    /* synthetic */ void m123lambda$onCreateView$0$comexamplemyadermoshopSellsFragment(View view) {
        try {
            loadCartsBetween(this.dbFormat.parse(this.editTextStartDate.getText().toString()), this.dbFormat.parse(this.editTextEndDate.getText().toString()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadCartsBetween(Date date, Date date2) {
        this.cartList.clear();
        this.cartList.addAll(this.dbHelper.getCartsBetweenDates(this.dbFormat.format(date), this.dbFormat.format(date2)));
        this.allSalesAdapter.notifyDataSetChanged();
    }

    private void setUpDatePicker(final EditText editText) {
        editText.setOnClickListener(new View.OnClickListener() { // from class: com.example.myadermoshop.SellsFragment$$ExternalSyntheticLambda2
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                this.f$0.m125lambda$setUpDatePicker$2$comexamplemyadermoshopSellsFragment(editText, view);
            }
        });
    }

    /* renamed from: lambda$setUpDatePicker$2$com-example-myadermoshop-SellsFragment, reason: not valid java name */
    /* synthetic */ void m125lambda$setUpDatePicker$2$comexamplemyadermoshopSellsFragment(final EditText editText, View view) {
        Calendar calendar = Calendar.getInstance();
        new DatePickerDialog(getContext(), new DatePickerDialog.OnDateSetListener() { // from class: com.example.myadermoshop.SellsFragment$$ExternalSyntheticLambda1
            @Override // android.app.DatePickerDialog.OnDateSetListener
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                this.f$0.m124lambda$setUpDatePicker$1$comexamplemyadermoshopSellsFragment(editText, datePicker, i, i2, i3);
            }
        }, calendar.get(1), calendar.get(2), calendar.get(5)).show();
    }

    /* renamed from: lambda$setUpDatePicker$1$com-example-myadermoshop-SellsFragment, reason: not valid java name */
    /* synthetic */ void m124lambda$setUpDatePicker$1$comexamplemyadermoshopSellsFragment(EditText editText, DatePicker datePicker, int i, int i2, int i3) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(i, i2, i3);
        editText.setText(this.dbFormat.format(calendar.getTime()));
    }
}