package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.tvfootballhd.liveandstream.R;

public final class LiveMatchLayoutBinding implements ViewBinding {
    public final CardView LiveScoreId;
    public final TextView awayGoalId;
    public final TextView homeGoalId;
    public final ImageView imageView;
    public final ImageView liveAwayFlagId;
    public final TextView liveAwayNameId;
    public final ImageView liveHomeFlagId;
    public final TextView liveHomeNameId;
    public final TextView liveTimeId;
    private final CardView rootView;
    public final ConstraintLayout timeRL;

    private LiveMatchLayoutBinding(CardView cardView, CardView cardView2, TextView textView, TextView textView2, ImageView imageView2, ImageView imageView3, TextView textView3, ImageView imageView4, TextView textView4, TextView textView5, ConstraintLayout constraintLayout) {
        this.rootView = cardView;
        this.LiveScoreId = cardView2;
        this.awayGoalId = textView;
        this.homeGoalId = textView2;
        this.imageView = imageView2;
        this.liveAwayFlagId = imageView3;
        this.liveAwayNameId = textView3;
        this.liveHomeFlagId = imageView4;
        this.liveHomeNameId = textView4;
        this.liveTimeId = textView5;
        this.timeRL = constraintLayout;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static LiveMatchLayoutBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static LiveMatchLayoutBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.live_match_layout, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static LiveMatchLayoutBinding bind(View view) {
        CardView cardView = (CardView) view;
        int i = R.id.awayGoalId;
        TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
        if (textView != null) {
            i = R.id.homeGoalId;
            TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
            if (textView2 != null) {
                i = R.id.imageView;
                ImageView imageView2 = (ImageView) ViewBindings.findChildViewById(view, i);
                if (imageView2 != null) {
                    i = R.id.liveAwayFlagId;
                    ImageView imageView3 = (ImageView) ViewBindings.findChildViewById(view, i);
                    if (imageView3 != null) {
                        i = R.id.liveAwayNameId;
                        TextView textView3 = (TextView) ViewBindings.findChildViewById(view, i);
                        if (textView3 != null) {
                            i = R.id.liveHomeFlagId;
                            ImageView imageView4 = (ImageView) ViewBindings.findChildViewById(view, i);
                            if (imageView4 != null) {
                                i = R.id.liveHomeNameId;
                                TextView textView4 = (TextView) ViewBindings.findChildViewById(view, i);
                                if (textView4 != null) {
                                    i = R.id.liveTimeId;
                                    TextView textView5 = (TextView) ViewBindings.findChildViewById(view, i);
                                    if (textView5 != null) {
                                        i = R.id.timeRL;
                                        ConstraintLayout constraintLayout = (ConstraintLayout) ViewBindings.findChildViewById(view, i);
                                        if (constraintLayout != null) {
                                            return new LiveMatchLayoutBinding(cardView, cardView, textView, textView2, imageView2, imageView3, textView3, imageView4, textView4, textView5, constraintLayout);
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
