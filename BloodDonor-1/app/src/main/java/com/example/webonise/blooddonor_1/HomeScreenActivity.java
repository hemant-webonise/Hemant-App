package com.example.webonise.blooddonor_1;

import android.app.Activity;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.view.View;
import android.widget.Button;
import android.widget.RemoteViews;


public class HomeScreenActivity extends Activity implements View.OnClickListener {
    Button btnToMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        btnToMap=(Button)findViewById(R.id.btnToMap);
        btnToMap.setOnClickListener(this);
    }
    @Override
    public void onClick(View view) {

        Intent toMap = new Intent(this,MapsActivity.class);
        startActivity(toMap);
    }

}
