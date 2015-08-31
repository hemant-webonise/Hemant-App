package com.example.webonise.blooddonor_1;

import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.Intent;

public class BootCompleteReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if (intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
			Intent serviceIntent = new Intent(context, AutoStartUpService.class);
			context.startService(serviceIntent);
		}
	}
}