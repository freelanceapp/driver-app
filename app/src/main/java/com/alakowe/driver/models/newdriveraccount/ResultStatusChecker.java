package com.alakowe.driver.models.newdriveraccount;

/**
 * Created by lenovo-pc on 6/15/2017.
 */

public class ResultStatusChecker {


    /**
     * status : 1
     * message : Login Succesfully
     */

    private int status;
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
