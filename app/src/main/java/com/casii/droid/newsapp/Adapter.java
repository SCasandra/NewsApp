package com.casii.droid.newsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Casi on 09.07.2017.
 */

public class Adapter extends ArrayAdapter<New> {
    Adapter(@NonNull Context context, List<New> news) {
        super(context, 0, news);
    }

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        New currentNew = getItem(position);
        holder.titleView.setText(getContext().getString(R.string.title) + "    "+ currentNew.getTitle());
        holder.sectionView.setText(getContext().getString(R.string.section) + "    "+ currentNew.getAuthor());
        holder.webUrlView.setText(getContext().getString(R.string.webUrl) + "    "+ currentNew.getWebUrl());
        return convertView;
    }

    static class ViewHolder {
        @BindView(R.id.new_title)
        TextView titleView;
        @BindView(R.id.section_name)
        TextView sectionView;
        @BindView(R.id.new_webUrl)
        TextView webUrlView;

        public ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
