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

public final class PlayerListBinding implements ViewBinding {
    public final TextView playerNameId;
    public final TextView playerTeamId;
    public final ImageView profileImageViewId;
    private final CardView rootView;
    public final CardView view2;

    private PlayerListBinding(CardView cardView, TextView textView, TextView textView2, ImageView imageView, CardView cardView2) {
        this.rootView = cardView;
        this.playerNameId = textView;
        this.playerTeamId = textView2;
        this.profileImageViewId = imageView;
        this.view2 = cardView2;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static PlayerListBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static PlayerListBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.player_list, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static PlayerListBinding bind(View view) {
        int i = R.id.playerNameId;
        TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
        if (textView != null) {
            i = R.id.playerTeamId;
            TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
            if (textView2 != null) {
                i = R.id.profileImageViewId;
                ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
                if (imageView != null) {
                    i = R.id.view2;
                    CardView cardView = (CardView) ViewBindings.findChildViewById(view, i);
                    if (cardView != null) {
                        return new PlayerListBinding((CardView) view, textView, textView2, imageView, cardView);
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
