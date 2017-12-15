
package com.apporio.demotaxiappdriver.models.viewrideinfodriver;

public class ViewRideInfoDriver {


    /**
     * result : 1
     * msg :
     * details : {"ride_id":"1515","user_id":"323","coupon_code":"","pickup_lat":"28.412090804771257","pickup_long":"77.04330235719681","pickup_location":"68, Plaza St, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India","drop_lat":"28.427036620986584","drop_long":"77.06294283270836","drop_location":"N 247, Pearl Ln, Block N, Mayfield Garden, Sector 51, Shamshpur, Haryana 122003, India","ride_date":"Monday, Aug 28","ride_time":"16:07:01","last_time_stamp":"04:07:21 PM","ride_image":"https:maps.googleapis.com/maps/api/staticmap?center=&zoom=12&size=600x300&maptype=roadmap&markers=color:green|label:S|28.412090804771257,77.04330235719681&markers=color:red|label:D|28.427036620986584,77.06294283270836&key=AIzaSyAIFe17P91Mfez3T6cqk7hfDSyvMO812Z4","later_date":"","later_time":"","driver_id":"212","car_type_id":"2","ride_type":"1","ride_status":"7","reason_id":"0","payment_option_id":"1","card_id":"0","ride_admin_status":"1","date":"2017-08-28","payment_option_name":"Cash","user_image":"","user_name":"Samir Goel","user_phone":"+919650923089","rating":"4.7954545454545","arrived_time":"04:07:06 PM","begin_lat":"28.412090804771257","begin_long":"77.04330235719681","begin_location":"68, Plaza St, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India","begin_time":"04:07:11 PM","end_lat":"28.4120412","end_long":"77.043294","end_location":"68, Plaza St, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India","amount":"100.00","distance":"0.00 Miles","time":"0","payment_status":"0","waiting_time":"0 Min","waiting_price":"0.00","done_ride_time":"0 Min","ride_time_price":"00.00","end_time":"04:07:21 PM","peak_time_charge":"0.00","night_time_charge":"0.00","coupons_code":"","coupons_price":"","total_amount":"100"}
     */

    private int result;
    private String msg;
    private DetailsBean details;

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DetailsBean getDetails() {
        return details;
    }

    public void setDetails(DetailsBean details) {
        this.details = details;
    }

    public static class DetailsBean {
        /**
         * ride_id : 1515
         * user_id : 323
         * coupon_code :
         * pickup_lat : 28.412090804771257
         * pickup_long : 77.04330235719681
         * pickup_location : 68, Plaza St, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India
         * drop_lat : 28.427036620986584
         * drop_long : 77.06294283270836
         * drop_location : N 247, Pearl Ln, Block N, Mayfield Garden, Sector 51, Shamshpur, Haryana 122003, India
         * ride_date : Monday, Aug 28
         * ride_time : 16:07:01
         * last_time_stamp : 04:07:21 PM
         * ride_image : https:maps.googleapis.com/maps/api/staticmap?center=&zoom=12&size=600x300&maptype=roadmap&markers=color:green|label:S|28.412090804771257,77.04330235719681&markers=color:red|label:D|28.427036620986584,77.06294283270836&key=AIzaSyAIFe17P91Mfez3T6cqk7hfDSyvMO812Z4
         * later_date :
         * later_time :
         * driver_id : 212
         * car_type_id : 2
         * ride_type : 1
         * ride_status : 7
         * reason_id : 0
         * payment_option_id : 1
         * card_id : 0
         * ride_admin_status : 1
         * date : 2017-08-28
         * payment_option_name : Cash
         * user_image :
         * user_name : Samir Goel
         * user_phone : +919650923089
         * rating : 4.7954545454545
         * arrived_time : 04:07:06 PM
         * begin_lat : 28.412090804771257
         * begin_long : 77.04330235719681
         * begin_location : 68, Plaza St, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India
         * begin_time : 04:07:11 PM
         * end_lat : 28.4120412
         * end_long : 77.043294
         * end_location : 68, Plaza St, Block S, Uppal Southend, Sector 49, Gurugram, Haryana 122018, India
         * amount : 100.00
         * distance : 0.00 Miles
         * time : 0
         * payment_status : 0
         * waiting_time : 0 Min
         * waiting_price : 0.00
         * done_ride_time : 0 Min
         * ride_time_price : 00.00
         * end_time : 04:07:21 PM
         * peak_time_charge : 0.00
         * night_time_charge : 0.00
         * coupons_code :
         * coupons_price :
         * total_amount : 100
         */

