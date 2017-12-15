package com.apporio.demotaxiappdriver;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;


/**
 * Created by samirgoel3@gmail.com on 4/1/2017.
 */

public class Config {

    public static String currency_symbol = "₦";


    public static String DriverReference = "Drivers_A";
    public static String DriverEventReference = "driver_event";
    public static String Devicetype = "2";
    public static String GeoFireReference = "geofire";
    public static String ActiveRidesRefrence = "Activeride";
    public static String RideTableReference = "RideTable";
    public static boolean ReceiverPassengerActivity = false;
    public static boolean RentalReceivepassengerActivity = false ;

    public static float bearingfactor = 0 ;
    public static String Null_Address_Message = "Out Of Network";

    public static int MainScrenZoon = 15 ;
    public static long LocationUpdateTimeinterval = 2000 ;


    public interface ApiKeys{
        String KEY_ARRIVED = "ride_arrived";
        String KEY_BEGIN_TRIP = "begin_trip";
        String KEY_END_TRIP = "end_trip";
        String KEY_CANCEL_TRIP = "cancel_trip";
        String KEY_CANCEL_REASONS = "cancel_reasons";
        String KEY_View_cities = "view_cities";
        String KEY_View_car_by_city = "view_car_by_cities";
        String KEY_View_car_Model = "view_car_model";
        String KEY_Driver_register = "driver_register";
        String KEY_View_done_ride_info = "view_done_ride_info";
        String KEY_Documents_Submit = "submit_documents";


        String KEY_ONLINE_OFFLINE = "online_offline";
        String KEY_UPDATE_DRIVER_LAT_LONG = "driver_latlong";
        String KEY_UPDATE_DEVICE_ID = "deviceid_driver";
        String KEY_CALL_SUPPORT = "call_support";
        String KEY_RATING_DRIVER = "rating_driver";
        String KEY_APP_CONFIG = "app_config_from_mockable";
        String KEY_NEW_RIDE_SYNC = "new_ride_sync";
        String KEY_VIEW_RIDE_INFO_DRIVER = "view_ride_info_driver";

        String KEY_ACEPT_RIDE = "ride_accept";
        String KEY_REJECT_RIDE = "ride_reject";
        String KEY_RIDES_HISTORY = "view_rides_driver";
        String KEY_ABOUT_US = "about";
        String KEY_TERMS_AND_CONDITION = "tc";
        String KEY_CUSTOMER_SUPPORT = "customer_support";
        String KEY_SOS = "KEY_SOS";
        String KEY_VERIFY_OTP = "verify_otp";

        /////// account module keys
        String KEY_NEW_LOGIN = "Login";
        String KEY_SIGN_UP_STEP_ONE = "Signup_Stap_One";
        String KEY_SIGNUP_TWO = "Signup_Stap_Two";
        String KEY_SIGNUP_THREE = "Signup_Stap_Three";
        String KEY_GET_ALL = "getAll";

        String KEY_DOCUMENT_LIST = "Document_List";
        String KEY_DOCUMENT_UPLOAD = "Document_Upload";
        String KEY_GET_SIGNUP_STAP_FOUR = "Signup_Stap_Four";
        String KEY_DOCUMENTS_UPLOAD_LIST = "Document_Upload";


        //////////////////   rest apis
        String KEY_REST_RIDE_SYNC = "Ride_Sync";
        String KEY_REST_RIDE_INFO = "Ride_Info";
        String KEY_RESt_ACCEPT_API = "Rental_Ride_Accept";
        String KEY_REST_REJECT_RIDE = "Rental_Driver_Reject_Ride";
        String KEY_REST_RIDE_ARRIVED = "Rental_Driver_Arrive";
        String KEY_REST_START_RIDE = "Rental_Driver_Start_Ride";
        String KEY_REST_END_RIDE = "Rental_Driver_End_Ride";
        String KEY_REST_DONE_RIDE_INFO = "Done_Ride_Info";
        String KEY_REST_RATING = "Rating";
        String KEY_REST_RIDE_HISTORY = "Driver_Ride_History";
        String KEY_REST_RIDE_DETAILS = "Ride_Details";
        String KEY_REST_CANCEl_RIDE = "Cancel_Ride";

