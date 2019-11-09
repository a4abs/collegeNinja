package com.collegeninja.college.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

import androidx.annotation.Nullable;

public class TextViewBarlow extends TextView {
    Context mContext;
    Typeface tfBarlow;
    public TextViewBarlow(Context context) {
        super(context);
        init(context,null);
    }

    public TextViewBarlow(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context,attrs);
    }

    public TextViewBarlow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context,attrs);
    }


    void init(Context context, Object o) {
        if (isInEditMode())
            return;
        mContext = context;
        tfBarlow = Typeface.createFromAsset(getResources().getAssets(),
                "fonts/Barlow-Black.ttf");
        this.setTypeface(tfBarlow);
    }
}
