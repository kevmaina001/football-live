package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager.widget.ViewPager;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.tabs.TabLayout;
import com.tvfootballhd.liveandstream.R;

public final class ActivityLiveDetailsBinding implements ViewBinding {
    public final LinearLayout LeagueScheduleActivityscoreId;
    public final ImageView LeagueScheduleActivitystatisticsAwayIconId;
    public final TextView LeagueScheduleActivitystatisticsAwayNameId;
    public final TextView LeagueScheduleActivitystatisticsGoalTvId;
    public final ImageView LeagueScheduleActivitystatisticsHomeIconId;
    public final TextView LeagueScheduleActivitystatisticsHomeNameId;
    public final TextView LeagueScheduleActivitystatisticsTimeTvId;
    public final LinearLayout awayLL;
    public final LinearLayout bannerLinear;
    public final RelativeLayout headingLL;
    public final LinearLayout homeLL;
    public final TabLayout leagueScheduleDetailsTabLayout;
    public final LinearLayout leagueScheduleHeadingLL;
    public final View line1;
    public final LottieAnimationView lottiId;
    private final RelativeLayout rootView;
    public final ViewPager scheduleDetailsViewPager;
    public final ToolbarOneBinding toolbar;

    private ActivityLiveDetailsBinding(RelativeLayout relativeLayout, LinearLayout linearLayout, ImageView imageView, TextView textView, TextView textView2, ImageView imageView2, TextView textView3, TextView textView4, LinearLayout linearLayout2, LinearLayout linearLayout3, RelativeLayout relativeLayout2, LinearLayout linearLayout4, TabLayout tabLayout, LinearLayout linearLayout5, View view, LottieAnimationView lottieAnimationView, ViewPager viewPager, ToolbarOneBinding toolbarOneBinding) {
        this.rootView = relativeLayout;
        this.LeagueScheduleActivityscoreId = linearLayout;
        this.LeagueScheduleActivitystatisticsAwayIconId = imageView;
        this.LeagueScheduleActivitystatisticsAwayNameId = textView;
        this.LeagueScheduleActivitystatisticsGoalTvId = textView2;
        this.LeagueScheduleActivitystatisticsHomeIconId = imageView2;
        this.LeagueScheduleActivitystatisticsHomeNameId = textView3;
        this.LeagueScheduleActivitystatisticsTimeTvId = textView4;
        this.awayLL = linearLayout2;
        this.bannerLinear = linearLayout3;
        this.headingLL = relativeLayout2;
        this.homeLL = linearLayout4;
        this.leagueScheduleDetailsTabLayout = tabLayout;
        this.leagueScheduleHeadingLL = linearLayout5;
        this.line1 = view;
        this.lottiId = lottieAnimationView;
        this.scheduleDetailsViewPager = viewPager;
        this.toolbar = toolbarOneBinding;
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static ActivityLiveDetailsBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static ActivityLiveDetailsBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.activity_live_details, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:26:0x0093, code lost:
        r1 = com.livefoota.footballpro.R.id.line1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00b3, code lost:
        r1 = com.livefoota.footballpro.R.id.toolbar;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.ActivityLiveDetailsBinding bind(android.view.View r22) {
        /*
            r0 = r22
            int r1 = com.livefoota.footballpro.R.id.LeagueScheduleActivityscoreId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r5 = r2
            android.widget.LinearLayout r5 = (android.widget.LinearLayout) r5
            if (r5 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.LeagueScheduleActivitystatisticsAwayIconId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r6 = r2
            android.widget.ImageView r6 = (android.widget.ImageView) r6
            if (r6 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.LeagueScheduleActivitystatisticsAwayNameId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r7 = r2
            android.widget.TextView r7 = (android.widget.TextView) r7
            if (r7 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.LeagueScheduleActivitystatisticsGoalTvId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r8 = r2
            android.widget.TextView r8 = (android.widget.TextView) r8
            if (r8 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.LeagueScheduleActivitystatisticsHomeIconId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r9 = r2
            android.widget.ImageView r9 = (android.widget.ImageView) r9
            if (r9 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.LeagueScheduleActivitystatisticsHomeNameId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r10 = r2
            android.widget.TextView r10 = (android.widget.TextView) r10
            if (r10 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.LeagueScheduleActivitystatisticsTimeTvId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r11 = r2
            android.widget.TextView r11 = (android.widget.TextView) r11
            if (r11 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.awayLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r12 = r2
            android.widget.LinearLayout r12 = (android.widget.LinearLayout) r12
            if (r12 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.banner_linear
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r13 = r2
            android.widget.LinearLayout r13 = (android.widget.LinearLayout) r13
            if (r13 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.headingLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r14 = r2
            android.widget.RelativeLayout r14 = (android.widget.RelativeLayout) r14
            if (r14 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.homeLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r15 = r2
            android.widget.LinearLayout r15 = (android.widget.LinearLayout) r15
            if (r15 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.leagueScheduleDetailsTabLayout
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r16 = r2
            com.google.android.material.tabs.TabLayout r16 = (com.google.android.material.tabs.TabLayout) r16
            if (r16 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.leagueScheduleHeadingLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r17 = r2
            android.widget.LinearLayout r17 = (android.widget.LinearLayout) r17
            if (r17 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.line1
            android.view.View r18 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            if (r18 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r19 = r2
            com.airbnb.lottie.LottieAnimationView r19 = (com.airbnb.lottie.LottieAnimationView) r19
            if (r19 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.scheduleDetailsViewPager
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r20 = r2
            androidx.viewpager.widget.ViewPager r20 = (androidx.viewpager.widget.ViewPager) r20
            if (r20 == 0) goto L_0x00c9
            int r1 = com.livefoota.footballpro.R.id.toolbar
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            if (r2 == 0) goto L_0x00c9
            com.livefoota.footballpro.databinding.ToolbarOneBinding r21 = com.livefoota.footballpro.databinding.ToolbarOneBinding.bind(r2)
            com.livefoota.footballpro.databinding.ActivityLiveDetailsBinding r1 = new com.livefoota.footballpro.databinding.ActivityLiveDetailsBinding
            r3 = r1
            r4 = r0
            android.widget.RelativeLayout r4 = (android.widget.RelativeLayout) r4
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21)
            return r1
        L_0x00c9:
            android.content.res.Resources r0 = r22.getResources()
            java.lang.String r0 = r0.getResourceName(r1)
            java.lang.NullPointerException r1 = new java.lang.NullPointerException
            java.lang.String r2 = "Missing required view with ID: "
            java.lang.String r0 = r2.concat(r0)
            r1.<init>(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.ActivityLiveDetailsBinding.bind(android.view.View):com.livefoota.footballpro.databinding.ActivityLiveDetailsBinding");
    }
}