        String KEY_WEEKLY_EARNING = "Weekly_Earning";
        String KEY_DAILY_EARNING = "Daily_Earning";
        String KEY_DAILY_EARNING_RENTAL = "KEY_DAILY_EARNING_RENTAL";
        String KEY_CHANGE_DESTINATION  = "change_destination";
        String KEY_REST_NOTIFICATIONS = "KEY_NOTIFICATIONS";

        String KEY_FORGOTPASS_OTP = "forgot_pass_otp";
        String KEY_FORGOTPASS_CONFIRMPASS = "forgot_pass_confirm_pass";
    }


    public interface IntentKeys {
      String DOCUMENT_ID = "document_id";
        String RIDE_ID= "ride_id";
    }


//////////////////////////// config attributes

    public static int DistanceGap_tail = 100 ; //


    public static int getScreenWidth (Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;
        return  width ;
    }


    public static int getScreenheight (Activity activity){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        return  height ;
    }





    public interface Status{
        String VAL_1 = "1";  // ride booked by user
        String VAL_2 = "2";  //  ride cancelled by user
        String VAL_3 = "3";  // accepted by demotaxiappdriver
        String VAL_4 = "4";  // Ride cancelled by demotaxiappdriver and trying to alloting to other
        String VAL_5 = "5";  // demotaxiappdriver arrived on door
        String VAL_6 = "6";  //  Ride started
        String VAL_7 = "7";  ///  ride ended by demotaxiappdriver
        String VAL_8 = "8";  // when user booked ride for later
        String VAL_9 = "9";  // ride cancelled by demotaxiappdriver

        // for rental approach
        String RENTAL_BOOKED = "10";  // ride booked via user
        String RENTAL_ACCEPTED = "11";  // rental booking acepted by demotaxiappdriver
        String RENTAL_ARRIVED = "12";  // rental demotaxiappdriver arrived
        String RENTAl_RIDE_STARTED = "13";  // rental ride started by demotaxiappdriver
        String RENTAL_RIDE_REJECTED = "14";  // rental ride reject by demotaxiappdriver
        String RENTAL_RIDE_CANCEL_BY_USER = "15";  // rental ride cancelled by user
        String RENTAL_RIDE_END = "16";  // rental ride end by demotaxiappdriver
        String RENTAL_RIDE_CANCEl_BY_ADMIN = "17";  // rental ride cancel by admin
        String RENTAL_RIDE_CANCELLED_BY_DRIVER = "18";  // rental ride end by demotaxiappdriver


    }
    public static String getStatustext(String val ){
        if(val.equals(""+Status.VAL_1)){
            return  "Booked";
        } if(val.equals(""+Status.VAL_2)){
            return  "Cancelled";
        } if(val.equals(""+Status.VAL_3)){
            return  "Accepted";
        } if(val.equals(""+Status.VAL_4)){
            return  "Rejected";
        } if(val.equals(""+Status.VAL_5)){
            return  "Arrived";
        } if(val.equals(""+Status.VAL_6)){
            return  "Riding Now";
        } if(val.equals(""+Status.VAL_7)){
            return "Completed";
        } if(val.equals(""+Status.VAL_8)){
            return  "Later Request";
        } if(val.equals(""+Status.VAL_9)){
            return  "Cancelled by You";
        }if(val.equals(""+Status.RENTAL_BOOKED)){
            return  "Rental Booking";
        }if(val.equals(""+Status.RENTAL_ARRIVED)){
            return  "Arrived";
        }if(val.equals(""+Status.RENTAl_RIDE_STARTED)){
            return  "Riding Now";
        }if(val.equals(""+Status.RENTAL_ACCEPTED)){
            return  "Accepted";
        }if(val.equals(""+Status.RENTAL_RIDE_END)){
            return  "Completed";
        }if(val.equals(""+Status.RENTAL_RIDE_REJECTED)){
            return  "Rejected By You";
        }if(val.equals(""+Status.RENTAL_RIDE_CANCEL_BY_USER)){
            return  "User Cancelled";
        }if(val.equals(""+Status.RENTAL_RIDE_CANCELLED_BY_DRIVER)){
            return  "You Cancelled";
        }else {
            return  "Something Went Wrong";
        }

    }


    public static boolean isConnectingToInternet(Context context) {
        ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

}
