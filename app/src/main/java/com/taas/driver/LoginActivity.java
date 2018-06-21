package com.taas.driver;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.apporio.apporiologs.ApporioLog;
import com.taas.driver.manager.LanguageManager;
import com.taas.driver.manager.SessionManager;
import com.taas.driver.models.register.Register;
import com.taas.driver.others.FirebaseUtils;
import com.taas.driver.parsing.AccountModule;
import com.taas.driver.samwork.ApiManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hbb20.CountryCodePicker;
import com.taas.driver.manager.LanguageManager;
import com.taas.driver.manager.SessionManager;
import com.taas.driver.models.register.Register;
import com.taas.driver.others.FirebaseUtils;
import com.taas.driver.parsing.AccountModule;
import com.taas.driver.samwork.ApiManager;

public class LoginActivity extends AppCompatActivity implements ApiManager.APIFETCHER {

    LinearLayout ll_back_login, ll_login, ll_forgot, ll_login_testing;
    EditText edt_phone_login, edt_pass_login, edt_email_login;
    public static Activity loginactivity1;
    ProgressDialog pd;
    LinearLayout tv_forgot;
    LanguageManager languageManager;
    String language_id;
    FirebaseUtils firebaseUtils;
    CountryCodePicker ccp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // firebaseUtils = new FirebaseUtils(this);
        setContentView(com.taas.driver.R.layout.activity_login);
        getSupportActionBar().hide();
        loginactivity1 = this;

        pd = new ProgressDialog(this);
        pd.setMessage("" + this.getResources().getString(com.taas.driver.R.string.loading));

