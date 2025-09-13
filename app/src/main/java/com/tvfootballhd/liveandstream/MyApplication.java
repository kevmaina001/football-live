package com.tvfootballhd.liveandstream;

import android.app.Application;

import com.anythink.core.api.ATSDK;
import com.tvfootballhd.liveandstream.Utils.Config;
import com.onesignal.OneSignal;
import com.squareup.picasso.Picasso;

public class MyApplication extends Application {
    public void onCreate(){
        super.onCreate();

        OneSignal.setLogLevel(OneSignal.LOG_LEVEL.VERBOSE, OneSignal.LOG_LEVEL.NONE);
        OneSignal.initWithContext(this);
        OneSignal.setAppId(Config.getOneSignalApiKey());
        Picasso.setSingletonInstance(new Picasso.Builder(this).build());
        ATSDK.init(this, "h68401d39230ae", "a382688cdbdde5ca050da4220c79d87e5");
    }
}
