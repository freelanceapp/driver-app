package com.apporio.taasdriver;

import android.content.Context;
import android.support.multidex.MultiDex;

import com.apporio.taasdriver.analytics.MyApplication;
import com.apporio.taasdriver.manager.RideSession;
import com.apporio.taasdriver.manager.SessionManager;
import com.bugfender.sdk.Bugfender;
import com.onesignal.OneSignal;

/**
 * Created by samirgoel3@gmail.com on 6/5/2017.
 */

public class MainApplication extends MyApplication {


    private static RideSession rideSession = null ;
    private static SessionManager sessionManager = null ;
    private static Context context ;

    @Override
    public void onCreate() {
        super.onCreate();
        context = this ;
        OneSignal.startInit(this)
                .autoPromptLocation(true)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();


        OneSignal.sendTag("driver_id" , ""+getSessionManager().getUserDetails().get(SessionManager.KEY_DRIVER_ID));

    }

    public static RideSession getRideSession(){
        if(rideSession == null){
            rideSession = new RideSession(context);
            return rideSession ;
        }else{
            return rideSession ;
        }

    }



    public static SessionManager getSessionManager(){
        if(sessionManager == null){
            sessionManager = new SessionManager(context);
            return sessionManager ;
        }else{
            return sessionManager ;
        }

    }



    @Override
    protected void attachBaseContext(Context base) {
        MultiDex.install(base);
        super.attachBaseContext(base);
    }
}
