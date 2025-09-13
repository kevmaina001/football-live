package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import androidx.viewbinding.ViewBinding;
import com.tvfootballhd.liveandstream.R;

public final class NothingFoundBinding implements ViewBinding {
    private final LinearLayout rootView;

    private NothingFoundBinding(LinearLayout linearLayout) {
        this.rootView = linearLayout;
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static NothingFoundBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static NothingFoundBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.nothing_found, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static NothingFoundBinding bind(View view) {
        if (view != null) {
            return new NothingFoundBinding((LinearLayout) view);
        }
        throw new NullPointerException("rootView");
    }
}
