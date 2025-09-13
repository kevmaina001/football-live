package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;

import com.tvfootballhd.liveandstream.R;

public final class AlertDialogForNoInternetBinding implements ViewBinding {
    public final TextView retry;
    private final RelativeLayout rootView;

    private AlertDialogForNoInternetBinding(RelativeLayout relativeLayout, TextView textView) {
        this.rootView = relativeLayout;
        this.retry = textView;
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static AlertDialogForNoInternetBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static AlertDialogForNoInternetBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.alert_dialog_for_no_internet, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static AlertDialogForNoInternetBinding bind(View view) {
        int i = R.id.retry;
        TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
        if (textView != null) {
            return new AlertDialogForNoInternetBinding((RelativeLayout) view, textView);
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
