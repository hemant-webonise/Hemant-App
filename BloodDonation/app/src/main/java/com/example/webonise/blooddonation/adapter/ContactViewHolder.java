package com.example.webonise.blooddonation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.webonise.blooddonation.R;

/**
 * Created by webonise on 2/9/15.
 */
public class ContactViewHolder extends RecyclerView.ViewHolder {
    protected TextView vName;
    protected TextView tvLocation;
    protected TextView tvDate;
    protected TextView vTitle;


    public ContactViewHolder(View v) {
        super(v);
        vName =  (TextView) v.findViewById(R.id.tvLocation);
        tvLocation = (TextView)  v.findViewById(R.id.tvLocation);
        tvDate = (TextView)  v.findViewById(R.id.tvDate);
        vTitle = (TextView) v.findViewById(R.id.title);

    }
}