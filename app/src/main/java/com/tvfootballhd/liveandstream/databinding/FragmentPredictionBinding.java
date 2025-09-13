package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewpager2.widget.ViewPager2;
import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import me.relex.circleindicator.CircleIndicator3;
import com.tvfootballhd.liveandstream.R;

public final class FragmentPredictionBinding implements ViewBinding {
    public final TextView awayCountId;
    public final TextView awayCountId2;
    public final TextView awayCountId3;
    public final LinearProgressIndicator awayTypeId;
    public final LinearProgressIndicator awayTypeId2;
    public final LinearProgressIndicator awayTypeId3;
    public final LinearLayout comparisonLL;
    public final TextView homeCountId;
    public final TextView homeCountId2;
    public final TextView homeCountId3;
    public final LinearProgressIndicator homeTypeId;
    public final LinearProgressIndicator homeTypeId2;
    public final LinearProgressIndicator homeTypeId3;
    public final CircleIndicator3 indicator;
    public final LottieAnimationView lottiId;
    public final LinearLayout mainLL;
    public final NothingFoundBinding nothingFoundLL;
    public final ViewPager2 predictionViewPagerId;
    private final FrameLayout rootView;

    private FragmentPredictionBinding(FrameLayout frameLayout, TextView textView, TextView textView2, TextView textView3, LinearProgressIndicator linearProgressIndicator, LinearProgressIndicator linearProgressIndicator2, LinearProgressIndicator linearProgressIndicator3, LinearLayout linearLayout, TextView textView4, TextView textView5, TextView textView6, LinearProgressIndicator linearProgressIndicator4, LinearProgressIndicator linearProgressIndicator5, LinearProgressIndicator linearProgressIndicator6, CircleIndicator3 circleIndicator3, LottieAnimationView lottieAnimationView, LinearLayout linearLayout2, NothingFoundBinding nothingFoundBinding, ViewPager2 viewPager2) {
        this.rootView = frameLayout;
        this.awayCountId = textView;
        this.awayCountId2 = textView2;
        this.awayCountId3 = textView3;
        this.awayTypeId = linearProgressIndicator;
        this.awayTypeId2 = linearProgressIndicator2;
        this.awayTypeId3 = linearProgressIndicator3;
        this.comparisonLL = linearLayout;
        this.homeCountId = textView4;
        this.homeCountId2 = textView5;
        this.homeCountId3 = textView6;
        this.homeTypeId = linearProgressIndicator4;
        this.homeTypeId2 = linearProgressIndicator5;
        this.homeTypeId3 = linearProgressIndicator6;
        this.indicator = circleIndicator3;
        this.lottiId = lottieAnimationView;
        this.mainLL = linearLayout2;
        this.nothingFoundLL = nothingFoundBinding;
        this.predictionViewPagerId = viewPager2;
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static FragmentPredictionBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentPredictionBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_prediction, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:32:0x00b7, code lost:
        r1 = com.livefoota.footballpro.R.id.nothingFoundLL;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.FragmentPredictionBinding bind(android.view.View r23) {
        /*
            r0 = r23
            int r1 = com.livefoota.footballpro.R.id.awayCountId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r5 = r2
            android.widget.TextView r5 = (android.widget.TextView) r5
            if (r5 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.awayCountId2
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r6 = r2
            android.widget.TextView r6 = (android.widget.TextView) r6
            if (r6 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.awayCountId3
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r7 = r2
            android.widget.TextView r7 = (android.widget.TextView) r7
            if (r7 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.awayTypeId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r8 = r2
            com.google.android.material.progressindicator.LinearProgressIndicator r8 = (com.google.android.material.progressindicator.LinearProgressIndicator) r8
            if (r8 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.awayTypeId2
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r9 = r2
            com.google.android.material.progressindicator.LinearProgressIndicator r9 = (com.google.android.material.progressindicator.LinearProgressIndicator) r9
            if (r9 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.awayTypeId3
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r10 = r2
            com.google.android.material.progressindicator.LinearProgressIndicator r10 = (com.google.android.material.progressindicator.LinearProgressIndicator) r10
            if (r10 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.comparisonLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r11 = r2
            android.widget.LinearLayout r11 = (android.widget.LinearLayout) r11
            if (r11 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.homeCountId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r12 = r2
            android.widget.TextView r12 = (android.widget.TextView) r12
            if (r12 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.homeCountId2
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r13 = r2
            android.widget.TextView r13 = (android.widget.TextView) r13
            if (r13 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.homeCountId3
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r14 = r2
            android.widget.TextView r14 = (android.widget.TextView) r14
            if (r14 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.homeTypeId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r15 = r2
            com.google.android.material.progressindicator.LinearProgressIndicator r15 = (com.google.android.material.progressindicator.LinearProgressIndicator) r15
            if (r15 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.homeTypeId2
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r16 = r2
            com.google.android.material.progressindicator.LinearProgressIndicator r16 = (com.google.android.material.progressindicator.LinearProgressIndicator) r16
            if (r16 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.homeTypeId3
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r17 = r2
            com.google.android.material.progressindicator.LinearProgressIndicator r17 = (com.google.android.material.progressindicator.LinearProgressIndicator) r17
            if (r17 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.indicator
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r18 = r2
            me.relex.circleindicator.CircleIndicator3 r18 = (me.relex.circleindicator.CircleIndicator3) r18
            if (r18 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r19 = r2
            com.airbnb.lottie.LottieAnimationView r19 = (com.airbnb.lottie.LottieAnimationView) r19
            if (r19 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.mainLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r20 = r2
            android.widget.LinearLayout r20 = (android.widget.LinearLayout) r20
            if (r20 == 0) goto L_0x00d9
            int r1 = com.livefoota.footballpro.R.id.nothingFoundLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            if (r2 == 0) goto L_0x00d9
            com.livefoota.footballpro.databinding.NothingFoundBinding r21 = com.livefoota.footballpro.databinding.NothingFoundBinding.bind(r2)
            int r1 = com.livefoota.footballpro.R.id.predictionViewPagerId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r22 = r2
            androidx.viewpager2.widget.ViewPager2 r22 = (androidx.viewpager2.widget.ViewPager2) r22
            if (r22 == 0) goto L_0x00d9
            com.livefoota.footballpro.databinding.FragmentPredictionBinding r1 = new com.livefoota.footballpro.databinding.FragmentPredictionBinding
            r3 = r1
            r4 = r0
            android.widget.FrameLayout r4 = (android.widget.FrameLayout) r4
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22)
            return r1
        L_0x00d9:
            android.content.res.Resources r0 = r23.getResources()
            java.lang.String r0 = r0.getResourceName(r1)
            java.lang.NullPointerException r1 = new java.lang.NullPointerException
            java.lang.String r2 = "Missing required view with ID: "
            java.lang.String r0 = r2.concat(r0)
            r1.<init>(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.FragmentPredictionBinding.bind(android.view.View):com.livefoota.footballpro.databinding.FragmentPredictionBinding");
    }
}
