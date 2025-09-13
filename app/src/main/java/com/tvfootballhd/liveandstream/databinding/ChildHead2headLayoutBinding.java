package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;

import com.tvfootballhd.liveandstream.R;

public final class ChildHead2headLayoutBinding implements ViewBinding {
    public final TextView awayGoal;
    public final TextView dateTV;
    public final TextView homeGoal;
    public final ImageView leagueAwayFlagId;
    public final TextView leagueAwayNameId;
    public final ImageView leagueHomeFlagId;
    public final TextView leagueHomeNameId;
    public final CardView matchMainLL;
    public final ImageView notificationIconId;
    private final CardView rootView;
    public final LinearLayout timeLL;
    public final TextView timeTVId;

    private ChildHead2headLayoutBinding(CardView cardView, TextView textView, TextView textView2, TextView textView3, ImageView imageView, TextView textView4, ImageView imageView2, TextView textView5, CardView cardView2, ImageView imageView3, LinearLayout linearLayout, TextView textView6) {
        this.rootView = cardView;
        this.awayGoal = textView;
        this.dateTV = textView2;
        this.homeGoal = textView3;
        this.leagueAwayFlagId = imageView;
        this.leagueAwayNameId = textView4;
        this.leagueHomeFlagId = imageView2;
        this.leagueHomeNameId = textView5;
        this.matchMainLL = cardView2;
        this.notificationIconId = imageView3;
        this.timeLL = linearLayout;
        this.timeTVId = textView6;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static ChildHead2headLayoutBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static ChildHead2headLayoutBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.child_head2head_layout, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static ChildHead2headLayoutBinding bind(View view) {
        int i = R.id.awayGoal;
        TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
        if (textView != null) {
            i = R.id.dateTV;
            TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
            if (textView2 != null) {
                i = R.id.homeGoal;
                TextView textView3 = (TextView) ViewBindings.findChildViewById(view, i);
                if (textView3 != null) {
                    i = R.id.leagueAwayFlagId;
                    ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
                    if (imageView != null) {
                        i = R.id.leagueAwayNameId;
                        TextView textView4 = (TextView) ViewBindings.findChildViewById(view, i);
                        if (textView4 != null) {
                            i = R.id.leagueHomeFlagId;
                            ImageView imageView2 = (ImageView) ViewBindings.findChildViewById(view, i);
                            if (imageView2 != null) {
                                i = R.id.leagueHomeNameId;
                                TextView textView5 = (TextView) ViewBindings.findChildViewById(view, i);
                                if (textView5 != null) {
                                    CardView cardView = (CardView) view;
                                    i = R.id.notificationIconId;
                                    ImageView imageView3 = (ImageView) ViewBindings.findChildViewById(view, i);
                                    if (imageView3 != null) {
                                        i = R.id.timeLL;
                                        LinearLayout linearLayout = (LinearLayout) ViewBindings.findChildViewById(view, i);
                                        if (linearLayout != null) {
                                            i = R.id.timeTVId;
                                            TextView textView6 = (TextView) ViewBindings.findChildViewById(view, i);
                                            if (textView6 != null) {
                                                return new ChildHead2headLayoutBinding(cardView, textView, textView2, textView3, imageView, textView4, imageView2, textView5, cardView, imageView3, linearLayout, textView6);
                                            }
                                        }
                                    }
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
