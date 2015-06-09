package com.example.fsdf;

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
public class Edit extends FragmentActivity {

	String job;
double lat=0,longt=0;
private GoogleMap googleMap;
SQLiteDatabase mydatabase;
private Button button;
final Context context = this;
PendingIntent pendingIntent;
LatLng position;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit);
		Intent i = getIntent();
		job = i.getStringExtra("job");
		lat = i.getDoubleExtra("lat", 0);
		longt = i.getDoubleExtra("longt",0);
		
		
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
                        userInput.setText(job);

                        // set dialog message
                        alertDialogBuilder
                                .setCancelable(false)
                                .setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                // get user input and set it to result
                                                // edit text
                                                mydatabase = openOrCreateDatabase("geomind", MODE_PRIVATE, null);
                                                mydatabase.execSQL("CREATE TABLE IF NOT EXISTS Store(Job VARCHAR,Latitude double, Longitude double);");
                                                mydatabase.execSQL("INSERT INTO Store VALUES('" + userInput.getText() + "'," + position.latitude + "," + position.longitude + ");");
                                                mydatabase.close();
                                                Toast.makeText(
                                                        Edit.this,
                                                        "Edited",
                                                        Toast.LENGTH_LONG).show();
                                                Intent i = new Intent(Edit.this, Second.class);
                                                startActivity(i);
                                            }
                                        })
                                .setNegativeButton("Cancel",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
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
    	//googleMap.clear();
    }
    protected void onResume() {
    	 
        super.onResume();
       googleMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        
        position = new LatLng(lat,longt);
        if (googleMap !=null){
        	CameraPosition cameraPosition = new CameraPosition.Builder().target(
                    new LatLng(lat, longt)).zoom(15).build();
     
    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
  		  googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, longt)).draggable(true)
  		      .title("Drag to the required location"));
  	
  		  
  		  googleMap.setOnMarkerDragListener(new OnMarkerDragListener() {

              @Override
              public void onMarkerDragStart(Marker marker) {
                  
              }

              @Override
              public void onMarkerDragEnd(Marker marker) {

               position = marker.getPosition(); //
               Toast.makeText(
     	                Edit.this,
     	                "Lat " + position.latitude
     	                        ,
     	                Toast.LENGTH_SHORT).show();
   	Toast.makeText(
	                Edit.this,
	                 "Long " + position.longitude,
	                Toast.LENGTH_SHORT).show();
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
		int id = item.getItemId();
		if (id == R.id.about) {
            // get prompts.xml view
            LayoutInflater li = LayoutInflater.from(context);
            View aboutView = li.inflate(R.layout.about, null);

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                    context);

            // set prompts.xml to alertdialog builder
            alertDialogBuilder.setView(aboutView);



            alertDialogBuilder
                    .setCancelable(true)
                    .setPositiveButton("Alright!",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // get user input and set it to result
                                    // edit text
                                    dialog.cancel();
                                }
                            });



            // create alert dialog
            final AlertDialog alertDialog = alertDialogBuilder.create();

            // show it
            alertDialog.show();
            return true;

        }
       
        return super.onOptionsItemSelected(item);
    
	}
}
