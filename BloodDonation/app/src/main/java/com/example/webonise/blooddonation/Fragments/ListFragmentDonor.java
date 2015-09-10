package com.example.webonise.blooddonation.Fragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.webonise.blooddonation.R;
import com.example.webonise.blooddonation.adapter.CustomListAdapterDonor;
import com.example.webonise.blooddonation.app.AppController;
import com.example.webonise.blooddonation.app.Constant;
import com.example.webonise.blooddonation.model.Donor;
import com.google.gson.Gson;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

public class ListFragmentDonor extends Fragment {

    // Movies json url
    private static final String url = Constant.JSONURL;
    private ProgressDialog loadingDialog;
    private List<Donor.DataEntity> dataEntities ;
    private ListView listView;
    private CustomListAdapterDonor adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_display,container,false);
        initiateDialog();
        listView = (ListView) view.findViewById(R.id.list);



        JsonArrayRequest DonorRec = new JsonArrayRequest(Constant.JSONURL,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        hidePDialog();


                    }
                }, new Response.ErrorListener() {
            public static final String TAG ="Tag" ;

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });
        Gson  gson= new Gson();
        Donor donorsObj = gson.fromJson(Constant.JSONURL, Donor.class);
        dataEntities = donorsObj.getData();
        adapter = new CustomListAdapterDonor(getActivity(), dataEntities);
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        AppController.getInstance().addToRequestQueue(DonorRec);
        return view;
    }


    private void initiateDialog() {
        loadingDialog = new ProgressDialog(getActivity());
        loadingDialog.setMessage("Loading...");
        loadingDialog.show();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (loadingDialog != null) {
            loadingDialog.dismiss();
            loadingDialog = null;
        }
    }

}
