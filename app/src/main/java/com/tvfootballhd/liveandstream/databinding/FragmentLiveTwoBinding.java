package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import androidx.viewpager.widget.ViewPager;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.tabs.TabLayout;
import com.tvfootballhd.liveandstream.R;

public final class FragmentLiveTwoBinding implements ViewBinding {
    public final View headingLine;
    public final RelativeLayout headingRl;
    public final LottieAnimationView lottiId;
    public final ImageView menuIcon;
    public final ViewPager pager;
    private final FrameLayout rootView;
    public final TabLayout tabLayout;

    private FragmentLiveTwoBinding(FrameLayout frameLayout, View view, RelativeLayout relativeLayout, LottieAnimationView lottieAnimationView, ImageView imageView, ViewPager viewPager, TabLayout tabLayout2) {
        this.rootView = frameLayout;
        this.headingLine = view;
        this.headingRl = relativeLayout;
        this.lottiId = lottieAnimationView;
        this.menuIcon = imageView;
        this.pager = viewPager;
        this.tabLayout = tabLayout2;
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static FragmentLiveTwoBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentLiveTwoBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_live_two, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static FragmentLiveTwoBinding bind(View view) {
        int i = R.id.headingLine;
        View findChildViewById = ViewBindings.findChildViewById(view, i);
        if (findChildViewById != null) {
            i = R.id.headingRl;
            RelativeLayout relativeLayout = (RelativeLayout) ViewBindings.findChildViewById(view, i);
            if (relativeLayout != null) {
                i = R.id.lottiId;
                LottieAnimationView lottieAnimationView = (LottieAnimationView) ViewBindings.findChildViewById(view, i);
                if (lottieAnimationView != null) {
                    i = R.id.menuIcon;
                    ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
                    if (imageView != null) {
                        i = R.id.pager;
                        ViewPager viewPager = (ViewPager) ViewBindings.findChildViewById(view, i);
                        if (viewPager != null) {
                            i = R.id.tab_layout;
                            TabLayout tabLayout2 = (TabLayout) ViewBindings.findChildViewById(view, i);
                            if (tabLayout2 != null) {
                                return new FragmentLiveTwoBinding((FrameLayout) view, findChildViewById, relativeLayout, lottieAnimationView, imageView, viewPager, tabLayout2);
                            }
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
