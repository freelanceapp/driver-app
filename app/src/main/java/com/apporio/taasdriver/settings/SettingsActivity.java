package com.apporio.taasdriver.settings;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.apporio.taasdriver.AboutActivity;
import com.apporio.taasdriver.Config;
import com.apporio.taasdriver.CustomerSupportActivity;
import com.apporio.taasdriver.R;
import com.apporio.taasdriver.SplashActivity;
import com.apporio.taasdriver.TermsAndCondition;
import com.apporio.taasdriver.manager.LanguageManager;
import com.apporio.taasdriver.models.ModelReportIssue;
import com.apporio.taasdriver.samwork.ApiManager;
import com.apporio.taasdriver.urls.Apis;
import com.crowdfire.cfalertdialog.CFAlertDialog;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        getSupportActionBar().hide();

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
    }

    private void initialization() {

        ButterKnife.bind(this);
        progressDialog = new ProgressDialog(this);
        apiManager = new ApiManager(this);
        languageManager = new LanguageManager(this);
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
                        startActivity(new Intent(SettingsActivity.this, SplashActivity.class));
                        finish();
                    }
                });
                builder.show();

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
        }

    }

    @Override
    public void onFetchResultZero(String script) {

    }
}
