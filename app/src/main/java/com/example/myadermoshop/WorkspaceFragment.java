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
public class WorkspaceFragment extends Fragment {
    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View viewInflate = layoutInflater.inflate(R.layout.fragment_workspace, viewGroup, false);
        TabLayout tabLayout = viewInflate.findViewById(R.id.tabLayoutWorkspace);
        ViewPager2 viewPager2 = viewInflate.findViewById(R.id.viewPagerWorkspace);
        viewPager2.setAdapter(new ViewPagerAdapter(getActivity()));
        new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() { // from class: com.example.myadermoshop.WorkspaceFragment.1
            @Override // com.google.android.material.tabs.TabLayoutMediator.TabConfigurationStrategy
            public void onConfigureTab(TabLayout.Tab tab, int i) {
                if (i == 0) {
                    tab.setText("Accueil");
                } else if (i == 1) {
                    tab.setText("Ventes");
                } else {
                    if (i != 2) {
                        return;
                    }
                    tab.setText("Clôtures");
                }
            }
        }).attach();
        return viewInflate;
    }

    private static class ViewPagerAdapter extends FragmentStateAdapter {
        @Override // androidx.recyclerview.widget.RecyclerView.Adapter
        public int getItemCount() {
            return 3;
        }

        public ViewPagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override // androidx.viewpager2.adapter.FragmentStateAdapter
        public Fragment createFragment(int i) {
            if (i == 0) {
                return new HomeFragment();
            }
            if (i == 1) {
                return new SellsFragment();
            }
            if (i == 2) {
                return new ClosingFragment();
            }
            return new HomeFragment();
        }
    }
}