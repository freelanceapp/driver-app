package com.taas.driver;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;

import butterknife.Bind;
import butterknife.ButterKnife;
import customviews.typefacesviews.TypefaceDosisRegular;

/**
 * Created by samirgoel3@gmail.com on 9/11/2017.
 */

public class StatusActiity extends Activity {
    @Bind(com.taas.driver.R.id.back)
    RelativeLayout back;
    @Bind(com.taas.driver.R.id.image)
    ImageView image;
    @Bind(com.taas.driver.R.id.message)
    TypefaceDosisRegular message;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.taas.driver.R.layout.activity_status_activity);
        ButterKnife.bind(this);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        try{
            Glide.with(this).load(""+getIntent().getExtras().getString("image")).into(image);
        }catch
                (Exception e){}
        try{message.setText(""+getIntent().getExtras().getString("message"));}catch (Exception e){}

    }


}
