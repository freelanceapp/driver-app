package com.taas.driver.typeface;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;

/**
 * Created by user on 2/28/2017.
 */

public class TypeFaceTextMeseoSemiBold extends android.support.v7.widget.AppCompatTextView {


    public TypeFaceTextMeseoSemiBold(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public TypeFaceTextMeseoSemiBold(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public TypeFaceTextMeseoSemiBold(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("MuseoSans-SemiBold.otf", context);
        setTypeface(customFont);
    }
}
