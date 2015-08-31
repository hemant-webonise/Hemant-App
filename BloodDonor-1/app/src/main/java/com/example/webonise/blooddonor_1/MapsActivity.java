package com.example.webonise.blooddonor_1;


import android.app.ListActivity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;
import android.widget.Toast;


import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class MapsActivity extends FragmentActivity implements GoogleMap.OnMapClickListener ,View.OnClickListener{

    private GoogleApiClient mGoogleApiClient;
    Button btnOnMap;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final LatLng INDIA = new LatLng(18.562622, 73.808723);
    private static final LatLng NEARINDIA = new LatLng(19.562622, 72.808723);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .build();
        setUpMapIfNeeded();


        mMap.setOnMapClickListener(this);
        btnOnMap=(Button)findViewById(R.id.btnOnMap);
          btnOnMap.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }


    private void setUpMap() {
        /*Use Of UiSetting in Maps*/
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);

        // Get LocationManager object from System Service LOCATION_SERVICE
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//        LatLng l = PlaceDetectionApi.getCurrentPlace();

        mMap.addMarker(new MarkerOptions().position(INDIA).title("India").snippet("Pune"));
        mMap.addMarker(new MarkerOptions().position(NEARINDIA).title("India").snippet("Near - Pune"));

        for (int i = 19; i < 28; i++) {
            LatLng ltlng = new LatLng(i, i + 10);
            mMap.addMarker(new MarkerOptions().position(ltlng).title("Steps :" + i));
        }

        // zoom in the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(INDIA, 15));
        // animate the zoom process
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);

      /*Animated the zooming - to show then point */
       /*


        // zoom in the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(NEARINDIA, 15));
        // animate the zoom process
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);*/
    }


    @Override
    public void onMapClick(LatLng latLng) {




      /*  LocationAddress locationAddress = new LocationAddress();*/
        //Convert Location to LatLng
        LatLng newLatLng = latLng;

        MarkerOptions markerOptions = new MarkerOptions()
                .position(newLatLng)
                .title(newLatLng.toString());
        mMap.addMarker(markerOptions);

        double longitude=newLatLng.longitude;
        double latitude=newLatLng.latitude;
        /*The following code may not work in emulator and the similar logic is given in the function*/
        Geocoder geocoder = new Geocoder(this, Locale.ENGLISH);

        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if(addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                for(int i=0; i<returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                Toast.makeText(this,strReturnedAddress,Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(this,"No Address returned!",Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(this,"Cannot find!",Toast.LENGTH_LONG).show();
        }
      /*  locationAddress.getAddressFromLocation(latitude, longitude,getApplicationContext(), new GeocoderHandler());*/
        CustomNotification(newLatLng.toString());
    }
    public void CustomNotification(String s) {
        // Using RemoteViews to bind custom layouts into Notification
        RemoteViews remoteViews = new RemoteViews(getPackageName(),R.layout.customnotification);

        // Set Notification Title
        String strtitle = getString(R.string.customnotificationtitle);
        // Set Notification Text
        String strtext = getString(R.string.location)+s;

        // Open NotificationView Class on Notification Click
        Intent intent = new Intent(this, NotificationView.class);
        // Send data to NotificationView Class
        intent.putExtra("title", strtitle);
        intent.putExtra("text", strtext);
        // Open NotificationView.java Activity
        PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent,PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // Set Icon
                .setSmallIcon(R.drawable.fimages)
                        // Set Ticker Message
                .setTicker(getString(R.string.customnotificationticker))
                        // Dismiss Notification
                .setAutoCancel(true)
                        // Set PendingIntent into Notification
                .setContentIntent(pIntent)
                        // Set RemoteViews into Notification
                .setContent(remoteViews);

        // Locate and set the Image into customnotificationtext.xml ImageViews
        remoteViews.setImageViewResource(R.id.imagenotileft,R.drawable.fimages);
        remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.fimages);

        // Locate and set the Text into customnotificationtext.xml TextViews
        remoteViews.setTextViewText(R.id.title,getString(R.string.customnotificationtitle));
        remoteViews.setTextViewText(R.id.text,strtext);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Build Notification with Notification Manager
        notificationmanager.notify(0, builder.build());

    }

    @Override
    public void onClick(View view) {
        Intent tolist = new Intent(this, ListDisplayActivity.class);
        startActivity(tolist);

    }



}
