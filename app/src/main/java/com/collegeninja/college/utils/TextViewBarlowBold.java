package com.collegeninja.college.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TextViewBarlowBold extends TextView {
    Context mContext;
    Typeface tfBarlowBold;
    public TextViewBarlowBold(Context context) {
        super(context);
        init(context,null);
    }

    public TextViewBarlowBold(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public TextViewBarlowBold(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }


    void init(Context context, Object o) {
        if (isInEditMode())
            return;
        mContext = context;
        tfBarlowBold = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Bold.ttf");
        this.setTypeface(tfBarlowBold);
    }
}
