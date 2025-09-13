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

public final class PlayerStateListBinding implements ViewBinding {
    public final ImageView leagueIconId;
    public final TextView leagueNameId;
    private final CardView rootView;
    public final TextView totalGameId;
    public final TextView totalGoalId;
    public final TextView totalRedCardId;
    public final TextView totalShotId;
    public final TextView totalYellowCardId;

    private PlayerStateListBinding(CardView cardView, ImageView imageView, TextView textView, TextView textView2, TextView textView3, TextView textView4, TextView textView5, TextView textView6) {
        this.rootView = cardView;
        this.leagueIconId = imageView;
        this.leagueNameId = textView;
        this.totalGameId = textView2;
        this.totalGoalId = textView3;
        this.totalRedCardId = textView4;
        this.totalShotId = textView5;
        this.totalYellowCardId = textView6;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static PlayerStateListBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static PlayerStateListBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.player_state_list, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static PlayerStateListBinding bind(View view) {
        int i = R.id.leagueIconId;
        ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
        if (imageView != null) {
            i = R.id.leagueNameId;
            TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
            if (textView != null) {
                i = R.id.totalGameId;
                TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
                if (textView2 != null) {
                    i = R.id.totalGoalId;
                    TextView textView3 = (TextView) ViewBindings.findChildViewById(view, i);
                    if (textView3 != null) {
                        i = R.id.totalRedCardId;
                        TextView textView4 = (TextView) ViewBindings.findChildViewById(view, i);
                        if (textView4 != null) {
                            i = R.id.totalShotId;
                            TextView textView5 = (TextView) ViewBindings.findChildViewById(view, i);
                            if (textView5 != null) {
                                i = R.id.totalYellowCardId;
                                TextView textView6 = (TextView) ViewBindings.findChildViewById(view, i);
                                if (textView6 != null) {
                                    return new PlayerStateListBinding((CardView) view, imageView, textView, textView2, textView3, textView4, textView5, textView6);
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
