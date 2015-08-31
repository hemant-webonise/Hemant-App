package com.example.webonise.blooddonor_1;

import android.app.Service;
import android.os.IBinder;
import android.widget.Toast;
import android.content.Intent;

public class AutoStartUpService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	@Override
	public void onCreate() {
		super.onCreate();
		Toast.makeText(this, "You can Save some-one Today", Toast.LENGTH_LONG).show();
		// do something when the service is created
	}

}