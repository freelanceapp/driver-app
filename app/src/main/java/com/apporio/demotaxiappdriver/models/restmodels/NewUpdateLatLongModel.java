package com.apporio.demotaxiappdriver.models.restmodels;

/**
 * Created by samirgoel3@gmail.com on 8/4/2017.
 */

public class NewUpdateLatLongModel {

    /**
     * result : 1
     * msg : Updated Successfully
     * details : IRIS Tech Park, Sector 49, Gurugram, Haryana 122018, India
     */

    private int result;
    private String msg;
    private String details;

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

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
