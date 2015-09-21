package com.example.webonise.blooddonation.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.gson.Gson;

import java.util.List;


public class MapFragmentDonor extends Fragment {

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

    public MapFragmentDonor() {
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
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(final Marker marker) {
                callerDialog(marker);
            }
        });

    }

    private void callerDialog(final Marker marker) {
        new AlertDialog.Builder(getActivity())
                .setTitle(getString(R.string.really_call) + marker.getTitle())
                .setMessage(getString(R.string.sure) + marker.getSnippet())
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface arg0, int arg1) {
                        String call = marker.getSnippet();
                        Intent intent = new Intent(Intent.ACTION_DIAL);
                        intent.setData(Uri.parse(getString(R.string.tel) + call));
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
            LatLng latLng = new LatLng(gpsTracker.getLatitude(), gpsTracker.getLongitude());
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(latLng,10);
            mMap.animateCamera(yourLocation);
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(latLng)              // Sets the center of the map to LatLng
                    .zoom(5)                     // Sets the zoom
                    .tilt(30)                   // Sets the tilt of the camera to 30 degrees
                    .build();                   // Creates a CameraPosition from the builder
            mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            Gson gson = new Gson();
            Donor donorsObj = gson.fromJson(Constant.JSONURL, Donor.class);
            dataEntities = donorsObj.getData();
            Toast.makeText(getActivity(), latLng.toString(), Toast.LENGTH_LONG).show();
            LatLng latlng;
            if (dataEntities != null) {
                for (int i = 0; i < dataEntities.size(); i++) {
                    latlng = new LatLng(dataEntities.get(i).getLat(), dataEntities.get(i).getLng());
                    mMap.addMarker(new MarkerOptions().position(latlng).title(dataEntities.get(i).getName()).snippet(dataEntities.get(i).getPhone()));

                }
            }

        } else {
            gpsTracker.showSettingsAlert();
        }

    }

}