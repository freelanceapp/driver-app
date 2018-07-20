package com.taas.driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.taas.driver.manager.LanguageManager;
import com.taas.driver.manager.RideSession;
import com.taas.driver.manager.SessionManager;
import com.taas.driver.models.deviceid.DeviceId;
import com.taas.driver.models.newridesync.NewRideSync;
import com.taas.driver.models.rideaccept.RideAccept;
import com.taas.driver.models.viewrideinfodriver.ViewRideInfoDriver;
import com.taas.driver.others.FirebaseUtils;
import com.taas.driver.others.MyBroadcastReceiver;
import com.taas.driver.others.RideSessionEvent;
import com.taas.driver.samwork.ApiManager;
import com.taas.driver.trackride.TrackRideActivity;
import com.taas.driver.urls.Apis;
import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taas.driver.manager.LanguageManager;
import com.taas.driver.manager.RideSession;
import com.taas.driver.manager.SessionManager;
import com.taas.driver.models.deviceid.DeviceId;
import com.taas.driver.models.newridesync.NewRideSync;
import com.taas.driver.models.rideaccept.RideAccept;
import com.taas.driver.models.viewrideinfodriver.ViewRideInfoDriver;
import com.taas.driver.others.FirebaseUtils;
import com.taas.driver.others.RideSessionEvent;
import com.taas.driver.samwork.ApiManager;
import com.taas.driver.trackride.TrackRideActivity;
import com.taas.driver.urls.Apis;

import butterknife.Bind;
import butterknife.ButterKnife;
import customviews.progresswheel.ProgressWheel;
import de.hdodenhof.circleimageview.CircleImageView;




public class TrialReceivePassengerActivity extends Activity implements ApiManager.APIFETCHER {

    @Bind(com.taas.driver.R.id.cancel_btn) LinearLayout cancelBtn;
    @Bind(com.taas.driver.R.id.activity_countdown_timer_days) ProgressWheel mProgresdsWheel;
    @Bind(com.taas.driver.R.id.cancel_layout) LinearLayout cancelLayout;
    @Bind(com.taas.driver.R.id.map_image) CircleImageView mapImage;
    @Bind(com.taas.driver.R.id.time_txt) TextView timeTxt;
    @Bind(com.taas.driver.R.id.main_layout_pickup_txt) TextView mainLayoutPickupTxt;
    @Bind(com.taas.driver.R.id.main_layout) LinearLayout mainLayout;
    @Bind(com.taas.driver.R.id.ride_expire_pick_address_txt) TextView rideExpirePickAddressTxt;
    @Bind(com.taas.driver.R.id.ride_expire_drop_address_txt) TextView rideExpireDropAddressTxt;
    @Bind(com.taas.driver.R.id.ride_expire_ok_btn) LinearLayout rideExpireOkBtn;
    @Bind(com.taas.driver.R.id.ride_expire_layout) LinearLayout rideExpireLayout;
    @Bind(com.taas.driver.R.id.accept_ride_btn) LinearLayout acceptRideBtn;
    @Bind(com.taas.driver.R.id.expire_msg) TextView expireMsg;
    @Bind(com.taas.driver.R.id.cash_layout) LinearLayout cash_Layout;
    @Bind(com.taas.driver.R.id.card_layout) LinearLayout card_Layout;



    int max_progress = 360;
    ApiManager apiManager;
    SessionManager sessionManager;
    LanguageManager languageManager;

