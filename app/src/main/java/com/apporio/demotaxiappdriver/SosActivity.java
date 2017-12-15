package com.apporio.demotaxiappdriver;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import com.apporio.demotaxiappdriver.adapter.SosAdapter;
import com.apporio.demotaxiappdriver.models.NewSosModel;
import com.apporio.demotaxiappdriver.models.newdriveraccount.ResultStatusChecker;
import com.apporio.demotaxiappdriver.samwork.ApiManager;
import com.apporio.demotaxiappdriver.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SosActivity extends Activity implements ApiManager.APIFETCHER {

    @Bind(R.id.back)
    ImageView back;
    @Bind(R.id.list)
    ListView list;

    ApiManager apiManager ;
    Gson gson ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson =  new GsonBuilder().create();
        apiManager = new ApiManager(this  );
        setContentView(R.layout.activity_sos);
        ButterKnife.bind(this);

        apiManager.execution_method_get(""+ Config.ApiKeys.KEY_SOS, ""+ Apis.Sos);

        back.setOnClickListener(new View.OnClickListener() {
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
        try {
            ResultStatusChecker rs_check = new GsonBuilder().create().fromJson(""+script , ResultStatusChecker.class);
            if(rs_check.getStatus() == 1){
                NewSosModel newsos_response = gson.fromJson(""+script , NewSosModel.class);
                list.setAdapter(new SosAdapter(this , newsos_response));
            }else {
                Toast.makeText(this, "Result - 0 ", Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){}

    }
}
