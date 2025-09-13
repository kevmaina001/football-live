package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.core.widget.NestedScrollView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.tvfootballhd.liveandstream.R;

public final class FragmentSecondBinding implements ViewBinding {
    public final Button buttonSecond;
    private final NestedScrollView rootView;
    public final TextView textviewSecond;

    private FragmentSecondBinding(NestedScrollView nestedScrollView, Button button, TextView textView) {
        this.rootView = nestedScrollView;
        this.buttonSecond = button;
        this.textviewSecond = textView;
    }

    public NestedScrollView getRoot() {
        return this.rootView;
    }

    public static FragmentSecondBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentSecondBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_second, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static FragmentSecondBinding bind(View view) {
        int i = R.id.button_second;
        Button button = (Button) ViewBindings.findChildViewById(view, i);
        if (button != null) {
            i = R.id.textview_second;
            TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
            if (textView != null) {
                return new FragmentSecondBinding((NestedScrollView) view, button, textView);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
