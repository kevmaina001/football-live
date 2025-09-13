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

public final class EventListBinding implements ViewBinding {
    public final TextView elapseTimeTVId;
    public final ImageView homeEventImageId;
    public final TextView homePlayerId;
    public final TextView homeReplacedPlayerId;
    public final ImageView imageView;
    private final CardView rootView;
    public final ImageView teamImageId;
    public final TextView teamNameId;
    public final ConstraintLayout timeRL;

    private EventListBinding(CardView cardView, TextView textView, ImageView imageView2, TextView textView2, TextView textView3, ImageView imageView3, ImageView imageView4, TextView textView4, ConstraintLayout constraintLayout) {
        this.rootView = cardView;
        this.elapseTimeTVId = textView;
        this.homeEventImageId = imageView2;
        this.homePlayerId = textView2;
        this.homeReplacedPlayerId = textView3;
        this.imageView = imageView3;
        this.teamImageId = imageView4;
        this.teamNameId = textView4;
        this.timeRL = constraintLayout;
    }

    public CardView getRoot() {
        return this.rootView;
    }

    public static EventListBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static EventListBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.event_list, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static EventListBinding bind(View view) {
        int i = R.id.elapseTimeTVId;
        TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
        if (textView != null) {
            i = R.id.homeEventImageId;
            ImageView imageView2 = (ImageView) ViewBindings.findChildViewById(view, i);
            if (imageView2 != null) {
                i = R.id.homePlayerId;
                TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
                if (textView2 != null) {
                    i = R.id.homeReplacedPlayerId;
                    TextView textView3 = (TextView) ViewBindings.findChildViewById(view, i);
                    if (textView3 != null) {
                        i = R.id.imageView;
                        ImageView imageView3 = (ImageView) ViewBindings.findChildViewById(view, i);
                        if (imageView3 != null) {
                            i = R.id.teamImageId;
                            ImageView imageView4 = (ImageView) ViewBindings.findChildViewById(view, i);
                            if (imageView4 != null) {
                                i = R.id.teamNameId;
                                TextView textView4 = (TextView) ViewBindings.findChildViewById(view, i);
                                if (textView4 != null) {
                                    i = R.id.timeRL;
                                    ConstraintLayout constraintLayout = (ConstraintLayout) ViewBindings.findChildViewById(view, i);
                                    if (constraintLayout != null) {
                                        return new EventListBinding((CardView) view, textView, imageView2, textView2, textView3, imageView3, imageView4, textView4, constraintLayout);
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
