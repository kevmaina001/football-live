package com.tvfootballhd.liveandstream.VideoPlayer;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.content.ContextCompat;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.source.hls.HlsMediaSource;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.tvfootballhd.liveandstream.AdsManager;
import com.tvfootballhd.liveandstream.R;
import com.tvfootballhd.liveandstream.ads.IntertestialAdManager;

public class ExoPlayerActivity extends AppCompatActivity implements Player.EventListener {
    private boolean adShown = false;
    boolean fullscreen = false;
    ExoPlayer player;
    Handler mHandler;
    Runnable mRunnable;
    ProgressBar spinnerVideoDetails;
    PlayerView videoFullScreenPlayer;
    String videoUri;

    public void onLoadingChanged(boolean z) {
    }

    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {
    }

    public void onPlayerError(ExoPlaybackException exoPlaybackException) {
    }

    public void onPositionDiscontinuity(int i) {
    }

    public void onRepeatModeChanged(int i) {
    }

    public void onSeekProcessed() {
    }

    public void onShuffleModeEnabledChanged(boolean z) {
    }

    public void onTimelineChanged(Timeline timeline, Object obj, int i) {
    }

    public void onTracksChanged(TrackGroupArray trackGroupArray, TrackSelectionArray trackSelectionArray) {
    }

    public static Intent getStartIntent(Context context, String str) {
        Intent intent = new Intent(context, ExoPlayerActivity.class);
        intent.putExtra("videourl", str);
        return intent;
    }

    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        requestWindowFeature(1);
        getWindow().setFlags(1024, 1024);
        setContentView(R.layout.activity_exo_player);
        AdsManager.LoadInterstitialAd(this);
        if (!this.adShown) {
            AdsManager.ShowInterstitialAd(this);
            IntertestialAdManager.ShowIntertestialAd(this);
            this.adShown = true;
        }
        PlayerView playerView = (PlayerView) findViewById(R.id.playerView);
        this.videoFullScreenPlayer = playerView;


        final ImageView imageView = (ImageView) playerView.findViewById(R.id.exo_fullscreen_icon);
        imageView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                if (ExoPlayerActivity.this.fullscreen) {
                    imageView.setImageDrawable(ContextCompat.getDrawable(ExoPlayerActivity.this, R.drawable.ic_fullscreen_open));
                    ExoPlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                    RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) ExoPlayerActivity.this.videoFullScreenPlayer.getLayoutParams();
                    layoutParams.width = -1;
                    layoutParams.height = -1;
                    ExoPlayerActivity.this.videoFullScreenPlayer.setLayoutParams(layoutParams);
                    ExoPlayerActivity.this.fullscreen = false;
                    return;
                }
                imageView.setImageDrawable(ContextCompat.getDrawable(ExoPlayerActivity.this, R.drawable.ic_fullscreen_close));
                ExoPlayerActivity.this.getWindow().getDecorView().setSystemUiVisibility(4102);
                if (ExoPlayerActivity.this.getSupportActionBar() != null) {
                    ExoPlayerActivity.this.getSupportActionBar().hide();
                }
                ExoPlayerActivity.this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) ExoPlayerActivity.this.videoFullScreenPlayer.getLayoutParams();
                layoutParams2.width = -1;
                layoutParams2.height = -1;
                ExoPlayerActivity.this.videoFullScreenPlayer.setLayoutParams(layoutParams2);
                ExoPlayerActivity.this.fullscreen = true;
            }
        });
        this.spinnerVideoDetails = (ProgressBar) findViewById(R.id.spinnerVideoDetails);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        if (getIntent().hasExtra("videourl")) {
            String stringExtra = getIntent().getStringExtra("videourl");
            this.videoUri = stringExtra;
            Log.i("videoURILink", stringExtra.toString());
        }
        setUp();
    }

    private void setUp() {
        initializePlayer();
        String str = this.videoUri;

    }

    private void initializePlayer() {
        if (this.player == null) {
            DefaultLoadControl defaultLoadControl = new DefaultLoadControl();
           // player = new ExoPlayer.Builder(ExoPlayerActivity.this).build();
        }
    }

    private void buildMediaSource(Uri uri) {
        new DefaultBandwidthMeter();
        this.player.prepare(new HlsMediaSource.Factory((DataSource.Factory) new DefaultHttpDataSourceFactory(Util.getUserAgent(this, getString(R.string.app_name)))).createMediaSource(uri));
        this.player.setPlayWhenReady(true);
        this.player.addListener((Player.Listener) this);
    }

    private void releasePlayer() {
        SimpleExoPlayer simpleExoPlayer = (SimpleExoPlayer) this.player;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.release();
            this.player = null;
        }
    }

    private void pausePlayer() {
        SimpleExoPlayer simpleExoPlayer = (SimpleExoPlayer) this.player;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(false);
            this.player.getPlaybackState();
        }
    }

    private void resumePlayer() {
        SimpleExoPlayer simpleExoPlayer = (SimpleExoPlayer) this.player;
        if (simpleExoPlayer != null) {
            simpleExoPlayer.setPlayWhenReady(true);
            this.player.getPlaybackState();
        }
    }

    public void onPause() {
        super.onPause();
        pausePlayer();
        Runnable runnable = this.mRunnable;
        if (runnable != null) {
            this.mHandler.removeCallbacks(runnable);
        }
    }

    public void onRestart() {
        super.onRestart();
        resumePlayer();
    }


    public void onResume() {
        super.onResume();
        resumePlayer();
    }

    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    public void onPlayerStateChanged(boolean z, int i) {
        if (i == 2) {
            this.spinnerVideoDetails.setVisibility(0);
        } else if (i == 3) {
            this.spinnerVideoDetails.setVisibility(8);
        }
        if (i == 1 || i == 4 || !z) {
            this.videoFullScreenPlayer.setKeepScreenOn(false);
        } else {
            this.videoFullScreenPlayer.setKeepScreenOn(true);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        if (this.adShown && configuration.orientation != 2) {
            this.adShown = true;
        }
    }
}
