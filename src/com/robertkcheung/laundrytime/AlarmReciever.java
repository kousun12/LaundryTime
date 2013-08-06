package com.robertkcheung.laundrytime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.media.AudioManager;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;

public class AlarmReciever extends BroadcastReceiver {
	private static int NOTIFICATION_ID = 12121212;

@Override
	public void onReceive(Context context, Intent intent) {
		//NotificationManager mNotificationManager = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		NotificationManager manger = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent contentIntent = PendingIntent.getActivity(context, NOTIFICATION_ID, new Intent(context, HallActivity.class), 0);

		Bundle extras=intent.getExtras();
		String note=extras.getString("note");
		String title=extras.getString("title");
		//Notification notification = new Notification(R.drawable.icon, "Combi Note", System.currentTimeMillis());
		NotificationCompat.Builder builder =  new NotificationCompat.Builder(context)
		.setContentIntent(contentIntent)
		.setSmallIcon(com.robertkcheung.laundrytime.R.drawable.smallnotif)
		.setContentTitle(title)
		.setContentText(note);

		
		
		Notification notification = builder.build();
		
		//here we get the title and description of our Notification
		//
		
		notification.flags |= Notification.FLAG_SHOW_LIGHTS | Notification.FLAG_AUTO_CANCEL | Notification.FLAG_INSISTENT ;
		notification.defaults |= Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS;
		//here we set the default sound for our 
		//notification
		notification.audioStreamType = AudioManager.STREAM_ALARM;
		notification.sound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
		notification.ledARGB = context.getResources().getColor(R.color.salmon);
		// The PendingIntent to launch our activity if the user selects this notification
		SharedPreferences sp = context.getSharedPreferences("schoolPref",
				context.MODE_WORLD_READABLE);
		Editor e = sp.edit();
		e.putBoolean("NotificationDSet",false);
		e.putBoolean("NotificationWSet",false);
		
		e.commit();
		manger.notify(NOTIFICATION_ID++, notification);
		//Toast t = Toast.makeText(context, "WORKEDDDDD", Toast.LENGTH_LONG);
		//t.show();

	}

}
