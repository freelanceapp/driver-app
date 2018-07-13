package com.taas.driver;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LegalActivity extends BaseActivity {
    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.tv_toolbar_text)
    TextView tvToolbarText;
    @Bind(R.id.root_action_bar)
    LinearLayout rootActionBar;
    @Bind(R.id.ll_about_btn)
    LinearLayout llAboutBtn;
    @Bind(R.id.ll_terms_btn)
    LinearLayout llTermsBtn;
    @Bind(R.id.ll_privacy_policy)
    LinearLayout llPrivacyPolicy;
    @Bind(R.id.ll_software_licences)
    LinearLayout llSoftwareLicences;
    @Bind(R.id.ll_copyrights)
    LinearLayout llCopyrights;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_legal);
        ButterKnife.bind(this);
    }

    @Override
    protected void onResumeWithConnectionState(boolean connectivityStatus) {

    }


    @OnClick(R.id.llBack)
    public void onBackBtnClicked(){

        finish();
    }


    @OnClick(R.id.ll_about_btn)
    public void onAboutUsBtnClicked(){

        startActivity(new Intent(LegalActivity.this, AboutActivity.class));
    }

    @OnClick(R.id.ll_terms_btn)
    public void onTermsBtnClicked(){


        startActivity(new Intent(LegalActivity.this, TermsAndCondition.class));
    }

    @OnClick(R.id.ll_privacy_policy)
    public void onPrivacyPolicyBtnClicked(){
        startActivity(new Intent(LegalActivity.this, PrivacyPolicyActivity.class));

    }

    @OnClick(R.id.ll_software_licences)
    public void onSwLicenceBtnClicked(){
        startActivity(new Intent(LegalActivity.this, SoftwareLicenceActivity.class));

    }

    @OnClick(R.id.ll_copyrights)
    public void onCopyrightsBtnClicked(){
        startActivity(new Intent(LegalActivity.this, CopyrightsActivity.class));

    }
}
