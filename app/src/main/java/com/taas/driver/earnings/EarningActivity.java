package com.taas.driver.earnings;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import com.taas.driver.Config;
import com.taas.driver.R;
import com.taas.driver.manager.SessionManager;
import com.taas.driver.newmodels.WeeklyEarningModel;
import com.taas.driver.samwork.ApiManager;
import com.taas.driver.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.taas.driver.newmodels.WeeklyEarningModel;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Calendar;

public class EarningActivity extends FragmentActivity implements ApiManager.APIFETCHER, DatePickerDialog.OnDateSetListener {

    ProgressDialog pd;
    TextView dateTV, totalSummaryTV;
    SessionManager sessinManager;
    ApiManager apiManager;
    String driver_id;
    FrameLayout container ;
    String date = "NA";

    TextView total_payout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        apiManager = new ApiManager(this);
        sessinManager = new SessionManager(EarningActivity.this);
        setContentView(R.layout.activity_earning);

        driver_id = sessinManager.getUserDetails().get(SessionManager.KEY_DRIVER_ID);
        pd = new ProgressDialog(this);
        pd.setMessage("" + this.getResources().getString(R.string.loading));
        dateTV = (TextView) findViewById(R.id.dateTV);
        totalSummaryTV = (TextView) findViewById(R.id.totalSummaryTV);
        container = (FrameLayout) findViewById(R.id.container);
        total_payout = (TextView)findViewById(R.id.total_payout);

        findViewById(R.id.viewFullSummLL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EarningActivity.this, WeeklyStatementActivity.class).putExtra("date" , ""+date));
            }
        });


        findViewById(R.id.backBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EarningActivity.this.finish();
            }
        });



        findViewById(R.id.another_week_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                DatePickerDialog dpd = DatePickerDialog.newInstance(
                        EarningActivity.this,

                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                dpd.show(getFragmentManager(), "Datepickerdialog");
            }
        });

        weekelyEarningApi();
    }

    private void weekelyEarningApi() {

        apiManager.execution_method_get("" + Config.ApiKeys.KEY_WEEKLY_EARNING, "" + Apis.weeklyEarnings + driver_id);

    }

    @Override
    public void onAPIRunningState(int a, String APINAME) {
        if (a == ApiManager.APIFETCHER.KEY_API_IS_STARTED) {
            pd.show();
        }
        if (pd.isShowing()) {
            pd.dismiss();
        }
    }

    @Override
    public void onFetchComplete(Object script, String APINAME) {
        try{GsonBuilder builder = new GsonBuilder();
            Gson gson = builder.create();

            if (APINAME.equals("" + Config.ApiKeys.KEY_WEEKLY_EARNING)) {
                try {
                    WeeklyEarningModel weeklyEarningModel = new WeeklyEarningModel();
                    weeklyEarningModel = gson.fromJson("" + script, WeeklyEarningModel.class);

                    if (weeklyEarningModel.getResult() == 1) {
                        // Toast.makeText(this, "" + weeklyEarningModel.getMsg(), Toast.LENGTH_SHORT).show();

                        total_payout.setText(""+ sessinManager.getCurrencyCode() + String.valueOf(weeklyEarningModel.getWeekly_amount()));
                        totalSummaryTV.setText(sessinManager.getCurrencyCode() + weeklyEarningModel.getCompany_payment().toString());
                        for (int i = 0; i < weeklyEarningModel.getDetails().size(); i++) {

                            String current_date = weeklyEarningModel.getDetails().get(0).getDate().toString().replace("2017-" , "");
                            String next_date = weeklyEarningModel.getDetails().get(weeklyEarningModel.getDetails().size() - 1).getDetail().getDate().toString().replace("2017-" , "");
                            dateTV.setText(current_date + "     to     " + next_date);
                            dateTV.setTextColor(this.getResources().getColor(R.color.pure_black));
                        }

                        getSupportFragmentManager()
                                .beginTransaction()
                                .add(R.id.container, FragmentGraph.newInstance("message" , ""+script), "rageComicList")
                                .commit();

//                    view_pager.setAdapter(new GraphFragmentAdapter(getSupportFragmentManager(), "" + script));

                    } else {
                    //    Toast.makeText(this, "" + weeklyEarningModel.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Exception", "" + e);
                }
            }}catch (Exception e){}

    }


    @Override
    public void onFetchResultZero(String script) {

    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
         date = year+"-"+(monthOfYear+1)+"-"+dayOfMonth;
        Log.d("*****change_week" , ""+date);
        apiManager.execution_method_get("" + Config.ApiKeys.KEY_WEEKLY_EARNING, "" + Apis.week_amount + driver_id+"&date="+date);
    }
}
