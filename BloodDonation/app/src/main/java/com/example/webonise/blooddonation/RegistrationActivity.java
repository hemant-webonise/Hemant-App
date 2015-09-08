package com.example.webonise.blooddonation;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class RegistrationActivity extends Activity implements View.OnClickListener {
    EditText name, etEmailAddress, etNumber;
    TextView tvBlood;
    Switch privacyMode;
    final String PREFER = "prefer";
    SharedPreferences pref;
    Button btn;
    Bundle bundle;
    SharedPreferences.Editor editor;
    GPSTracker gps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration_acitivity);
        pref = getApplicationContext().getSharedPreferences(PREFER, MODE_PRIVATE);
        initiateLocationWithCheck();
        initialize();
        bundle = getIntent().getExtras();
        btn.setText(bundle.getString(getString(R.string.btnText)));
        if (btn.getText().toString().equalsIgnoreCase(getString(R.string.update))) {
            retrieveStoredStates();
        }
        setClickListeners();
    }

    private void initiateLocationWithCheck() {
        gps = new GPSTracker(this);
        // check if GPS enabled
        if(gps.canGetLocation()){
            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            Toast.makeText(getApplicationContext(), String.format(getString(R.string.comma), latitude, longitude), Toast.LENGTH_LONG).show();
        }else{
            gps.showSettingsAlert();
        }
    }
    private void retrieveStoredStates() {
        etEmailAddress.setText(pref.getString("etEmailAddress", null));
        etNumber.setText(pref.getString("etNumber", null));
        privacyMode.setChecked(pref.getBoolean("privacy", false));
        name.setText(pref.getString("name",null));
        tvBlood.setText(pref.getString("tvBlood", null));
    }

    private void initialize() {
        etEmailAddress = (EditText)findViewById(R.id.etEmailAddress);
        etNumber = (EditText)findViewById(R.id.etNumber);
        privacyMode = (Switch) findViewById(R.id.PrivacyMode);
        name = (EditText) findViewById(R.id.name);
        tvBlood = (TextView) findViewById(R.id.tvBlood);
        btn = (Button) findViewById(R.id.btnRegister);
    }

    private void setClickListeners() {
        privacyMode.setOnClickListener(this);
        tvBlood.setOnClickListener(this);
        btn.setOnClickListener(this);
        name.setOnClickListener(this);
        etEmailAddress.setOnClickListener(this);
        etNumber.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

      /*  Toast.makeText(RegistrationActivity.this, "Old value in Shared-Preferences" + pref.getString("name", "Could not be restored"), Toast.LENGTH_LONG).show();*/
        switch (view.getId()) {
            case R.id.name:
                name.setText("");
                break;
            case R.id.tvBlood:
                final CharSequence BloodGroup[] = new CharSequence[]{"O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.pick_blood))
                        .setItems(BloodGroup, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // the user clicked on colors[which]
                                Toast.makeText(getBaseContext(), BloodGroup[which], Toast.LENGTH_LONG).show();
                                tvBlood.setText(BloodGroup[which]);
                            }
                        })
                        .show();
                break;
            case R.id.etEmailAddress:
                etEmailAddress.setText("");
                break;
            case R.id.etNumber:
                etNumber.setText("");
                break;
            case R.id.PrivacyMode:
                if (privacyMode.isChecked()){
                    editor = pref.edit();
                   /* setImage();*/
                    try {
                        JSONObject regitration = getJsonRegistration();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    editor.putBoolean("privacy", true);
                    editor.commit();
                }
                else
                {   editor = pref.edit();
                    editor.putBoolean("privacy", false);
                    editor.commit();
                }
                break;
            case R.id.btnRegister:
                if (btn.getText().toString().equalsIgnoreCase(getString(R.string.register))) {
                    if(validateFields()){
                    putInSharedPreferences();
                        moveToSearchActivity();
                    }
                } else if (btn.getText().toString().equalsIgnoreCase(getString(R.string.update))) {
                    if(validateFields()){
                   /* retrieveStoredStates();*/
                    putInSharedPreferences();
                    moveToSearchActivity();
                    }
                }
                break;
        }
    }

    private JSONObject getJsonRegistration() throws JSONException {
        JSONObject outer = new JSONObject();
        JSONObject user = new JSONObject();

        user.put("name", "Sandeep Rathore");
        user.put("bloodGroup", "O+");
        user.put("phone", "+918149325524");
        user.put("email", "sandeep.rathore@weboniselab.com");

        outer.put("user", user);
        outer.put("lat", "18.5203");
        outer.put("lng", "73.8567");
        Toast.makeText(this, outer.toString(), Toast.LENGTH_LONG).show();
        return outer;
    }

    private void setImage() {
        //LinearLayOut Setup
        LinearLayout linearLayout= new LinearLayout(this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);

        linearLayout.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.MATCH_PARENT));

        //ImageView Setup
        ImageView imageView = new ImageView(this);
        //setting image resource
        imageView.setImageResource(R.drawable.webo);
        //setting image position
        imageView.setLayoutParams(new AbsListView.LayoutParams(
                AbsListView.LayoutParams.MATCH_PARENT,
                AbsListView.LayoutParams.WRAP_CONTENT));

        //adding view to layout
        linearLayout.addView(imageView);
        //make visible to program
        setContentView(linearLayout);
    }


    private void moveToSearchActivity() {
        Intent searchActivity = new Intent(this, SearchActivity.class);
        startActivity(searchActivity);
        finish();
    }

    private boolean validateFields() {
        if(isEmpty(name)){
            name.setError("Please fill the Name.");
        }
        else if(isEmpty(tvBlood)){
            Toast.makeText(this,"Please select your BloodGroup !",Toast.LENGTH_LONG).show();
           /* tvBlood.setError("Please Select Blood Type.");*/
        }
        else if (isEmpty(etNumber)){
            etNumber.setError("Please fill the Number.");
        }
        else if (!isValidPhoneNumber(etNumber.getText())){
            etNumber.setError("The number Should contain 6 - 13 Number.");
        }
        else if (isEmpty(etEmailAddress)){
            etEmailAddress.setError("Please fill the EmailAddress.");
        }
        else if (!isValidEmail(etEmailAddress.getText())){
            etEmailAddress.setError("Please fill the Email-Address.");
            Toast.makeText(this,"Format-abc@d.com",Toast.LENGTH_LONG).show();
        }
        else{
            return true;
        }

        return false;
    }

    private boolean isValidEmail(CharSequence email) {
        if (!TextUtils.isEmpty(email)) {
            return Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
        return false;
    }

    private boolean isValidPhoneNumber(CharSequence phoneNumber) {
        if (!TextUtils.isEmpty(phoneNumber)) {
            return Patterns.PHONE.matcher(phoneNumber).matches();
        }
        return false;
    }

    private void putInSharedPreferences() {
        editor = pref.edit();
       /* editor.putString("privacy", privacyMode);*/
        editor.putString("name", name.getText().toString());
        editor.putString("tvBlood", tvBlood.getText().toString());
        editor.putString("etEmailAddress", etEmailAddress.getText().toString());
        editor.putString("etNumber", etNumber.getText().toString());
        editor.commit();
    }

    private boolean isEmpty(EditText input) {
        return input.getText().toString().trim().length() == 0;
    }
    private boolean isEmpty(TextView input) {
        return input.getText().toString().trim().length() == 0;
    }


}
