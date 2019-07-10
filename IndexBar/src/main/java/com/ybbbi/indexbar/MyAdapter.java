package com.ybbbi.indexbar;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * ybbbi
 * 2019-07-10 15:35
 */
public class MyAdapter extends BaseAdapter {
    ArrayList<Friend> friends;


    public MyAdapter(ArrayList<Friend> list) {
        friends = list;

    }

    @Override
    public int getCount() {
        return friends.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MyHolder holder;

        if (convertView == null) {
            holder = new MyHolder();
            convertView = convertView.inflate(parent.getContext(), R.layout.friend, null);
            holder.name = convertView.findViewById(R.id.text);
            holder.title = convertView.findViewById(R.id.text_head);
            convertView.setTag(holder);
        } else {
            holder = (MyHolder) convertView.getTag();

        }
        if (position > 0) {
            if (friends.get(position).pinyin.equals(friends.get(position - 1).pinyin))

                holder.title.setVisibility(View.GONE);
            else

                holder.title.setVisibility(View.VISIBLE);

        }
        holder.title.setText(friends.get(position).pinyin);


        holder.name.setText(friends.get(position).name);

        return convertView;
    }

    static class MyHolder {
        TextView title, name;

    }
}
