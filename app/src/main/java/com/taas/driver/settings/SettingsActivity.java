package com.taas.driver.settings;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apporio.apporiologs.ApporioLog;
import com.bumptech.glide.Glide;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taas.driver.AboutActivity;
import com.taas.driver.Config;
import com.taas.driver.CustomerSupportActivity;
import com.taas.driver.MainActivity;
import com.taas.driver.ProfileActivity;
import com.taas.driver.R;
import com.taas.driver.SplashActivity;
import com.taas.driver.TermsAndCondition;
import com.taas.driver.manager.LanguageManager;
import com.taas.driver.manager.SessionManager;
import com.taas.driver.models.ModelReportIssue;
import com.taas.driver.samwork.ApiManager;
import com.taas.driver.typeface.TypeFaceMuseoRegular;
import com.taas.driver.typeface.TypeFaceTextMeseoSemiBold;
import com.taas.driver.urls.Apis;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class SettingsActivity extends AppCompatActivity implements View.OnClickListener, ApiManager.APIFETCHER {

    @Bind(R.id.tv_toolbar_text)
    TextView tv_toolbar_text;
    @Bind(R.id.ll_language_btn)
    LinearLayout ll_language_btn;
    @Bind(R.id.ll_customer_btn)
    LinearLayout ll_customer_btn;
    @Bind(R.id.ll_report_issue_btn)
    LinearLayout ll_report_issue_btn;
    @Bind(R.id.ll_terms_btn)
    LinearLayout ll_terms_btn;
    @Bind(R.id.ll_about_btn)
    LinearLayout ll_about_btn;
    @Bind(R.id.textView_version_name)
    TextView textView_version_name;

    ProgressDialog progressDialog;
    ApiManager apiManager;
    ModelReportIssue modelReportIssue;
    LanguageManager languageManager;
    GsonBuilder builder;
    Gson gson;
    String versionName;
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.root_action_bar)
    LinearLayout rootActionBar;
    @Bind(R.id.iv_profile_pic)
    CircleImageView ivProfilePic;
    @Bind(R.id.tv_name)
    TextView tvName;
    @Bind(R.id.tv_phone_number)
    TextView tvPhoneNumber;
    @Bind(R.id.tv_profile_email)
    TextView tvProfileEmail;
    @Bind(R.id.llName)
    LinearLayout llName;
    @Bind(R.id.iv_edit)
    CircleImageView ivEdit;
    @Bind(R.id.ll_logout_btn)
    LinearLayout llLogoutBtn;


    String driverImage;

    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        // getSupportActionBar().hide();


        driverImage = getIntent().getStringExtra("image");
        initialization();
        onClickListeners();
        apiCallingMethod();



    }

    private void apiCallingMethod() {
        apiManager.execution_method_get("" + Config.ApiKeys.KEY_REPORT_ISSUE, "" + Apis.RepostIssueDetails);
    }

    private void onClickListeners() {

        ll_language_btn.setOnClickListener(this);
        ll_customer_btn.setOnClickListener(this);
        ll_report_issue_btn.setOnClickListener(this);
        ll_terms_btn.setOnClickListener(this);
        ll_about_btn.setOnClickListener(this);
        back.setOnClickListener(this);
        ivEdit.setOnClickListener(this);
        llLogoutBtn.setOnClickListener(this);
    }

    private void initialization() {

        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        apiManager = new ApiManager(this);
        languageManager = new LanguageManager(this);
        sessionManager = new SessionManager(this);
        builder = new GsonBuilder();
        gson = builder.create();


        try {
            versionName = getPackageManager()
                    .getPackageInfo(getPackageName(), 0).versionName;

            Log.e("VersionNmae", "" + versionName);
            textView_version_name.setText("(" + versionName + ")");
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        String inmage = Apis.imageDomain + driverImage;
        if (!inmage.equals("")) {
            ApporioLog.logD("**driver_image", "" + inmage);

            Glide.with(this).load("" + inmage.replace(" ", "")).into(ivProfilePic);
        }

        tvName.setText("" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_NAME));
        tvPhoneNumber.setText("" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_PHONE));
        tvProfileEmail.setText("" + sessionManager.getUserDetails().get(SessionManager.KEY_DriverEmail));

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.ll_language_btn:
                final String[] languages = new String[]{"English", "Arabic"};
                CFAlertDialog.Builder builder = new CFAlertDialog.Builder(SettingsActivity.this);
                builder.setDialogStyle(CFAlertDialog.CFAlertStyle.ALERT);
                builder.setTitle(R.string.select_language);
                builder.setItems(languages, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int index) {
                        switch (languages[index]) {
                            case "English":
                                languageManager.setLanguage("en");
                                break;
                            case "Arabic":
                                languageManager.setLanguage("ar");
                                break;
                        }
                        dialogInterface.dismiss();
                        Intent intent =new Intent(SettingsActivity.this, SplashActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                    }
                });
                builder.show();

                break;

            case R.id.iv_edit:

                startActivity(new Intent(SettingsActivity.this, ProfileActivity.class));

                break;


            case R.id.ll_logout_btn:

