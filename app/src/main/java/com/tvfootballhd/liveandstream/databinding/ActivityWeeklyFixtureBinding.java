package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.airbnb.lottie.LottieAnimationView;
import com.tvfootballhd.liveandstream.R;

public final class ActivityWeeklyFixtureBinding implements ViewBinding {
    public final LinearLayout bannerLinear;
    public final RelativeLayout headingLL;
    public final TextView leagueCountryNameId;
    public final ImageView leagueIconId;
    public final TextView leagueNameId;
    public final View line1;
    public final LottieAnimationView lottiId;
    public final LinearLayout mainLL;
    public final LinearLayout nothingFoundLL;
    public final RecyclerView recyclerView;
    private final RelativeLayout rootView;
    public final ToolbarOneBinding toolbar;

    private ActivityWeeklyFixtureBinding(RelativeLayout relativeLayout, LinearLayout linearLayout, RelativeLayout relativeLayout2, TextView textView, ImageView imageView, TextView textView2, View view, LottieAnimationView lottieAnimationView, LinearLayout linearLayout2, LinearLayout linearLayout3, RecyclerView recyclerView2, ToolbarOneBinding toolbarOneBinding) {
        this.rootView = relativeLayout;
        this.bannerLinear = linearLayout;
        this.headingLL = relativeLayout2;
        this.leagueCountryNameId = textView;
        this.leagueIconId = imageView;
        this.leagueNameId = textView2;
        this.line1 = view;
        this.lottiId = lottieAnimationView;
        this.mainLL = linearLayout2;
        this.nothingFoundLL = linearLayout3;
        this.recyclerView = recyclerView2;
        this.toolbar = toolbarOneBinding;
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static ActivityWeeklyFixtureBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static ActivityWeeklyFixtureBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.activity_weekly_fixture, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0037, code lost:
        r0 = com.livefoota.footballpro.R.id.line1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x006b, code lost:
        r0 = com.livefoota.footballpro.R.id.toolbar;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.ActivityWeeklyFixtureBinding bind(android.view.View r15) {
        /*
            int r0 = com.livefoota.footballpro.R.id.banner_linear
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r4 = r1
            android.widget.LinearLayout r4 = (android.widget.LinearLayout) r4
            if (r4 == 0) goto L_0x0081
            int r0 = com.livefoota.footballpro.R.id.headingLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r5 = r1
            android.widget.RelativeLayout r5 = (android.widget.RelativeLayout) r5
            if (r5 == 0) goto L_0x0081
            int r0 = com.livefoota.footballpro.R.id.leagueCountryNameId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r6 = r1
            android.widget.TextView r6 = (android.widget.TextView) r6
            if (r6 == 0) goto L_0x0081
            int r0 = com.livefoota.footballpro.R.id.leagueIconId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r7 = r1
            android.widget.ImageView r7 = (android.widget.ImageView) r7
            if (r7 == 0) goto L_0x0081
            int r0 = com.livefoota.footballpro.R.id.leagueNameId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r8 = r1
            android.widget.TextView r8 = (android.widget.TextView) r8
            if (r8 == 0) goto L_0x0081
            int r0 = com.livefoota.footballpro.R.id.line1
            android.view.View r9 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            if (r9 == 0) goto L_0x0081
            int r0 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r10 = r1
            com.airbnb.lottie.LottieAnimationView r10 = (com.airbnb.lottie.LottieAnimationView) r10
            if (r10 == 0) goto L_0x0081
            int r0 = com.livefoota.footballpro.R.id.mainLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r11 = r1
            android.widget.LinearLayout r11 = (android.widget.LinearLayout) r11
            if (r11 == 0) goto L_0x0081
            int r0 = com.livefoota.footballpro.R.id.nothingFoundLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r12 = r1
            android.widget.LinearLayout r12 = (android.widget.LinearLayout) r12
            if (r12 == 0) goto L_0x0081
            int r0 = com.livefoota.footballpro.R.id.recyclerView
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r13 = r1
            androidx.recyclerview.widget.RecyclerView r13 = (androidx.recyclerview.widget.RecyclerView) r13
            if (r13 == 0) goto L_0x0081
            int r0 = com.livefoota.footballpro.R.id.toolbar
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            if (r1 == 0) goto L_0x0081
            com.livefoota.footballpro.databinding.ToolbarOneBinding r14 = com.livefoota.footballpro.databinding.ToolbarOneBinding.bind(r1)
            com.livefoota.footballpro.databinding.ActivityWeeklyFixtureBinding r0 = new com.livefoota.footballpro.databinding.ActivityWeeklyFixtureBinding
            r3 = r15
            android.widget.RelativeLayout r3 = (android.widget.RelativeLayout) r3
            r2 = r0
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14)
            return r0
        L_0x0081:
            android.content.res.Resources r15 = r15.getResources()
            java.lang.String r15 = r15.getResourceName(r0)
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "Missing required view with ID: "
            java.lang.String r15 = r1.concat(r15)
            r0.<init>(r15)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.ActivityWeeklyFixtureBinding.bind(android.view.View):com.livefoota.footballpro.databinding.ActivityWeeklyFixtureBinding");
    }
}
