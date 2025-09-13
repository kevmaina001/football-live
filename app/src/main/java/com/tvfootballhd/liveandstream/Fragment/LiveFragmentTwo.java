package com.tvfootballhd.liveandstream.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.tvfootballhd.liveandstream.AdsManager;
import com.google.android.material.tabs.TabLayout;
import com.tvfootballhd.liveandstream.Adapter.ScheduleDetailsViewPagerAdapter;
import com.tvfootballhd.liveandstream.R;

import java.util.ArrayList;

public class LiveFragmentTwo extends Fragment {
    private TabLayout tabLayout;
    /* access modifiers changed from: private */
    public ViewPager viewPager;

    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_live_two, viewGroup, false);
        AdsManager.LoadInterstitialAd(getActivity());
        this.tabLayout = (TabLayout) inflate.findViewById(R.id.tab_layout);
        this.viewPager = (ViewPager) inflate.findViewById(R.id.pager);
        TabLayout tabLayout2 = this.tabLayout;
        tabLayout2.addTab(tabLayout2.newTab().setText((CharSequence) "Live Score"));
        //TabLayout tabLayout3 = this.tabLayout;
       // tabLayout3.addTab(tabLayout3.newTab().setText((CharSequence) "Live"));
        ArrayList arrayList = new ArrayList();
        arrayList.add(new LiveFragment());
        arrayList.add(new VideoFragment());
        this.viewPager.setAdapter(new ScheduleDetailsViewPagerAdapter(getActivity().getSupportFragmentManager(), getContext(), arrayList));
        this.viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(this.tabLayout));
        this.tabLayout.setOnTabSelectedListener((TabLayout.OnTabSelectedListener) new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {
            }

            public void onTabUnselected(TabLayout.Tab tab) {
            }

            public void onTabSelected(TabLayout.Tab tab) {
                LiveFragmentTwo.this.viewPager.setCurrentItem(tab.getPosition());
            }
        });
        return inflate;
    }
}
