package com.alakowe.driver;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.alakowe.driver.manager.DeviceManager;
import com.alakowe.driver.manager.LanguageManager;
import com.alakowe.driver.manager.SessionManager;
import com.alakowe.driver.models.restmodels.NewDemoResponse;
import com.alakowe.driver.others.ConnectionDetector;
import com.alakowe.driver.others.FirebaseUtils;
import com.alakowe.driver.samwork.ApiManager;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.alakowe.driver.logger.Logger;
import com.sampermissionutils.AfterPermissionGranted;
import com.sampermissionutils.AppSettingsDialog;
import com.sampermissionutils.EasyPermissions;

import java.util.HashMap;
import java.util.List;

public class SplashActivity extends AppCompatActivity  implements ApiManager.APIFETCHER , EasyPermissions.PermissionCallbacks{
    LinearLayout LoginBox, ll_login_splash , root;
    CoordinatorLayout cl_splash;
    public static Activity splash;
    SessionManager sessionManager;
    ProgressDialog progressDialog ;

    protected static final int REQUEST_CHECK_SETTINGS = 0x1;

    ConnectionDetector cd;
    Boolean isInternetPresent = false;

    String ACCESS_FINE_LOCATION = "android.permission.ACCESS_FINE_LOCATION";
    String CALL_PHONE = "android.permission.CALL_PHONE";
    String READ_EXTERNAL_STORAGE = "android.permission.READ_EXTERNAL_STORAGE";


    DeviceManager deviceManager;
    LanguageManager languageManager;
    Gson gson ;
    FirebaseUtils firebaseUtils ;

    String[] perms = { android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.CAMERA, android.Manifest.permission.CALL_PHONE ,  android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE };


    ApiManager apiManager ;
    private static final int RC_LOCATION_CONTACTS_CAMERA_EXTERNALSTORAGE_PERM = 124;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        languageManager = new LanguageManager(this);
        deviceManager = new DeviceManager(this);
        firebaseUtils = new FirebaseUtils(this);     sessionManager = new SessionManager(this);
        languageManager = new LanguageManager(this);
        apiManager = new ApiManager(this);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(this.getResources().getString(R.string.loading));
        progressDialog.setCancelable(false);
        gson = new GsonBuilder().create();

        setContentView(R.layout.activity_splash);
        LoginBox = (LinearLayout) findViewById(R.id.LoginBox);

        ll_login_splash = (LinearLayout) findViewById(R.id.ll_login_splash);
        cl_splash = (CoordinatorLayout) findViewById(R.id.cl_splash);
        root = (LinearLayout) findViewById(R.id.root);
        splash = this;
        startService(new Intent(this, TimeService.class));
        startService(new Intent(this, TimelySessionService.class));




//        new CheckNewAppVersion(this).setOnTaskCompleteListener(new CheckNewAppVersion.ITaskComplete() {
//            @Override
//            public void onTaskComplete(CheckNewAppVersion.Result result) {
//
//
//                Toast.makeText(SplashActivity.this, "new version available "+result.hasNewVersion(), Toast.LENGTH_SHORT).show();
//                result.getNewVersionCode();
//                Toast.makeText(SplashActivity.this, "new version code "+result.getNewVersionCode(), Toast.LENGTH_SHORT).show();
//                //Get the app current version code.
//                result.getOldVersionCode();
//                Toast.makeText(SplashActivity.this, "old version code "+result.getOldVersionCode(), Toast.LENGTH_SHORT).show();
//                //Opens the Google Play Store on your app page to do the update.
//                result.openUpdateLink();
//            }
//        }).execute();



