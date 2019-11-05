package com.collegeninja.college.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TextViewBarlowExtraBold extends TextView {
    Context mContext;
    Typeface tfBarlowBold;

    public TextViewBarlowExtraBold(Context context) {
        super(context);
        init(context,null);
    }

    public TextViewBarlowExtraBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public TextViewBarlowExtraBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }

    void init(Context context, Object o) {
        if (isInEditMode())
            return;
        mContext = context;
        tfBarlowBold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-ExtraBold.ttf");
        this.setTypeface(tfBarlowBold);
    }
}
