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

public final class InfoListItemBinding implements ViewBinding {
    public final RecyclerView individualItemRecyclerView;
    public final ImageView leagueIconId;
    public final TextView leagueNameInfoId;
    private final CardView rootView;

    private InfoListItemBinding(CardView cardView, RecyclerView recyclerView, ImageView imageView, TextView textView) {
        this.rootView = cardView;
        this.individualItemRecyclerView = recyclerView;
        this.leagueIconId = imageView;
        this.leagueNameInfoId = textView;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static InfoListItemBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static InfoListItemBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.info_list_item, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static InfoListItemBinding bind(View view) {
        int i = R.id.individualItemRecyclerView;
        RecyclerView recyclerView = (RecyclerView) ViewBindings.findChildViewById(view, i);
        if (recyclerView != null) {
            i = R.id.leagueIconId;
            ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
            if (imageView != null) {
                i = R.id.leagueNameInfoId;
                TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
                if (textView != null) {
                    return new InfoListItemBinding((CardView) view, recyclerView, imageView, textView);
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
