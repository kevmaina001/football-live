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

public final class SliderLayoutBinding implements ViewBinding {
    public final ImageView awayImageId;
    public final TextView awayNameId;
    public final ImageView homeImageId;
    public final TextView homeNameId;
    public final TextView leagueId;
    private final LinearLayout rootView;
    public final TextView roundId;
    public final TextView timeId;

    private SliderLayoutBinding(LinearLayout linearLayout, ImageView imageView, TextView textView, ImageView imageView2, TextView textView2, TextView textView3, TextView textView4, TextView textView5) {
        this.rootView = linearLayout;
        this.awayImageId = imageView;
        this.awayNameId = textView;
        this.homeImageId = imageView2;
        this.homeNameId = textView2;
        this.leagueId = textView3;
        this.roundId = textView4;
        this.timeId = textView5;
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static SliderLayoutBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static SliderLayoutBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.slider_layout, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static SliderLayoutBinding bind(View view) {
        int i = R.id.awayImageId;
        ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
        if (imageView != null) {
            i = R.id.awayNameId;
            TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
            if (textView != null) {
                i = R.id.homeImageId;
                ImageView imageView2 = (ImageView) ViewBindings.findChildViewById(view, i);
                if (imageView2 != null) {
                    i = R.id.homeNameId;
                    TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
                    if (textView2 != null) {
                        i = R.id.leagueId;
                        TextView textView3 = (TextView) ViewBindings.findChildViewById(view, i);
                        if (textView3 != null) {
                            i = R.id.roundId;
                            TextView textView4 = (TextView) ViewBindings.findChildViewById(view, i);
                            if (textView4 != null) {
                                i = R.id.timeId;
                                TextView textView5 = (TextView) ViewBindings.findChildViewById(view, i);
                                if (textView5 != null) {
                                    return new SliderLayoutBinding((LinearLayout) view, imageView, textView, imageView2, textView2, textView3, textView4, textView5);
                                }
                            }
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
