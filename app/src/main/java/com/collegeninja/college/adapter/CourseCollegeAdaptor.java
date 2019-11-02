package com.collegeninja.college.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.collegeninja.college.activity.CollegeDetailsActivity;
import com.fdscollege.college.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseCollegeAdaptor extends RecyclerView.Adapter<CourseCollegeAdaptor.MyView> {

    private ArrayList<HashMap<String, String>> arrayList;

    private Context mContext;

    public CourseCollegeAdaptor(Context context, ArrayList<HashMap<String, String>> arrayList) {
        mContext = context;
        this.arrayList = arrayList;
    }

    @Override
    public CourseCollegeAdaptor.MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.librarycustom_layout, parent, false);
        return new CourseCollegeAdaptor.MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final CourseCollegeAdaptor.MyView holder, final int position) {

        final String _id = arrayList.get(position).get("id");
        final String _name = arrayList.get(position).get("name");
        final String _thumb_img = arrayList.get(position).get("thumb_img");

        holder.header.setText(_name);


       // try {
            //JSONArray jsonArray = new JSONArray(_thumb_img);
            //JSONObject _jsonObject = jsonArray.getJSONObject(0);
            String img_path = null;
            Picasso.with(mContext)
                    .load(img_path)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.logo)
                    .fit()
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .into(holder.pic);

        /*} catch (JSONException e) {
            e.printStackTrace();
        }*/


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CollegeDetailsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id", arrayList.get(position).get("id"));
                intent.putExtra("name", arrayList.get(position).get("name"));
                intent.putExtra("description", arrayList.get(position).get("description"));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        ImageView pic;
        TextView header;

        public MyView(View view) {
            super(view);
            pic = view.findViewById(R.id.pic);
            header = view.findViewById(R.id.header);
        }
    }
}