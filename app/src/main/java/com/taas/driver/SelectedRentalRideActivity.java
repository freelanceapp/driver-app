package com.taas.driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.taas.driver.manager.LanguageManager;
import com.taas.driver.manager.SessionManager;
import com.taas.driver.models.restmodels.NewRentalRideDeatilsModel;
import com.taas.driver.models.restmodels.ResultStatusChecker;
import com.taas.driver.samwork.ApiManager;
import com.taas.driver.typeface.TypefaceTextView;
import com.taas.driver.urls.Apis;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taas.driver.manager.LanguageManager;
import com.taas.driver.manager.SessionManager;
import com.taas.driver.models.restmodels.NewRentalRideDeatilsModel;
import com.taas.driver.models.restmodels.ResultStatusChecker;
import com.taas.driver.samwork.ApiManager;
import com.taas.driver.typeface.TypefaceTextView;
import com.taas.driver.urls.Apis;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SelectedRentalRideActivity extends Activity implements ApiManager.APIFETCHER {

    LanguageManager languageManager;
    SessionManager sessionManager;
    ApiManager apiManager;
    ProgressDialog pd;
    String RIDE_ID, RIDE_STATUS, RIDE_MODE;
    NewRentalRideDeatilsModel response;

    Gson gson;
    public static Activity activity;

    @Bind(com.taas.driver.R.id.ll_back_ride_select)
    LinearLayout llBackRideSelect;
    @Bind(com.taas.driver.R.id.tv_date_time1)
    TypefaceTextView tvDateTime1;
    @Bind(com.taas.driver.R.id.iv_image_driver)
    CircleImageView ivImageDriver;
    @Bind(com.taas.driver.R.id.customer_name_txt)
    TextView customerNameTxt;
    @Bind(com.taas.driver.R.id.customer_phone_txt)
    TextView customerPhoneTxt;
    @Bind(com.taas.driver.R.id.rating_selected)
    RatingBar ratingSelected;
    @Bind(com.taas.driver.R.id.ll_driver_ki_detail)
    LinearLayout llDriverKiDetail;
    @Bind(com.taas.driver.R.id.tv_car_ima)
    CircleImageView tvCarIma;
    @Bind(com.taas.driver.R.id.tv_rupee)
    TextView tvRupee;
    @Bind(com.taas.driver.R.id.tv_dis)
    TextView tvDis;
    @Bind(com.taas.driver.R.id.tv_time1)
    TextView tvTime1;
    @Bind(com.taas.driver.R.id.ll_bill)
    LinearLayout llBill;
    @Bind(com.taas.driver.R.id.start_time_txt)
    TextView startTimeTxt;
    @Bind(com.taas.driver.R.id.tv_start_location)
    TextView tvStartLocation;
    @Bind(com.taas.driver.R.id.drop_time_txt)
    TextView dropTimeTxt;
    @Bind(com.taas.driver.R.id.tv_end_location)
    TextView tvEndLocation;
    @Bind(com.taas.driver.R.id.ll_location_module)
    LinearLayout llLocationModule;
    @Bind(com.taas.driver.R.id.tv_ride_distance)
    TextView tvRideDistance;
    @Bind(com.taas.driver.R.id.textView6)
    TextView textView6;
    @Bind(com.taas.driver.R.id.total_hours_txt)
    TextView totalHoursTxt;
    @Bind(com.taas.driver.R.id.base_package_txt)
    TextView basePackageTxt;
    @Bind(com.taas.driver.R.id.base_package_price)
    TextView basePackagePrice;
    @Bind(com.taas.driver.R.id.extra_distance_txt)
    TextView extraDistanceTxt;
    @Bind(com.taas.driver.R.id.extra_distance_price_txt)
    TextView extraDistancePriceTxt;
    @Bind(com.taas.driver.R.id.extra_time_txt)
    TextView extraTimeTxt;
    @Bind(com.taas.driver.R.id.extra_time_price_txt)
    TextView extraTimePriceTxt;
    @Bind(com.taas.driver.R.id.total_price_txt)
    TextView totalPriceTxt;
    @Bind(com.taas.driver.R.id.coupon_txt)
    TextView couponTxt;
    @Bind(com.taas.driver.R.id.coupon_price_txt)
    TextView couponPriceTxt;
    @Bind(com.taas.driver.R.id.total_payble_bottom)
    TextView totalPaybleBottom;
    @Bind(com.taas.driver.R.id.ll_track_ride)
    LinearLayout llTrackRide;
    @Bind(com.taas.driver.R.id.activity_selected_rides)
    LinearLayout activitySelectedRides;
    @Bind(com.taas.driver.R.id.bill_layout)
    LinearLayout bill_layout;
    @Bind(com.taas.driver.R.id.drop_layout)
    LinearLayout drop_layout;
    @Bind(com.taas.driver.R.id.night_time_txt)
    TextView nightTimeTxt;
    @Bind(com.taas.driver.R.id.night_time_price_txt)
    TextView nightTimePriceTxt;
    @Bind(com.taas.driver.R.id.peak_time_txt)
    TextView peakTimeTxt;
    @Bind(com.taas.driver.R.id.peak_time_price_txt)
    TextView peakTimePriceTxt;
    @Bind(com.taas.driver.R.id.payMode)
    TextView payMode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activity = this;
        gson = new GsonBuilder().create();
        apiManager = new ApiManager(this);
        pd = new ProgressDialog(this);
        pd.setMessage("" + this.getResources().getString(com.taas.driver.R.string.loading));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        sessionManager = new SessionManager(this);
        languageManager = new LanguageManager(this);

        setContentView(com.taas.driver.R.layout.activity_selected_rental_ride);
        ButterKnife.bind(this);
        RIDE_ID = super.getIntent().getExtras().getString("ride_id");
        RIDE_STATUS = super.getIntent().getExtras().getString("ride_status");
        RIDE_MODE = super.getIntent().getExtras().getString("ride_mode");


        HashMap<String, String> data = new HashMap<>();
        data.put("ride_mode", "" + RIDE_MODE);
        data.put("booking_id", "" + RIDE_ID);
        apiManager.execution_method_post("" + Config.ApiKeys.KEY_REST_RIDE_DETAILS, "" + Apis.RideDetails, data);


        llTrackRide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SelectedRentalRideActivity.this, RentalTrackRideActivity.class));
            }
        });


        llBackRideSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    @Override
    public void onAPIRunningState(int a, String APINAME) {

        if (a == ApiManager.APIFETCHER.KEY_API_IS_STARTED) {
            pd.show();
        } else if (pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{ResultStatusChecker rs = gson.fromJson("" + script, ResultStatusChecker.class);
            if (rs.getStatus() == 1) {
                response = gson.fromJson("" + script, NewRentalRideDeatilsModel.class);
                setView();
            } else if (rs.getStatus() == 0) {
                Toast.makeText(this, "" + rs.getMessage(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, com.taas.driver.R.string.SELECTED_RENTAL_RIDE_ACTIVITY__something_went_wrong_in_api, Toast.LENGTH_SHORT).show();
            }} catch (Exception e){}
    }

    @Override
    public void onFetchResultZero(String script) {

    }


    private void setView() {
        if (response.getDetails().getFinal_bill_amount().equals("0")) {
            bill_layout.setVisibility(View.GONE);
            llBill.setVisibility(View.GONE
            );
        }

        if (response.getDetails().getEnd_location().equals("")) {
            drop_layout.setVisibility(View.GONE);
        }
        RIDE_STATUS = ""+response.getDetails().getBooking_status();
        tvRupee.setText(""+sessionManager.getCurrencyCode() + response.getDetails().getFinal_bill_amount());
        tvDis.setText("" + response.getDetails().getTotal_distance_travel());
        tvTime1.setText("" + response.getDetails().getTotal_time_travel());
        startTimeTxt.setText("" + response.getDetails().getBegin_time());
        dropTimeTxt.setText("" + response.getDetails().getEnd_time());
        tvStartLocation.setText("" + response.getDetails().getPickup_location());
        tvEndLocation.setText("" + response.getDetails().getEnd_location());

        Glide.with(SelectedRentalRideActivity.this).load(""+response.getDetails().getUser_image()).into(ivImageDriver);

        tvRideDistance.setText("" + response.getDetails().getTotal_distance_travel());
        totalHoursTxt.setText("" + response.getDetails().getTotal_time_travel());
        basePackageTxt.setText(getString(com.taas.driver.R.string.SELECTED_RENTAL_RIDE_ACTIVITY__base_package_txt_package) + response.getDetails().getRental_package_distance() + " " + getString(com.taas.driver.R.string.SELECTED_RENTAL_RIDE_ACTIVITY__for) + " " + response.getDetails().getRental_package_hours() + getString(com.taas.driver.R.string.SELECTED_RENTAL_RIDE_ACTIVITY__hours));
        basePackagePrice.setText("" +sessionManager.getCurrencyCode()+ response.getDetails().getRental_package_price());
        extraDistanceTxt.setText(getString(com.taas.driver.R.string.SELECTED_RENTAL_RIDE_ACTIVITY__extra_distance_travel) + response.getDetails().getExtra_distance_travel() + this.getResources().getString(com.taas.driver.R.string.distance_symbol) + " )");
        extraDistancePriceTxt.setText(""+sessionManager.getCurrencyCode() + response.getDetails().getExtra_distance_travel_charge());
        extraTimePriceTxt.setText(getString(com.taas.driver.R.string.SELECTED_RENTAL_RIDE_ACTIVITY__extra_time) + response.getDetails().getExtra_hours_travel() + ")");
        extraTimePriceTxt.setText(""+sessionManager.getCurrencyCode() + response.getDetails().getExtra_hours_travel_charge());
        totalPriceTxt.setText(""+sessionManager.getCurrencyCode() + response.getDetails().getFinal_bill_amount());  /// need to be changes later
        totalPaybleBottom.setText("" +sessionManager.getCurrencyCode()+ response.getDetails().getFinal_bill_amount());
        try{ratingSelected.setRating(Float.parseFloat(""+response.getDetails().getDriver_rating()));}catch (Exception e){}

        if (response.getDetails().getPayment_option_id().equals("1")){
            payMode.setText("CASH");
        }else {
            payMode.setText("PAY WITH CARD");
        }

        customerNameTxt.setText("" + response.getDetails().getUser_name());
        customerPhoneTxt.setText("" + response.getDetails().getUser_phone());

        if (RIDE_STATUS.equals("" + Config.Status.RENTAL_RIDE_END) || RIDE_STATUS.equals(""+Config.Status.RENTAL_RIDE_CANCEl_BY_ADMIN ) || RIDE_STATUS.equals(""+Config.Status.RENTAL_RIDE_CANCELLED_BY_DRIVER ) || RIDE_STATUS.equals(""+Config.Status.RENTAL_RIDE_CANCEL_BY_USER ) || RIDE_STATUS.equals(""+Config.Status.NORMAL_RIDE_CANCEl_BY_ADMIN) || RIDE_STATUS.equals(""+Config.Status.RENTAL_RIDE_CANCEl_BY_ADMIN )) {
            llTrackRide.setVisibility(View.GONE);
        } else {
            llTrackRide.setVisibility(View.VISIBLE);
        }
    }
}
