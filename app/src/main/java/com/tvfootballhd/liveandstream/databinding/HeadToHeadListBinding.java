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

public final class HeadToHeadListBinding implements ViewBinding {
    public final RecyclerView h2hIndividualLeagueMatchId;
    public final ImageView h2hMatchLeagueIconId;
    public final TextView h2hMatchLeagueNameId;
    private final CardView rootView;

    private HeadToHeadListBinding(CardView cardView, RecyclerView recyclerView, ImageView imageView, TextView textView) {
        this.rootView = cardView;
        this.h2hIndividualLeagueMatchId = recyclerView;
        this.h2hMatchLeagueIconId = imageView;
        this.h2hMatchLeagueNameId = textView;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static HeadToHeadListBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static HeadToHeadListBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.head_to_head_list, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static HeadToHeadListBinding bind(View view) {
        int i = R.id.h2hIndividualLeagueMatchId;
        RecyclerView recyclerView = (RecyclerView) ViewBindings.findChildViewById(view, i);
        if (recyclerView != null) {
            i = R.id.h2hMatchLeagueIconId;
            ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
            if (imageView != null) {
                i = R.id.h2hMatchLeagueNameId;
                TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
                if (textView != null) {
                    return new HeadToHeadListBinding((CardView) view, recyclerView, imageView, textView);
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
