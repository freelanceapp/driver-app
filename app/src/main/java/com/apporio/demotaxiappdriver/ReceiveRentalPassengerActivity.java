package com.apporio.demotaxiappdriver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apporio.demotaxiappdriver.manager.RideSession;
import com.apporio.demotaxiappdriver.manager.SessionManager;
import com.apporio.demotaxiappdriver.models.restmodels.NewRideAcceptmodel;
import com.apporio.demotaxiappdriver.models.restmodels.NewRideRejectModel;
import com.apporio.demotaxiappdriver.models.restmodels.ResultStatusChecker;
import com.apporio.demotaxiappdriver.others.MyBroadcastReceiver;
import com.apporio.demotaxiappdriver.others.RideSessionEvent;
import com.apporio.demotaxiappdriver.samwork.ApiManager;
import com.apporio.demotaxiappdriver.urls.Apis;
import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import customviews.progresswheel.ProgressWheel;
import de.hdodenhof.circleimageview.CircleImageView;

public class ReceiveRentalPassengerActivity extends Activity implements ApiManager.APIFETCHER {

    int max_progress = 360;
    CountDownTimer countDownTimer;
    ApiManager apiManager;
    SessionManager sessionManager;
    RideSession rideSession ;
    Gson gson;
    ProgressDialog progressDialog ;

    LinearLayout cash_layout, card_layout;

    TextView main_layout_payment;

