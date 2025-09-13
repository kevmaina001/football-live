package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.exoplayer2.ui.DefaultTimeBar;
import com.tvfootballhd.liveandstream.R;

public final class ExoPlaybackControlViewBinding implements ViewBinding {
    public final TextView exoDuration;
    public final ImageButton exoFfwd;
    public final FrameLayout exoFullscreenButton;
    public final ImageView exoFullscreenIcon;
    public final ImageButton exoPause;
    public final ImageButton exoPlay;
    public final TextView exoPosition;
    public final DefaultTimeBar exoProgress;
    public final ImageButton exoRew;
    private final LinearLayout rootView;

    private ExoPlaybackControlViewBinding(LinearLayout linearLayout, TextView textView, ImageButton imageButton, FrameLayout frameLayout, ImageView imageView, ImageButton imageButton2, ImageButton imageButton3, TextView textView2, DefaultTimeBar defaultTimeBar, ImageButton imageButton4) {
        this.rootView = linearLayout;
        this.exoDuration = textView;
        this.exoFfwd = imageButton;
        this.exoFullscreenButton = frameLayout;
        this.exoFullscreenIcon = imageView;
        this.exoPause = imageButton2;
        this.exoPlay = imageButton3;
        this.exoPosition = textView2;
        this.exoProgress = defaultTimeBar;
        this.exoRew = imageButton4;
    }

    public LinearLayout getRoot() {
        return this.rootView;
    }

    public static ExoPlaybackControlViewBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static ExoPlaybackControlViewBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.exo_playback_control_view, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static ExoPlaybackControlViewBinding bind(View view) {
        int i = com.google.android.exoplayer2.R.id.exo_duration;
        TextView textView = (TextView) ViewBindings.findChildViewById(view, i);
        if (textView != null) {
            i = com.google.android.exoplayer2.R.id.exo_ffwd;
            ImageButton imageButton = (ImageButton) ViewBindings.findChildViewById(view, i);
            if (imageButton != null) {
                i = R.id.exo_fullscreen_button;
                FrameLayout frameLayout = (FrameLayout) ViewBindings.findChildViewById(view, i);
                if (frameLayout != null) {
                    i = R.id.exo_fullscreen_icon;
                    ImageView imageView = (ImageView) ViewBindings.findChildViewById(view, i);
                    if (imageView != null) {
                        i = com.google.android.exoplayer2.R.id.exo_pause;
                        ImageButton imageButton2 = (ImageButton) ViewBindings.findChildViewById(view, i);
                        if (imageButton2 != null) {
                            i = com.google.android.exoplayer2.R.id.exo_play;
                            ImageButton imageButton3 = (ImageButton) ViewBindings.findChildViewById(view, i);
                            if (imageButton3 != null) {
                                i = com.google.android.exoplayer2.R.id.exo_position;
                                TextView textView2 = (TextView) ViewBindings.findChildViewById(view, i);
                                if (textView2 != null) {
                                    i = com.google.android.exoplayer2.R.id.exo_progress;
                                    DefaultTimeBar defaultTimeBar = (DefaultTimeBar) ViewBindings.findChildViewById(view, i);
                                    if (defaultTimeBar != null) {
                                        i = com.google.android.exoplayer2.R.id.exo_rew;
                                        ImageButton imageButton4 = (ImageButton) ViewBindings.findChildViewById(view, i);
                                        if (imageButton4 != null) {
                                            return new ExoPlaybackControlViewBinding((LinearLayout) view, textView, imageButton, frameLayout, imageView, imageButton2, imageButton3, textView2, defaultTimeBar, imageButton4);
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