        languageManager = new LanguageManager(this);
        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);

        ccp = (CountryCodePicker) findViewById(com.taas.driver.R.id.phone_ccp);
        tv_forgot = (LinearLayout) findViewById(com.taas.driver.R.id.tv_forgot);
        ll_back_login = (LinearLayout) findViewById(com.taas.driver.R.id.ll_back_login);
        ll_login = (LinearLayout) findViewById(com.taas.driver.R.id.ll_login);
        edt_phone_login = (EditText) findViewById(com.taas.driver.R.id.edt_phone_login);
        edt_pass_login = (EditText) findViewById(com.taas.driver.R.id.edt_pass_login);
        edt_email_login = (EditText) findViewById(com.taas.driver.R.id.edt_email_login);

        edt_pass_login.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf"));
        edt_phone_login.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf"));

        ll_back_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        ll_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AccountModule accountModule = new AccountModule(LoginActivity.this, LoginActivity.this);

                //        String phone = edt_phone_login.getText().toString().trim();
                String pass = edt_pass_login.getText().toString().trim();

                if (edt_phone_login.getText().toString().equals("") && edt_email_login.getText().toString().equals("")) {
                    ApporioLog.logD("**both edt empty, 1", "1");
                    Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(com.taas.driver.R.string.email_can_not_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!edt_email_login.getText().toString().equals("") && edt_phone_login.getText().toString().equals("")) {
                    ApporioLog.logD("**phone edt empty not email, 2", "2");
                    accountModule.loginApi(edt_email_login.getText().toString(), pass, language_id, "1");
                } else if (!edt_phone_login.getText().toString().equals("") && edt_email_login.getText().toString().equals("")) {
                    ApporioLog.logD("**email edt empty not phone, 3", "3");
                    accountModule.loginApi(ccp.getSelectedCountryCodeWithPlus() + edt_phone_login.getText().toString(), pass, language_id, "2");
                } else if (pass.equals("")) {
                    ApporioLog.logD("**passwrd edt empty, 4", "4");
                    Toast.makeText(LoginActivity.this, LoginActivity.this.getResources().getString(com.taas.driver.R.string.password_can_not_be_empty), Toast.LENGTH_SHORT).show();
                } else if (!edt_phone_login.getText().toString().equals("") && !edt_email_login.getText().toString().equals("")) {
                    ApporioLog.logD("**both edt not empty, 5", "5");
                    accountModule.loginApi(edt_email_login.getText().toString(), pass, language_id, "1");
                } else {
                    accountModule.loginApi(edt_email_login.getText().toString(), pass, language_id, "1");
                }
            }
        });

        findViewById(R.id.ll_signUp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                overridePendingTransition(com.taas.driver.R.anim.abc_fade_in, com.taas.driver.R.anim.abc_fade_out);
            }
        });

        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgotPass_VerifyOTP.class));
            }
        });
    }


    @Override
    public void onAPIRunningState(int a, String APINAME) {
        if (a == ApiManager.APIFETCHER.KEY_API_IS_STARTED) {
            pd.show();
        }
        if (a == ApiManager.APIFETCHER.KEY_API_IS_STOPPED) {
            pd.dismiss();
        }
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {

        try {
            GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if (APINAME.equals("Login")) {

                ApporioLog.logD("****** LOGIN_RESPONSE", "" + script);
                Register register;
                register = gson.fromJson("" + script, Register.class);

                Log.e("Driver_phone_register11",""+ register.getDetails().getDriver_phone().toString());

                if (register.getResult() == 1) {

                    String driver_id = register.getDetails().getDriver_id();
                    String detail_status = register.getDetails().getDetail_status();

                    if (detail_status.equals("1")) {
                        Log.e("Driver_phone_register",""+ register.getDetails().getDriver_phone().toString());

                        startActivity(new Intent(LoginActivity.this, DocumentActivity.class)
                                .putExtra("driver_id", "" + register.getDetails().getDriver_id())
                                .putExtra("city_id", "" + register.getDetails().getCity_id())
                                .putExtra("phone", "" + register.getDetails().getDriver_phone().trim())
                                .putExtra("password", "" + edt_pass_login.getText().toString().trim()));
                    } else if (detail_status.equals("2")) {
                        new SessionManager(this).createLoginSession(register.getDetails().getDriver_id(),
                                register.getDetails().getDriver_name(), register.getDetails().getDriver_phone(),
                                register.getDetails().getDriver_email(), register.getDetails().getDriver_image(),
                                register.getDetails().getDriver_password(), register.getDetails().getDriver_token(),
                                register.getDetails().getDevice_id(), Config.Devicetype, register.getDetails().getRating(),
                                register.getDetails().getCar_type_id(), register.getDetails().getCar_model_id(),
                                register.getDetails().getCar_number(), register.getDetails().getCity_id(),
                                register.getDetails().getRegister_date(), register.getDetails().getLicense(),
                                register.getDetails().getRc(), register.getDetails().getInsurance(), "other_doc", "getlast update", "last update date ",
                                register.getDetails().getCompleted_rides(), register.getDetails().getReject_rides(),
                                register.getDetails().getCancelled_rides(),
                                register.getDetails().getLogin_logout(), register.getDetails().getBusy(),
                                register.getDetails().getOnline_offline(), register.getDetails().getDetail_status(),
                                register.getDetails().getDriver_admin_status(), register.getDetails().getCar_type_name(),
                                register.getDetails().getCar_model_name(), "", "" + register.getDetails().getCity_name(),
                                register.getDetails().getDriver_bank_name(), register.getDetails().getDriver_account_number(),
                                register.getDetails().getDriver_account_name());

                       // firebaseUtils.setUpDriver();
                       // firebaseUtils.createRidePool(FirebaseUtils.NO_RIDES, FirebaseUtils.NO_RIDE_STATUS);
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    } else if (detail_status.equals("3")) {
                        startActivity(new Intent(LoginActivity.this, StatusActiity.class)
                                .putExtra("image", "" + register.getDetails().getDriver_status_image())
                                .putExtra("message", "" + register.getDetails().getDriver_status_message()));
                        try {
                            overridePendingTransition(com.taas.driver.R.anim.abc_fade_in, com.taas.driver.R.anim.abc_fade_out);
                            finish();
                            SplashActivity.splash.finish();
                        } catch (Exception e) {
                        }
                    }
                    overridePendingTransition(com.taas.driver.R.anim.abc_fade_in, com.taas.driver.R.anim.abc_fade_out);
                    finish();
                    SplashActivity.splash.finish();
                } else {
                    Toast.makeText(this, "" + register.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
        }

    }

    @Override
    public void onFetchResultZero(String script) {

    }

}
