package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.tvfootballhd.liveandstream.R;

public final class ActivityMainBinding implements ViewBinding {
    public final LinearLayout bannerLinear;
    public final BottomNavigationView mainBottomNavigationId;
    public final FrameLayout mainFrameLayout;
    private final RelativeLayout rootView;

    private ActivityMainBinding(RelativeLayout relativeLayout, LinearLayout linearLayout, BottomNavigationView bottomNavigationView, FrameLayout frameLayout) {
        this.rootView = relativeLayout;
        this.bannerLinear = linearLayout;
        this.mainBottomNavigationId = bottomNavigationView;
        this.mainFrameLayout = frameLayout;
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static ActivityMainBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static ActivityMainBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.activity_main, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static ActivityMainBinding bind(View view) {
        int i = R.id.banner_linear;
        LinearLayout linearLayout = (LinearLayout) ViewBindings.findChildViewById(view, i);
        if (linearLayout != null) {
            i = R.id.mainBottomNavigationId;
            BottomNavigationView bottomNavigationView = (BottomNavigationView) ViewBindings.findChildViewById(view, i);
            if (bottomNavigationView != null) {
                i = R.id.mainFrameLayout;
                FrameLayout frameLayout = (FrameLayout) ViewBindings.findChildViewById(view, i);
                if (frameLayout != null) {
                    return new ActivityMainBinding((RelativeLayout) view, linearLayout, bottomNavigationView, frameLayout);
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
