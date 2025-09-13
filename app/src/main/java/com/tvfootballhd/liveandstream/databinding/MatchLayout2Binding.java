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

public final class MatchLayout2Binding implements ViewBinding {
    public final ImageView matchAwayFlagId;
    public final TextView matchAwayNameId;
    public final TextView matchDateId;
    public final ImageView matchHomeFlagId;
    public final TextView matchHomeNameId;
    public final TextView matchLeagueId;
    public final TextView matchTimeOrGoalTVId;
    private final CardView rootView;

    private MatchLayout2Binding(CardView cardView, ImageView imageView, TextView textView, TextView textView2, ImageView imageView2, TextView textView3, TextView textView4, TextView textView5) {
        this.rootView = cardView;
        this.matchAwayFlagId = imageView;
        this.matchAwayNameId = textView;
        this.matchDateId = textView2;
        this.matchHomeFlagId = imageView2;
        this.matchHomeNameId = textView3;
        this.matchLeagueId = textView4;
        this.matchTimeOrGoalTVId = textView5;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static MatchLayout2Binding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static MatchLayout2Binding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.match_layout_2, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static MatchLayout2Binding bind(View view) {
        int i = R.id.matchAwayFlagId;
        ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
        if (imageView != null) {
            i = R.id.matchAwayNameId;
            TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
            if (textView != null) {
                i = R.id.matchDateId;
                TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
                if (textView2 != null) {
                    i = R.id.matchHomeFlagId;
                    ImageView imageView2 = (ImageView) ViewBindings.findChildViewById(view, i);
                    if (imageView2 != null) {
                        i = R.id.matchHomeNameId;
                        TextView textView3 = (TextView) ViewBindings.findChildViewById(view, i);
                        if (textView3 != null) {
                            i = R.id.matchLeagueId;
                            TextView textView4 = (TextView) ViewBindings.findChildViewById(view, i);
                            if (textView4 != null) {
                                i = R.id.matchTimeOrGoalTVId;
                                TextView textView5 = (TextView) ViewBindings.findChildViewById(view, i);
                                if (textView5 != null) {
                                    return new MatchLayout2Binding((CardView) view, imageView, textView, textView2, imageView2, textView3, textView4, textView5);
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
