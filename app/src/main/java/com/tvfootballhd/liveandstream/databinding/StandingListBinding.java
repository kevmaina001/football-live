package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import com.tvfootballhd.liveandstream.R;

public final class StandingListBinding implements ViewBinding {
    public final TextView drawTVId;
    public final TextView goalsTVId;
    public final LinearLayout linearLayout;
    public final TextView lostTVId;
    public final TextView playedTVId;
    public final TextView pointsTVId;
    public final TextView rankTVId;
    private final CardView rootView;
    public final ImageView teamIconId;
    public final TextView teamNameId;
    public final View viewId;
    public final TextView wonTVId;

    private StandingListBinding(CardView cardView, TextView textView, TextView textView2, LinearLayout linearLayout2, TextView textView3, TextView textView4, TextView textView5, TextView textView6, ImageView imageView, TextView textView7, View view, TextView textView8) {
        this.rootView = cardView;
        this.drawTVId = textView;
        this.goalsTVId = textView2;
        this.linearLayout = linearLayout2;
        this.lostTVId = textView3;
        this.playedTVId = textView4;
        this.pointsTVId = textView5;
        this.rankTVId = textView6;
        this.teamIconId = imageView;
        this.teamNameId = textView7;
        this.viewId = view;
        this.wonTVId = textView8;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static StandingListBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static StandingListBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.standing_list, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0063, code lost:
        r0 = com.livefoota.footballpro.R.id.viewId;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.StandingListBinding bind(android.view.View r15) {
        /*
            int r0 = com.livefoota.footballpro.R.id.drawTVId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r4 = r1
            android.widget.TextView r4 = (android.widget.TextView) r4
            if (r4 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.goalsTVId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r5 = r1
            android.widget.TextView r5 = (android.widget.TextView) r5
            if (r5 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.linearLayout
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r6 = r1
            android.widget.LinearLayout r6 = (android.widget.LinearLayout) r6
            if (r6 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.lostTVId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r7 = r1
            android.widget.TextView r7 = (android.widget.TextView) r7
            if (r7 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.playedTVId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r8 = r1
            android.widget.TextView r8 = (android.widget.TextView) r8
            if (r8 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.pointsTVId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r9 = r1
            android.widget.TextView r9 = (android.widget.TextView) r9
            if (r9 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.rankTVId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r10 = r1
            android.widget.TextView r10 = (android.widget.TextView) r10
            if (r10 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.teamIconId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r11 = r1
            android.widget.ImageView r11 = (android.widget.ImageView) r11
            if (r11 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.teamNameId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r12 = r1
            android.widget.TextView r12 = (android.widget.TextView) r12
            if (r12 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.viewId
            android.view.View r13 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            if (r13 == 0) goto L_0x0080
            int r0 = com.livefoota.footballpro.R.id.wonTVId
            android.view.View r1 = androidx.viewbinding.ViewBindings.findChildViewById(r15, r0)
            r14 = r1
            android.widget.TextView r14 = (android.widget.TextView) r14
            if (r14 == 0) goto L_0x0080
            com.livefoota.footballpro.databinding.StandingListBinding r0 = new com.livefoota.footballpro.databinding.StandingListBinding
            r3 = r15
            androidx.cardview.widget.CardView r3 = (androidx.cardview.widget.CardView) r3
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
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.StandingListBinding.bind(android.view.View):com.livefoota.footballpro.databinding.StandingListBinding");
    }
}
