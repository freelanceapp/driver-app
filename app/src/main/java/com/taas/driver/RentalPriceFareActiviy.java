package com.taas.driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taas.driver.manager.SessionManager;
import com.taas.driver.models.restmodels.NewDoneRidemodel;
import com.taas.driver.models.restmodels.ResultStatusChecker;
import com.taas.driver.samwork.ApiManager;
import com.taas.driver.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taas.driver.manager.SessionManager;
import com.taas.driver.models.restmodels.NewDoneRidemodel;
import com.taas.driver.models.restmodels.ResultStatusChecker;
import com.taas.driver.samwork.ApiManager;
import com.taas.driver.urls.Apis;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RentalPriceFareActiviy extends Activity implements ApiManager.APIFETCHER {


    Gson gson;
    NewDoneRidemodel done_ride_response;
    ApiManager apiManager;
    TextView pay_mode;
    ProgressDialog progressDialog;
    @Bind(com.taas.driver.R.id.pick_location_txt)
    TextView pickLocationTxt;
   /* @Bind(R.id.start_meter_txt)
    TextView startMeterTxt;
   */ @Bind(com.taas.driver.R.id.drop_location_txt)
    TextView dropLocationTxt;
   /* @Bind(R.id.end_meter_txt)
    TextView endMeterTxt;*/
    @Bind(com.taas.driver.R.id.total_payble_top)
    TextView totalPaybleTop;
    /*@Bind(R.id.tv_ride_distance)
    TextView tvRideDistance;
    */@Bind(com.taas.driver.R.id.total_hours_txt)
    TextView totalHoursTxt;
    @Bind(com.taas.driver.R.id.base_package_txt)
    TextView basePackageTxt;
    @Bind(com.taas.driver.R.id.base_package_price)
    TextView basePackagePrice;
   /* @Bind(R.id.extra_distance_txt)
    TextView extraDistanceTxt;
    @Bind(R.id.extra_distance_price_txt)
    TextView extraDistancePriceTxt;
   *//* @Bind(R.id.extra_time_txt)
    TextView extraTimeTxt;
    @Bind(R.id.extra_time_price_txt)
    TextView extraTimePriceTxt;
   */ @Bind(com.taas.driver.R.id.total_price_txt)
    TextView totalPriceTxt;
    @Bind(com.taas.driver.R.id.coupon_txt)
    TextView couponTxt;
    @Bind(com.taas.driver.R.id.coupon_price_txt)
    TextView couponPriceTxt;
    @Bind(com.taas.driver.R.id.total_payble_bottom)
    TextView totalPaybleBottom;
    @Bind(com.taas.driver.R.id.rating_bar)
    RatingBar ratingBar;
    @Bind(com.taas.driver.R.id.ll_submit_rating)
    LinearLayout llSubmitRating;
    @Bind(com.taas.driver.R.id.comments)
    EditText comments;
    @Bind(com.taas.driver.R.id.night_time_txt)
    TextView nightTimeTxt;
    @Bind(com.taas.driver.R.id.night_time_price_txt)
    TextView nightTimePriceTxt;
    @Bind(com.taas.driver.R.id.peak_time_txt)
    TextView peakTimeTxt;
    @Bind(com.taas.driver.R.id.peak_time_price_txt)
    TextView peakTimePriceTxt;
    SessionManager sessionManager ;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new GsonBuilder().create();
        apiManager = new ApiManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("" + this.getResources().getString(com.taas.driver.R.string.loading));
        progressDialog.setCancelable(false);
        sessionManager = new SessionManager(this);
        setContentView(com.taas.driver.R.layout.activity_rental_price_fare_activiy);
        ButterKnife.bind(this);


        pay_mode = (TextView) findViewById(com.taas.driver.R.id.pay_mode);

        HashMap<String, String> data = new HashMap<>();
        data.put("rental_booking_id", "" + getIntent().getExtras().getString("ride_id"));
        apiManager.execution_method_post(Config.ApiKeys.KEY_REST_DONE_RIDE_INFO, "" + Apis.Done_Ride_Info, data);


        llSubmitRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String rating = String.valueOf(ratingBar.getRating());

                if (rating.equals("0.0")) {
                    Toast.makeText(RentalPriceFareActiviy.this, RentalPriceFareActiviy.this.getResources().getString(com.taas.driver.R.string.please_select_stars), Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String, String> data = new HashMap<String, String>();
                    data.put("rating_star", ""+ratingBar.getRating());
                    data.put("rental_booking_id", "" + done_ride_response.getDetails().getRental_booking_id());
                    data.put("comment", "" + comments.getText().toString());
                    data.put("user_id", "" + done_ride_response.getDetails().getUser_id());
                    data.put("driver_id", "" + done_ride_response.getDetails().getDriver_id());
                    data.put("app_id", "2");  // 1 for customer and 2 for demotaxiappdriver
                    apiManager.execution_method_post("" + Config.ApiKeys.KEY_REST_RATING, "" + Apis.Rating, data);
                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finalizeActivity();
    }

    @Override
    public void onAPIRunningState(int a, String APINAME) {
        if (a == ApiManager.APIFETCHER.KEY_API_IS_STARTED) {
            progressDialog.show();
        } else if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ResultStatusChecker rs = gson.fromJson("" + script, ResultStatusChecker.class);
            if (rs.getStatus() == 1) {
                switch (APINAME) {
                    case Config.ApiKeys.KEY_REST_DONE_RIDE_INFO:
                        done_ride_response = gson.fromJson("" + script, NewDoneRidemodel.class);
                        setView();
                        break;
                    case Config.ApiKeys.KEY_REST_RATING:
                        finalizeActivity();
                        break;
                }
            } else {
                Toast.makeText(this, "" + rs.getMessage(), Toast.LENGTH_SHORT).show();
            }} catch (Exception e ){}


    }


    @Override
    public void onFetchResultZero(String script) {

    }

    private void finalizeActivity() {
        finish();
        try {
            RentalTrackRideActivity.activity.finish();
        } catch (Exception e) {

        }
        try {
            SelectedRentalRideActivity.activity.finish();
        } catch (Exception e) {

        }
        try {
            TripHistoryActivity.activity.finish();
        } catch (Exception e) {

        }
    }

    private void setView() {

        String value = ""+done_ride_response.getDetails().getPayment_option_id();

        if (value.equals("1")){
            pay_mode.setText("Payment Mode : CASH");
        }else {
            pay_mode.setText("Payment Mode : PAY WITH CARD");
        }


        pickLocationTxt.setText("" + done_ride_response.getDetails().getBegin_location());
        dropLocationTxt.setText("" + done_ride_response.getDetails().getEnd_location());
      //  startMeterTxt.setText("" + done_ride_response.getDetails().getStart_meter_reading());
       // endMeterTxt.setText("" + done_ride_response.getDetails().getEnd_meter_reading());

        totalPaybleTop.setText(""+sessionManager.getCurrencyCode()+ done_ride_response.getDetails().getFinal_bill_amount());
      //  tvRideDistance.setText("" + done_ride_response.getDetails().getTotal_distance_travel());
        totalHoursTxt.setText("" + done_ride_response.getDetails().getTotal_time_travel());

        basePackageTxt.setText(getString(com.taas.driver.R.string.RENTAL_PRICE_FARE_ACTIVITY__package) + done_ride_response.getDetails().getRental_package_distance() + " " + getString(com.taas.driver.R.string.RENTAL_PRICE_FARE_ACTIVITY__for) + " " + done_ride_response.getDetails().getRental_package_hours() + getString(com.taas.driver.R.string.RENTAL_PRICE_FARE_ACTIVITY__hours));
        basePackagePrice.setText(""+sessionManager.getCurrencyCode() + done_ride_response.getDetails().getRental_package_price());

        //extraDistanceTxt.setText(getString(R.string.RENTAL_PRICE_FARE_ACTIVITY__extra_distance_travel) + done_ride_response.getDetails().getExtra_distance_travel() + " )");
        //extraDistancePriceTxt.setText("" + done_ride_response.getDetails().getExtra_distance_travel_charge());

      //  extraTimePriceTxt.setText(getString(R.string.RENTAL_PRICE_FARE_ACTIVITY__extra_time) + done_ride_response.getDetails().getExtra_hours_travel() + ")");
        //extraTimePriceTxt.setText("" + done_ride_response.getDetails().getExtra_hours_travel_charge());

        totalPriceTxt.setText(""+sessionManager.getCurrencyCode() + done_ride_response.getDetails().getTotal_amount());

        totalPaybleBottom.setText(""+sessionManager.getCurrencyCode() + done_ride_response.getDetails().getFinal_bill_amount());
        if (!done_ride_response.getDetails().getCoupan_code().equals("")) {
            couponPriceTxt.setVisibility(View.VISIBLE);
            couponTxt.setVisibility(View.VISIBLE);
            couponTxt.setText("Coupon Applied " + "(" + done_ride_response.getDetails().getCoupan_code() + ")");
            couponPriceTxt.setText("" + sessionManager.getCurrencyCode() + done_ride_response.getDetails().getCoupan_price());
        } else {
            couponPriceTxt.setVisibility(View.GONE);
            couponTxt.setVisibility(View.GONE);
        }

    }


}
