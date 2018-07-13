package com.taas.driver;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taas.driver.manager.LanguageManager;
import com.taas.driver.models.PrivacyPolicyResponse;
import com.taas.driver.samwork.ApiManager;
import com.taas.driver.urls.Apis;

public class PrivacyPolicyActivity extends AppCompatActivity implements  ApiManager.APIFETCHER{


    LinearLayout bck;
    TextView tv_desc;
    public static Activity tca;

    LanguageManager languageManager;
    String language_id;
    ApiManager apiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        tca = this;
        apiManager = new ApiManager(this);

        bck = (LinearLayout) findViewById(R.id.bck);
        tv_desc = (TextView) findViewById(R.id.tc);

        languageManager = new LanguageManager(this);
        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);

        apiManager.execution_method_get(""+ Config.ApiKeys.Key_privacy_policy, ""+ Apis.privacy_policy+"language_id="+language_id);

        bck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    @Override
    public void onAPIRunningState(int a, String APINAME) {

    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if (APINAME.equals(""+ Config.ApiKeys.Key_privacy_policy)) {
                PrivacyPolicyResponse privacyPolicyResponse;
                privacyPolicyResponse = gson.fromJson(""+script, PrivacyPolicyResponse.class);

                if (privacyPolicyResponse.getResult() == 1) {
                    String desc = privacyPolicyResponse.getDetails().getDescription();
                    tv_desc.setText(Html.fromHtml(""+desc));
                }
            }}catch (Exception e){}

    }

    @Override
    public void onFetchResultZero(String s) {

    }

}
