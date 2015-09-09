package com.example.webonise.blooddonation.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.net.Uri;
import android.support.annotation.Nullable;

import android.os.Bundle;

import com.example.webonise.blooddonation.GPSTracker;
import com.example.webonise.blooddonation.R;
import com.example.webonise.blooddonation.app.Constant;
import com.example.webonise.blooddonation.model.Donor;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapFragment extends Fragment implements GoogleMap.OnMapClickListener, View.OnClickListener {

    GoogleApiClient mGoogleApiClient;
    Button btnOnMap;
    private GoogleMap mMap;
    private List<Donor.DataEntity> dataEntities;
    GPSTracker gpsTracker;
    private static final LatLng INDIA = new LatLng(18.562622, 73.808723);


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container, false);
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .build();
        setUpMapIfNeeded();
        return view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        /*btnOnMap = (Button) getView().findViewById(R.id.btnOnMap);*/
       /* btnOnMap.setOnClickListener(this);*/

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                callerDialog(marker);
            }
        });

    }

    private void callerDialog(final Marker marker) {
        new AlertDialog.Builder(getActivity())
                .setTitle("Really call?\n"+marker.getTitle())
                .setMessage("Are you sure you want to Call?\n"+marker.getSnippet())
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        String call = marker.getSnippet();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse("tel:" + call));
                        getActivity().startActivity(intent);
                    }
                }).create().show();
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }


    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map)).getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }

        }
    }


    private void setUpMap() {

        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);

        gpsTracker = new GPSTracker(getActivity());
        // check if GPS enabled
        if (gpsTracker.canGetLocation()) {

            // Create a LatLng object for the current location
            LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());

            Gson gson = new Gson();
            Donor donorsObj = gson.fromJson(Constant.JSONURL, Donor.class);
            dataEntities = donorsObj.getData();
            Toast.makeText(getActivity(), latLng.toString(), Toast.LENGTH_LONG).show();
            LatLng latlng;
            if (dataEntities != null) {
                for (int i = 0; i < dataEntities.size(); i++) {
                    latlng = new LatLng(dataEntities.get(i).getLat(), dataEntities.get(i).getLng());
                    mMap.addMarker(new MarkerOptions().position(latlng).title(dataEntities.get(i).getName()).snippet(dataEntities.get(i).getPhone()));
                    Log.w("w", "hello");
                }
            }

            mMap.addMarker(new MarkerOptions().position(INDIA).title("Maharastra").snippet("Pune"));

            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(INDIA, 12);
            mMap.animateCamera(yourLocation);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(INDIA)      // Sets the center of the map to LatLng (refer to previous snippet)
                    .zoom(17)                   // Sets the zoom
                    .bearing(90)                // Sets the orientation of the camera to east
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));


        } else {
            gpsTracker.showSettingsAlert();
        }

    }

    @Override
    public void onMapClick(LatLng latLng) {
        LatLng newLatLng = latLng;
        MarkerOptions markerOptions = new MarkerOptions()
                .position(newLatLng)
                .title(newLatLng.toString()
                );
        mMap.addMarker(markerOptions);
        double longitude = newLatLng.longitude;
        double latitude = newLatLng.latitude;
        getAddress(longitude, latitude);

    }

    private void getAddress(double longitude, double latitude) {
        Geocoder geocoder = new Geocoder(getActivity(), Locale.ENGLISH);
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);

            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("Address:\n");
                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                Toast.makeText(getActivity(), strReturnedAddress, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getActivity(), "No Address returned!", Toast.LENGTH_LONG).show();
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Toast.makeText(getActivity(), "Cannot find!", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View view) {

    }


}