        private String ride_id;
        private String user_id;
        private String coupon_code;
        private String pickup_lat;
        private String pickup_long;
        private String pickup_location;
        private String drop_lat;
        private String drop_long;
        private String drop_location;
        private String ride_date;
        private String ride_time;
        private String last_time_stamp;
        private String ride_image;
        private String later_date;
        private String later_time;
        private String driver_id;
        private String car_type_id;
        private String ride_type;
        private String ride_status;
        private String reason_id;
        private String payment_option_id;
        private String card_id;
        private String ride_admin_status;
        private String date;
        private String payment_option_name;
        private String user_image;
        private String user_name;
        private String user_phone;
        private String rating;
        private String arrived_time;
        private String begin_lat;
        private String begin_long;
        private String begin_location;
        private String begin_time;
        private String end_lat;
        private String end_long;
        private String end_location;
        private String amount;
        private String distance;
        private String time;
        private String payment_status;
        private String waiting_time;
        private String waiting_price;
        private String done_ride_time;
        private String ride_time_price;
        private String end_time;
        private String peak_time_charge;
        private String night_time_charge;
        private String coupons_code;
        private String coupons_price;
        private String total_amount;

        public String getRide_id() {
            return ride_id;
        }

        public void setRide_id(String ride_id) {
            this.ride_id = ride_id;
        }

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getCoupon_code() {
            return coupon_code;
        }

        public void setCoupon_code(String coupon_code) {
            this.coupon_code = coupon_code;
        }

        public String getPickup_lat() {
            return pickup_lat;
        }

        public void setPickup_lat(String pickup_lat) {
            this.pickup_lat = pickup_lat;
        }

        public String getPickup_long() {
            return pickup_long;
        }

        public void setPickup_long(String pickup_long) {
            this.pickup_long = pickup_long;
        }

        public String getPickup_location() {
            return pickup_location;
        }

        public void setPickup_location(String pickup_location) {
            this.pickup_location = pickup_location;
        }

        public String getDrop_lat() {
            return drop_lat;
        }

        public void setDrop_lat(String drop_lat) {
            this.drop_lat = drop_lat;
        }

        public String getDrop_long() {
            return drop_long;
        }

        public void setDrop_long(String drop_long) {
            this.drop_long = drop_long;
        }

        public String getDrop_location() {
            return drop_location;
        }

        public void setDrop_location(String drop_location) {
            this.drop_location = drop_location;
        }

        public String getRide_date() {
            return ride_date;
        }

        public void setRide_date(String ride_date) {
            this.ride_date = ride_date;
        }

        public String getRide_time() {
            return ride_time;
        }

        public void setRide_time(String ride_time) {
            this.ride_time = ride_time;
        }

        public String getLast_time_stamp() {
            return last_time_stamp;
        }

        public void setLast_time_stamp(String last_time_stamp) {
            this.last_time_stamp = last_time_stamp;
        }

        public String getRide_image() {
            return ride_image;
        }

        public void setRide_image(String ride_image) {
            this.ride_image = ride_image;
        }

        public String getLater_date() {
            return later_date;
        }

        public void setLater_date(String later_date) {
            this.later_date = later_date;
        }

        public String getLater_time() {
            return later_time;
        }

