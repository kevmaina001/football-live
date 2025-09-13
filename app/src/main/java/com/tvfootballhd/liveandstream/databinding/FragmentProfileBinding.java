package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import com.airbnb.lottie.LottieAnimationView;
import com.tvfootballhd.liveandstream.R;

public final class FragmentProfileBinding implements ViewBinding {
    public final TextView ageId;
    public final TextView heightId;
    public final LottieAnimationView lottiId;
    public final LinearLayout mainLL;
    public final NothingFoundBinding nothingFoundLL;
    public final TextView positionId;
    public final TextView ratingId;
    private final FrameLayout rootView;
    public final TextView shirtNumberId;
    public final TextView teamNameId;
    public final TextView weightId;

    private FragmentProfileBinding(FrameLayout frameLayout, TextView textView, TextView textView2, LottieAnimationView lottieAnimationView, LinearLayout linearLayout, NothingFoundBinding nothingFoundBinding, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7) {
        this.rootView = frameLayout;
        this.ageId = textView;
        this.heightId = textView2;
        this.lottiId = lottieAnimationView;
        this.mainLL = linearLayout;
        this.nothingFoundLL = nothingFoundBinding;
        this.positionId = textView3;
        this.ratingId = textView4;
        this.shirtNumberId = textView5;
        this.teamNameId = textView6;
        this.weightId = textView7;
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static FragmentProfileBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentProfileBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_profile, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002c, code lost:
        r0 = com.livefoota.footballpro.R.id.nothingFoundLL;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.FragmentProfileBinding bind(android.view.View r14) {
        /*
            int r0 = com.livefoota.footballpro.R.id.ageId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r4 = r1
            android.widget.TextView r4 = (android.widget.TextView) r4
            if (r4 == 0) goto L_0x0079
            int r0 = com.livefoota.footballpro.R.id.heightId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r5 = r1
            android.widget.TextView r5 = (android.widget.TextView) r5
            if (r5 == 0) goto L_0x0079
            int r0 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r6 = r1
            com.airbnb.lottie.LottieAnimationView r6 = (com.airbnb.lottie.LottieAnimationView) r6
            if (r6 == 0) goto L_0x0079
            int r0 = com.livefoota.footballpro.R.id.mainLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r7 = r1
            android.widget.LinearLayout r7 = (android.widget.LinearLayout) r7
            if (r7 == 0) goto L_0x0079
            int r0 = com.livefoota.footballpro.R.id.nothingFoundLL
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            if (r1 == 0) goto L_0x0079
            com.livefoota.footballpro.databinding.NothingFoundBinding r8 = com.livefoota.footballpro.databinding.NothingFoundBinding.bind(r1)
            int r0 = com.livefoota.footballpro.R.id.positionId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r9 = r1
            android.widget.TextView r9 = (android.widget.TextView) r9
            if (r9 == 0) goto L_0x0079
            int r0 = com.livefoota.footballpro.R.id.ratingId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r10 = r1
            android.widget.TextView r10 = (android.widget.TextView) r10
            if (r10 == 0) goto L_0x0079
            int r0 = com.livefoota.footballpro.R.id.shirtNumberId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r11 = r1
            android.widget.TextView r11 = (android.widget.TextView) r11
            if (r11 == 0) goto L_0x0079
            int r0 = com.livefoota.footballpro.R.id.teamNameId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r12 = r1
            android.widget.TextView r12 = (android.widget.TextView) r12
            if (r12 == 0) goto L_0x0079
            int r0 = com.livefoota.footballpro.R.id.weightId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r14, r0)
            r13 = r1
            android.widget.TextView r13 = (android.widget.TextView) r13
            if (r13 == 0) goto L_0x0079
            com.livefoota.footballpro.databinding.FragmentProfileBinding r0 = new com.livefoota.footballpro.databinding.FragmentProfileBinding
            r3 = r14
            android.widget.FrameLayout r3 = (android.widget.FrameLayout) r3
            r2 = r0
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13)
            return r0
        L_0x0079:
            android.content.res.Resources r14 = r14.getResources()
            java.lang.String r14 = r14.getResourceName(r0)
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "Missing required view with ID: "
            java.lang.String r14 = r1.concat(r14)
            r0.<init>(r14)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.FragmentProfileBinding.bind(android.view.View):com.livefoota.footballpro.databinding.FragmentProfileBinding");
    }
}
