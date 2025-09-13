package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import com.airbnb.lottie.LottieAnimationView;
import com.tvfootballhd.liveandstream.R;

public final class FragmentStandingBinding implements ViewBinding {
    public final LottieAnimationView lottiId;
    public final NothingFoundBinding nothingFoundLL;
    private final FrameLayout rootView;
    public final LinearLayout standingMainLL;
    public final RecyclerView standingRecyclerView;

    private FragmentStandingBinding(FrameLayout frameLayout, LottieAnimationView lottieAnimationView, NothingFoundBinding nothingFoundBinding, LinearLayout linearLayout, RecyclerView recyclerView) {
        this.rootView = frameLayout;
        this.lottiId = lottieAnimationView;
        this.nothingFoundLL = nothingFoundBinding;
        this.standingMainLL = linearLayout;
        this.standingRecyclerView = recyclerView;
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static FragmentStandingBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentStandingBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_standing, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:2:0x000b, code lost:
        r0 = com.livefoota.footballpro.R.id.nothingFoundLL;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.FragmentStandingBinding bind(android.view.View r8) {
        /*
            int r0 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r8, r0)
            r4 = r1
            com.airbnb.lottie.LottieAnimationView r4 = (com.airbnb.lottie.LottieAnimationView) r4
            if (r4 == 0) goto L_0x0037
            int r0 = com.livefoota.footballpro.R.id.nothingFoundLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r8, r0)
            if (r1 == 0) goto L_0x0037
            com.livefoota.footballpro.databinding.NothingFoundBinding r5 = com.livefoota.footballpro.databinding.NothingFoundBinding.bind(r1)
            int r0 = com.livefoota.footballpro.R.id.standingMainLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r8, r0)
            r6 = r1
            android.widget.LinearLayout r6 = (android.widget.LinearLayout) r6
            if (r6 == 0) goto L_0x0037
            int r0 = com.livefoota.footballpro.R.id.standingRecyclerView
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r8, r0)
            r7 = r1
            androidx.recyclerview.widget.RecyclerView r7 = (androidx.recyclerview.widget.RecyclerView) r7
            if (r7 == 0) goto L_0x0037
            com.livefoota.footballpro.databinding.FragmentStandingBinding r0 = new com.livefoota.footballpro.databinding.FragmentStandingBinding
            r3 = r8
            android.widget.FrameLayout r3 = (android.widget.FrameLayout) r3
            r2 = r0
            r2.<init>(r3, r4, r5, r6, r7)
            return r0
        L_0x0037:
            android.content.res.Resources r8 = r8.getResources()
            java.lang.String r8 = r8.getResourceName(r0)
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "Missing required view with ID: "
            java.lang.String r8 = r1.concat(r8)
            r0.<init>(r8)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.FragmentStandingBinding.bind(android.view.View):com.livefoota.footballpro.databinding.FragmentStandingBinding");
    }
}
