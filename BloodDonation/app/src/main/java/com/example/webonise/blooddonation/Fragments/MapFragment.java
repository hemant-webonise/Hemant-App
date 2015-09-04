package com.example.webonise.blooddonation.Fragments;


import android.location.Criteria;
import android.location.Location;
import android.support.annotation.Nullable;

import android.os.Bundle;

import com.example.webonise.blooddonation.R;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceDetectionApi;
import com.google.android.gms.location.places.PlaceFilter;
import com.google.android.gms.location.places.PlaceLikelihoodBuffer;
import com.google.android.gms.location.places.PlaceReport;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MapFragment extends Fragment implements GoogleMap.OnMapClickListener, View.OnClickListener {

    private GoogleApiClient mGoogleApiClient;
    Button btnOnMap;

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private static final LatLng INDIA = new LatLng(18.562622, 73.808723);
    private static final LatLng NEARINDIA = new LatLng(19.562622, 72.808723);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public MapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_maps, container,false);
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

        mMap.setOnMapClickListener(this);
        btnOnMap = (Button) getView().findViewById(R.id.btnOnMap);
        btnOnMap.setOnClickListener(this);
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
        /*Use Of UiSetting in Maps*/
        mMap.setMyLocationEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setIndoorLevelPickerEnabled(true);

        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(bestProvider);


       /* LatLng LastLatLng =  new LatLng(location.getLatitude(),location.getLongitude());*/
        mMap.addMarker(new MarkerOptions()
                .position(INDIA)
                .title("India")
                .snippet("Pune"));
       /* mMap.addMarker(new MarkerOptions().position(NEARINDIA).title("India").snippet("Near - Pune"));

        for (int i = 19; i < 28; i++) {
            LatLng ltlng = new LatLng(i, i + 10);
            mMap.addMarker(new MarkerOptions().position(ltlng).title("Steps :" + i));
        }*/

        // zoom in the camera
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(INDIA, 15));
        // animate the zoom process
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15), 2000, null);
    }


    @Override
    public void onMapClick(LatLng latLng) {
         /*  LocationAddress locationAddress = new LocationAddress();*/
        //Convert Location to LatLng
        LatLng newLatLng = latLng;

        MarkerOptions markerOptions = new MarkerOptions()
                .position(newLatLng)
                .title(newLatLng.toString()
                );
        mMap.addMarker(markerOptions);

        double longitude = newLatLng.longitude;
        double latitude = newLatLng.latitude;
        /*The following code may not work in emulator and the similar logic is given in the function*/
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