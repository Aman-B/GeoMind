package com.example.fsdf;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;



public class MainActivity extends FragmentActivity {
	private GoogleMap googleMap;
	 SQLiteDatabase mydatabase;
	LatLng position;
	private Button button;
	final Context context = this;
	PendingIntent pendingIntent;
	private AlarmManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, 0);
        
        button = (Button) findViewById(R.id.button1);
        button.setOnClickListener(new OnClickListener() {
        	 
			@Override
			public void onClick(View arg0) {
 
				// get prompts.xml view
				LayoutInflater li = LayoutInflater.from(context);
				View promptsView = li.inflate(R.layout.prompts, null);
 
				AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						context);
 
				// set prompts.xml to alertdialog builder
				alertDialogBuilder.setView(promptsView);
 
				final EditText userInput = (EditText) promptsView
						.findViewById(R.id.editTextDialogUserInput);
 
				// set dialog message
				alertDialogBuilder
					.setCancelable(false)
					.setPositiveButton("OK",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						// get user input and set it to result
						// edit text
					    	mydatabase = openOrCreateDatabase("seproject",MODE_PRIVATE,null);
					        mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Store(Job VARCHAR,Latitude double, Longitude double);");
					        mydatabase.execSQL("INSERT INTO Store VALUES('"+userInput.getText()+"',"+position.latitude+","+position.longitude+");");
					        mydatabase.close();
					        Toast.makeText(
		              	               MainActivity.this,
		              	                "Added",
		              	                Toast.LENGTH_LONG).show();
					    }
					  })
					.setNegativeButton("Cancel",
					  new DialogInterface.OnClickListener() {
					    public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();
					    }
					  });
 
				// create alert dialog
				AlertDialog alertDialog = alertDialogBuilder.create();
 
				// show it
				alertDialog.show();
 
			}

		
			
		});
        
        
    }
    protected void onPause()
    {
    	super.onPause();
    	googleMap.clear();
    }
    protected void onResume() {
    	 double lat,longt;
        super.onResume();
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 10000;
       
        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
      //  Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
        googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        GPSTracker gps = new GPSTracker(this);
        if(gps.canGetLocation()){
         lat = 	gps.getLatitude(); // returns latitude
       longt = 	gps.getLongitude();
      
       //Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + lat + "\nLong: " + longt, Toast.LENGTH_LONG).show();  
        
        }
        else
        {
        	 lat = 13.3470;
        	 longt = 74.7880;
        	 
        }
        position = new LatLng(lat,longt);
        if (googleMap !=null){
        	CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(lat, longt)).zoom(12).build();
     
    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

  		  googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, longt)).draggable(true)
  		      .title("Drag to the desired location"));
  		  googleMap.setOnMarkerDragListener(new OnMarkerDragListener() {

              @Override
              public void onMarkerDragStart(Marker marker) {
                  
              }

              @Override
              public void onMarkerDragEnd(Marker marker) {

               position = marker.getPosition(); //
              	Toast.makeText(
              	                MainActivity.this,
              	                "Lat " + position.latitude
              	                        ,
              	                Toast.LENGTH_LONG).show();
            	Toast.makeText(
      	                MainActivity.this,
      	                 "Long " + position.longitude,
      	                Toast.LENGTH_LONG).show();
              }

              @Override
              public void onMarkerDrag(Marker marker) {
                  
              }
          });

  		}
  	else
  	{
  		  Toast.makeText(getApplicationContext(),
                    "Sorry! unable to create maps", Toast.LENGTH_SHORT)
                    .show();
  	}
    }
 
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
//        switch (item.getItemId())
//        {
//
//        case R.id.menu_view:
//            Intent i = new Intent(MainActivity.this, Second.class);
//            startActivity(i);
          return true;
//            default:
//            	return super.onOptionsItemSelected(item);    }
       /* if (id == R.id.action_settings) {
            return true;
        }
       
        return super.onOptionsItemSelected(item);
         */
    }
}    