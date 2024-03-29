package com.taas.driver.newmodels;

import java.util.List;

/**
 * Created by Bhuvneshwar on 7/12/2017.
 */

public class DailyEarningModel {

    /**
     * result : 1
     * msg : Ride Details
     * details : {"driver_earning_id":"1","driver_id":"1","total_amount":"95000","rides":"1","amount":"41800.00","outstanding_amount":"53200.00","ride_mode":"1","date":"06-11-2017","company_cut":"53200.00","fare_recevied":"95000","full_ride_details":[{"ride_id":"1","ride_time":"15:12:20","amount":"41800.00","ride_mode":1}]}
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
         * driver_earning_id : 1
         * driver_id : 1
         * total_amount : 95000
         * rides : 1
         * amount : 41800.00
         * outstanding_amount : 53200.00
         * ride_mode : 1
         * date : 06-11-2017
         * company_cut : 53200.00
         * fare_recevied : 95000
         * full_ride_details : [{"ride_id":"1","ride_time":"15:12:20","amount":"41800.00","ride_mode":1}]
         */

        private String driver_earning_id;
        private String driver_id;
        private String total_amount;
        private String rides;
        private String amount;
        private String outstanding_amount;
        private String ride_mode;
        private String date;
        private String company_cut;
        private String fare_recevied;
        private List<FullRideDetailsBean> full_ride_details;

        public String getDriver_earning_id() {
            return driver_earning_id;
        }

        public void setDriver_earning_id(String driver_earning_id) {
            this.driver_earning_id = driver_earning_id;
        }

        public String getDriver_id() {
            return driver_id;
        }

        public void setDriver_id(String driver_id) {
            this.driver_id = driver_id;
        }

        public String getTotal_amount() {
            return total_amount;
        }

        public void setTotal_amount(String total_amount) {
            this.total_amount = total_amount;
        }

        public String getRides() {
            return rides;
        }

        public void setRides(String rides) {
            this.rides = rides;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getOutstanding_amount() {
            return outstanding_amount;
        }

        public void setOutstanding_amount(String outstanding_amount) {
            this.outstanding_amount = outstanding_amount;
        }

        public String getRide_mode() {
            return ride_mode;
        }

        public void setRide_mode(String ride_mode) {
            this.ride_mode = ride_mode;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getCompany_cut() {
            return company_cut;
        }

        public void setCompany_cut(String company_cut) {
            this.company_cut = company_cut;
        }

        public String getFare_recevied() {
            return fare_recevied;
        }

        public void setFare_recevied(String fare_recevied) {
            this.fare_recevied = fare_recevied;
        }

        public List<FullRideDetailsBean> getFull_ride_details() {
            return full_ride_details;
        }

        public void setFull_ride_details(List<FullRideDetailsBean> full_ride_details) {
            this.full_ride_details = full_ride_details;
        }

        public static class FullRideDetailsBean {
            /**
             * ride_id : 1
             * ride_time : 15:12:20
             * amount : 41800.00
             * ride_mode : 1
             */

            private String ride_id;
            private String ride_time;
            private String amount;
            private int ride_mode;

            public String getRide_id() {
                return ride_id;
            }

            public void setRide_id(String ride_id) {
                this.ride_id = ride_id;
            }

            public String getRide_time() {
                return ride_time;
            }

            public void setRide_time(String ride_time) {
                this.ride_time = ride_time;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
            }

            public int getRide_mode() {
                return ride_mode;
            }

            public void setRide_mode(int ride_mode) {
                this.ride_mode = ride_mode;
            }
        }
    }
}