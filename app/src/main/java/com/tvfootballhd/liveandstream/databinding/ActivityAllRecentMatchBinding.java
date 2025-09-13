package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.airbnb.lottie.LottieAnimationView;
import com.tvfootballhd.liveandstream.R;

public final class ActivityAllRecentMatchBinding implements ViewBinding {
    public final LinearLayout bannerLinear;
    public final RelativeLayout headingLL;
    public final View line1;
    public final LottieAnimationView lottiId;
    public final RecyclerView recyclerView;
    private final RelativeLayout rootView;
    public final ToolbarOneBinding toolbar;

    private ActivityAllRecentMatchBinding(RelativeLayout relativeLayout, LinearLayout linearLayout, RelativeLayout relativeLayout2, View view, LottieAnimationView lottieAnimationView, RecyclerView recyclerView2, ToolbarOneBinding toolbarOneBinding) {
        this.rootView = relativeLayout;
        this.bannerLinear = linearLayout;
        this.headingLL = relativeLayout2;
        this.line1 = view;
        this.lottiId = lottieAnimationView;
        this.recyclerView = recyclerView2;
        this.toolbar = toolbarOneBinding;
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static ActivityAllRecentMatchBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static ActivityAllRecentMatchBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.activity_all_recent_match, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0034, code lost:
        r0 = com.livefoota.footballpro.R.id.toolbar;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0016, code lost:
        r0 = com.livefoota.footballpro.R.id.line1;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.ActivityAllRecentMatchBinding bind(android.view.View r10) {
        /*
            int r0 = com.livefoota.footballpro.R.id.banner_linear
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r10, r0)
            r4 = r1
            android.widget.LinearLayout r4 = (android.widget.LinearLayout) r4
            if (r4 == 0) goto L_0x004a
            int r0 = com.livefoota.footballpro.R.id.headingLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r10, r0)
            r5 = r1
            android.widget.RelativeLayout r5 = (android.widget.RelativeLayout) r5
            if (r5 == 0) goto L_0x004a
            int r0 = com.livefoota.footballpro.R.id.line1
            android.view.View r6 = androidx.viewbinding.ViewBindings.findChildViewById(r10, r0)
            if (r6 == 0) goto L_0x004a
            int r0 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r10, r0)
            r7 = r1
            com.airbnb.lottie.LottieAnimationView r7 = (com.airbnb.lottie.LottieAnimationView) r7
            if (r7 == 0) goto L_0x004a
            int r0 = com.livefoota.footballpro.R.id.recyclerView
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r10, r0)
            r8 = r1
            androidx.recyclerview.widget.RecyclerView r8 = (androidx.recyclerview.widget.RecyclerView) r8
            if (r8 == 0) goto L_0x004a
            int r0 = com.livefoota.footballpro.R.id.toolbar
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r10, r0)
            if (r1 == 0) goto L_0x004a
            com.livefoota.footballpro.databinding.ToolbarOneBinding r9 = com.livefoota.footballpro.databinding.ToolbarOneBinding.bind(r1)
            com.livefoota.footballpro.databinding.ActivityAllRecentMatchBinding r0 = new com.livefoota.footballpro.databinding.ActivityAllRecentMatchBinding
            r3 = r10
            android.widget.RelativeLayout r3 = (android.widget.RelativeLayout) r3
            r2 = r0
            r2.<init>(r3, r4, r5, r6, r7, r8, r9)
            return r0
        L_0x004a:
            android.content.res.Resources r10 = r10.getResources()
            java.lang.String r10 = r10.getResourceName(r0)
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "Missing required view with ID: "
            java.lang.String r10 = r1.concat(r10)
            r0.<init>(r10)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.ActivityAllRecentMatchBinding.bind(android.view.View):com.livefoota.footballpro.databinding.ActivityAllRecentMatchBinding");
    }
}
