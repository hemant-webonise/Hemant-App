package com.example.webonise.blooddonation.adapter;

/**
 * Created by webonise on 7/9/15.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.webonise.blooddonation.GPSTracker;
import com.example.webonise.blooddonation.R;
import com.example.webonise.blooddonation.model.Donor;
import com.google.android.gms.maps.model.LatLng;

import java.util.List;


public class CustomListAdapterDonor extends BaseAdapter implements View.OnClickListener {
    private Activity activity;
    private LayoutInflater inflater;
    public List<Donor.DataEntity> dataEntities;
    Button tvPhone;
    TextView tvName, tvDistance;
    GPSTracker gpsTracker;
   /* ImageLoader imageLoader = AppController.getInstance().getImageLoader();*/

    public CustomListAdapterDonor(Activity activity, List<Donor.DataEntity> dataEntities) {
        this.activity = activity;
        this.dataEntities = dataEntities;
    }

    @Override
    public int getCount() {
        return dataEntities.size();
    }

    @Override
    public Object getItem(int location) {
        return dataEntities.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row_donor, null);


        tvName = (TextView) convertView.findViewById(R.id.tvName);
        tvDistance = (TextView) convertView.findViewById(R.id.tvDistance);
        tvPhone = (Button) convertView.findViewById(R.id.tvPhone);
        tvPhone.setOnClickListener(this);

        tvName.setText(dataEntities.get(position).getName());
        tvPhone.setText(dataEntities.get(position).getPhone());



        gpsTracker = new GPSTracker(activity);
        // check if GPS enabled
        if(gpsTracker.canGetLocation()){
            Location location=gpsTracker.getLocation();
            Location location1 = new Location(LocationManager.GPS_PROVIDER);
            location1.setLatitude(dataEntities.get(position).getLat());
            location1.setLongitude(dataEntities.get(position).getLng());
            if(location!=null){
            String temp = String.valueOf((int) location.distanceTo(location1)/1000)+"Km";
            tvDistance.setText(temp);
            }
            else {
                Toast.makeText(activity,activity.getString(R.string.location_unfetched),Toast.LENGTH_LONG).show();
            }
        }else{
            gpsTracker.showSettingsAlert();
        }


/*
        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
            NetworkImageView thumbNail = (NetworkImageView) convertView
                                            .findViewById(R.id.thumbnail);
        // thumbnail image
        thumbNail.setImageUrl(donor.getThumbnailUrl(), imageLoader);*/

        return convertView;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvPhone:
                new AlertDialog.Builder(activity)
                        .setTitle("Really call?")
                        .setMessage("Are you sure you want to Call?")
                        .setNegativeButton(android.R.string.no, null)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface arg0, int arg1) {
                                String call = tvPhone.getText().toString();
                                Intent intent = new Intent(Intent.ACTION_DIAL);
                                intent.setData(Uri.parse("tel:" + call));
                                activity.startActivity(intent);
                            }
                        }).create().show();
                break;

        }


    }
}