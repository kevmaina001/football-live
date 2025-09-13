package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewbinding.ViewBinding;
import com.airbnb.lottie.LottieAnimationView;
import com.tvfootballhd.liveandstream.R;

public final class FragmentLiveBinding implements ViewBinding {
    public final SwipeRefreshLayout liveLayoutId;
    public final RecyclerView liveRecyclerView;
    public final LottieAnimationView lottiId;
    public final LinearLayout mainLL;
    public final NothingFoundBinding nothingFoundLL;
    private final FrameLayout rootView;

    private FragmentLiveBinding(FrameLayout frameLayout, SwipeRefreshLayout swipeRefreshLayout, RecyclerView recyclerView, LottieAnimationView lottieAnimationView, LinearLayout linearLayout, NothingFoundBinding nothingFoundBinding) {
        this.rootView = frameLayout;
        this.liveLayoutId = swipeRefreshLayout;
        this.liveRecyclerView = recyclerView;
        this.lottiId = lottieAnimationView;
        this.mainLL = linearLayout;
        this.nothingFoundLL = nothingFoundBinding;
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static FragmentLiveBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentLiveBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_live, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002c, code lost:
        r0 = com.livefoota.footballpro.R.id.nothingFoundLL;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.FragmentLiveBinding bind(android.view.View r9) {
        /*
            int r0 = com.livefoota.footballpro.R.id.liveLayoutId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r9, r0)
            r4 = r1
            androidx.swiperefreshlayout.widget.SwipeRefreshLayout r4 = (androidx.swiperefreshlayout.widget.SwipeRefreshLayout) r4
            if (r4 == 0) goto L_0x0042
            int r0 = com.livefoota.footballpro.R.id.liveRecyclerView
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r9, r0)
            r5 = r1
            androidx.recyclerview.widget.RecyclerView r5 = (androidx.recyclerview.widget.RecyclerView) r5
            if (r5 == 0) goto L_0x0042
            int r0 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r9, r0)
            r6 = r1
            com.airbnb.lottie.LottieAnimationView r6 = (com.airbnb.lottie.LottieAnimationView) r6
            if (r6 == 0) goto L_0x0042
            int r0 = com.livefoota.footballpro.R.id.mainLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r9, r0)
            r7 = r1
            android.widget.LinearLayout r7 = (android.widget.LinearLayout) r7
            if (r7 == 0) goto L_0x0042
            int r0 = com.livefoota.footballpro.R.id.nothingFoundLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r9, r0)
            if (r1 == 0) goto L_0x0042
            com.livefoota.footballpro.databinding.NothingFoundBinding r8 = com.livefoota.footballpro.databinding.NothingFoundBinding.bind(r1)
            com.livefoota.footballpro.databinding.FragmentLiveBinding r0 = new com.livefoota.footballpro.databinding.FragmentLiveBinding
            r3 = r9
            android.widget.FrameLayout r3 = (android.widget.FrameLayout) r3
            r2 = r0
            r2.<init>(r3, r4, r5, r6, r7, r8)
            return r0
        L_0x0042:
            android.content.res.Resources r9 = r9.getResources()
            java.lang.String r9 = r9.getResourceName(r0)
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "Missing required view with ID: "
            java.lang.String r9 = r1.concat(r9)
            r0.<init>(r9)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.FragmentLiveBinding.bind(android.view.View):com.livefoota.footballpro.databinding.FragmentLiveBinding");
    }
}
