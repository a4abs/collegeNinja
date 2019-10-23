package com.collegeninja.college.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.collegeninja.college.activity.SignUpActivity;
import com.collegeninja.college.extra.ItemOffsetDecoration;
import com.fdscollege.college.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class DiscussionAdapter extends RecyclerView.Adapter<DiscussionAdapter.MyView> {

    private ArrayList<HashMap<String, String>> arrayList;

    private Context mcon;

    public DiscussionAdapter(Context context, ArrayList<HashMap<String, String>> arrayList) {
        mcon= context;
        this.arrayList = arrayList;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = LayoutInflater.from(mcon).inflate(R.layout.discussion_layout, parent, false);

        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(final MyView holder, final int position) {

        ArrayList<HashMap<String,String>> _arrayList = new ArrayList<>();

        final String _title = arrayList.get(position).get("title");
        holder.title.setText(Html.escapeHtml(_title));

        LinearLayoutManager gridLayoutManager = new LinearLayoutManager(mcon);
        holder.comment.setLayoutManager(gridLayoutManager);

        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(mcon, R.dimen.item_offset);
        holder.comment.addItemDecoration(itemDecoration);

        final String comments = arrayList.get(position).get("comments");
        try {
            JSONArray jsonArray = new JSONArray(comments);

            for(int i = 0; i < jsonArray.length(); i++) {
                JSONObject _jsonObject = jsonArray.getJSONObject(i);
                HashMap<String, String> map = new HashMap<>();
                String id = _jsonObject.getString("id");
                String name = _jsonObject.getString("comment");

                map.put("id",id);
                map.put("name",name);

                _arrayList.add(map);
            }

            CommentAdapter adapter= new CommentAdapter(mcon, _arrayList);
            holder.comment.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class MyView extends RecyclerView.ViewHolder {

        public TextView title,like;
        RecyclerView comment;

        public MyView(View view) {
            super(view);
            title = view.findViewById(R.id.title);
            like = view.findViewById(R.id.like);
            comment = view.findViewById(R.id.comment);
        }
    }
}