    GsonBuilder builder;
    Gson gson;
    CountDownTimer countDownTimer;
    ProgressDialog progressDialog;
   // FirebaseUtils firebaseUtils ;
    ViewRideInfoDriver viewRideInfoDriver ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("" + this.getResources().getString(com.taas.driver.R.string.loading));
        progressDialog.setCancelable(false);
       // firebaseUtils = new FirebaseUtils(this);
        builder = new GsonBuilder();
        gson = builder.create();
        apiManager = new ApiManager(this);
        sessionManager = new SessionManager(this);
        languageManager = new LanguageManager(this);
        setContentView(com.taas.driver.R.layout.activity_trial_receive_passenger);
        ButterKnife.bind(this);

        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                max_progress = max_progress - 12;
                mProgresdsWheel.setProgress(max_progress);
            }

            public void onFinish() {
                // invoke expired ride view
                if (Config.ReceiverPassengerActivity) {
                    apiManager.execution_method_get(Config.ApiKeys.KEY_REJECT_RIDE, Apis.rejectRide + "?ride_id=" + getIntent().getExtras().getString("" + Config.IntentKeys.RIDE_ID) + "&driver_id=" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID) + "&ride_status=4" + "&driver_token=" + sessionManager.getUserDetails().get(SessionManager.KEY_DriverToken) + "&language_id=" + languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
                }
            }
        };

        apiManager.execution_method_get(Config.ApiKeys.KEY_VIEW_RIDE_INFO_DRIVER, Apis.viewRideInfoDriver + "?ride_id=" + super.getIntent().getExtras().getString("" + Config.IntentKeys.RIDE_ID) + "&driver_token=" + sessionManager.getUserDetails().get(SessionManager.KEY_DriverToken) + "&language_id=" + languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
        apiManager.execution_method_get(Config.ApiKeys.KEY_NEW_RIDE_SYNC, Apis.newRideSync + "?ride_id=" + super.getIntent().getExtras().getString("" + Config.IntentKeys.RIDE_ID) + "&driver_id=" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID) + "&language_id=" + languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));


        rideExpireOkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                   // firebaseUtils.createRidePool(""+FirebaseUtils.NO_RIDES , ""+FirebaseUtils.NO_RIDE_STATUS);
                }catch (Exception e){}
                finish();
            }
        });

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                apiManager.execution_method_get(Config.ApiKeys.KEY_REJECT_RIDE, Apis.rejectRide
                        + "?ride_id=" + getIntent().getExtras().getString("" + Config.IntentKeys.RIDE_ID) + "&driver_id=" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID) + "&ride_status=4" + "&driver_token=" + sessionManager.getUserDetails().get(SessionManager.KEY_DriverToken) + "&language_id=" + languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
            }
        });
        countDownTimer.cancel();

        acceptRideBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                countDownTimer.cancel();
                apiManager.execution_method_get(Config.ApiKeys.KEY_ACEPT_RIDE, Apis.acceptRide + "?ride_id=" + getIntent().getExtras().getString("" + Config.IntentKeys.RIDE_ID) + "&driver_id=" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID) + "&driver_token=" + sessionManager.getUserDetails().get(SessionManager.KEY_DriverToken) + "&language_id=" + languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        Config.ReceiverPassengerActivity = true;

    }


    @Override
    protected void onPause() {
        super.onPause();
        Config.ReceiverPassengerActivity = false;
    }

    public void setViewAccordingToRideStatus(String ride_status, String message) {

        if (ride_status.equals("1")) {
            cancelLayout.setVisibility(View.VISIBLE);
            mainLayout.setVisibility(View.VISIBLE);
            acceptRideBtn.setVisibility(View.VISIBLE);
            rideExpireLayout.setVisibility(View.GONE);
            mProgresdsWheel.setProgress(360);
            countDownTimer.start();

        } // Ride is still live for demotaxiappdriver
        else {  // some thing went wrong
            cancelLayout.setVisibility(View.GONE);
            mainLayout.setVisibility(View.GONE);
            acceptRideBtn.setVisibility(View.GONE);
            rideExpireLayout.setVisibility(View.VISIBLE);
            expireMsg.setText(""+message);
        }
    }

    @Override
    public void onAPIRunningState(int a, String APINAME) {

        if (a == ApiManager.APIFETCHER.KEY_API_IS_STARTED) {
            progressDialog.show();
        } else {
            if (progressDialog != null) {
                progressDialog.dismiss();
            }

        }
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{
            if (APINAME.equals("" + Config.ApiKeys.KEY_VIEW_RIDE_INFO_DRIVER)) {
                 viewRideInfoDriver = gson.fromJson("" + script, ViewRideInfoDriver.class);
                mainLayoutPickupTxt.setText("" + viewRideInfoDriver.getDetails().getPickup_location());
                rideExpirePickAddressTxt.setText("" + viewRideInfoDriver.getDetails().getPickup_location());
                rideExpireDropAddressTxt.setText("" + viewRideInfoDriver.getDetails().getDrop_location());
                Glide.with(this).load("" + Apis.googleImage + "" + viewRideInfoDriver.getDetails().getPickup_lat() + "," + viewRideInfoDriver.getDetails().getPickup_long() + "&zoom=15&size=400x400&key=" + TrialReceivePassengerActivity.this.getResources().getString(com.taas.driver.R.string.google_map_key)).into(mapImage);

                String value = viewRideInfoDriver.getDetails().getPayment_option_name();
                Log.d("**value==", viewRideInfoDriver.getDetails().getPayment_option_name());

                if (value.equals("Cash")){
                    cash_Layout.setVisibility(View.VISIBLE);
                }else{
                    card_Layout.setVisibility(View.VISIBLE);
                } }

            if (APINAME.equals(Config.ApiKeys.KEY_NEW_RIDE_SYNC)) {
                AcceptCheck ac_one = gson.fromJson(""+script , AcceptCheck.class);
                if (ac_one.getResult() == 1 ){
                    NewRideSync newRideSync = gson.fromJson("" + script, NewRideSync.class);
                    setViewAccordingToRideStatus(newRideSync.getResult().toString() , ""+newRideSync.getMsg());
                }else{
                    setViewAccordingToRideStatus("0" , ""+ac_one.getMsg());
                }
            }


            if (APINAME.equals("" + Config.ApiKeys.KEY_REJECT_RIDE)) {
                DeviceId deviceId = gson.fromJson("" + script, DeviceId.class);
                if (deviceId.getResult().toString().equals("1")) {
                    Toast.makeText(this, "" + deviceId.getMsg(), Toast.LENGTH_SHORT).show();
                    finish();
                   // firebaseUtils.createRidePool(""+FirebaseUtils.NO_RIDES , ""+FirebaseUtils.NO_RIDE_STATUS);
                } else {
                    setViewAccordingToRideStatus("0", ""+deviceId.getMsg());
                }
            }


            if (APINAME.equals("" + Config.ApiKeys.KEY_ACEPT_RIDE)) {
                AcceptCheck ac = gson.fromJson(""+script , AcceptCheck.class);
                if(ac.getResult() == 1){
                    RideAccept rideAccept = gson.fromJson("" + script, RideAccept.class);
                    Log.e("**script--trialreceivepassener", String.valueOf(script));

                    if (rideAccept.getResult() == 1) {
                        new RideSession(this).setRideSesion(rideAccept.getDetails().getRide_id(),rideAccept.getDetails().getUser_id(),rideAccept.getDetails().getUser_name(),rideAccept.getDetails().getUser_phone(),rideAccept.getDetails().getCoupon_code(),rideAccept.getDetails().getPickup_lat(),rideAccept.getDetails().getPickup_long(),rideAccept.getDetails().getPickup_location(),rideAccept.getDetails().getDrop_lat(),rideAccept.getDetails().getDrop_long(),rideAccept.getDetails().getDrop_location(),rideAccept.getDetails().getRide_date(),rideAccept.getDetails().getRide_time(),rideAccept.getDetails().getLater_date(),rideAccept.getDetails().getLater_time(),rideAccept.getDetails().getDriver_id(),rideAccept.getDetails().getRide_type(),rideAccept.getDetails().getRide_status(),rideAccept.getDetails().getStatus());
                      //  FirebaseDatabase.getInstance().getReference(""+Config.RideTableReference).child(""+rideAccept.getDetails().getRide_id()).setValue(new RideSessionEvent(""+rideAccept.getDetails().getRide_id() , ""+Config.Status.NORMAL_ACCEPTED, "Not yet generated" , "0"));
                        startActivity(new Intent(this, TrackRideActivity.class)
                                .putExtra("customer_name", "" + rideAccept.getDetails().getUser_name())
                                .putExtra("customer_phone", "" + rideAccept.getDetails().getUser_phone()));
                        //firebaseUtils.createRidePool(""+FirebaseUtils.NO_RIDES , ""+FirebaseUtils.NO_RIDE_STATUS);
                        finish();
                    }
                }else{
                    setViewAccordingToRideStatus("0" , ""+ac.getMsg());
                }
            }
        }catch (Exception e){
            Toast.makeText(this, ""+e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }



    @Override
    public void onFetchResultZero(String script) {

    }


    public class AcceptCheck {

        /**
         * result : 0
         * msg : Ride Expire
         */

        private int result;
        private String msg;

        public int getResult() {
            return result;
        }

        public void setResult(int result) {
            this.result = result;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }
    }


    @Override
    public void onBackPressed() {

    }
}
