package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.tvfootballhd.liveandstream.R;

public final class StandingList2Binding implements ViewBinding {
    public final TextView groupTextId;
    private final CardView rootView;
    public final RecyclerView standing2RecyclerView;

    private StandingList2Binding(CardView cardView, TextView textView, RecyclerView recyclerView) {
        this.rootView = cardView;
        this.groupTextId = textView;
        this.standing2RecyclerView = recyclerView;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static StandingList2Binding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static StandingList2Binding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.standing_list2, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static StandingList2Binding bind(View view) {
        int i = R.id.groupTextId;
        TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
        if (textView != null) {
            i = R.id.standing2RecyclerView;
            RecyclerView recyclerView = (RecyclerView) ViewBindings.findChildViewById(view, i);
            if (recyclerView != null) {
                return new StandingList2Binding((CardView) view, textView, recyclerView);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
