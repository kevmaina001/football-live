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

public final class FragmentSquadBinding implements ViewBinding {
    public final LottieAnimationView lottiId;
    public final LinearLayout mainLL;
    public final NothingFoundBinding nothingFoundLL;
    public final RecyclerView recyclerView;
    private final FrameLayout rootView;

    private FragmentSquadBinding(FrameLayout frameLayout, LottieAnimationView lottieAnimationView, LinearLayout linearLayout, NothingFoundBinding nothingFoundBinding, RecyclerView recyclerView2) {
        this.rootView = frameLayout;
        this.lottiId = lottieAnimationView;
        this.mainLL = linearLayout;
        this.nothingFoundLL = nothingFoundBinding;
        this.recyclerView = recyclerView2;
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static FragmentSquadBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentSquadBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_squad, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0016, code lost:
        r0 = com.livefoota.footballpro.R.id.nothingFoundLL;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.FragmentSquadBinding bind(android.view.View r8) {
        /*
            int r0 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r8, r0)
            r4 = r1
            com.airbnb.lottie.LottieAnimationView r4 = (com.airbnb.lottie.LottieAnimationView) r4
            if (r4 == 0) goto L_0x0037
            int r0 = com.livefoota.footballpro.R.id.mainLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r8, r0)
            r5 = r1
            android.widget.LinearLayout r5 = (android.widget.LinearLayout) r5
            if (r5 == 0) goto L_0x0037
            int r0 = com.livefoota.footballpro.R.id.nothingFoundLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r8, r0)
            if (r1 == 0) goto L_0x0037
            com.livefoota.footballpro.databinding.NothingFoundBinding r6 = com.livefoota.footballpro.databinding.NothingFoundBinding.bind(r1)
            int r0 = com.livefoota.footballpro.R.id.recyclerView
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r8, r0)
            r7 = r1
            androidx.recyclerview.widget.RecyclerView r7 = (androidx.recyclerview.widget.RecyclerView) r7
            if (r7 == 0) goto L_0x0037
            com.livefoota.footballpro.databinding.FragmentSquadBinding r0 = new com.livefoota.footballpro.databinding.FragmentSquadBinding
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
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.FragmentSquadBinding.bind(android.view.View):com.livefoota.footballpro.databinding.FragmentSquadBinding");
    }
}
