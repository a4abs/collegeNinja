package com.collegeninja.college.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.collegeninja.college.activity.ArticleActivity;
import com.collegeninja.college.activity.CollegesActivity;
import com.collegeninja.college.activity.CourseDetailActivity;
import com.fdscollege.college.R;

import java.util.ArrayList;
import java.util.HashMap;

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.MyView> {

    private ArrayList<HashMap<String, String>> arrayList;

    private Context mcon;

    public CourseAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        mcon = context;
        this.arrayList = arrayList;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.librarycustom_layout, parent, false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        final String _id = arrayList.get(position).get("id");
        final String _name = arrayList.get(position).get("name");

        holder.header.setText(_name);

        Glide.with(mcon).load(arrayList.get(position).get("thumb_img")).listener(new RequestListener<String, GlideDrawable>() {
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
                Intent intent = new Intent(mcon, CollegesActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("id",_id);
                intent.putExtra("title",_name);
                intent.putExtra("domain",arrayList.get(position).get("domain"));
                intent.putExtra("description",arrayList.get(position).get("description"));
                intent.putExtra("image", arrayList.get(position).get("thumb_img"));
                intent.putExtra("colleges", arrayList.get(position).get("colleges"));

                mcon.startActivity(intent);
            }
        });

      /*  holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mcon.startActivity(new Intent(mcon, CourseDetailActivity.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK).putExtra("id",_id).putExtra("title",_name).putExtra("description",arrayList.get(position).get("description")).putExtra("image",arrayList.get(position).get("thumb_img")));
            }
        });*/
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