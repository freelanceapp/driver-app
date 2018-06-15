package com.taas.driver;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.taas.driver.adapter.SosAdapter;
import com.taas.driver.location.SamLocationRequestService;
import com.taas.driver.manager.RideSession;
import com.taas.driver.manager.SessionManager;
import com.taas.driver.models.NewSosModel;
import com.taas.driver.models.SosRequestModel;
import com.taas.driver.models.newdriveraccount.ResultStatusChecker;
import com.taas.driver.samwork.ApiManager;
import com.taas.driver.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sampermissionutils.AfterPermissionGranted;
import com.sampermissionutils.EasyPermissions;
import com.taas.driver.adapter.SosAdapter;
import com.taas.driver.location.SamLocationRequestService;
import com.taas.driver.manager.RideSession;
import com.taas.driver.manager.SessionManager;
import com.taas.driver.models.NewSosModel;
import com.taas.driver.models.SosRequestModel;
import com.taas.driver.models.newdriveraccount.ResultStatusChecker;
import com.taas.driver.samwork.ApiManager;
import com.taas.driver.urls.Apis;

import java.util.HashMap;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SosActivity extends Activity implements ApiManager.APIFETCHER {

    @Bind(com.taas.driver.R.id.back)
    ImageView back;
    @Bind(com.taas.driver.R.id.list)
    ListView list;

    ApiManager apiManager;
    Gson gson;
    private static final int TELEPHONE_PERM = 657;
    RideSession rideSession;
    SessionManager sessionManager;
    SamLocationRequestService samLocationRequestService;
    NewSosModel newsos_response;
    String NUMBER_To_CALL = "";
    @Bind(com.taas.driver.R.id.callEmergencyBtn)
    LinearLayout callEmergencyBtn;
    @Bind(com.taas.driver.R.id.closeBtn)
    LinearLayout closeBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gson = new GsonBuilder().create();
        apiManager = new ApiManager(this);
        setContentView(com.taas.driver.R.layout.activity_sos);
        ButterKnife.bind(this);
        sessionManager = new SessionManager(this);
        rideSession = new RideSession(this);
        samLocationRequestService = new SamLocationRequestService(this);


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                if (newsos_response != null) {
                    try {


                        try {
                            NUMBER_To_CALL = "" + newsos_response.getDetails().get(i).getSos_number();
                           // callingTask("" + newsos_response.getDetails().get(i).getSos_number());
                        } catch (Exception e) {
                        }

//                        samLocationRequestService.executeService(new SamLocationRequestService.SamLocationListener() {
//                            @Override
//                            public void onLocationUpdate(Location location) {
//                                HashMap<String, String> data = new HashMap<String, String>();
//                                data.put("ride_id", "" + rideSession.getCurrentRideDetails().get(RideSession.RIDE_ID));
//                                data.put("driver_id", "" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID));
//                                data.put("user_id", "" + rideSession.getCurrentRideDetails().get(RideSession.USER_ID));
//                                data.put("sos_number", "" + newsos_response.getDetails().get(i).getSos_number());
//                                data.put("latitude", "" + location.getLatitude());
//                                data.put("longitude", "" + location.getLongitude());
//                                data.put("application", "2");
//                                apiManager.execution_method_post("" + Config.ApiKeys.SOS_REQUEST_NOTIFIER, "" + Apis.Sos_Request, data);
//                            }
//                        });
                    } catch (Exception e) {
                    }
                }
            }
        });

        callEmergencyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!NUMBER_To_CALL.isEmpty()){
                    try {


                        try {

                            callingTask(NUMBER_To_CALL );
                        } catch (Exception e) {
                        }

                        samLocationRequestService.executeService(new SamLocationRequestService.SamLocationListener() {
                            @Override
                            public void onLocationUpdate(Location location) {
                                HashMap<String, String> data = new HashMap<String, String>();
                                data.put("ride_id", "" + rideSession.getCurrentRideDetails().get(RideSession.RIDE_ID));
                                data.put("driver_id", "" + sessionManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID));
                                data.put("user_id", "" + rideSession.getCurrentRideDetails().get(RideSession.USER_ID));
                                data.put("sos_number", "" + NUMBER_To_CALL);
                                data.put("latitude", "" + location.getLatitude());
                                data.put("longitude", "" + location.getLongitude());
                                data.put("application", "2");
                                apiManager.execution_method_post("" + Config.ApiKeys.SOS_REQUEST_NOTIFIER, "" + Apis.Sos_Request, data);
                            }
                        });
                    } catch (Exception e) {
                    }
                }
            }
        });

        apiManager.execution_method_get("" + Config.ApiKeys.KEY_SOS, "" + Apis.Sos);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });



    }


    @AfterPermissionGranted(TELEPHONE_PERM)
    public void callingTask(String number) throws Exception {
        if (EasyPermissions.hasPermissions(this, Manifest.permission.CALL_PHONE)) {
            try { // Have permission, do the thing!
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + number));
                startActivity(intent);
            } catch (Exception e) {
            }
        } else {
            EasyPermissions.requestPermissions(this, getString(com.taas.driver.R.string.this_app_need_telephony_permission), TELEPHONE_PERM, Manifest.permission.CALL_PHONE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        try {
            callingTask(NUMBER_To_CALL);
        } catch (Exception e) {
        }
    }


    @Override
    public void onAPIRunningState(int a, String APINAME) {

    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        if (APINAME.equals("" + Config.ApiKeys.KEY_SOS)) {
            try {
                ResultStatusChecker rs_check = new GsonBuilder().create().fromJson("" + script, ResultStatusChecker.class);
                if (rs_check.getStatus() == 1) {
                    newsos_response = gson.fromJson("" + script, NewSosModel.class);
                    list.setAdapter(new SosAdapter(this, newsos_response));
                } else {
                    Toast.makeText(this, "Result - 0 ", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
            }
        } else if (APINAME.equals("" + Config.ApiKeys.SOS_REQUEST_NOTIFIER)) {
            SosRequestModel sosRequestModel = gson.fromJson("" + script, SosRequestModel.class);
            finish();
        }


    }


    @Override
    public void onFetchResultZero(String script) {

    }

}
