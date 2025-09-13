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

public final class FragmentFirstBinding implements ViewBinding {
    public final Button buttonFirst;
    private final NestedScrollView rootView;
    public final TextView textviewFirst;

    private FragmentFirstBinding(NestedScrollView nestedScrollView, Button button, TextView textView) {
        this.rootView = nestedScrollView;
        this.buttonFirst = button;
        this.textviewFirst = textView;
    }

    public NestedScrollView getRoot() {
        return this.rootView;
    }

    public static FragmentFirstBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static FragmentFirstBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.fragment_first, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static FragmentFirstBinding bind(View view) {
        int i = R.id.button_first;
        Button button = (Button) ViewBindings.findChildViewById(view, i);
        if (button != null) {
            i = R.id.textview_first;
            TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
            if (textView != null) {
                return new FragmentFirstBinding((NestedScrollView) view, button, textView);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
