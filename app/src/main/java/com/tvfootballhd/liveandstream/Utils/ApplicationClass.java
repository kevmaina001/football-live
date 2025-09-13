package com.tvfootballhd.liveandstream.Utils;

import android.app.Application;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;
import com.anythink.core.api.ATSDK;

public class ApplicationClass extends Application {
    public void onCreate() {
        super.onCreate();

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(Config.getOneSignalApiKey());
        Picasso.setSingletonInstance(new Picasso.Builder(this).build());
        ATSDK.init(this, "a683d6933de914", "a1daf35b998dfd88ec5e888fa1b719236");
    }
}
