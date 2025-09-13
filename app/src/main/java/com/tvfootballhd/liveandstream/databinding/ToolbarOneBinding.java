package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.tvfootballhd.liveandstream.R;

public final class ToolbarOneBinding implements ViewBinding {
    public final LinearLayout backLL;
    private final RelativeLayout rootView;
    public final TextView titleId;

    private ToolbarOneBinding(RelativeLayout relativeLayout, LinearLayout linearLayout, TextView textView) {
        this.rootView = relativeLayout;
        this.backLL = linearLayout;
        this.titleId = textView;
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static ToolbarOneBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static ToolbarOneBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.toolbar_one, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static ToolbarOneBinding bind(View view) {
        int i = R.id.backLL;
        LinearLayout linearLayout = (LinearLayout) ViewBindings.findChildViewById(view, i);
        if (linearLayout != null) {
            i = R.id.titleId;
            TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
            if (textView != null) {
                return new ToolbarOneBinding((RelativeLayout) view, linearLayout, textView);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
