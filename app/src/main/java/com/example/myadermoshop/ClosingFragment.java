package com.example.myadermoshop;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class ClosingFragment extends Fragment {

    private EditText etStartDate;
    private EditText etEndDate;
    private ImageButton btnSearch;
    private ImageButton btnShowAll;
    private RecyclerView recyclerViewClosing;

    private ClosingSummaryAdapter closingSummaryAdapter;
    private List<ClosingSummary> closingSummaryList;

    private final SimpleDateFormat sdf =
            new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_closing, container, false);

        etStartDate        = root.findViewById(R.id.etStartDate);
        etEndDate          = root.findViewById(R.id.etEndDate);
        btnSearch          = root.findViewById(R.id.btnSearch);
        btnShowAll         = root.findViewById(R.id.btnShowAll);
        recyclerViewClosing = root.findViewById(R.id.recyclerViewClosing);

        recyclerViewClosing.setLayoutManager(new LinearLayoutManager(getContext()));
        closingSummaryList    = new ArrayList<>();
        closingSummaryAdapter = new ClosingSummaryAdapter(getContext(), closingSummaryList);
        recyclerViewClosing.setAdapter(closingSummaryAdapter);

        setupDatePickers();
        setupListeners();
        loadLastFifteenDays();

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        loadLastFifteenDays();
    }

    // ── Date pickers ──────────────────────────────────────────────────────────

    private void setupDatePickers() {
        etStartDate.setOnClickListener(v -> showDatePicker(etStartDate));
        etEndDate.setOnClickListener(v -> showDatePicker(etEndDate));
    }

    private void showDatePicker(final EditText target) {
        Calendar cal = Calendar.getInstance();
        new DatePickerDialog(requireContext(),
                (picker, year, month, day) -> {
                    cal.set(year, month, day);
                    target.setText(sdf.format(cal.getTime()));
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH))
                .show();
    }

    // ── Button listeners ──────────────────────────────────────────────────────

    private void setupListeners() {
        btnSearch.setOnClickListener(v -> {
            String start = etStartDate.getText().toString();
            String end   = etEndDate.getText().toString();
            if (!start.isEmpty() && !end.isEmpty()) {
                loadFilteredDates(start, end);
            }
        });

        btnShowAll.setOnClickListener(v -> {
            etStartDate.setText("");
            etEndDate.setText("");
            loadLastFifteenDays();
        });
    }

    // ── Data loading ──────────────────────────────────────────────────────────

    private void loadLastFifteenDays() {
        DatabaseHelper db = new DatabaseHelper(requireContext());
        closingSummaryList.clear();

        List<String> allDates = db.getDateRangeFromLastControlToToday();
        int from = Math.max(allDates.size() - 15, 0);
        for (String date : allDates.subList(from, allDates.size())) {
            closingSummaryList.add(buildSummary(db, date));
        }
        closingSummaryAdapter.notifyDataSetChanged();
    }

    private void loadFilteredDates(String startStr, String endStr) {
        DatabaseHelper db = new DatabaseHelper(requireContext());
        closingSummaryList.clear();

        try {
            Date start = sdf.parse(startStr);
            Date end   = sdf.parse(endStr);
            for (String date : db.getDateRangeFromLastControlToToday()) {
                Date d = sdf.parse(date);
                if (d != null && !d.before(start) && !d.after(end)) {
                    closingSummaryList.add(buildSummary(db, date));
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        closingSummaryAdapter.notifyDataSetChanged();
    }

    private ClosingSummary buildSummary(DatabaseHelper db, String date) {
        return new ClosingSummary(
                date,
                db.getTotalPurchasePrice(date),
                db.getTotalSalePrice(date),
                db.getSalesByPaymentType(date),
                db.getSalesSummary(date),
                db.getStocksForDate(date));
    }
}