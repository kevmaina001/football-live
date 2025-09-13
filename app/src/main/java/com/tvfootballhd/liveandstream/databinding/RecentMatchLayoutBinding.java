package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.tvfootballhd.liveandstream.R;

public final class RecentMatchLayoutBinding implements ViewBinding {
    public final ImageView awayImageId;
    public final TextView awayNameId;
    public final TextView goalId;
    public final ImageView homeImageId;
    public final TextView homeNameId;
    private final LinearLayout rootView;
    public final TextView statusId;

    private RecentMatchLayoutBinding(LinearLayout linearLayout, ImageView imageView, TextView textView, TextView textView2, ImageView imageView2, TextView textView3, TextView textView4) {
        this.rootView = linearLayout;
        this.awayImageId = imageView;
        this.awayNameId = textView;
        this.goalId = textView2;
        this.homeImageId = imageView2;
        this.homeNameId = textView3;
        this.statusId = textView4;
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static RecentMatchLayoutBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static RecentMatchLayoutBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.recent_match_layout, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static RecentMatchLayoutBinding bind(View view) {
        int i = R.id.awayImageId;
        ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
        if (imageView != null) {
            i = R.id.awayNameId;
            TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
            if (textView != null) {
                i = R.id.goalId;
                TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
                if (textView2 != null) {
                    i = R.id.homeImageId;
                    ImageView imageView2 = (ImageView) ViewBindings.findChildViewById(view, i);
                    if (imageView2 != null) {
                        i = R.id.homeNameId;
                        TextView textView3 = (TextView) ViewBindings.findChildViewById(view, i);
                        if (textView3 != null) {
                            i = R.id.statusId;
                            TextView textView4 = (TextView) ViewBindings.findChildViewById(view, i);
                            if (textView4 != null) {
                                return new RecentMatchLayoutBinding((LinearLayout) view, imageView, textView, textView2, imageView2, textView3, textView4);
                            }
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
