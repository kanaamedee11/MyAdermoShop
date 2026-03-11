package com.example.myadermoshop;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.fragment.app.Fragment;

/* loaded from: classes.dex */
public class SettingsFragment extends Fragment {
    private static final String KEY_LABEL_COUNT = "labelCount";
    private static final String PREFS_NAME = "BarcodePrefs";
    private RadioGroup radioGroup;
    private RadioButton radioOne;
    private RadioButton radioTwo;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_settings, viewGroup, false);
        this.radioGroup = viewInflate.findViewById(R.id.radioGroupPrintCount);
        this.radioOne = viewInflate.findViewById(R.id.radioOne);
        this.radioTwo = viewInflate.findViewById(R.id.radioTwo);
        final SharedPreferences sharedPreferences = requireContext().getSharedPreferences(PREFS_NAME, 0);
        if (sharedPreferences.getInt(KEY_LABEL_COUNT, 2) == 1) {
            this.radioOne.setChecked(true);
        } else {
            this.radioTwo.setChecked(true);
        }
        this.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() { // from class: com.example.myadermoshop.SettingsFragment$$ExternalSyntheticLambda0
            @Override // android.widget.RadioGroup.OnCheckedChangeListener
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                sharedPreferences.edit().putInt(SettingsFragment.KEY_LABEL_COUNT, i == R.id.radioOne ? 1 : 2).apply();
            }
        });
        return viewInflate;
    }

    public static int getSavedLabelCount(Context context) {
        return context.getSharedPreferences(PREFS_NAME, 0).getInt(KEY_LABEL_COUNT, 2);
    }
}