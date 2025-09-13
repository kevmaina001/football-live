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

public final class ActivityTeamDetailsBinding implements ViewBinding {
    public final LinearLayout bannerLinear;
    public final RelativeLayout headingLL;
    public final ImageView imageView;
    public final LinearLayout leagueScheduleHeadingLL;
    public final View line1;
    public final LottieAnimationView lottiId;
    private final RelativeLayout rootView;
    public final TabLayout tabLayout;
    public final TextView teamNameId;
    public final ToolbarOneBinding toolbar;
    public final ViewPager viewPagerId;

    private ActivityTeamDetailsBinding(RelativeLayout relativeLayout, LinearLayout linearLayout, RelativeLayout relativeLayout2, ImageView imageView2, LinearLayout linearLayout2, View view, LottieAnimationView lottieAnimationView, TabLayout tabLayout2, TextView textView, ToolbarOneBinding toolbarOneBinding, ViewPager viewPager) {
        this.rootView = relativeLayout;
        this.bannerLinear = linearLayout;
        this.headingLL = relativeLayout2;
        this.imageView = imageView2;
        this.leagueScheduleHeadingLL = linearLayout2;
        this.line1 = view;
        this.lottiId = lottieAnimationView;
        this.tabLayout = tabLayout2;
        this.teamNameId = textView;
        this.toolbar = toolbarOneBinding;
        this.viewPagerId = viewPager;
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static ActivityTeamDetailsBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static ActivityTeamDetailsBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.activity_team_details, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0055, code lost:
        r0 = com.livefoota.footballpro.R.id.toolbar;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002c, code lost:
        r0 = com.livefoota.footballpro.R.id.line1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.ActivityTeamDetailsBinding bind(android.view.View r14) {
        /*
            int r0 = com.livefoota.footballpro.R.id.banner_linear
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r4 = r1
            android.widget.LinearLayout r4 = (android.widget.LinearLayout) r4
            if (r4 == 0) goto L_0x0076
            int r0 = com.livefoota.footballpro.R.id.headingLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r5 = r1
            android.widget.RelativeLayout r5 = (android.widget.RelativeLayout) r5
            if (r5 == 0) goto L_0x0076
            int r0 = com.livefoota.footballpro.R.id.imageView
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r6 = r1
            android.widget.ImageView r6 = (android.widget.ImageView) r6
            if (r6 == 0) goto L_0x0076
            int r0 = com.livefoota.footballpro.R.id.leagueScheduleHeadingLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r7 = r1
            android.widget.LinearLayout r7 = (android.widget.LinearLayout) r7
            if (r7 == 0) goto L_0x0076
            int r0 = com.livefoota.footballpro.R.id.line1
            android.view.View r8 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            if (r8 == 0) goto L_0x0076
            int r0 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r9 = r1
            com.airbnb.lottie.LottieAnimationView r9 = (com.airbnb.lottie.LottieAnimationView) r9
            if (r9 == 0) goto L_0x0076
            int r0 = com.livefoota.footballpro.R.id.tab_layout
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r10 = r1
            com.google.android.material.tabs.TabLayout r10 = (com.google.android.material.tabs.TabLayout) r10
            if (r10 == 0) goto L_0x0076
            int r0 = com.livefoota.footballpro.R.id.teamNameId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r11 = r1
            android.widget.TextView r11 = (android.widget.TextView) r11
            if (r11 == 0) goto L_0x0076
            int r0 = com.livefoota.footballpro.R.id.toolbar
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            if (r1 == 0) goto L_0x0076
            com.livefoota.footballpro.databinding.ToolbarOneBinding r12 = com.livefoota.footballpro.databinding.ToolbarOneBinding.bind(r1)
            int r0 = com.livefoota.footballpro.R.id.viewPagerId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r13 = r1
            androidx.viewpager.widget.ViewPager r13 = (androidx.viewpager.widget.ViewPager) r13
            if (r13 == 0) goto L_0x0076
            com.livefoota.footballpro.databinding.ActivityTeamDetailsBinding r0 = new com.livefoota.footballpro.databinding.ActivityTeamDetailsBinding
            r3 = r14
            android.widget.RelativeLayout r3 = (android.widget.RelativeLayout) r3
            r2 = r0
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)
            return r0
        L_0x0076:
            android.content.res.Resources r14 = r14.getResources()
            java.lang.String r14 = r14.getResourceName(r0)
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "Missing required view with ID: "
            java.lang.String r14 = r1.concat(r14)
            r0.<init>(r14)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.ActivityTeamDetailsBinding.bind(android.view.View):com.livefoota.footballpro.databinding.ActivityTeamDetailsBinding");
    }
}
