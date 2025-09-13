package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.tvfootballhd.liveandstream.R;

public final class TvChannelListBinding implements ViewBinding {
    public final ImageView channelImageView;
    public final TextView channelNameId;
    private final RelativeLayout rootView;

    private TvChannelListBinding(RelativeLayout relativeLayout, ImageView imageView, TextView textView) {
        this.rootView = relativeLayout;
        this.channelImageView = imageView;
        this.channelNameId = textView;
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static TvChannelListBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static TvChannelListBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.tv_channel_list, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static TvChannelListBinding bind(View view) {
        int i = R.id.channelImageView;
        ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
        if (imageView != null) {
            i = R.id.channelNameId;
            TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
            if (textView != null) {
                return new TvChannelListBinding((RelativeLayout) view, imageView, textView);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
