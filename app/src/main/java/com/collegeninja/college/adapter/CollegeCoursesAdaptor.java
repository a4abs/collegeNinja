package com.collegeninja.college.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.collegeninja.college.activity.CollegeCourseDetails;
import com.collegeninja.college.activity.CollegesActivity;
import com.fdscollege.college.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CollegeCoursesAdaptor extends RecyclerView.Adapter<CollegeCoursesAdaptor.MyView> {
    private ArrayList<HashMap<String, String>> arrayListCourses;

    private Context mContext;

    public CollegeCoursesAdaptor(Context context, ArrayList<HashMap<String, String>> arrayList){
        mContext = context;
        arrayListCourses = arrayList;

    }

    @NonNull
    @Override
    public CollegeCoursesAdaptor.MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.college_course, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull CollegeCoursesAdaptor.MyView holder, final int position) {
        final String collegeId = arrayListCourses.get(position).get("collegeId");
        final String courseId = arrayListCourses.get(position).get("courseId");
        final String _name = arrayListCourses.get(position).get("name");
        final String _domain = arrayListCourses.get(position).get("domain");

        holder.header.setText(_name);

        holder.header.setText(_name);

        Glide.with(mContext).load(arrayListCourses.get(position).get("thumb_img")).listener(new RequestListener<String, GlideDrawable>() {
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
                Intent intent = new Intent(mContext, CollegeCourseDetails.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("collegeId",courseId);
                intent.putExtra("courseId",courseId);
                intent.putExtra("title",_name);
                intent.putExtra("domain",_domain);
                intent.putExtra("description",arrayListCourses.get(position).get("description"));
                intent.putExtra("image", arrayListCourses.get(position).get("thumb_img"));
                intent.putExtra("colleges", arrayListCourses.get(position).get("colleges"));

                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayListCourses.size();
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
