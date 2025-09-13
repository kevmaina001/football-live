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

public final class ActivityPlayerDetailsBinding implements ViewBinding {
    public final LinearLayout bannerLinear;
    public final RelativeLayout headingLL;
    public final TabLayout leagueScheduleDetailsTabLayout;
    public final LinearLayout leagueScheduleHeadingLL;
    public final View line1;
    public final LottieAnimationView lottiId;
    public final ImageView playerImageViewId;
    public final TextView playerNameId;
    private final RelativeLayout rootView;
    public final ImageView teamIconId;
    public final TextView teamNameId;
    public final ToolbarOneBinding toolbar;
    public final ViewPager viewPagerId;

    private ActivityPlayerDetailsBinding(RelativeLayout relativeLayout, LinearLayout linearLayout, RelativeLayout relativeLayout2, TabLayout tabLayout, LinearLayout linearLayout2, View view, LottieAnimationView lottieAnimationView, ImageView imageView, TextView textView, ImageView imageView2, TextView textView2, ToolbarOneBinding toolbarOneBinding, ViewPager viewPager) {
        this.rootView = relativeLayout;
        this.bannerLinear = linearLayout;
        this.headingLL = relativeLayout2;
        this.leagueScheduleDetailsTabLayout = tabLayout;
        this.leagueScheduleHeadingLL = linearLayout2;
        this.line1 = view;
        this.lottiId = lottieAnimationView;
        this.playerImageViewId = imageView;
        this.playerNameId = textView;
        this.teamIconId = imageView2;
        this.teamNameId = textView2;
        this.toolbar = toolbarOneBinding;
        this.viewPagerId = viewPager;
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static ActivityPlayerDetailsBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static ActivityPlayerDetailsBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.activity_player_details, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006d, code lost:
        r1 = com.livefoota.footballpro.R.id.toolbar;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002e, code lost:
        r1 = com.livefoota.footballpro.R.id.line1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.ActivityPlayerDetailsBinding bind(android.view.View r17) {
        /*
            r0 = r17
            int r1 = com.livefoota.footballpro.R.id.banner_linear
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r5 = r2
            android.widget.LinearLayout r5 = (android.widget.LinearLayout) r5
            if (r5 == 0) goto L_0x008f
            int r1 = com.livefoota.footballpro.R.id.headingLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r6 = r2
            android.widget.RelativeLayout r6 = (android.widget.RelativeLayout) r6
            if (r6 == 0) goto L_0x008f
            int r1 = com.livefoota.footballpro.R.id.leagueScheduleDetailsTabLayout
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r7 = r2
            com.google.android.material.tabs.TabLayout r7 = (com.google.android.material.tabs.TabLayout) r7
            if (r7 == 0) goto L_0x008f
            int r1 = com.livefoota.footballpro.R.id.leagueScheduleHeadingLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r8 = r2
            android.widget.LinearLayout r8 = (android.widget.LinearLayout) r8
            if (r8 == 0) goto L_0x008f
            int r1 = com.livefoota.footballpro.R.id.line1
            android.view.View r9 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            if (r9 == 0) goto L_0x008f
            int r1 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r10 = r2
            com.airbnb.lottie.LottieAnimationView r10 = (com.airbnb.lottie.LottieAnimationView) r10
            if (r10 == 0) goto L_0x008f
            int r1 = com.livefoota.footballpro.R.id.playerImageViewId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r11 = r2
            android.widget.ImageView r11 = (android.widget.ImageView) r11
            if (r11 == 0) goto L_0x008f
            int r1 = com.livefoota.footballpro.R.id.playerNameId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r12 = r2
            android.widget.TextView r12 = (android.widget.TextView) r12
            if (r12 == 0) goto L_0x008f
            int r1 = com.livefoota.footballpro.R.id.teamIconId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r13 = r2
            android.widget.ImageView r13 = (android.widget.ImageView) r13
            if (r13 == 0) goto L_0x008f
            int r1 = com.livefoota.footballpro.R.id.teamNameId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r14 = r2
            android.widget.TextView r14 = (android.widget.TextView) r14
            if (r14 == 0) goto L_0x008f
            int r1 = com.livefoota.footballpro.R.id.toolbar
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            if (r2 == 0) goto L_0x008f
            com.livefoota.footballpro.databinding.ToolbarOneBinding r15 = com.livefoota.footballpro.databinding.ToolbarOneBinding.bind(r2)
            int r1 = com.livefoota.footballpro.R.id.viewPagerId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r16 = r2
            androidx.viewpager.widget.ViewPager r16 = (androidx.viewpager.widget.ViewPager) r16
            if (r16 == 0) goto L_0x008f
            com.livefoota.footballpro.databinding.ActivityPlayerDetailsBinding r1 = new com.livefoota.footballpro.databinding.ActivityPlayerDetailsBinding
            r4 = r0
            android.widget.RelativeLayout r4 = (android.widget.RelativeLayout) r4
            r3 = r1
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16)
            return r1
        L_0x008f:
            android.content.res.Resources r0 = r17.getResources()
            java.lang.String r0 = r0.getResourceName(r1)
            java.lang.NullPointerException r1 = new java.lang.NullPointerException
            java.lang.String r2 = "Missing required view with ID: "
            java.lang.String r0 = r2.concat(r0)
            r1.<init>(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.ActivityPlayerDetailsBinding.bind(android.view.View):com.livefoota.footballpro.databinding.ActivityPlayerDetailsBinding");
    }
}
