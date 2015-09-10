package com.example.webonise.blooddonation;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.webonise.blooddonation.app.Constant;

import org.json.JSONException;
import org.json.JSONObject;


public class SearchActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnSearch;
    Button btnHistory;
    GPSTracker gps;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_acitvity);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //Title and subtitle
        toolbar.setTitle("Blood Donation ");
        toolbar.setSubtitle("What's your blood type ?");


        setSupportActionBar(toolbar);
        btnSearch=(Button)findViewById(R.id.btnSearch);
        btnSearch.setOnClickListener(this);
        btnHistory=(Button)findViewById(R.id.btnHistory);
        btnHistory.setOnClickListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_search_acitvity, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Toast.makeText(getApplicationContext(), getString(R.string.setting), Toast.LENGTH_LONG).show();
            return true;
        }
        //noinspection SimplifiableIfStatement
        if (id == R.id.privacy) {
            Toast.makeText(getApplicationContext(),getString(R.string.privacy),Toast.LENGTH_LONG).show();
            return true;
        }
        if (id == R.id.editProfile) {
            Toast.makeText(getApplicationContext(),getString(R.string.update_profile),Toast.LENGTH_LONG).show();
            startActivity(new Intent(getString(R.string.registration_activity)).putExtra(getString(R.string.btnText),getString(R.string.update)));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }





    @Override
    public void onClick(View view) {
       switch(view.getId()){
            case R.id.btnSearch :

                final CharSequence BloodGroup[] = new CharSequence[] {"O-", "O+","A-","A+","B-","B+","AB-","AB+"};
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getString(R.string.select_color))
                       .setItems(BloodGroup, new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialog, int which) {
                               // the user clicked on colors[which]
                               try {
                                   gps = new GPSTracker(SearchActivity.this);
                                   // check if GPS enabled
                                   if(gps.canGetLocation()){
                                       double latitude = gps.getLatitude();
                                       double longitude = gps.getLongitude();
                                       // \n is for new line
                                       Toast.makeText(getApplicationContext(), String.format(getString(R.string.comma), latitude, longitude), Toast.LENGTH_LONG).show();
                                       getJSONOnBloodType(BloodGroup[which]);
                                       toPagerActivity(BloodGroup[which]);
                                   }else{
                                       gps.showSettingsAlert();
                                   }
                               } catch (JSONException e) {
                                   e.printStackTrace();
                               }
                           }
                       })
               .show();


                break;
           case R.id.btnHistory:
               startActivity(new Intent(SearchActivity.this,HistoryActivityWithSQL.class));


        }
    }

    private void toPagerActivity(CharSequence text) {
        Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
        Intent pager = new Intent(this, PagerActivity.class);
        pager.putExtra("SELECTED", text);
        pager.putExtra(Constant.TAG_Lat,  gps.getLatitude());
        pager.putExtra(Constant.TAG_Lng, gps.getLongitude());
        startActivity(pager);
    }

    private void getJSONOnBloodType(CharSequence value) throws JSONException {
        JSONObject locationUpdateJson = new JSONObject();
        locationUpdateJson.put(Constant.TAG_Lat,gps.getLatitude());
        locationUpdateJson.put(Constant.TAG_Lng,gps.getLatitude());
        locationUpdateJson.put(Constant.TAG_bloodGroup, value);
        Toast.makeText(this,locationUpdateJson.toString(),Toast.LENGTH_LONG).show();
        Log.w("-",locationUpdateJson.toString());
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setTitle(getString(R.string.recheck_dialog_title))
                .setMessage(getString(R.string.check_dialog))
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface arg0, int arg1) {
                        SearchActivity.super.onBackPressed();
                       /*Some Logic to close it */
                        finish();
                    }
                }).create().show();

    }
}
