package com.tvfootballhd.liveandstream.Utils;

import android.os.Handler;
import androidx.viewpager2.widget.ViewPager2;
import com.google.android.exoplayer2.DefaultRenderersFactory;

public class AutoSlider implements Runnable {
    private int currentPage = 0;
    private Handler handler;
    private boolean isAutoSliding = true;
    private int numPages;
    private ViewPager2 viewPager;

    public AutoSlider(ViewPager2 viewPager2) {
        this.viewPager = viewPager2;
        this.numPages = viewPager2.getAdapter().getItemCount();
        this.handler = new Handler();
    }

    public void run() {
        if (this.isAutoSliding) {
            int i = (this.currentPage + 1) % this.numPages;
            this.currentPage = i;
            this.viewPager.setCurrentItem(i, true);
            this.handler.postDelayed(this, 30000);
        }
    }

    public void startAutoSlide() {
        this.isAutoSliding = true;
        this.handler.postDelayed(this, DefaultRenderersFactory.DEFAULT_ALLOWED_VIDEO_JOINING_TIME_MS);
    }

    public void stopAutoSlide() {
        this.isAutoSliding = false;
    }
}
