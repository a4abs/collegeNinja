package com.collegeninja.college.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.collegeninja.college.activity.SelectGradeActivity;
import com.fdscollege.college.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CollegeFeatureAdapter extends RecyclerView.Adapter<CollegeFeatureAdapter.MyView> {

    private ArrayList<HashMap<String, String>> arrayList;

    private Context mcon;

    public CollegeFeatureAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        mcon = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.customfeature, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {
        final String _name = arrayList.get(position).get("name");
        final String description = arrayList.get(position).get("description");
        holder.tv_name.setText(_name);
        holder.tv_description.setText(description);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        public TextView tv_name,tv_description;

        public MyView(View view) {
            super(view);
            tv_name = view.findViewById(R.id.name);
            tv_description = view.findViewById(R.id.description);
        }
    }
}