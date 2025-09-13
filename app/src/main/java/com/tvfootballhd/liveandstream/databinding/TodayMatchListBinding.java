package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.tvfootballhd.liveandstream.R;

public final class TodayMatchListBinding implements ViewBinding {
    private final CardView rootView;
    public final RecyclerView todayIndividualLeagueMatchId;
    public final ImageView todayMatchLeagueIconId;
    public final TextView todayMatchLeagueNameId;

    private TodayMatchListBinding(CardView cardView, RecyclerView recyclerView, ImageView imageView, TextView textView) {
        this.rootView = cardView;
        this.todayIndividualLeagueMatchId = recyclerView;
        this.todayMatchLeagueIconId = imageView;
        this.todayMatchLeagueNameId = textView;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static TodayMatchListBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static TodayMatchListBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.today_match_list, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static TodayMatchListBinding bind(View view) {
        int i = R.id.todayIndividualLeagueMatchId;
        RecyclerView recyclerView = (RecyclerView) ViewBindings.findChildViewById(view, i);
        if (recyclerView != null) {
            i = R.id.todayMatchLeagueIconId;
            ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
            if (imageView != null) {
                i = R.id.todayMatchLeagueNameId;
                TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
                if (textView != null) {
                    return new TodayMatchListBinding((CardView) view, recyclerView, imageView, textView);
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
