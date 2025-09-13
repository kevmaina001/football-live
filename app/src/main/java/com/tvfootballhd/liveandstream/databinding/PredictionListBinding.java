package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import com.tvfootballhd.liveandstream.R;

public final class PredictionListBinding implements ViewBinding {
    public final TextView awayNameId;
    public final TextView awayResultId;
    public final LinearLayout circleLL;
    public final View circleOne;
    public final View circleThree;
    public final View circleTwo;
    public final TextView dateTV;
    public final TextView drawNameId;
    public final TextView drawResultId;
    public final TextView homeNameId;
    public final TextView homeResultId;
    public final View lineId;
    private final CardView rootView;
    public final LinearLayout textLL;
    public final TextView titleId;

    private PredictionListBinding(CardView cardView, TextView textView, TextView textView2, LinearLayout linearLayout, View view, View view2, View view3, TextView textView3, TextView textView4, TextView textView5, TextView textView6, TextView textView7, View view4, LinearLayout linearLayout2, TextView textView8) {
        this.rootView = cardView;
        this.awayNameId = textView;
        this.awayResultId = textView2;
        this.circleLL = linearLayout;
        this.circleOne = view;
        this.circleThree = view2;
        this.circleTwo = view3;
        this.dateTV = textView3;
        this.drawNameId = textView4;
        this.drawResultId = textView5;
        this.homeNameId = textView6;
        this.homeResultId = textView7;
        this.lineId = view4;
        this.textLL = linearLayout2;
        this.titleId = textView8;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static PredictionListBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static PredictionListBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.prediction_list, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0033, code lost:
        r1 = com.livefoota.footballpro.R.id.circleTwo;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0072, code lost:
        r1 = com.livefoota.footballpro.R.id.lineId;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:6:0x0023, code lost:
        r1 = com.livefoota.footballpro.R.id.circleOne;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x002b, code lost:
        r1 = com.livefoota.footballpro.R.id.circleThree;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static com.tvfootballhd.liveandstream.databinding.PredictionListBinding bind(android.view.View r19) {
        /*
            r0 = r19
            int r1 = com.livefoota.footballpro.R.id.awayNameId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r5 = r2
            android.widget.TextView r5 = (android.widget.TextView) r5
            if (r5 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.awayResultId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r6 = r2
            android.widget.TextView r6 = (android.widget.TextView) r6
            if (r6 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.circleLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r7 = r2
            android.widget.LinearLayout r7 = (android.widget.LinearLayout) r7
            if (r7 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.circleOne
            android.view.View r8 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            if (r8 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.circleThree
            android.view.View r9 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            if (r9 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.circleTwo
            android.view.View r10 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            if (r10 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.dateTV
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r11 = r2
            android.widget.TextView r11 = (android.widget.TextView) r11
            if (r11 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.drawNameId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r12 = r2
            android.widget.TextView r12 = (android.widget.TextView) r12
            if (r12 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.drawResultId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r13 = r2
            android.widget.TextView r13 = (android.widget.TextView) r13
            if (r13 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.homeNameId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r14 = r2
            android.widget.TextView r14 = (android.widget.TextView) r14
            if (r14 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.homeResultId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r15 = r2
            android.widget.TextView r15 = (android.widget.TextView) r15
            if (r15 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.lineId
            android.view.View r16 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            if (r16 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.textLL
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r17 = r2
            android.widget.LinearLayout r17 = (android.widget.LinearLayout) r17
            if (r17 == 0) goto L_0x009c
            int r1 = com.livefoota.footballpro.R.id.titleId
            android.view.View r2 = androidx.viewbinding.ViewBindings.findChildViewById(r0, r1)
            r18 = r2
            android.widget.TextView r18 = (android.widget.TextView) r18
            if (r18 == 0) goto L_0x009c
            com.livefoota.footballpro.databinding.PredictionListBinding r1 = new com.livefoota.footballpro.databinding.PredictionListBinding
            r4 = r0
            androidx.cardview.widget.CardView r4 = (androidx.cardview.widget.CardView) r4
            r3 = r1
            r3.<init>(r4, r5, r6, r7, r8, r9, r10, r11, r12, r13, r14, r15, r16, r17, r18)
            return r1
        L_0x009c:
            android.content.res.Resources r0 = r19.getResources()
            java.lang.String r0 = r0.getResourceName(r1)
            java.lang.NullPointerException r1 = new java.lang.NullPointerException
            java.lang.String r2 = "Missing required view with ID: "
            java.lang.String r0 = r2.concat(r0)
            r1.<init>(r0)
            throw r1
        */
        throw new UnsupportedOperationException("Method not decompiled: com.livefoota.footballpro.databinding.PredictionListBinding.bind(android.view.View):com.livefoota.footballpro.databinding.PredictionListBinding");
    }
}
