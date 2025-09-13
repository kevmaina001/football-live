package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import android.os.Build;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MatchViewPagerAdapter extends FragmentPagerAdapter {
    Context context;
    List<LocalDate> date;
    int mNumOfTabs;

    public MatchViewPagerAdapter(FragmentManager fragmentManager, int i, Context context2, List<LocalDate> list) {
        super(fragmentManager);
        this.mNumOfTabs = i;
        this.context = context2;
        this.date = list;
    }

    public Fragment getItem(int i) {
        if (Build.VERSION.SDK_INT >= 26) {
            return new FragmentTab(this.date.get(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd")), this.context);
        }
        return new FragmentTab("2022-08-19", this.context);
    }

    public int getCount() {
        return this.mNumOfTabs;
    }
}
