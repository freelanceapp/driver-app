package com.apporio.demotaxiappdriver;

import com.apporio.demotaxiappdriver.analytics.MyApplication;
import com.bugfender.sdk.Bugfender;

/**
 * Created by lenovo-pc on 6/5/2017.
 */

public class MainApplication extends MyApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Bugfender.init(this, "kCSJzZm4iEPQvHnaUeCSHhHAPpXjalFe", io.fabric.sdk.android.BuildConfig.DEBUG);
        Bugfender.enableLogcatLogging();
        Bugfender.enableUIEventLogging(this);
    }

}
