package com.alakowe.driver.fcmclasses;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.alakowe.driver.Config;
import com.alakowe.driver.MainActivity;
import com.alakowe.driver.NotificationActivity;
import com.alakowe.driver.R;
import com.alakowe.driver.RidesActivity;
import com.alakowe.driver.manager.LanguageManager;
import com.alakowe.driver.manager.RideSession;
import com.alakowe.driver.manager.SessionManager;
import com.alakowe.driver.models.restmodels.NewRideSyncModel;
import com.alakowe.driver.models.newdriveraccount.ResultStatusChecker;
import com.alakowe.driver.models.newridesync.NewRideSync;
import com.alakowe.driver.models.viewrideinfodriver.ViewRideInfoDriver;
import com.alakowe.driver.others.ChangeLocationEvent;
import com.alakowe.driver.others.Constants;
import com.alakowe.driver.samwork.ApiManager;
import com.alakowe.driver.trackride.TrackRideActivity;
import com.alakowe.driver.urls.Apis;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import com.alakowe.driver.logger.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;

public class MyFirebaseMessagingService extends FirebaseMessagingService implements ApiManager.APIFETCHER {

    Intent intent;
    String pn_message, pn_ride_id, pn_ride_status, app_id;
    String driver_id, language_id;
    SessionManager sessionManager;
    RideSession rideSession ;
    LanguageManager languageManager;
    ApiManager apiManager_new ;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        pn_message = remoteMessage.getData().get("message");
        pn_ride_id = remoteMessage.getData().get("ride_id");
        pn_ride_status = remoteMessage.getData().get("ride_status");
        app_id = remoteMessage.getData().get("app_id");
        Logger.e("pn_message       " + pn_message);
        Logger.e("pn_ride_id       " + pn_ride_id);
        Logger.e("pn_ride_status       " + pn_ride_status);
        Logger.e("app_id       " + app_id);


