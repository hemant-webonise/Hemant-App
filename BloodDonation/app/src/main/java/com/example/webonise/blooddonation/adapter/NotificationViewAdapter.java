package com.example.webonise.blooddonation.adapter;

/**
 * Created by webonise on 28/8/15.
 */
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.webonise.blooddonation.R;

public class NotificationViewAdapter extends Activity {
    String title;
    String text;
    TextView txttitle;
    TextView txttext;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.acitivity_custom_notification);

        // Create Notification Manager
        NotificationManager notificationmanager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        // Dismiss Notification
        notificationmanager.cancel(0);
        // Retrive the data from MainActivity.java
        Intent i = getIntent();
        title = i.getStringExtra("title");
        text = i.getStringExtra("text");
        // Locate the TextView
        txttitle = (TextView) findViewById(R.id.title);
        txttext = (TextView) findViewById(R.id.text);
        // Set the data into TextView
        txttitle.setText(title);
        txttext.setText(text);
    }
}
