package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager.widget.ViewPager;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.tabs.TabLayout;
import com.tvfootballhd.liveandstream.R;

public final class ActivityLeagueDetailsBinding implements ViewBinding {
    public final LinearLayout bannerLinear;
    public final RelativeLayout headingLL;
    public final TabLayout leagueScheduleDetailsTabLayout;
    public final LinearLayout leagueScheduleHeadingLL;
    public final View line1;
    public final LottieAnimationView lottiId;
    private final RelativeLayout rootView;
    public final ViewPager scheduleDetailsViewPager;
    public final ToolbarOneBinding toolbar;

    private ActivityLeagueDetailsBinding(RelativeLayout relativeLayout, LinearLayout linearLayout, RelativeLayout relativeLayout2, TabLayout tabLayout, LinearLayout linearLayout2, View view, LottieAnimationView lottieAnimationView, ViewPager viewPager, ToolbarOneBinding toolbarOneBinding) {
        this.rootView = relativeLayout;
        this.bannerLinear = linearLayout;
        this.headingLL = relativeLayout2;
        this.leagueScheduleDetailsTabLayout = tabLayout;
        this.leagueScheduleHeadingLL = linearLayout2;
        this.line1 = view;
        this.lottiId = lottieAnimationView;
        this.scheduleDetailsViewPager = viewPager;
        this.toolbar = toolbarOneBinding;
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static ActivityLeagueDetailsBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static ActivityLeagueDetailsBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.activity_league_details, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:14:0x004a, code lost:
        r0 = com.livefoota.footballpro.R.id.toolbar;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002c, code lost:
        r0 = com.livefoota.footballpro.R.id.line1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.ActivityLeagueDetailsBinding bind(android.view.View r12) {
        /*
            int r0 = com.livefoota.footballpro.R.id.banner_linear
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r12, r0)
            r4 = r1
            android.widget.LinearLayout r4 = (android.widget.LinearLayout) r4
            if (r4 == 0) goto L_0x0060
            int r0 = com.livefoota.footballpro.R.id.headingLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r12, r0)
            r5 = r1
            android.widget.RelativeLayout r5 = (android.widget.RelativeLayout) r5
            if (r5 == 0) goto L_0x0060
            int r0 = com.livefoota.footballpro.R.id.leagueScheduleDetailsTabLayout
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r12, r0)
            r6 = r1
            com.google.android.material.tabs.TabLayout r6 = (com.google.android.material.tabs.TabLayout) r6
            if (r6 == 0) goto L_0x0060
            int r0 = com.livefoota.footballpro.R.id.leagueScheduleHeadingLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r12, r0)
            r7 = r1
            android.widget.LinearLayout r7 = (android.widget.LinearLayout) r7
            if (r7 == 0) goto L_0x0060
            int r0 = com.livefoota.footballpro.R.id.line1
            android.view.View r8 = androidx.viewbinding.ViewBindings.findChildViewById(r12, r0)
            if (r8 == 0) goto L_0x0060
            int r0 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r12, r0)
            r9 = r1
            com.airbnb.lottie.LottieAnimationView r9 = (com.airbnb.lottie.LottieAnimationView) r9
            if (r9 == 0) goto L_0x0060
            int r0 = com.livefoota.footballpro.R.id.scheduleDetailsViewPager
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r12, r0)
            r10 = r1
            androidx.viewpager.widget.ViewPager r10 = (androidx.viewpager.widget.ViewPager) r10
            if (r10 == 0) goto L_0x0060
            int r0 = com.livefoota.footballpro.R.id.toolbar
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r12, r0)
            if (r1 == 0) goto L_0x0060
            com.livefoota.footballpro.databinding.ToolbarOneBinding r11 = com.livefoota.footballpro.databinding.ToolbarOneBinding.bind(r1)
            com.livefoota.footballpro.databinding.ActivityLeagueDetailsBinding r0 = new com.livefoota.footballpro.databinding.ActivityLeagueDetailsBinding
            r3 = r12
            android.widget.RelativeLayout r3 = (android.widget.RelativeLayout) r3
            r2 = r0
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11)
            return r0
        L_0x0060:
            android.content.res.Resources r12 = r12.getResources()
            java.lang.String r12 = r12.getResourceName(r0)
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "Missing required view with ID: "
            java.lang.String r12 = r1.concat(r12)
            r0.<init>(r12)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.ActivityLeagueDetailsBinding.bind(android.view.View):com.livefoota.footballpro.databinding.ActivityLeagueDetailsBinding");
    }
}
