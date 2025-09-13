package com.tvfootballhd.liveandstream.databinding;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import androidx.viewbinding.ViewBinding;
import androidx.viewbinding.ViewBindings;
import com.google.android.exoplayer2.ui.PlayerView;
import com.tvfootballhd.liveandstream.R;

public final class ActivityExoPlayerBinding implements ViewBinding {
    private final RelativeLayout rootView;
    public final ProgressBar spinnerVideoDetails;
    public final PlayerView videoFullScreenPlayer;

    private ActivityExoPlayerBinding(RelativeLayout relativeLayout, ProgressBar progressBar, PlayerView playerView) {
        this.rootView = relativeLayout;
        this.spinnerVideoDetails = progressBar;
        this.videoFullScreenPlayer = playerView;
    }

    public RelativeLayout getRoot() {
        return this.rootView;
    }

    public static ActivityExoPlayerBinding inflate(LayoutInflater layoutInflater) {
        return inflate(layoutInflater, (ViewGroup) null, false);
    }

    public static ActivityExoPlayerBinding inflate(LayoutInflater layoutInflater, ViewGroup viewGroup, boolean z) {
        View inflate = layoutInflater.inflate(R.layout.activity_exo_player, viewGroup, false);
        if (z) {
            viewGroup.addView(inflate);
        }
        return bind(inflate);
    }

    public static ActivityExoPlayerBinding bind(View view) {
        int i = R.id.spinnerVideoDetails;
        ProgressBar progressBar = (ProgressBar) ViewBindings.findChildViewById(view, i);
        if (progressBar != null) {
            i = R.id.playerView;
            PlayerView playerView = (PlayerView) ViewBindings.findChildViewById(view, i);
            if (playerView != null) {
                return new ActivityExoPlayerBinding((RelativeLayout) view, progressBar, playerView);
            }
        }
        throw new NullPointerException("Missing required view with ID: ".concat(view.getResources().getResourceName(i)));
    }
}