    @Bind(R.id.activity_countdown_timer_days) ProgressWheel mProgresdsWheel;
    @Bind(R.id.map_image) CircleImageView mapImage;
    @Bind(R.id.car_type_image) ImageView carTypeImage;
    @Bind(R.id.car_type_name_txt) TextView carTypeNameTxt;
    @Bind(R.id.package_txt) TextView packageTxt;
    @Bind(R.id._time_of_booking_txt) TextView TimeOfBookingTxt;
    @Bind(R.id.eta_price_txt) TextView etaPriceTxt;
    @Bind(R.id.pickup_address_txt) TextView pickupAddressTxt;
    @Bind(R.id.accept_btn) LinearLayout acceptBtn;
    @Bind(R.id.reject_btn) LinearLayout rejectBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiManager = new ApiManager(this);
        sessionManager = new SessionManager(this);
        rideSession = new RideSession(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(""+this.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        gson = new GsonBuilder().create();
        setContentView(R.layout.activity_receive_rental_passenger);
        ButterKnife.bind(this);

   //     main_layout_payment = (TextView) findViewById(R.id.main_layout_payment);

        HashMap<String, String> data = new HashMap<>();
        data.put("rental_booking_id", "" + getIntent().getExtras().getString("" + Config.IntentKeys.RIDE_ID));
        apiManager.execution_method_post(Config.ApiKeys.KEY_REST_RIDE_INFO, "" + Apis.Rideinfo, data);

        card_layout = (LinearLayout) findViewById(R.id.card_layout);
        cash_layout = (LinearLayout) findViewById(R.id.cash_layout);

        countDownTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                max_progress = max_progress - 12;
                mProgresdsWheel.setProgress(max_progress);
            }

            public void onFinish() {
                // invoke expired ride view
                if (Config.RentalReceivepassengerActivity) {
                    try {
                        MyBroadcastReceiver.mediaPlayer.stop();
                    }catch (Exception e){
                    }
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("rental_booking_id", "" + getIntent().getExtras().getString("" + Config.IntentKeys.RIDE_ID));
                    data.put("driver_id", "" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID));
                    data.put("driver_token", "" + sessionManager.getUserDetails().get(SessionManager.KEY_DriverToken));
                    apiManager.execution_method_post(Config.ApiKeys.KEY_REST_REJECT_RIDE, "" + Apis.RejectRide, data);
                }
            }
        };

        Glide.with(this).load("http://i2.wikimapia.org/?x=5850&y=3412&zoom=13&type=map&lng=0").into(mapImage);

        mProgresdsWheel.setProgress(360);
        countDownTimer.start();


        acceptBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MyBroadcastReceiver.mediaPlayer.stop();
                }catch (Exception e){
                }

                HashMap<String, String> data = new HashMap<String, String>();
                data.put("rental_booking_id", "" + getIntent().getExtras().getString("" + Config.IntentKeys.RIDE_ID));
                data.put("driver_id", "" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID));
                data.put("driver_token", "" + sessionManager.getUserDetails().get(SessionManager.KEY_DriverToken));

                apiManager.execution_method_post(Config.ApiKeys.KEY_RESt_ACCEPT_API, "" + Apis.AcceptRide, data);
            }
        });

        rejectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    MyBroadcastReceiver.mediaPlayer.stop();
                }catch (Exception e){
                }
                HashMap<String, String> data = new HashMap<String, String>();
                data.put("rental_booking_id", "" + getIntent().getExtras().getString("" + Config.IntentKeys.RIDE_ID));
                data.put("driver_id", "" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID));
                data.put("driver_token", "" + sessionManager.getUserDetails().get(SessionManager.KEY_DriverToken));
                apiManager.execution_method_post(Config.ApiKeys.KEY_REST_REJECT_RIDE, "" + Apis.RejectRide, data);
            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        Config.RentalReceivepassengerActivity = true ;
    }

    @Override
    protected void onPause() {
        super.onPause();
        Config.RentalReceivepassengerActivity = false ;
    }

    @Override
    public void onAPIRunningState(int a, String APINAME) {

        if(a == ApiManager.APIFETCHER.KEY_API_IS_STARTED){
            progressDialog.show();
        }else if (progressDialog.isShowing()){
            progressDialog.dismiss();
        }
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ switch (APINAME){
            case Config.ApiKeys.KEY_REST_RIDE_INFO :
                ResultStatusChecker rs = gson.fromJson("" + script, ResultStatusChecker.class);
                if (rs.getStatus() == 1) {
                  //  NewRideInfoModel response = gson.fromJson("" + script, NewRideInfoModel.class);
                    RentalRideInfoModel response = gson.fromJson("" + script, RentalRideInfoModel.class);
                    pickupAddressTxt.setText("" + response.getDetails().getPickup_location());
                    TimeOfBookingTxt.setText("" + response.getDetails().getBooking_time());
                    carTypeNameTxt.setText(""+response.getDetails().getCar_type_name());
                    packageTxt.setText(""+response.getDetails().getPackage_name());
                    etaPriceTxt.setText(sessionManager.getCurrencyCode()+""+response.getDetails().getPackage_price());

                    String value = ""+response.getDetails().getPayment_option_id();

                    Log.d("**value==", response.getDetails().getPayment_option_id());

                    if (value.equals("1")){
                        cash_layout.setVisibility(View.VISIBLE);
                    }else{
                        card_layout.setVisibility(View.VISIBLE);
                    }
                } else if (rs.getStatus() == 0) {

                } else {
                    Toast.makeText(this, "Something went wrong with API ", Toast.LENGTH_SHORT).show();
                }
                break ;
            case Config.ApiKeys.KEY_RESt_ACCEPT_API:
                AcceptCheck ac_check = gson.fromJson(""+script , AcceptCheck.class);
                if(ac_check.getStatus() == 1){
                    NewRideAcceptmodel accept_response = gson.fromJson(""+script , NewRideAcceptmodel.class);
                    new RideSession(this).setRentalRideSession(accept_response.getDetails().getRental_booking_id(),accept_response.getDetails().getUser_id(),accept_response.getDetails().getUser_name(),accept_response.getDetails().getUser_phone(),accept_response.getDetails().getReferral_code(),accept_response.getDetails().getPickup_lat(),accept_response.getDetails().getPickup_long(),accept_response.getDetails().getPickup_location(),"" , "" , "",accept_response.getDetails().getBooking_date(),"ride_time",accept_response.getDetails().getBooking_date(),accept_response.getDetails().getBooking_time(),accept_response.getDetails().getDriver_id(),accept_response.getDetails().getBooking_type(),""+Config.Status.RENTAL_ACCEPTED,accept_response.getDetails().getStatus());
                    finish();
                    startActivity(new Intent(ReceiveRentalPassengerActivity.this , RentalTrackRideActivity.class));
                    Toast.makeText(this, ""+accept_response.getMessage(), Toast.LENGTH_SHORT).show();
                    FirebaseDatabase.getInstance().getReference(""+Config.RideTableReference).child(""+accept_response.getDetails().getRental_booking_id()).setValue(new RideSessionEvent(""+accept_response.getDetails().getRental_booking_id() , ""+Config.Status.RENTAL_ACCEPTED , "Not yet generated" , "0"));

                }else{
                    finish();
                    Toast.makeText(this, ""+ac_check.getMessage(), Toast.LENGTH_SHORT).show();
                }
                break ;

            case Config.ApiKeys.KEY_REST_REJECT_RIDE:
                AcceptCheck ac = gson.fromJson(""+script , AcceptCheck.class);
                if(ac.getStatus() == 1){
                    NewRideRejectModel reject_response = gson.fromJson(""+script , NewRideRejectModel.class);
                    finish();
                    Toast.makeText(this, ""+reject_response.getMessage(), Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(this, ""+ac.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                }
                break ;
        }}catch (Exception e){}
    }







    public class AcceptCheck {


        /**
         * status : 0
         * message : Ride Expire
         */

        private int status;
        private String message;

        public int getStatus() {
            return status;
        }

        public void setStatus(int status) {
            this.status = status;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }
    }


    @Override
    public void onBackPressed() {

    }
}
