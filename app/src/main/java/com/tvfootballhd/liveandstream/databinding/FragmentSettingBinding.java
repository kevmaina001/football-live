package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import androidx.viewbinding.ViewBinding;
import com.tvfootballhd.liveandstream.R;

public final class FragmentSettingBinding implements ViewBinding {
    public final ImageView aboutUsIcon;
    public final RelativeLayout aboutUsId;
    public final View headingLine;
    public final RelativeLayout headingRl;
    public final ImageView menuIcon;
    public final RelativeLayout privacyPolicyId;
    public final ImageView privacyPolicyImageView;
    public final ImageView rateMeId;
    public final RelativeLayout rateRLId;
    private final FrameLayout rootView;
    public final RelativeLayout shareId;
    public final ImageView shareImage;

    private FragmentSettingBinding(FrameLayout frameLayout, ImageView imageView, RelativeLayout relativeLayout, View view, RelativeLayout relativeLayout2, ImageView imageView2, RelativeLayout relativeLayout3, ImageView imageView3, ImageView imageView4, RelativeLayout relativeLayout4, RelativeLayout relativeLayout5, ImageView imageView5) {
        this.rootView = frameLayout;
        this.aboutUsIcon = imageView;
        this.aboutUsId = relativeLayout;
        this.headingLine = view;
        this.headingRl = relativeLayout2;
        this.menuIcon = imageView2;
        this.privacyPolicyId = relativeLayout3;
        this.privacyPolicyImageView = imageView3;
        this.rateMeId = imageView4;
        this.rateRLId = relativeLayout4;
        this.shareId = relativeLayout5;
        this.shareImage = imageView5;
    }

    public FrameLayout getRoot() {
        return this.rootView;
    }

    public static FragmentSettingBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentSettingBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_setting, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:4:0x0016, code lost:
        r0 = com.livefoota.footballpro.R.id.headingLine;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.FragmentSettingBinding bind(android.view.View r15) {
        /*
            int r0 = com.livefoota.footballpro.R.id.aboutUsIcon
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r4 = r1
            android.widget.ImageView r4 = (android.widget.ImageView) r4
            if (r4 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.aboutUsId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r5 = r1
            android.widget.RelativeLayout r5 = (android.widget.RelativeLayout) r5
            if (r5 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.headingLine
            android.view.View r6 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            if (r6 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.headingRl
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r7 = r1
            android.widget.RelativeLayout r7 = (android.widget.RelativeLayout) r7
            if (r7 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.menuIcon
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r8 = r1
            android.widget.ImageView r8 = (android.widget.ImageView) r8
            if (r8 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.privacyPolicyId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r9 = r1
            android.widget.RelativeLayout r9 = (android.widget.RelativeLayout) r9
            if (r9 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.privacyPolicyImageView
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r10 = r1
            android.widget.ImageView r10 = (android.widget.ImageView) r10
            if (r10 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.rateMeId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r11 = r1
            android.widget.ImageView r11 = (android.widget.ImageView) r11
            if (r11 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.rateRLId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r12 = r1
            android.widget.RelativeLayout r12 = (android.widget.RelativeLayout) r12
            if (r12 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.shareId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r13 = r1
            android.widget.RelativeLayout r13 = (android.widget.RelativeLayout) r13
            if (r13 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.shareImage
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r14 = r1
            android.widget.ImageView r14 = (android.widget.ImageView) r14
            if (r14 == 0) goto L_0x0080
            com.livefoota.footballpro.databinding.FragmentSettingBinding r0 = new com.livefoota.footballpro.databinding.FragmentSettingBinding
            r3 = r15
            android.widget.FrameLayout r3 = (android.widget.FrameLayout) r3
            r2 = r0
            r2.<init>(r3, r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14)
            return r0
        L_0x0080:
            android.content.res.Resources r15 = r15.getResources()
            java.lang.String r15 = r15.getResourceName(r0)
            java.lang.NullPointerException r0 = new java.lang.NullPointerException
            java.lang.String r1 = "Missing required view with ID: "
            java.lang.String r15 = r1.concat(r15)
            r0.<init>(r15)
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.FragmentSettingBinding.bind(android.view.View):com.livefoota.footballpro.databinding.FragmentSettingBinding");
    }
}
