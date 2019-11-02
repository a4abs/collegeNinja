package com.collegeninja.college.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.collegeninja.college.activity.CollegeGallery;
import com.fdscollege.college.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CollegeImagesAdaptor extends  RecyclerView.Adapter<CollegeImagesAdaptor.MyView> {
    private ArrayList<HashMap<String, String>> arrayList;
    private Activity mActivity;
    private Context mContext;

    public CollegeImagesAdaptor(Context context, Activity activity, ArrayList<HashMap<String, String>> arrayList){
        this.arrayList = arrayList;
        mContext = context;
        mActivity = activity;

    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_imageview, viewGroup, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, final int i) {
        final String imageUrl = arrayList.get(i).get("img_path");
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.logo)
                .fit()
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.imgViewGallery);

        holder.imgViewGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Image","Clicked");
                Intent intent = new Intent(mActivity, CollegeGallery.class);
                intent.putExtra("position", i);
                intent.putExtra("arrayList", arrayList);
                mActivity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyView extends RecyclerView.ViewHolder {
        ImageView imgViewGallery;
        public MyView(@NonNull View itemView) {
            super(itemView);
            imgViewGallery = itemView.findViewById(R.id.gallery_image);
        }
    }
}
