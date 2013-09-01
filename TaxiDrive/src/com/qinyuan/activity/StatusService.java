package com.qinyuan.activity;

import com.qinyuan.activity.R;
import com.qinyuan.activity.TaxiActivity;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;

public class StatusService extends IntentService
{
	public StatusService(){
		super("StatusService");
	}
	
	@Override
	protected void onHandleIntent(Intent arg0)
	{
		showNotification();
	}

	private void showNotification()
	{
		Notification n = new Notification(R.drawable.taxi, "打车灵-司机端", System.currentTimeMillis());
		Intent intent = new Intent(this,TaxiActivity.class);
		PendingIntent p = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
		n.setLatestEventInfo(this, "打车灵-司机端", "", p);
		
		NotificationManager nm = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
		nm.notify(R.id.but, n);
	
	}

}
