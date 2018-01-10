package com.apporio.demotaxiappdriver;


import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.apporio.apporiologs.ApporioLog;
import com.apporio.demotaxiappdriver.manager.LanguageManager;
import com.apporio.demotaxiappdriver.manager.SessionManager;
import com.apporio.demotaxiappdriver.models.ModelAppVersion;
import com.apporio.demotaxiappdriver.others.AppUtils;
import com.apporio.demotaxiappdriver.samwork.ApiManager;
import com.apporio.demotaxiappdriver.urls.Apis;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.vansuita.library.CheckNewAppVersion;

import org.json.JSONException;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;


// Hierarchy
// Check Permission --> Check GPS Connectivity --> Check Internet Connectivity --> fetch Remote Config --> Check Build variant --> Check Session Login Status --> Run Handler (2 sec )--> main Activity
public class SplashActivity extends BaseInternetCheckActivity implements ApiManager.APIFETCHER {

    private static final String TAG = "SplashActivity";
    @Bind(R.id.register_btn)
    LinearLayout registerBtn;
    @Bind(R.id.login_btn)
    LinearLayout loginBtn;
    @Bind(R.id.LoginBox)
    LinearLayout LoginBox;
    @Bind(R.id.loading_text)
    TextView loadingText;
    private boolean is_gps_dialog_shown = false;
    private boolean is_internet_dialog_is_shown = false;
    private boolean is_version_dialog_is_shown = false;
    ApiManager apiManager;
    String[] PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
    Gson gson;
    ModelAppVersion modelAppVersion;
    public static Activity splash;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseRemoteConfigSettings remoteConfigSettings = new FirebaseRemoteConfigSettings.Builder().setDeveloperModeEnabled(BuildConfig.DEBUG).build();
        apiManager = new ApiManager(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        ButterKnife.bind(this);
        gson = new GsonBuilder().create();
        splash = this;
        new LanguageManager(this).createLanguageSession();

        if (!AppUtils.hasPermissions(this, PERMISSIONS)) {
            ApporioLog.logI(TAG, "Checking Permission On Splash");
            ActivityCompat.requestPermissions(this, PERMISSIONS, 1);
        } else {
            startGPSCheck();
        }


        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, LoginActivity.class));
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SplashActivity.this, RegisterActivity.class));
                overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
            }
        });
    }


    private void startInternetCheckProcess() {
        ApporioLog.logI(TAG, "Now Checking net Connectivity");
        if (AppUtils.isNetworkConnected(this)) {
            ApporioLog.logI(TAG, "Internet Connectivity Status " + true);
            try {
                fetchRemoteConfig();
            } catch (Exception e) {
            }
        } else {
            ApporioLog.logI(TAG, "Internet Connectivity Status " + false + ", Now Showing Internet Dialog");
            if (!is_internet_dialog_is_shown) {
                showInternetDialog();
            }
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (!AppUtils.checkGPSisOnOrNot(SplashActivity.this)) {
            showGPSDialog();
        } else {
            ApporioLog.logI(TAG, "Now GPS Status = " + true);
            startInternetCheckProcess();
        }
    }

    private void fetchRemoteConfig() throws Exception {
        ApporioLog.logI(TAG, "Fetching  Remote Config");
        long cacheExpiration = 3600;
        ApporioLog.logI("" + TAG, "Started fetching Configurations");
        HashMap<String , String > data  = new HashMap<>();
        data.put("application_version" , "0");
        data.put("flag" , "2");
        data.put("application" , "2");
        apiManager.execution_method_post("" + Config.ApiKeys.APP_VERSIONS, "" + Apis.AppVersions , data);
    }

    private void checkForVersionUpdation() throws JSONException {
        new CheckNewAppVersion(this).setOnTaskCompleteListener(new CheckNewAppVersion.ITaskComplete() {
            @Override
            public void onTaskComplete(final CheckNewAppVersion.Result result) {
                ApporioLog.logI("" + TAG, "Has new version available " + result.hasNewVersion());
                ApporioLog.logI("" + TAG, "App store version code " + result.getNewVersionCode());
                ApporioLog.logI("" + TAG, "Current version of App" + result.getOldVersionCode());


                try {
                    if (result.hasNewVersion() && modelAppVersion.getDetails().getAndroid_driver_current_version().contains(""+result.getNewVersionCode()) &&modelAppVersion.getDetails().getAndroid_driver_mandantory_update().contains("1")) {
                        ApporioLog.logI(TAG, "Now Showing app update dialog with mandatory approach");
                        loadingText.setText(R.string.some_man_datory_is_available);
                        showUpdationDialog(true, result);
                    } else if (result.hasNewVersion() && modelAppVersion.getDetails().getAndroid_driver_current_version().equals("" + result.getNewVersionCode()) && modelAppVersion.getDetails().getAndroid_driver_mandantory_update().contains("")) {
                        ApporioLog.logI(TAG, "Now Showing app update dialog with Non mandatory approach");
                        loadingText.setText(R.string.non_mandatory_update);
                        showUpdationDialog(false, result);
                    }else if (result.hasNewVersion() && !modelAppVersion.getDetails().getAndroid_driver_current_version().equals("" + result.getNewVersionCode())){
                        ApporioLog.logI(TAG, "Now Showing app update dialog with Non mandatory approach because unable to judge from back end ");
                        loadingText.setText(R.string.non_mandatory_update);
                        showUpdationDialog(false, result);
                    } else if (!result.hasNewVersion()) {
                        ApporioLog.logI(TAG, "Initiating splash process");
                        loadingText.setText(R.string.app_is_up_to_date);
                        startCheckingLoginProcedure();
                    } else{
                        loadingText.setText("Something went wrong");
                    }
                } catch (Exception e) {
                    Toast.makeText(SplashActivity.this, "" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }).execute();


    }

    private void startCheckingLoginProcedure() {
        ApporioLog.logI(TAG, "Checking login status in session");
        if (new SessionManager(this).isLoggedIn()) {
            ApporioLog.logI(TAG, "Driver is logged in and now launching MainActivity");
            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        } else {
            ApporioLog.logI(TAG, "Driver is not logged in and now launching Login Screen");
            LoginBox.setVisibility(View.VISIBLE);
        }

    }

    public void showGPSDialog() {
        if (!is_gps_dialog_shown) {
            ApporioLog.logI(TAG, "Now GPS Status = " + false + ", Now Showing Dialog");
            new CFAlertDialog.Builder(this)
                    .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                    .setTitle(R.string.enable_app_location)
                    .setMessage(R.string.in_order_to_use_app_settings)
                    .addButton(SplashActivity.this.getString(R.string.open_location_settings), -1, -1, CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            SplashActivity.this.startActivity(myIntent);
                            dialogInterface.dismiss();
                            is_gps_dialog_shown = false;
                        }
                    }).setCancelable(false).show();
            is_gps_dialog_shown = true;
        }

    }

    private void showInternetDialog() {
        new CFAlertDialog.Builder(this)
                .setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT)
                .setTextGravity(Gravity.CENTER)
                .setTitle(R.string.no_internet_connectivity)
                .setHeaderView(R.layout.dialog_header_no_internet)
                .setMessage(R.string.it_seems_you_are_out_of_internet_connection)
                .addButton(SplashActivity.this.getString(R.string.retry), -1, -1, CFAlertDialog.CFAlertActionStyle.DEFAULT, CFAlertDialog.CFAlertActionAlignment.END, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        is_internet_dialog_is_shown = false;
                        startInternetCheckProcess();
                    }
                }).setCancelable(false).show();
        is_internet_dialog_is_shown = true;
    }

    private void showUpdationDialog(final boolean is_maindatory, final CheckNewAppVersion.Result result) {
        if (!is_version_dialog_is_shown) {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(R.string.new_version_is_avaiable);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    is_version_dialog_is_shown = false;
                    result.openUpdateLink();
                }
            });

            if (!is_maindatory) {
                dialog.setNegativeButton(R.string.do_it_later, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                        is_version_dialog_is_shown = false;
                        startCheckingLoginProcedure();
                    }
                });
            }
            dialog.show();
            is_version_dialog_is_shown = true;
        }
    }



    private void showAppmaintainanceDialog() {
            final AlertDialog.Builder dialog = new AlertDialog.Builder(this);
            dialog.setMessage(R.string.your_app_is_in_maintainance);
            dialog.setCancelable(false);
            dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    finish();

                }
            });
            dialog.show();
    }




    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {

                if (AppUtils.hasPermissions(SplashActivity.this, PERMISSIONS)) {
                    startGPSCheck();
                } else {
                    ApporioLog.logI("" + TAG, "Some Permissions are missing");
                }
                return;
            }
        }
    }

    private void startGPSCheck() {
        ApporioLog.logI(TAG, "Checking GPS status");
        if (!AppUtils.checkGPSisOnOrNot(SplashActivity.this)) {
            showGPSDialog();
        } else {
            ApporioLog.logI(TAG, "Now GPS Status = " + true);
            startInternetCheckProcess();
        }
    }


    @Override
    public void onAPIRunningState(int a, String APINAME) {

        if(a == ApiManager.APIFETCHER.KEY_API_IS_STARTED){
            loadingText.setText(R.string.fetching_versions);
        }else {
            loadingText.setText(R.string.checking_new_versions);
        }
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        ApporioLog.logI(TAG, "Successfully fetched the remote config");
        modelAppVersion = gson.fromJson("" + script, ModelAppVersion.class);

        try {
            if(modelAppVersion.getDetails().getAndroid_driver_maintenance_mode().equals("1")){
                ApporioLog.logI(TAG, "Application is in under maintainence");
            showAppmaintainanceDialog();
            } else{
                ApporioLog.logI(TAG, "Checking version of application.");
                checkForVersionUpdation();
            }
        } catch (Exception e) {
            ApporioLog.logE(""+TAG , "Exception Caught in OnFetchComplete ==>"+e.getMessage());
        }
    }

    @Override
    public void onFetchResultZero(String script) {

    }


}
