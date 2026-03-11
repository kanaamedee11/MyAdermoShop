package com.example.myadermoshop;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

/* loaded from: classes.dex */
public class ReportItemsTabFragment extends Fragment {
    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_tabs_item_report, viewGroup, false);
        TabLayout tabLayout = viewInflate.findViewById(R.id.tabLayout);
        ViewPager2 viewPager2 = viewInflate.findViewById(R.id.viewPager);
        viewPager2.setAdapter(new ViewPagerAdapter(getActivity()));
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() { // from class: com.example.myadermoshop.ReportItemsTabFragment.1
            @Override // com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
            public void onConfigureTab(TabLayout.Tab tab, int i) {
                if (i == 0) {
                    tab.setText("Articles avec Instances");
                } else {
                    if (i != 1) {
                        return;
                    }
                    tab.setText("Articles sans Instances");
                }
            }
        }).attach();
        return viewInflate;
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return 2;
        }

        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override // androidx.viewpager2.adapter.FragmentStateAdapter
        public Fragment createFragment(int i) {
            if (i == 0) {
                return new ItemsWithInstancesFragment();
            }
            if (i == 1) {
                return new ItemsWithoutInstancesFragment();
            }
            return new ItemsWithInstancesFragment();
        }
    }
}