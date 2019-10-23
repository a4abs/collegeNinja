package com.collegeninja.college.adapter;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.MyView> {

    private ArrayList<HashMap<String, String>> arrayList;

    private Context mcon;

    public FaqAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        mcon= context;
        this.arrayList = arrayList;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mcon).inflate(R.layout.faq_layout, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        final String _title = arrayList.get(position).get("title");
        final String _description = arrayList.get(position).get("description");

        holder.title.setText(_title);
        holder.discussion.setText(_description);

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        public TextView title,discussion;

        public MyView(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            discussion = view.findViewById(R.id.discussion);
        }
    }
}