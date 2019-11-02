package com.collegeninja.college.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;
import com.fdscollege.college.R;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;

public class CollegeFeatureAdapter extends RecyclerView.Adapter<CollegeFeatureAdapter.MyView> {

    private ArrayList<HashMap<String, String>> arrayList;

    private Context mContext;

    public CollegeFeatureAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        mContext = context;
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
        final String logo = arrayList.get(position).get("logo");
        holder.tv_name.setText(_name);
        Picasso.with(mContext)
                .load(logo)
                .placeholder(R.drawable.group)
                .error(R.drawable.group)
                .fit()
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.imgViewFeature);
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        public TextView tv_name,tv_description;
        public ImageView imgViewFeature;

        public MyView(View view) {
            super(view);
            imgViewFeature = view.findViewById(R.id.feature_image);
            tv_name = view.findViewById(R.id.name);
        }
    }
}