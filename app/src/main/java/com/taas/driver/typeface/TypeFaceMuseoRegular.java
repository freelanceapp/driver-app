package com.taas.driver.typeface;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;

public class TypeFaceMuseoRegular extends AppCompatTextView {
    public TypeFaceMuseoRegular(Context context) {
        super(context);
        applyCustomFont(context);
    }

    public TypeFaceMuseoRegular(Context context, AttributeSet attrs) {
        super(context, attrs);
        applyCustomFont(context);

    }

    public TypeFaceMuseoRegular(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        applyCustomFont(context);

    }
    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface("MuseoSans-Regular.otf", context);
        setTypeface(customFont);
    }
}
