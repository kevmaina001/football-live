package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import com.airbnb.lottie.LottieAnimationView;
import com.tvfootballhd.liveandstream.R;

public final class FragmentStatisticsBinding implements ViewBinding {
    public final TextView a105;
    public final TextView a120;
    public final TextView a15;
    public final TextView a30;
    public final TextView a45;
    public final TextView a60;
    public final TextView a75;
    public final TextView a90;
    public final TextView drawTVId;
    public final TextView f105;
    public final TextView f120;
    public final TextView f15;
    public final TextView f30;
    public final TextView f45;
    public final TextView f60;
    public final TextView f75;
    public final TextView f90;
    public final TextView lostTVId;
    public final LottieAnimationView lottiId;
    public final ScrollView mainLL;
    public final NothingFoundBinding nothingFoundLL;
    public final TextView r105;
    public final TextView r120;
    public final TextView r15;
    public final TextView r30;
    public final TextView r45;
    public final TextView r60;
    public final TextView r75;
    public final TextView r90;
    private final FrameLayout rootView;
    public final TextView wonTVId;
    public final TextView y105;
    public final TextView y120;
    public final TextView y15;
    public final TextView y30;
    public final TextView y45;
    public final TextView y60;
    public final TextView y75;
    public final TextView y90;

    private FragmentStatisticsBinding(FrameLayout frameLayout, TextView textView, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7, TextView textView8, TextView textView9, TextView textView10, TextView textView11, TextView textView12, TextView textView13, TextView textView14, TextView textView15, TextView textView16, TextView textView17, TextView textView18, LottieAnimationView lottieAnimationView, ScrollView scrollView, NothingFoundBinding nothingFoundBinding, TextView textView19, TextView textView20, TextView textView21, TextView textView22, TextView textView23, TextView textView24, TextView textView25, TextView textView26, TextView textView27, TextView textView28, TextView textView29, TextView textView30, TextView textView31, TextView textView32, TextView textView33, TextView textView34, TextView textView35) {
        this.rootView = frameLayout;
        this.a105 = textView;
        this.a120 = textView2;
        this.a15 = textView3;
        this.a30 = textView4;
        this.a45 = textView5;
        this.a60 = textView6;
        this.a75 = textView7;
        this.a90 = textView8;
        this.drawTVId = textView9;
        this.f105 = textView10;
        this.f120 = textView11;
        this.f15 = textView12;
        this.f30 = textView13;
        this.f45 = textView14;
        this.f60 = textView15;
        this.f75 = textView16;
        this.f90 = textView17;
        this.lostTVId = textView18;
        this.lottiId = lottieAnimationView;
        this.mainLL = scrollView;
        this.nothingFoundLL = nothingFoundBinding;
        this.r105 = textView19;
        this.r120 = textView20;
        this.r15 = textView21;
        this.r30 = textView22;
        this.r45 = textView23;
        this.r60 = textView24;
        this.r75 = textView25;
        this.r90 = textView26;
        this.wonTVId = textView27;
        this.y105 = textView28;
        this.y120 = textView29;
        this.y15 = textView30;
        this.y30 = textView31;
        this.y45 = textView32;
        this.y60 = textView33;
        this.y75 = textView34;
        this.y90 = textView35;
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static FragmentStatisticsBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentStatisticsBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_statistics, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x00e7, code lost:
        r1 = com.livefoota.footballpro.R.id.nothingFoundLL;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.FragmentStatisticsBinding bind(android.view.View r43) {
        /*
            r0 = r43
            int r1 = com.livefoota.footballpro.R.id.a105
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r5 = r2
            android.widget.TextView r5 = (android.widget.TextView) r5
            if (r5 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.a120
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r6 = r2
            android.widget.TextView r6 = (android.widget.TextView) r6
            if (r6 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.a15
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r7 = r2
            android.widget.TextView r7 = (android.widget.TextView) r7
            if (r7 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.a30
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r8 = r2
            android.widget.TextView r8 = (android.widget.TextView) r8
            if (r8 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.a45
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r9 = r2
            android.widget.TextView r9 = (android.widget.TextView) r9
            if (r9 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.a60
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r10 = r2
            android.widget.TextView r10 = (android.widget.TextView) r10
            if (r10 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.a75
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r11 = r2
            android.widget.TextView r11 = (android.widget.TextView) r11
            if (r11 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.a90
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r12 = r2
            android.widget.TextView r12 = (android.widget.TextView) r12
            if (r12 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.drawTVId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r13 = r2
            android.widget.TextView r13 = (android.widget.TextView) r13
            if (r13 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.f105
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r14 = r2
            android.widget.TextView r14 = (android.widget.TextView) r14
            if (r14 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.f120
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r15 = r2
            android.widget.TextView r15 = (android.widget.TextView) r15
            if (r15 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.f15
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r16 = r2
            android.widget.TextView r16 = (android.widget.TextView) r16
            if (r16 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.f30
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r17 = r2
            android.widget.TextView r17 = (android.widget.TextView) r17
            if (r17 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.f45
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r18 = r2
            android.widget.TextView r18 = (android.widget.TextView) r18
            if (r18 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.f60
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r19 = r2
            android.widget.TextView r19 = (android.widget.TextView) r19
            if (r19 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.f75
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r20 = r2
            android.widget.TextView r20 = (android.widget.TextView) r20
            if (r20 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.f90
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r21 = r2
            android.widget.TextView r21 = (android.widget.TextView) r21
            if (r21 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.lostTVId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r22 = r2
            android.widget.TextView r22 = (android.widget.TextView) r22
            if (r22 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.lottiId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r23 = r2
            com.airbnb.lottie.LottieAnimationView r23 = (com.airbnb.lottie.LottieAnimationView) r23
            if (r23 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.mainLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r24 = r2
            android.widget.ScrollView r24 = (android.widget.ScrollView) r24
            if (r24 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.nothingFoundLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            if (r2 == 0) goto L_0x01c9
            com.livefoota.footballpro.databinding.NothingFoundBinding r25 = com.livefoota.footballpro.databinding.NothingFoundBinding.bind(r2)
            int r1 = com.livefoota.footballpro.R.id.r105
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r26 = r2
            android.widget.TextView r26 = (android.widget.TextView) r26
            if (r26 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.r120
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r27 = r2
            android.widget.TextView r27 = (android.widget.TextView) r27
            if (r27 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.r15
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r28 = r2
            android.widget.TextView r28 = (android.widget.TextView) r28
            if (r28 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.r30
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r29 = r2
            android.widget.TextView r29 = (android.widget.TextView) r29
            if (r29 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.r45
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r30 = r2
            android.widget.TextView r30 = (android.widget.TextView) r30
            if (r30 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.r60
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r31 = r2
            android.widget.TextView r31 = (android.widget.TextView) r31
            if (r31 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.r75
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r32 = r2
            android.widget.TextView r32 = (android.widget.TextView) r32
            if (r32 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.r90
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r33 = r2
            android.widget.TextView r33 = (android.widget.TextView) r33
            if (r33 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.wonTVId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r34 = r2
            android.widget.TextView r34 = (android.widget.TextView) r34
            if (r34 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.y105
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r35 = r2
            android.widget.TextView r35 = (android.widget.TextView) r35
            if (r35 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.y120
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r36 = r2
            android.widget.TextView r36 = (android.widget.TextView) r36
            if (r36 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.y15
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r37 = r2
            android.widget.TextView r37 = (android.widget.TextView) r37
            if (r37 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.y30
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r38 = r2
            android.widget.TextView r38 = (android.widget.TextView) r38
            if (r38 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.y45
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r39 = r2
            android.widget.TextView r39 = (android.widget.TextView) r39
            if (r39 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.y60
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r40 = r2
            android.widget.TextView r40 = (android.widget.TextView) r40
            if (r40 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.y75
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r41 = r2
            android.widget.TextView r41 = (android.widget.TextView) r41
            if (r41 == 0) goto L_0x01c9
            int r1 = com.livefoota.footballpro.R.id.y90
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r42 = r2
            android.widget.TextView r42 = (android.widget.TextView) r42
            if (r42 == 0) goto L_0x01c9
            com.livefoota.footballpro.databinding.FragmentStatisticsBinding r1 = new com.livefoota.footballpro.databinding.FragmentStatisticsBinding
            r3 = r1
            r4 = r0
            android.widget.FrameLayout r4 = (android.widget.FrameLayout) r4
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18, r19, r20, r21, r22, r23, r24, r25, r26, r27, r28, r29, r30, r31, r32, r33, r34, r35, r36, r37, r38, r39, r40, r41, r42)
            return r1
        L_0x01c9:
            android.content.res.Resources r0 = r43.getResources()
            java.lang.String r0 = r0.getResourceName(r1)
            java.lang.NullPointerException r1 = new java.lang.NullPointerException
            java.lang.String r2 = "Missing required view with ID: "
            java.lang.String r0 = r2.concat(r0)
            r1.<init>(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.FragmentStatisticsBinding.bind(android.view.View):com.livefoota.footballpro.databinding.FragmentStatisticsBinding");
    }
}
