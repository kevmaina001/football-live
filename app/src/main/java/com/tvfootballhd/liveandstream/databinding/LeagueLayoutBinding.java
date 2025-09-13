package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.tvfootballhd.liveandstream.R;

public final class LeagueLayoutBinding implements ViewBinding {
    public final TextView durationDate;
    public final ImageView leagueIconId;
    public final TextView leagueNameId;
    public final ImageView leagueOne;
    private final CardView rootView;

    private LeagueLayoutBinding(CardView cardView, TextView textView, ImageView imageView, TextView textView2, ImageView imageView2) {
        this.rootView = cardView;
        this.durationDate = textView;
        this.leagueIconId = imageView;
        this.leagueNameId = textView2;
        this.leagueOne = imageView2;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static LeagueLayoutBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static LeagueLayoutBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.league_layout, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static LeagueLayoutBinding bind(View view) {
        int i = R.id.durationDate;
        TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
        if (textView != null) {
            i = R.id.leagueIconId;
            ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
            if (imageView != null) {
                i = R.id.leagueNameId;
                TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
                if (textView2 != null) {
                    i = R.id.leagueOne;
                    ImageView imageView2 = (ImageView) ViewBindings.findChildViewById(view, i);
                    if (imageView2 != null) {
                        return new LeagueLayoutBinding((CardView) view, textView, imageView, textView2, imageView2);
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
