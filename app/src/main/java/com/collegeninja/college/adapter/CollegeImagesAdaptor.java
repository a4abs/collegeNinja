package com.collegeninja.college.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.fdscollege.college.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CollegeImagesAdaptor extends  RecyclerView.Adapter<CollegeImagesAdaptor.MyView> {
    private ArrayList<HashMap<String, String>> arrayList;

    private Context mContext;

    public CollegeImagesAdaptor(Context context, ArrayList<HashMap<String, String>> arrayList){
        this.arrayList = arrayList;
        mContext = context;

    }

    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.gallery_imageview, viewGroup, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int i) {
        String imageUrl = arrayList.get(i).get("img_path");
        Picasso.with(mContext)
                .load(imageUrl)
                .placeholder(R.drawable.logo)
                .error(R.drawable.logo)
                .fit()
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.imgViewGallery);
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
