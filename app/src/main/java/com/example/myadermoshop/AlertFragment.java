package com.example.myadermoshop;

import android.content.res.Resources;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;

/* loaded from: classes.dex */
public class AlertFragment extends Fragment {
    private static final String ARG_ALERT_MESSAGE = "alert_message";
    private static final String ARG_ALERT_TYPE = "alert_type";
    private String alertMessage;
    private AlertType alertType;

    public enum AlertType {
        SUCCESS,
        ERROR
    }

    public static AlertFragment newInstance(String str, AlertType alertType) {
        AlertFragment alertFragment = new AlertFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_ALERT_MESSAGE, str);
        bundle.putSerializable(ARG_ALERT_TYPE, alertType);
        alertFragment.setArguments(bundle);
        return alertFragment;
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_alert, viewGroup, false);
        if (getArguments() != null) {
            this.alertMessage = getArguments().getString(ARG_ALERT_MESSAGE);
            this.alertType = (AlertType) getArguments().getSerializable(ARG_ALERT_TYPE);
        }
        TextView textView = viewInflate.findViewById(R.id.alert_message);
        ImageView imageView = viewInflate.findViewById(R.id.alert_icon);
        View viewFindViewById = viewInflate.findViewById(R.id.left_border);
        final LinearLayout linearLayout = viewInflate.findViewById(R.id.alert_layout);
        textView.setText(this.alertMessage);
        if (this.alertType == AlertType.SUCCESS) {
            imageView.setImageResource(R.drawable.ic_success);
            textView.setTextColor(getResources().getColor(R.color.successTextColor));
            viewFindViewById.setBackgroundColor(getResources().getColor(R.color.successBorderColor));
        } else if (this.alertType == AlertType.ERROR) {
            imageView.setImageResource(R.drawable.ic_error);
            textView.setTextColor(getResources().getColor(R.color.errorTextColor));
            viewFindViewById.setBackgroundColor(getResources().getColor(R.color.errorBorderColor));
        }
        if (getContext() != null) {
            linearLayout.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_in_top));
        }
        new Handler().postDelayed(new Runnable() { // from class: com.example.myadermoshop.AlertFragment.1
            @Override // java.lang.Runnable
            public void run() throws Resources.NotFoundException {
                if (AlertFragment.this.getContext() != null) {
                    Animation animationLoadAnimation = AnimationUtils.loadAnimation(AlertFragment.this.getContext(), R.anim.slide_out_top);
                    linearLayout.startAnimation(animationLoadAnimation);
                    animationLoadAnimation.setAnimationListener(new Animation.AnimationListener() { // from class: com.example.myadermoshop.AlertFragment.1.1
                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationRepeat(Animation animation) {
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationStart(Animation animation) {
                        }

                        @Override // android.view.animation.Animation.AnimationListener
                        public void onAnimationEnd(Animation animation) {
                            if (AlertFragment.this.getActivity() == null || !AlertFragment.this.isAdded()) {
                                return;
                            }
                            AlertFragment.this.getFragmentManager().beginTransaction().remove(AlertFragment.this).commitAllowingStateLoss();
                        }
                    });
                }
            }
        }, 5000L);
        return viewInflate;
    }
}