        public void setLater_time(String later_time) {
            this.later_time = later_time;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getCar_type_id() {
            return car_type_id;
        }

        public void setCar_type_id(String car_type_id) {
            this.car_type_id = car_type_id;
        }

        public String getRide_type() {
            return ride_type;
        }

        public void setRide_type(String ride_type) {
            this.ride_type = ride_type;
        }

        public String getRide_status() {
            return ride_status;
        }

        public void setRide_status(String ride_status) {
            this.ride_status = ride_status;
        }

        public String getReason_id() {
            return reason_id;
        }

        public void setReason_id(String reason_id) {
            this.reason_id = reason_id;
        }

        public String getPayment_option_id() {
            return payment_option_id;
        }

        public void setPayment_option_id(String payment_option_id) {
            this.payment_option_id = payment_option_id;
        }

        public String getCard_id() {
            return card_id;
        }

        public void setCard_id(String card_id) {
            this.card_id = card_id;
        }

        public String getRide_admin_status() {
            return ride_admin_status;
        }

        public void setRide_admin_status(String ride_admin_status) {
            this.ride_admin_status = ride_admin_status;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getPayment_option_name() {
            return payment_option_name;
        }

        public void setPayment_option_name(String payment_option_name) {
            this.payment_option_name = payment_option_name;
        }

        public String getUser_image() {
            return user_image;
        }

        public void setUser_image(String user_image) {
            this.user_image = user_image;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getUser_phone() {
            return user_phone;
        }

        public void setUser_phone(String user_phone) {
            this.user_phone = user_phone;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getArrived_time() {
            return arrived_time;
        }

        public void setArrived_time(String arrived_time) {
            this.arrived_time = arrived_time;
        }

        public String getBegin_lat() {
            return begin_lat;
        }

        public void setBegin_lat(String begin_lat) {
            this.begin_lat = begin_lat;
        }

        public String getBegin_long() {
            return begin_long;
        }

        public void setBegin_long(String begin_long) {
            this.begin_long = begin_long;
        }

        public String getBegin_location() {
            return begin_location;
        }

        public void setBegin_location(String begin_location) {
            this.begin_location = begin_location;
        }

        public String getBegin_time() {
            return begin_time;
        }

        public void setBegin_time(String begin_time) {
            this.begin_time = begin_time;
        }

        public String getEnd_lat() {
            return end_lat;
        }

        public void setEnd_lat(String end_lat) {
            this.end_lat = end_lat;
        }

        public String getEnd_long() {
            return end_long;
        }

        public void setEnd_long(String end_long) {
            this.end_long = end_long;
        }

        public String getEnd_location() {
            return end_location;
        }

        public void setEnd_location(String end_location) {
            this.end_location = end_location;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getTime() {
            return time;
        }

        public void setTime(String time) {
            this.time = time;
        }

        public String getPayment_status() {
            return payment_status;
        }

        public void setPayment_status(String payment_status) {
            this.payment_status = payment_status;
        }

        public String getWaiting_time() {
            return waiting_time;
        }

        public void setWaiting_time(String waiting_time) {
            this.waiting_time = waiting_time;
        }

        public String getWaiting_price() {
            return waiting_price;
        }

        public void setWaiting_price(String waiting_price) {
            this.waiting_price = waiting_price;
        }

        public String getDone_ride_time() {
            return done_ride_time;
        }

        public void setDone_ride_time(String done_ride_time) {
            this.done_ride_time = done_ride_time;
        }

        public String getRide_time_price() {
            return ride_time_price;
        }

        public void setRide_time_price(String ride_time_price) {
            this.ride_time_price = ride_time_price;
        }

        public String getEnd_time() {
            return end_time;
        }

        public void setEnd_time(String end_time) {
            this.end_time = end_time;
        }

        public String getPeak_time_charge() {
            return peak_time_charge;
        }

        public void setPeak_time_charge(String peak_time_charge) {
            this.peak_time_charge = peak_time_charge;
        }

        public String getNight_time_charge() {
            return night_time_charge;
        }

        public void setNight_time_charge(String night_time_charge) {
            this.night_time_charge = night_time_charge;
        }

        public String getCoupons_code() {
            return coupons_code;
        }

        public void setCoupons_code(String coupons_code) {
            this.coupons_code = coupons_code;
        }

        public String getCoupons_price() {
            return coupons_price;
        }

        public void setCoupons_price(String coupons_price) {
            this.coupons_price = coupons_price;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }
    }
}
