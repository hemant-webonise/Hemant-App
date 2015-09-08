package com.example.webonise.blooddonation.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import com.example.webonise.blooddonation.R;

/**
 * Created by webonise on 2/9/15.
 */
public class ContactViewHolder extends RecyclerView.ViewHolder {
    protected TextView vName;
    protected TextView tvLocation;
    protected TextView tvphoneNumber;
    protected TextView vTitle;
    protected Switch Explore;

    public ContactViewHolder(View v) {
        super(v);
        vName =  (TextView) v.findViewById(R.id.tvName);
        tvLocation = (TextView)  v.findViewById(R.id.tvLocation);
        tvphoneNumber = (TextView)  v.findViewById(R.id.txtEmail);
        vTitle = (TextView) v.findViewById(R.id.title);
        Explore = (Switch)v.findViewById(R.id.Explore);
    }
}