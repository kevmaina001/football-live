package com.tvfootballhd.liveandstream.Adapter;

import android.content.Context;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import java.util.ArrayList;

public class ScheduleDetailsViewPagerAdapter extends FragmentStatePagerAdapter {
    Context context;
    ArrayList<Fragment> fragmentArrayList;

    public ScheduleDetailsViewPagerAdapter(FragmentManager fragmentManager, Context context2, ArrayList<Fragment> arrayList) {
        super(fragmentManager);
        this.context = context2;
        this.fragmentArrayList = arrayList;
    }

    public Fragment getItem(int i) {
        return this.fragmentArrayList.get(i);
    }

    public int getCount() {
        return this.fragmentArrayList.size();
    }
}
