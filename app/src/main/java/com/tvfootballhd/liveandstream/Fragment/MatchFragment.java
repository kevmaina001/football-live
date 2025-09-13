package com.tvfootballhd.liveandstream.Fragment;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import com.tvfootballhd.liveandstream.Adapter.MatchViewPagerAdapter;
import com.tvfootballhd.liveandstream.R;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MatchFragment extends Fragment {
    private List<LocalDate> localDateList;
    private TabLayout tabLayout;
    /* access modifiers changed from: private */
    public ViewPager viewPager;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_match, viewGroup, false);
        this.tabLayout = (TabLayout) inflate.findViewById(R.id.tab_layout);
        this.viewPager = (ViewPager) inflate.findViewById(R.id.pager);
        this.localDateList = new ArrayList();
        create15DaysLocalDateList();
        createTab();
        this.tabLayout.setTabMode(0);
        this.viewPager.setAdapter(new MatchViewPagerAdapter(getChildFragmentManager(), this.tabLayout.getTabCount(), getContext(), this.localDateList));
        this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tabLayout));
        this.tabLayout.setOnTabSelectedListener((TabLayout.OnTabSelectedListener) new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabSelected(TabLayout.Tab tab) {
                MatchFragment.this.viewPager.setCurrentItem(tab.getPosition());
            }
        });
        this.tabLayout.getTabAt(2).select();
        this.tabLayout.getTabAt(2).setText((CharSequence) "Today");
        this.tabLayout.getTabAt(3).setText((CharSequence) "Tomorrow");
        this.tabLayout.getTabAt(1).setText((CharSequence) "Yesterday");
        return inflate;
    }

    private void createTab() {
        if (Build.VERSION.SDK_INT >= 26) {
            DateTimeFormatter ofPattern = DateTimeFormatter.ofPattern("EEE dd MMM", Locale.ENGLISH);
            for (int i = 0; i < this.localDateList.size(); i++) {
                TabLayout tabLayout2 = this.tabLayout;
                tabLayout2.addTab(tabLayout2.newTab().setText((CharSequence) this.localDateList.get(i).format(ofPattern)));
            }
        }
    }

    private void create15DaysLocalDateList() {
        this.localDateList.clear();
        if (Build.VERSION.SDK_INT >= 26) {
            LocalDate now = LocalDate.now();
            for (int i = 2; i > 0; i--) {
                this.localDateList.add(now.minusDays((long) i));
                now = LocalDate.now();
            }
            LocalDate now2 = LocalDate.now();
            for (int i2 = 0; i2 < 5; i2++) {
                this.localDateList.add(now2.plusDays((long) i2));
                now2 = LocalDate.now();
            }
        }
    }
}