        findViewById(R.id.demo_driver_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showDialogForDemo();
            }
        });


    }




    @AfterPermissionGranted(RC_LOCATION_CONTACTS_CAMERA_EXTERNALSTORAGE_PERM)
    private void checkPermissions(){
        if (EasyPermissions.hasPermissions(this, perms)) {
            if(checkGPSisOnOrNot()){
                if(Config.isConnectingToInternet(this)){
                    initiateSplashProcess();
                }else {
                    Snackbar.make(root, "No Internet Connection !", Snackbar.LENGTH_LONG).setActionTextColor(this.getResources().getColor(R.color.icons_8_muted_red))
                            .setAction("Settings", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                                }
                            }).setDuration(Snackbar.LENGTH_INDEFINITE).show();
                }
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.permission_String), 124, perms);
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        if (sessionManager.isLoggedIn()) {
            LoginBox.setVisibility(View.GONE);
        } else if (!(sessionManager.isLoggedIn())) {
            LoginBox.setVisibility(View.VISIBLE);
        }
        checkPermissions();

    }

    private void showDialogForDemo() {

        final Dialog dialog = new Dialog(this, android.R.style.Theme_Translucent_NoTitleBar);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setContentView(R.layout.demo_driver_dialog);
        dialog.setCancelable(false);

        final EditText demo_name = (EditText) dialog.findViewById(R.id.demo_name);
        final EditText demo_phone_email = (EditText) dialog.findViewById(R.id.demo_phone_email);

        dialog.findViewById(R.id.back_close).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

      final   HashMap<String , String > data  = new HashMap<String, String>();
        data.put("unique_number" , ""+Settings.Secure.getString(SplashActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));



        dialog.findViewById(R.id.skip).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(demo_phone_email.getText().toString().contains("@")){
                    data.put("driver_name" , ""+demo_name.getText().toString());
                    data.put("driver_email" , ""+demo_phone_email.getText().toString());
                    data.put("driver_phone" , "");
                    apiManager.execution_method_post("demo_driver" , "http://www.apporiotaxi.com/api/demo_register_driver.php" , data);
                }else{
                    data.put("driver_name" , ""+demo_name.getText().toString());
                    data.put("driver_email" , "");
                    data.put("driver_phone" , ""+demo_phone_email.getText().toString());
                    apiManager.execution_method_post("demo_driver" , "http://www.apporiotaxi.com/api/demo_register_driver.php" , data);
                }
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.demo_ok_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(demo_phone_email.getText().toString().contains("@")){
                    data.put("driver_name" , ""+demo_name.getText().toString());
                    data.put("driver_email" , ""+demo_phone_email.getText().toString());
                    data.put("driver_phone" , "");
                    apiManager.execution_method_post("demo_driver" , "http://www.apporiotaxi.com/api/demo_register_driver.php" , data);
                }else{
                    data.put("driver_name" , ""+demo_name.getText().toString());
                    data.put("driver_email" , "");
                    data.put("driver_phone" , ""+demo_phone_email.getText().toString());
                    apiManager.execution_method_post("demo_driver" , "http://www.apporiotaxi.com/api/demo_register_driver.php" , data);
                }
                dialog.dismiss();
            }
        });


        dialog.show();
    }


    public void initiateSplashProcess(){
        languageManager.createLanguageSession();
        setViewAccordingToLoginStatus();


//        if ((ContextCompat.checkSelfPermission(SplashActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(SplashActivity.this, CALL_PHONE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(SplashActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.CALL_PHONE", "android.permission.READ_EXTERNAL_STORAGE"}, 1);
//        } else if ((ContextCompat.checkSelfPermission(SplashActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(SplashActivity.this, CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.CALL_PHONE"}, 1);
//        } else if ((ContextCompat.checkSelfPermission(SplashActivity.this, CALL_PHONE) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(SplashActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{"android.permission.CALL_PHONE", "android.permission.READ_EXTERNAL_STORAGE"}, 1);
//        } else if ((ContextCompat.checkSelfPermission(SplashActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(SplashActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{"android.permission.ACCESS_FINE_LOCATION", "android.permission.READ_EXTERNAL_STORAGE"}, 1);
//        } else if ((ContextCompat.checkSelfPermission(SplashActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{"android.permission.ACCESS_FINE_LOCATION"}, 1);
//        } else if ((ContextCompat.checkSelfPermission(SplashActivity.this, CALL_PHONE) != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{"android.permission.CALL_PHONE"}, 1);
//        } else if ((ContextCompat.checkSelfPermission(SplashActivity.this, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)) {
//            ActivityCompat.requestPermissions(SplashActivity.this, new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
//        } else {
//            checkNetworkStatus();
//        }

        ll_login_splash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });


        findViewById(R.id.ll_register_splash).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });
    }

    private void setViewAccordingToLoginStatus() {
        if (sessionManager.isLoggedIn()) {
            LoginBox.setVisibility(View.GONE);
            startActivity(new Intent(this , MainActivity.class));
            finish();
        } else if (!(sessionManager.isLoggedIn())) {
            LoginBox.setVisibility(View.VISIBLE);
        }
    }



    public void checkNetworkStatus() {
        cd = new ConnectionDetector(getApplicationContext());
        isInternetPresent = cd.isConnectingToInternet();
        if (isInternetPresent) {

            GoogleApiClient googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).build();
            googleApiClient.connect();

            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10000);
            locationRequest.setFastestInterval(10000 / 2);

            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true);

            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            Logger.e("SUCCESS       " + "All location settings are satisfied.");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            Logger.e("RESOLUTION_REQUIRED       " + "Location settings are not satisfied. Show the user a dialog to upgrade location settings ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the result in onActivityResult().
                                status.startResolutionForResult(SplashActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException e) {
                                Logger.e("Exception         " + "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            Logger.e("SETTINGS_CHANGE_UNAVAILABLE       " + "Location settings are inadequate, and cannot be fixed here. Dialog not created.");
                            break;
                    }
                }
            });


        } else {
            Snackbar snackbar = Snackbar
                    .make(cl_splash, SplashActivity.this.getResources().getString(R.string.no_internet_connection), Snackbar.LENGTH_LONG)
                    .setDuration(Snackbar.LENGTH_INDEFINITE)
                    .setActionTextColor(Color.RED)
                    .setAction(""+SplashActivity.this.getResources().getString(R.string.retry), new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            checkNetworkStatus();
                        }
                    });
            View sbView = snackbar.getView();
            TextView textView = (TextView) sbView.findViewById(android.support.design.R.id.snackbar_text);
            textView.setTextColor(Color.WHITE);
            snackbar.show();
        }
    }






    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Logger.e("RESULT_OK       " + "User agreed to make required location settings changes.");
                        break;
                    case Activity.RESULT_CANCELED:
                        Logger.e("RESULT_CANCELED       " + "User chose not to make required location settings changes.");
                        finish();
                        break;
                }
                break;
        }
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
        try{ NewDemoResponse demo_response = gson.fromJson(""+script , NewDemoResponse.class);
            if(demo_response.getResult() == 1){
          //      new SessionManager(this).createLoginSession(demo_response.getDetails().getDriver_id(),demo_response.getDetails().getDriver_name(),demo_response.getDetails().getDriver_phone(),demo_response.getDetails().getDriver_email(),demo_response.getDetails().getDriver_image(),demo_response.getDetails().getDriver_password(),demo_response.getDetails().getDriver_token(),demo_response.getDetails().getDevice_id(),Config.Devicetype,demo_response.getDetails().getRating(),demo_response.getDetails().getCar_type_id(),demo_response.getDetails().getCar_model_id(),
          //              demo_response.getDetails().getCar_number(),demo_response.getDetails().getCity_id(),demo_response.getDetails().getRegister_date(),demo_response.getDetails().getLicense(),demo_response.getDetails().getRc(),demo_response.getDetails().getInsurance(),"other_doc","getlast update","last update date ",demo_response.getDetails().getCompleted_rides(), demo_response.getDetails().getReject_rides(),demo_response.getDetails().getCancelled_rides(),
          //              demo_response.getDetails().getLogin_logout(),demo_response.getDetails().getBusy(),demo_response.getDetails().getOnline_offline(),demo_response.getDetails().getDetail_status(),demo_response.getDetails().getDriver_admin_status(),demo_response.getDetails().getCar_type_name(),demo_response.getDetails().getCar_model_name() , demo_response.getDetails().getUnique_number() , "demo city");

                firebaseUtils.setUpDriver();
                firebaseUtils.createRidePool(FirebaseUtils.NO_RIDES , FirebaseUtils.NO_RIDE_STATUS);


                if (demo_response.getDetails().getDetail_status().equals("1")) {
                    startActivity(new Intent(SplashActivity.this, DocumentActivity.class)
                            .putExtra("driver_id" , ""+demo_response.getDetails().getDriver_id())
                            .putExtra("city_id" , ""+demo_response.getDetails().getCity_id())
                            .putExtra("phone" , ""+demo_response.getDetails().getDriver_phone())
                            .putExtra("password" , ""+demo_response.getDetails().getDriver_password()));
                } else if (demo_response.getDetails().getDetail_status().equals("2")) {
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                }
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                finish();
                SplashActivity.splash.finish();

            }else {
                Toast.makeText(splash, "Unable To create demo user for this device", Toast.LENGTH_SHORT).show();
            }}catch (Exception e){}

    }





    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }


    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        Log.d("TrialSplash Screen ", "onPermissionsGranted:" + requestCode + ":" + perms.size());
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        Log.d("Trial Splasg screen ", "onPermissionsDenied:" + requestCode + ":" + perms.size());
        // (Optional) Check whether the user denied any permissions and checked "NEVER ASK AGAIN."
        // This will display a dialog directing them to enable the permission in app settings.
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }



    private boolean checkGPSisOnOrNot(){
        LocationManager lm = (LocationManager)this.getSystemService(this.LOCATION_SERVICE);
        boolean gps_enabled = false;
        boolean network_enabled = false;

        try {
            gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex) {}

        try {
            network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex) {}

        if(!gps_enabled && !network_enabled) {
            // notify user
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(R.string.enable_app_location);
            dialog.setPositiveButton(R.string.open_location_settings, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    // TODO Auto-generated method stub
                    Intent myIntent = new Intent( Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(myIntent);
                }
            });
            dialog.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();
                }
            });
            dialog.show();

        }
        if(!network_enabled&& !gps_enabled){
            return false;
        }else return true;
    }

}