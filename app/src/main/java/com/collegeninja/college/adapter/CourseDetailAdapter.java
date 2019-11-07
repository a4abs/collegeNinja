package com.collegeninja.college.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.collegeninja.college.activity.CollegesActivity;
import com.collegeninja.college.activity.CourseDetailActivity;
import com.fdscollege.college.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseDetailAdapter extends RecyclerView.Adapter<CourseDetailAdapter.MyView> {

    private ArrayList<HashMap<String, String>> arrayList;

    private Context mContext;

    public CourseDetailAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        mContext = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.college_course, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        final String courseId = arrayList.get(position).get("courseId");
        final String collegeId = arrayList.get(position).get("collegeId");
        final String collegeName = arrayList.get(position).get("name");
        final String courseName = arrayList.get(position).get("courseName");
        final String _domain = arrayList.get(position).get("domain");
        /*final String collegeId = arrayListCourses.get(position).get("collegeId");
        final String courseId = arrayListCourses.get(position).get("courseId");
        final String courseName = arrayListCourses.get(position).get("courseName");
        final String collegeName = arrayListCourses.get(position).get("title");
        final String _domain = arrayListCourses.get(position).get("domain");*/

        holder.header.setText(courseName);

        Glide.with(mContext).load(arrayList.get(position).get("thumb_img")).listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFirstResource) {
                //holder.progress.setVisibility(View.INVISIBLE);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, com.bumptech.glide.request.target.Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(holder.pic);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Course","==>"+courseName);
                Intent intent = new Intent(mContext, CollegesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("collegeId",collegeId);
                intent.putExtra("courseId",courseId);
                intent.putExtra("courseName",courseName);
                intent.putExtra("title",collegeName);
                intent.putExtra("domain",_domain);
                intent.putExtra("description",arrayList.get(position).get("description"));
                intent.putExtra("image", arrayList.get(position).get("thumb_img"));
                intent.putExtra("colleges", arrayList.get(position).get("colleges"));

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
            pic =  view.findViewById(R.id.pic);
            header =  view.findViewById(R.id.header);
        }
    }
}