        if (app_id.equals("2")) {
            sessionManager = new SessionManager(this);
            rideSession = new RideSession(this);
            languageManager = new LanguageManager(this);
            apiManager_new = new ApiManager(this);
            checkStatus();
        }
    }

    private void checkStatus() {

            if(!pn_ride_status.equals("20")){
                rideSession.setRideStatus(""+pn_ride_status);
            }

            if (pn_ride_status.equals("1")  || pn_ride_status.equals("8")  ) {
                if(rideSession.getCurrentRideDetails().get(RideSession.RIDE_ID).equals("")){
                    apiManager_new.execution_method_get(Config.ApiKeys.KEY_NEW_RIDE_SYNC , Apis.newRideSync+"?ride_id="+pn_ride_id+"&driver_id="+sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID)+"&language_id="+languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
                }
            }

            if(pn_ride_status.equals("2")){
                if (Constants.is_track_ride_activity_is_open == true) {
                    EventBus.getDefault().post(new RideEvent(pn_ride_id , pn_ride_status , pn_message));
                } else if (Constants.is_track_ride_activity_is_open == false) {
                    sendNotification(pn_message);
                } else {
                    sendNotification(pn_message);
                }
                rideSession.setRideStatus("2");
            }
            if(pn_ride_status.equals("8")){

            }


            else if (pn_ride_status.equals("10")){// that for rental request
                HashMap<String , String > data = new HashMap<>();
                data.put("rental_booking_id" , ""+pn_ride_id);
                data.put("app_id" , "2");
                 apiManager_new.execution_method_post(Config.ApiKeys.KEY_REST_RIDE_SYNC , ""+ Apis.RideSync , data);
            }
            else if (pn_ride_status.equals("15")){
                if (Constants.is_Rental_Track_Activity_is_open == true  || Constants.mainActivity == true) {
                    EventBus.getDefault().post(new RideEvent(pn_ride_id , pn_ride_status , pn_message));
                }else {
                    sendNotification(""+pn_message);
                }
            }
            else if (pn_ride_status.equals("20")){
                if(Constants.is_track_ride_activity_is_open){
                    EventBus.getDefault().post(new ChangeLocationEvent(""+pn_ride_status));
                }else {
                    apiManager_new.execution_method_get(Config.ApiKeys.KEY_VIEW_RIDE_INFO_DRIVER, Apis.viewRideInfoDriver + "?ride_id=" + rideSession.getCurrentRideDetails().get(RideSession.RIDE_ID) + "&driver_token=" + sessionManager.getUserDetails().get(SessionManager.KEY_DriverToken) + "&language_id=1" );
                }
            }
            else if (pn_ride_status.equals("51")){
                sendNotification(""+pn_message);
            }
    }

    void sendNotification(String message123) {

        if (pn_ride_status.equals("1") || pn_ride_status.equals("2") || pn_ride_status.equals("8")|| pn_ride_status.equals("10")) {
            intent = new Intent(this, MainActivity.class)
                    .putExtra("ride_id", pn_ride_id)
                    .putExtra("ride_status", pn_ride_status);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (pn_ride_status.equals("15")){
            intent = new Intent(this, RidesActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (pn_ride_status.equals("20")){
            intent = new Intent(this, TrackRideActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }else if (pn_ride_status.equals("51")){
            intent = new Intent(this, NotificationActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        }

        if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {

            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo_100);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.app_logo_100)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message123)
                    .setAutoCancel(true)
                    .setSound(alarmSound)
                    .setVibrate(pattern)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(0, notificationBuilder.build());
        } else {
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            long[] pattern = {500, 500, 500, 500, 500, 500, 500, 500, 500};
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Bitmap largeIcon = BitmapFactory.decodeResource(getResources(), R.drawable.app_logo_100);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                    .setSmallIcon(R.drawable.app_logo_100)
                    .setLargeIcon(largeIcon)
                    .setContentTitle(getString(R.string.app_name))
                    .setContentText(message123)
                    .setAutoCancel(true)
                    .setColor(Color.parseColor("#d7ab0f"))
                    .setSound(alarmSound)
                    .setVibrate(pattern)
                    .setContentIntent(pendingIntent);
            notificationManager.notify(0, notificationBuilder.build());
        }
    }


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onAPIRunningState(int a, String APINAME) {

    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if (APINAME.equals(""+Config.ApiKeys.KEY_NEW_RIDE_SYNC)) {

                NewRideSync newRideSync = new NewRideSync();
                newRideSync = gson.fromJson(""+script, NewRideSync.class);

                if (newRideSync.getResult().toString().equals("1")) {
                    pn_message = newRideSync.getMsg();
                    pn_ride_id = newRideSync.getDetails().getRideId();
                    pn_ride_status = newRideSync.getDetails().getRideStatus();

                    if (app_id.equals("2")) {
                        switch (pn_ride_status){
                            case "1":
                                if(!Config.ReceiverPassengerActivity  && rideSession.getCurrentRideDetails().get(RideSession.RIDE_ID).equals("")){
                                    Config.ReceiverPassengerActivity = true ;
                                    Intent broadcastIntent = new Intent();
                                    broadcastIntent.putExtra("ride_id", ""+pn_ride_id);
                                    broadcastIntent.putExtra("ride_status", ""+pn_ride_status);
                                    broadcastIntent.setAction("com.alakowe.driver");
                                    sendBroadcast(broadcastIntent);
                                }
                                break;
                            case "8":
                                if ( !Config.RentalReceivepassengerActivity && rideSession.getCurrentRideDetails().get(RideSession.RIDE_ID).equals("")){
                                    Config.RentalReceivepassengerActivity = true ;
                                    Intent broadcastIntent_rental = new Intent();
                                    broadcastIntent_rental.putExtra("ride_id", ""+pn_ride_id);
                                    broadcastIntent_rental.putExtra("ride_status", ""+pn_ride_status);
                                    broadcastIntent_rental.setAction("com.alakowe.driver");
                                    sendBroadcast(broadcastIntent_rental);
                                }
                                break;
                        }
                    }
                }
            }
            if(APINAME.equals(""+Config.ApiKeys.KEY_REST_RIDE_SYNC)){
                ResultStatusChecker rs = gson.fromJson(""+script , ResultStatusChecker.class);
                if (rs.getStatus() == 1) {

                    NewRideSyncModel response  = gson.fromJson(""+script , NewRideSyncModel.class);
                    pn_message = response.getMessage();
                    pn_ride_id = response.getDetails().getRental_booking_id();
                    pn_ride_status = response.getDetails().getBooking_status();

                    if (app_id.equals("2")) {
                        if (Constants.mainActivity == true) {
                            Intent broadcastIntent = new Intent();
                            broadcastIntent.putExtra("ride_id", pn_ride_id);
                            broadcastIntent.putExtra("ride_status", pn_ride_status);
                            broadcastIntent.setAction("com.alakowe.driver");
                            sendBroadcast(broadcastIntent);
                        } else if (Constants.mainActivity == false) {
                            sendNotification(pn_message);
                        } else {
                            sendNotification(pn_message);
                        }
                    }
                }else if (rs.getStatus() == 0){

                }
            }
            if(APINAME.equals(""+Config.ApiKeys.KEY_VIEW_RIDE_INFO_DRIVER)){
                ViewRideInfoDriver viewRideInfoDriver = gson.fromJson("" + script, ViewRideInfoDriver.class);
                rideSession.setDropLocation(viewRideInfoDriver.getDetails().getDrop_location() , ""+viewRideInfoDriver.getDetails().getDrop_lat(), ""+viewRideInfoDriver.getDetails().getDrop_long());
                sendNotification(pn_message);
            }}catch (Exception e){}

    }


    public static class RideEvent {
        public  String  RideId ;
        public  String  RideStatus ;



        public  String  RideMessage ;
        public RideEvent(String Rideid , String RideStatus , String RideNMessage ){
            this.RideId = Rideid ;
            this.RideStatus = RideStatus ;
            this.RideMessage = RideNMessage ;
        }

        public String getRideId() {
            return RideId;
        }

        public String getRideStatus() {
            return RideStatus;
        }

        public String getRideMessage() {
            return RideMessage;
        }


    }


}