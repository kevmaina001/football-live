package com.tvfootballhd.liveandstream.Utils;

import android.view.View;
import androidx.viewpager2.widget.ViewPager2;

public class SlowPageTransformer implements ViewPager2.PageTransformer {
    private static final float MIN_SCALE = 0.75f;

    public void transformPage(View view, float f) {
        int width = view.getWidth();
        float max = Math.max(0.75f, 1.0f - Math.abs(f));
        if (f < -1.0f) {
            view.setAlpha(0.0f);
        } else if (f <= 0.0f) {
            view.setAlpha(1.0f);
            view.setTranslationX(0.0f);
            view.setScaleX(1.0f);
            view.setScaleY(1.0f);
        } else if (f <= 1.0f) {
            view.setAlpha(1.0f - f);
            view.setTranslationX(((float) (-width)) * f);
            view.setScaleX(max);
            view.setScaleY(max);
        } else {
            view.setAlpha(0.0f);
        }
    }
}