//                CFAlertDialog.Builder builder1 = new CFAlertDialog.Builder(this)
//                        .setDialogStyle(CFAlertDialog.CFAlertStyle.BOTTOM_SHEET)
//                        .setTitle(SettingsActivity.this.getResources().getString(com.taas.driver.R.string.PROFILE_ACTIVITY__logout))
//                        .setMessage(SettingsActivity.this.getResources().getString(com.taas.driver.R.string.are_you_sure_to_log_out))
//                        .addButton(SettingsActivity.this.getResources().getString(com.taas.driver.R.string.PROFILE_ACTIVITY__logout), getResources().getColor(R.color.pure_black), getResources().getColor(com.taas.driver.R.color.pure_black), CFAlertDialog.CFAlertActionStyle.POSITIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                apiManager.execution_method_get("" + Config.ApiKeys.LOGOUT, "" + Apis.logout + "?driver_id=" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID) + "&driver_token=" + sessionManager.getUserDetails().get(SessionManager.KEY_DriverToken));
//                                dialogInterface.dismiss();
//                            }
//                        }).addButton(SettingsActivity.this.getResources().getString(com.taas.driver.R.string.TRACK_RIDE_ACTIVITY__cancel), getResources().getColor(R.color.pure_black), getResources().getColor(com.taas.driver.R.color.pure_black), CFAlertDialog.CFAlertActionStyle.NEGATIVE, CFAlertDialog.CFAlertActionAlignment.JUSTIFIED, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialogInterface, int i) {
//                                dialogInterface.dismiss();
//                            }
//                        });
//                builder1.show();


               // data.put("user_id", "" + sessionManager.getUserDetails().get(SessionManager.USER_ID));
               // data.put("unique_id", "" + Settings.Secure.getString(SettingsActivity.this.getContentResolver(), Settings.Secure.ANDROID_ID));
                                apiManager.execution_method_get("" + Config.ApiKeys.LOGOUT, "" + Apis.logout + "?driver_id=" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID) + "&driver_token=" + sessionManager.getUserDetails().get(SessionManager.KEY_DriverToken));

                break;


            case R.id.ll_customer_btn:
                startActivity(new Intent(SettingsActivity.this, CustomerSupportActivity.class));
                break;

            case R.id.ll_report_issue_btn:

                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts("mailto", "" + modelReportIssue.getDeatils(), null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "" + SettingsActivity.this.getResources().getString(R.string.report_issue));
                emailIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(Intent.createChooser(emailIntent, "" + SettingsActivity.this.getResources().getString(R.string.send_email)));
                emailIntent.setType("text/plain");

                break;

            case R.id.ll_terms_btn:
                startActivity(new Intent(SettingsActivity.this, TermsAndCondition.class));
                break;

            case R.id.ll_about_btn:
                startActivity(new Intent(SettingsActivity.this, AboutActivity.class));

                break;

            case R.id.back:
                finish();
                break;
        }
    }

    @Override
    public void onAPIRunningState(int a, String APINAME) {
        if (a == ApiManager.APIFETCHER.KEY_API_IS_STARTED) {
            progressDialog.show();
        } else {
            progressDialog.hide();
        }
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {

        if (APINAME.equals("" + Config.ApiKeys.KEY_REPORT_ISSUE)) {
            modelReportIssue = gson.fromJson("" + script, ModelReportIssue.class);
        }else if(APINAME.equals("" + Config.ApiKeys.LOGOUT)){
            sessionManager.logoutUser();
           Intent intent = new Intent(SettingsActivity.this, SplashActivity.class);
           intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        }



    }

    @Override
    public void onFetchResultZero(String script) {

    }
}
