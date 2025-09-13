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

public final class CurrentLeagueLayoutBinding implements ViewBinding {
    public final TextView leagueCountryNameId;
    public final ImageView leagueIconId;
    public final TextView leagueNameId;
    private final LinearLayout rootView;

    private CurrentLeagueLayoutBinding(LinearLayout linearLayout, TextView textView, ImageView imageView, TextView textView2) {
        this.rootView = linearLayout;
        this.leagueCountryNameId = textView;
        this.leagueIconId = imageView;
        this.leagueNameId = textView2;
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static CurrentLeagueLayoutBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static CurrentLeagueLayoutBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.current_league_layout, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static CurrentLeagueLayoutBinding bind(View view) {
        int i = R.id.leagueCountryNameId;
        TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
        if (textView != null) {
            i = R.id.leagueIconId;
            ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
            if (imageView != null) {
                i = R.id.leagueNameId;
                TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
                if (textView2 != null) {
                    return new CurrentLeagueLayoutBinding((LinearLayout) view, textView, imageView, textView2);
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
