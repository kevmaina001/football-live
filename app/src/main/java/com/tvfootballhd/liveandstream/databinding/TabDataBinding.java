package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.airbnb.lottie.LottieAnimationView;
import com.tvfootballhd.liveandstream.R;

public final class TabDataBinding implements ViewBinding {
    public final LottieAnimationView animationView;
    public final RecyclerView mainRecyclerViewId;
    private final RelativeLayout rootView;

    private TabDataBinding(RelativeLayout relativeLayout, LottieAnimationView lottieAnimationView, RecyclerView recyclerView) {
        this.rootView = relativeLayout;
        this.animationView = lottieAnimationView;
        this.mainRecyclerViewId = recyclerView;
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static TabDataBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static TabDataBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.tab_data, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static TabDataBinding bind(View view) {
        int i = R.id.animationView;
        LottieAnimationView lottieAnimationView = (LottieAnimationView) ViewBindings.findChildViewById(view, i);
        if (lottieAnimationView != null) {
            i = R.id.mainRecyclerViewId;
            RecyclerView recyclerView = (RecyclerView) ViewBindings.findChildViewById(view, i);
            if (recyclerView != null) {
                return new TabDataBinding((RelativeLayout) view, lottieAnimationView, recyclerView);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
