package com.collegeninja.college.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.collegeninja.college.activity.SelectTypeActivity;
import com.fdscollege.college.R;

public class IntroFragment extends Fragment {
    private static final String BACKGROUND_COLOR = "backgroundColor";
    private static final String PAGE = "page";
    private int mBackgroundColor, mPage;

    public static IntroFragment newInstance(int backgroundColor, int page) {
        IntroFragment frag = new IntroFragment();
        Bundle b = new Bundle();
        b.putInt(BACKGROUND_COLOR, backgroundColor);
        b.putInt(PAGE, page);
        frag.setArguments(b);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!getArguments().containsKey(BACKGROUND_COLOR))
            throw new RuntimeException("Fragment must contain a \"" + BACKGROUND_COLOR + "\" argument!");
        mBackgroundColor = getArguments().getInt(BACKGROUND_COLOR);

        if (!getArguments().containsKey(PAGE))
            throw new RuntimeException("Fragment must contain a \"" + PAGE + "\" argument!");
        mPage = getArguments().getInt(PAGE);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int layoutResId = 0;
        switch (mPage) {
            case 0:
                layoutResId = R.layout.intro_fragment_career_guide_layout;
                break;
            case 1:
                layoutResId = R.layout.intro_fragment_college_layout;
                break;
            case 2:
                layoutResId = R.layout.intro_fragment_news_layout;
                break;
            default:
                layoutResId = R.layout.intro_fragment_college_layout;
        }

        // Inflate the layout resource file
        View view = getActivity().getLayoutInflater().inflate(layoutResId, container, false);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i=new Intent(getActivity(),SelectTypeActivity.class);
                startActivity(i);
            }
        });

        // Set the current page index as the View's tag (useful in the PageTransformer)
        view.setTag(mPage);

        return view;
    }
}

