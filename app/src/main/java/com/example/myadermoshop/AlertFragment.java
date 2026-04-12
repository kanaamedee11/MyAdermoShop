package com.example.myadermoshop;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

public class AlertFragment extends Fragment {

    private static final String ARG_ALERT_MESSAGE = "alert_message";
    private static final String ARG_ALERT_TYPE    = "alert_type";

    private String    alertMessage;
    private AlertType alertType;

    public enum AlertType {
        SUCCESS, ERROR
    }

    public AlertFragment() { }

    public static AlertFragment newInstance(String message, AlertType type) {
        AlertFragment fragment = new AlertFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ALERT_MESSAGE, message);
        args.putSerializable(ARG_ALERT_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_alert, container, false);

        if (getArguments() != null) {
            alertMessage = getArguments().getString(ARG_ALERT_MESSAGE);
            alertType    = (AlertType) getArguments().getSerializable(ARG_ALERT_TYPE);
        }

        TextView     tvMessage   = view.findViewById(R.id.alert_message);
        ImageView    ivIcon      = view.findViewById(R.id.alert_icon);
        View         leftBorder  = view.findViewById(R.id.left_border);
        LinearLayout alertLayout = view.findViewById(R.id.alert_layout);

        tvMessage.setText(alertMessage);

        if (alertType == AlertType.SUCCESS) {
            ivIcon.setImageResource(R.drawable.ic_success);
            tvMessage.setTextColor(Color.parseColor("#2E7D32"));
            leftBorder.setBackgroundColor(Color.parseColor("#4CAF50"));
        } else if (alertType == AlertType.ERROR) {
            ivIcon.setImageResource(R.drawable.ic_error);
            tvMessage.setTextColor(Color.parseColor("#C62828"));
            leftBorder.setBackgroundColor(Color.parseColor("#F44336"));
        }

        // ── Slide in ──
        if (getContext() != null) {
            alertLayout.startAnimation(
                    AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top));
        }

        // ── Auto-dismiss after 5 seconds with slide-out ──
        new Handler().postDelayed(() -> {
            if (getContext() != null && alertLayout.getVisibility() == View.VISIBLE) {
                alertLayout.startAnimation(
                        AnimationUtils.loadAnimation(getContext(), R.anim.slide_out_top));
                alertLayout.setVisibility(View.GONE);
            }
        }, 5000);

        return view;
    }
}