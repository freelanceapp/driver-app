package com.alakowe.driver;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alakowe.driver.adapter.CarAdapter;
import com.alakowe.driver.adapter.CarModelAdapter;
import com.alakowe.driver.adapter.CityAdapter;
import com.alakowe.driver.manager.LanguageManager;
import com.alakowe.driver.models.ResultCheck;
import com.alakowe.driver.models.carmodels.CarModels;
import com.alakowe.driver.models.register.Register;
import com.alakowe.driver.models.viewcartype.ViewCarType;
import com.alakowe.driver.models.viewcity.ViewCity;
import com.alakowe.driver.samwork.ApiManager;
import com.alakowe.driver.urls.Apis;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity implements  com.alakowe.driver.samwork.ApiManager.APIFETCHER{

    TextView tv_car_type, tv_car_model, tv_city , tv_city_two, tv_ride_category, txt_phone_signup;
    EditText edt_username_signup, edt_email_signup, edt_pass_signup, edt_car_number, edt_bank_name, edt_account_number, edt_account_name;
    LinearLayout ll_register, ll_back_signup;

    public static Activity register;

    ArrayList<String> driver_arr;

    String city_id, city_name, car_id, car_name, car_model_id, car_model_name, ride_cat_id, password;

    ProgressDialog pd;

    String cityCheck = "", carTypeCheck = "", carNameCheck = "";

    ViewCity viewCity;
    ViewCarType viewCarType;
    CarModels carModels;

    LanguageManager languageManager;
    String language_id;

    com.alakowe.driver.samwork.ApiManager apimanager ;

    GsonBuilder builder ;
    Gson gson ;

    private static final int KEY_REGISTER = 110;
    String phoneNumber, bank_name, account_number, account_name;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apimanager = new ApiManager(this);
        builder = new GsonBuilder();
        gson = builder.create();
        setContentView(R.layout.activity_register);
        getSupportActionBar().hide();
        register = this;

        driver_arr = new ArrayList<>();
        driver_arr.add("Normal Ride");
        driver_arr.add("Charter Ride");
        driver_arr.add("Both");


        pd = new ProgressDialog(this);
        pd.setMessage(RegisterActivity.this.getResources().getString(R.string.loading));
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);

        tv_ride_category = (TextView) findViewById(R.id.tv_ride_category);

        tv_city = (TextView) findViewById(R.id.tv_city);
        tv_car_type = (TextView) findViewById(R.id.tv_car_type);
        tv_car_model = (TextView) findViewById(R.id.tv_car_model);

        edt_username_signup = (EditText) findViewById(R.id.edt_username_signup);
        edt_email_signup = (EditText) findViewById(R.id.edt_email_signup);
        edt_pass_signup = (EditText) findViewById(R.id.edt_pass_signup);
        tv_city_two = (TextView) findViewById(R.id.tv_city_two);
        txt_phone_signup = (TextView) findViewById(R.id.txt_phone_signup);

        edt_car_number = (EditText) findViewById(R.id.edt_car_number);
        ll_register = (LinearLayout) findViewById(R.id.ll_register);
        ll_back_signup = (LinearLayout) findViewById(R.id.ll_back_signup);

        edt_bank_name = (EditText) findViewById(R.id.bank_name);
        edt_account_number = (EditText) findViewById(R.id.account_number);
        edt_account_name = (EditText) findViewById(R.id.account_holder_name);


        edt_username_signup.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf"));
        edt_email_signup.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf"));
        txt_phone_signup.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf"));
        edt_pass_signup.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf"));
        edt_car_number.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf"));
        edt_bank_name.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf"));
        edt_account_number.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf"));
        edt_account_name.setTypeface(Typeface.createFromAsset(getAssets(), "OpenSans_Regular.ttf"));

        languageManager = new LanguageManager(this);
        language_id = languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID);

        apimanager.execution_method_get( Config.ApiKeys.KEY_View_cities , Apis.viewCities+"?&language_id="+languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID) );

        tv_city.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (cityCheck.equals("1")) {
                    final Dialog dialog = new Dialog(RegisterActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    dialog.setContentView(R.layout.dialog_for_city);

                    ListView lv_cities = (ListView) dialog.findViewById(R.id.lv_cities);
                    lv_cities.setAdapter(new CityAdapter(RegisterActivity.this, viewCity));


                    lv_cities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            city_id = viewCity.getMsg().get(position).getCityId();
                            if (language_id.equals("1")) {
                                city_name = viewCity.getMsg().get(position).getCityName();
                            } else if (language_id.equals("2")) {
                                city_name = viewCity.getMsg().get(position).getCityNameFrench();
                            } else if (language_id.equals("3")) {
                                city_name = viewCity.getMsg().get(position).getCityNameArabic();
                            }
                            tv_city.setText(city_name);
                            apimanager.execution_method_get(Config.ApiKeys.KEY_View_car_by_city , Apis.viewCarByCities+"?city_id="+city_id+"&language_id="+languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else if (cityCheck.equals("2")) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.no_city_found), Toast.LENGTH_SHORT).show();
                }
            }
        });


        txt_phone_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Intent intent = new Intent(RegisterActivity.this, Verify_OTP.class);
                // startActivityForResult(intent, KEY_REGISTER);
                startActivityForResult(new Intent(RegisterActivity.this, Verify_OTP.class), 110);
            }
        });


        tv_ride_category.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*if (tv_ride_category.getText().toString().equals("Type of Ride")) {
                    Toast.makeText(RegisterActivity.this,  RegisterActivity.this.getResources().getString(R.string.select_ride_category), Toast.LENGTH_SHORT).show();
                } else{*/
                    final Dialog dialog = new Dialog(RegisterActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    dialog.setContentView(R.layout.dialog_driver_category);

                    ListView lv_category = (ListView) dialog.findViewById(R.id.lv_category);
                    lv_category.setAdapter(new Driver_Category_Adapter(RegisterActivity.this, driver_arr));

                    lv_category.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            ride_cat_id = String.valueOf(position + 1);
                            Log.e("**ride_cat_id----", ride_cat_id);
                          //  dialog.dismiss();
                            tv_ride_category.setText(driver_arr.get(position));
                      //      tv_ride_category.setVisibility(View.GONE);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

            }
        });

        tv_city_two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cityCheck.equals("1")) {

                    final Dialog dialog = new Dialog(RegisterActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    dialog.setContentView(R.layout.dialog_for_city);

                    ListView lv_cities = (ListView) dialog.findViewById(R.id.lv_cities);
                    lv_cities.setAdapter(new CityAdapter(RegisterActivity.this, viewCity));


                    lv_cities.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            city_id = viewCity.getMsg().get(position).getCityId();
                            if (language_id.equals("1")) {
                                city_name = viewCity.getMsg().get(position).getCityName();
                                tv_city_two.setVisibility(View.GONE);
                            } else if (language_id.equals("2")) {
                                city_name = viewCity.getMsg().get(position).getCityNameFrench();
                                tv_city_two.setVisibility(View.GONE);
                            } else if (language_id.equals("3")) {
                                city_name = viewCity.getMsg().get(position).getCityNameArabic();
                                tv_city_two.setVisibility(View.GONE);
                            }
                            tv_city.setText(city_name);
                            tv_city_two.setVisibility(View.GONE);

                            apimanager.execution_method_get(Config.ApiKeys.KEY_View_car_by_city , Apis.viewCarByCities+"?city_id="+city_id+"&language_id="+languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else if (cityCheck.equals("2")) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.no_city_found), Toast.LENGTH_SHORT).show();
                }
            }
        });




        tv_car_type.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (tv_city.getText().toString().equals("Select City")) {
                    Toast.makeText(RegisterActivity.this,  RegisterActivity.this.getResources().getString(R.string.please_select_city), Toast.LENGTH_SHORT).show();
                } else if (carTypeCheck.equals("1")) {
                    final Dialog dialog = new Dialog(RegisterActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    dialog.setContentView(R.layout.dialog_for_car);

                    ListView lv_cars = (ListView) dialog.findViewById(R.id.lv_cars);
                    lv_cars.setAdapter(new CarAdapter(RegisterActivity.this, viewCarType));

                    lv_cars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            car_id = viewCarType.getMsg().get(position).getCarTypeId();

                            if (language_id.equals("1")) {
                                car_name = viewCarType.getMsg().get(position).getCarTypeName();
                            } else if (language_id.equals("2")) {
                                car_name = viewCarType.getMsg().get(position).getCarTypeNameFrench();
                            } else if (language_id.equals("3")) {
                                car_name = viewCarType.getMsg().get(position).getCarNameArabic();
                            }
                            tv_car_type.setText(car_name);
                            apimanager.execution_method_get(Config.ApiKeys.KEY_View_car_Model , Apis.viewCarModels+"?car_type_id="+car_id+"&language_id="+languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                } else if (carTypeCheck.equals("2")) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.no_car_found), Toast.LENGTH_SHORT).show();
                }
            }
        });




        tv_car_model.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tv_car_type.getText().toString().equals("Select Car Type")) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.please_select_car_type_first), Toast.LENGTH_SHORT).show();
                } else if (carNameCheck.equals("1")) {
                    final Dialog dialog = new Dialog(RegisterActivity.this, android.R.style.Theme_Translucent_NoTitleBar);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    Window window = dialog.getWindow();
                    window.setGravity(Gravity.CENTER);
                    dialog.setContentView(R.layout.dialog_for_car_model);

                    ListView lv_cars = (ListView) dialog.findViewById(R.id.lv_car_model);
                    lv_cars.setAdapter(new CarModelAdapter(RegisterActivity.this, carModels));

                    lv_cars.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            car_model_id = carModels.getMsg().get(position).getCarModelId();

                            if (language_id.equals("1")) {
                                car_model_name = carModels.getMsg().get(position).getCarModelName();
                            } else if (language_id.equals("2")) {
                                car_model_name = carModels.getMsg().get(position).getCarModelNameFrench();
                            } else if (language_id.equals("3")) {
                                car_model_name = carModels.getMsg().get(position).getCarModelNameArabic();
                            }
                            tv_car_model.setText(car_model_name);
                            dialog.dismiss();
                        }
                    });
                    dialog.show();

                } else {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.no_car_model_found), Toast.LENGTH_SHORT).show();
                }

            }
        });

        ll_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String name = edt_username_signup.getText().toString().trim();
                String email = edt_email_signup.getText().toString().trim();
                password = edt_pass_signup.getText().toString().trim();
                String carTypeName = tv_car_type.getText().toString();
                String carModelName = tv_car_model.getText().toString().trim();
                String carNumber = edt_car_number.getText().toString().trim();
                bank_name = edt_bank_name.getText().toString().trim();
                account_number = edt_account_number.getText().toString().trim();
                account_name = edt_account_name.getText().toString().trim();

                String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+.[a-z]+.[a-z]+";

                if (name.equals("")) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.please_enter_name), Toast.LENGTH_SHORT).show();
                } else if (email.equals("")) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.please_enter_email), Toast.LENGTH_SHORT).show();
                } else if (password.equals("")) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.please_enter_password), Toast.LENGTH_SHORT).show();
                } else if (carTypeName.equals("Select Car Type")) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.please_select_car_type), Toast.LENGTH_SHORT).show();
                } else if (carModelName.equals("Select Car Model")) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.please_select_car_model), Toast.LENGTH_SHORT).show();
                } else if (carNumber.equals("")) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.please_enter_car_number), Toast.LENGTH_SHORT).show();
                } else if (!(email.matches(emailPattern))) {
                    edt_email_signup.setText("");
                    Toast.makeText(getApplicationContext(), RegisterActivity.this.getResources().getString(R.string.please_enter_correct_email), Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.password_should_be_minimum_six_digit), Toast.LENGTH_SHORT).show();
                } else if (bank_name.equals("")) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.please_enter_bank_name), Toast.LENGTH_SHORT).show();
                } else if (account_number.equals("")) {
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.please_enter_account_number), Toast.LENGTH_SHORT).show();
                }else if (account_name.equals("")){
                    Toast.makeText(RegisterActivity.this, RegisterActivity.this.getResources().getString(R.string.please_enter_account_name), Toast.LENGTH_SHORT).show();
                }else {
                    cretaDriverAccount(name, email, password, city_id, car_id, car_model_id, carNumber, ride_cat_id, bank_name, account_number, account_name);
                }
            }
        });

        ll_back_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void cretaDriverAccount(String name, String email, String password, String city_id, String car_id, String car_model_id, String carNumber, String ride_cat_id, String bank_name, String account_number, String account_name) {
        HashMap<String, String> bodyParameters = new HashMap<String, String>();
        bodyParameters.put("driver_name", name);
        bodyParameters.put("driver_email", email);
        bodyParameters.put("driver_phone", phoneNumber);
        bodyParameters.put("driver_password", password);
        bodyParameters.put("city_id", city_id);
        bodyParameters.put("car_type_id", car_id);
        bodyParameters.put("car_number", carNumber);
        bodyParameters.put("car_model_id", car_model_id);
        bodyParameters.put("driver_category", ride_cat_id);
        bodyParameters.put("driver_bank_name", bank_name);
        bodyParameters.put("driver_account_name", account_name);
        bodyParameters.put("driver_account_number", account_number);
        bodyParameters.put("language_id", languageManager.getLanguageDetail().get(LanguageManager.LANGUAGE_ID));
        apimanager.execution_method_post(Config.ApiKeys.KEY_Driver_register , Apis.register, bodyParameters);

      //  apimanager.execution_method_get(Config.ApiKeys.KEY_Driver_register, Apis.register + "?driver_name="+ name + "&driver_email=" + email + "&driver_phone=" +phoneNumber+ "&driver_password="+password+"&city_id="+city_id+"&car_type_id="+car_id+"&car_number="+carNumber+"&car_model_id="+car_model_id+"&driver_category="+ride_cat_id+"&driver_bank_name="+bank_name+"&driver_account_name="+account_name+"&driver_account_number="+account_number);

    }


    /////////////// samir work
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
        try{ if(APINAME.equals(Config.ApiKeys.KEY_View_cities)){
            ResultCheck resultCheck;
            resultCheck = gson.fromJson(""+script, ResultCheck.class);
            if (resultCheck.result.toString().equals("1")) {
                cityCheck = "1";
                viewCity = gson.fromJson(""+script, ViewCity.class);
            } else {
                cityCheck = "2";
            }
        }
            if (APINAME.equals(Config.ApiKeys.KEY_View_car_by_city)) {
                ResultCheck resultCheck;
                resultCheck = gson.fromJson(""+script, ResultCheck.class);
                if (resultCheck.result.equals("1")) {
                    carTypeCheck = "1";
                    viewCarType = gson.fromJson(""+script, ViewCarType.class);
                } else {
                    carTypeCheck = "2";
                }
            }
            if (APINAME.equals(Config.ApiKeys.KEY_View_car_Model)){
                ResultCheck resultCheck;
                resultCheck = gson.fromJson(""+script, ResultCheck.class);
                if (resultCheck.result.equals("1")) {
                    carNameCheck = "1";
                    carModels = gson.fromJson(""+script, CarModels.class);
                } else {
                    carNameCheck = "2";
                }
            }
            if (APINAME.equals(Config.ApiKeys.KEY_Driver_register)){
                Register register = new Register();
                register = gson.fromJson(""+script + "", Register.class);
                if (register.getResult()== 1) {
                    Toast.makeText(this, "" + register.getMsg(), Toast.LENGTH_SHORT).show();
//                new SessionManager(this).createLoginSession(register.getDetails().getDriverId(),register.getDetails().getDriverName(),register.getDetails().getDriverPhone(),register.getDetails().getDriverEmail(),register.getDetails().getDriverImage(),register.getDetails().getDriverPassword(),register.getDetails().getDriverToken(),register.getDetails().getDeviceId(),Config.Devicetype,register.getDetails().getRating(),register.getDetails().getCarTypeId(),register.getDetails().getCarModelId(),
//                        register.getDetails().getCarNumber(),register.getDetails().getCityId(),register.getDetails().getRegisterDate(),register.getDetails().getLicense(),register.getDetails().getRc(),register.getDetails().getInsurance(),"other_doc","getlast update","last update date ",register.getDetails().getCompletedRides(), register.getDetails().getRejectRides(),register.getDetails().getCancelledRides(),
//                        register.getDetails().getLoginLogout(),register.getDetails().getBusy(),register.getDetails().getOnlineOffline(),register.getDetails().getDetailStatus(),register.getDetails().getStatus(),register.getDetails().getCarTypeName(),register.getDetails().getCarModelName() , "");
                    startActivity(new Intent(this, DocumentActivity.class)
                            .putExtra("driver_id", "" + register.getDetails().getDriver_id())
                            .putExtra("city_id", "" + register.getDetails().getCity_id())
                            .putExtra("email", edt_email_signup.getText().toString())
                            .putExtra("password", password));

                    Log.d("**city_id===", register.getDetails().getCity_id());
                    Log.d("**driver_id===", register.getDetails().getDriver_id());
                    Log.d("**email===", edt_email_signup.getText().toString());
                    Log.d("**password===", password);
//                firebaseUtils.setUpDriver(register);
                    overridePendingTransition(R.anim.abc_fade_in, R.anim.abc_fade_out);
                    finish();
                    SplashActivity.splash.finish();
                } else {
                    Toast.makeText(this, "" + register.getMsg(), Toast.LENGTH_SHORT).show();
                }
            }}catch (Exception e){}

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case KEY_REGISTER:
                phoneNumber = data.getExtras().getString("phone_number");
                Log.e("**PHONE_NUMBER---", phoneNumber);
                txt_phone_signup.setText(data.getExtras().getString("phone_number"));
                break;
        }
    }
}