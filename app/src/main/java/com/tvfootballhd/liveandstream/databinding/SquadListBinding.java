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

public final class SquadListBinding implements ViewBinding {
    public final TextView playerNameId;
    public final TextView playerNumberId;
    public final TextView positionId;
    public final ImageView profileImageViewId;
    private final CardView rootView;
    public final CardView view2;

    private SquadListBinding(CardView cardView, TextView textView, TextView textView2, TextView textView3, ImageView imageView, CardView cardView2) {
        this.rootView = cardView;
        this.playerNameId = textView;
        this.playerNumberId = textView2;
        this.positionId = textView3;
        this.profileImageViewId = imageView;
        this.view2 = cardView2;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static SquadListBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static SquadListBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.squad_list, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static SquadListBinding bind(View view) {
        int i = R.id.playerNameId;
        TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
        if (textView != null) {
            i = R.id.playerNumberId;
            TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
            if (textView2 != null) {
                i = R.id.positionId;
                TextView textView3 = (TextView) ViewBindings.findChildViewById(view, i);
                if (textView3 != null) {
                    i = R.id.profileImageViewId;
                    ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
                    if (imageView != null) {
                        i = R.id.view2;
                        CardView cardView = (CardView) ViewBindings.findChildViewById(view, i);
                        if (cardView != null) {
                            return new SquadListBinding((CardView) view, textView, textView2, textView3, imageView, cardView);
                        }
                    }
                }
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
