package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.material.progressindicator.LinearProgressIndicator;
import com.tvfootballhd.liveandstream.R;

public final class PossesListBinding implements ViewBinding {
    public final TextView awayCountId;
    public final LinearProgressIndicator awayTypeId;
    public final TextView homeCountId;
    public final LinearProgressIndicator homeTypeId;
    private final LinearLayout rootView;
    public final TextView typeId;

    private PossesListBinding(LinearLayout linearLayout, TextView textView, LinearProgressIndicator linearProgressIndicator, TextView textView2, LinearProgressIndicator linearProgressIndicator2, TextView textView3) {
        this.rootView = linearLayout;
        this.awayCountId = textView;
        this.awayTypeId = linearProgressIndicator;
        this.homeCountId = textView2;
        this.homeTypeId = linearProgressIndicator2;
        this.typeId = textView3;
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static PossesListBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static PossesListBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.posses_list, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static PossesListBinding bind(View view) {
        int i = R.id.awayCountId;
        TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
        if (textView != null) {
            i = R.id.awayTypeId;
            LinearProgressIndicator linearProgressIndicator = (LinearProgressIndicator) ViewBindings.findChildViewById(view, i);
            if (linearProgressIndicator != null) {
                i = R.id.homeCountId;
                TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
                if (textView2 != null) {
                    i = R.id.homeTypeId;
                    LinearProgressIndicator linearProgressIndicator2 = (LinearProgressIndicator) ViewBindings.findChildViewById(view, i);
                    if (linearProgressIndicator2 != null) {
                        i = R.id.typeId;
                        TextView textView3 = (TextView) ViewBindings.findChildViewById(view, i);
                        if (textView3 != null) {
                            return new PossesListBinding((LinearLayout) view, textView, linearProgressIndicator, textView2, linearProgressIndicator2, textView3);
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
