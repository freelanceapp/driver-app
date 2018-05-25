package com.apporio.taasdriver.earnings;

import com.apporio.taasdriver.Config;
import com.apporio.taasdriver.manager.SessionManager;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.text.DecimalFormat;

public class MyAxisValueFormatter implements IAxisValueFormatter
{

    private DecimalFormat mFormat;
    SessionManager sessionManager ;

    public MyAxisValueFormatter(SessionManager sessionManager) {
        this.sessionManager = sessionManager ;
        mFormat = new DecimalFormat("###,###,###,##0.0");
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis ) {
        return mFormat.format(value) + " "+ sessionManager.getCurrencyCode();
    }
}