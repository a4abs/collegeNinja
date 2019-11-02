package com.collegeninja.college.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.recyclerview.widget.RecyclerView;

import com.collegeninja.college.activity.SignUpActivity;
import com.fdscollege.college.R;

import java.util.ArrayList;
import java.util.HashMap;

public class StreamAdapter extends RecyclerView.Adapter<StreamAdapter.MyView> {

    private ArrayList<HashMap<String, String>> arrayList;

    private Context mcon;

    public StreamAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        mcon= context;
        this.arrayList = arrayList;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.streamcustom_layout, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {
        final String _id = arrayList.get(position).get("id");
        final String _name = arrayList.get(position).get("name");
        holder.stream.setText(_name);

        holder.stream.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences pref = mcon.getSharedPreferences("college", 0);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("stream_id", _id);
                editor.apply();

                Intent i = new Intent(mcon, SignUpActivity.class);
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                mcon.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        public Button stream;

        public MyView(View view) {
            super(view);
            stream = (Button) view.findViewById(R.id.stream);
        }
    }
}