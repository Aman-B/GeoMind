package com.example.fsdf;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

public class AlarmReceiver extends BroadcastReceiver {
	private double rad2deg(double rad) {
		
		  return (rad * 180 / Math.PI);
		
		}
	private double deg2rad(double deg) {
		
		  return (deg * Math.PI / 180.0);
	
		}
	SQLiteDatabase mydatabase;
	 NotificationManager NM;
    @SuppressWarnings("deprecation")
	@Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
    	mydatabase = arg0.openOrCreateDatabase("geomind",0,null);
    	 mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Store(Job VARCHAR,Latitude double, Longitude double);");
		Cursor cursor = mydatabase.rawQuery("Select * from Store",null);
        //Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
		GPSTracker gps = new GPSTracker(arg0);
        if(gps.canGetLocation()){
        double lat = 	gps.getLatitude(); // returns latitude
       double longt = 	gps.getLongitude();
		if(cursor!=null && cursor.getCount()>0)
		{
			
		      cursor.moveToFirst();
		     
		        do {
		        	double lon1 = cursor.getDouble(2);
					double lat1 = cursor.getDouble(1);
		        	double theta = lon1 - longt;
		        	
		        	  double dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat)) * Math.cos(deg2rad(theta));
		        	
		        	  dist = Math.acos(dist);
		        	
		        	  dist = rad2deg(dist);
		        	
		        	  dist = dist * 60 * 1.1515;
		        	
		        	 
		        	    dist = dist * 1.609344;
		        	
		        	   
		        	    if(dist <0.2)
		        		{
		        	// Toast.makeText(arg0, cursor.getString(0) , Toast.LENGTH_SHORT).show();
		        	 NM=(NotificationManager)arg0.getSystemService(Context.NOTIFICATION_SERVICE);
		      //  	 Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);



		             @SuppressWarnings("deprecation")
		             
					Notification notify=new Notification(android.R.drawable.ic_lock_idle_alarm,"GeoMind",System.currentTimeMillis());
		              
		             PendingIntent pending=PendingIntent.getActivity(arg0.getApplicationContext(),0, new Intent(),0);
		             notify.setLatestEventInfo(arg0.getApplicationContext(),"GeoMind",cursor.getString(0),pending);


		             NM.notify(0, notify);
		             
		             try {
		            	    Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		            	    Ringtone r = RingtoneManager.getRingtone(arg0.getApplicationContext(), notification);
		            	    r.play();

                         Thread.sleep(1000);
		            	    r.stop();
		            	} catch (Exception e) {
		            	    e.printStackTrace();
		            	}
		             mydatabase.delete("Store", "Job" + "='" +cursor.getString(0)+"'", null);
		             
		             mydatabase.close();
		        		}

		     	  
		        } while (cursor.moveToNext());
		}
		
		


    }

}
    
}
