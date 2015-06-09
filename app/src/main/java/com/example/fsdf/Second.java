package com.example.fsdf;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class Second extends ActionBarActivity {
    SQLiteDatabase mydatabase;
    ListView lv;
    int CheckedItem;
    private Button button;
    private Button button2;
    double lat[] = new double[100];
    double longt[] = new double[100];
    int listsize;
    ArrayAdapter <String> adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        lv = (ListView) findViewById(R.id.listView1);
        mydatabase = openOrCreateDatabase("seproject", MODE_PRIVATE, null);
        Cursor cursor = mydatabase.rawQuery("Select * from Store", null);

        final ArrayList<String> list = new ArrayList<String>();
        int i = 0;
//        Collections.reverse(list);  //to get items in order in the list by the order that they were saved.
        if (cursor != null && cursor.getCount() > 0) {

            cursor.moveToFirst();

            do {
                String s= cursor.getString(0);
                System.out.println("Query result : " + s);
                list.add(cursor.getString(0));

                lat[i] = Double.parseDouble(cursor.getString(1));
                longt[i++] = Double.parseDouble(cursor.getString(2));


            } while (cursor.moveToNext());
            mydatabase.close();

        }
//        Collections.reverse(list);  //to get items in order in the list by the order that they were saved.

        adapter  = new marrayadapter(this,list);
        lv.setAdapter(adapter);
        lv.setItemChecked(list.size()-1, true);
         listsize= list.size();
        CheckedItem = --listsize;
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub


                CheckedItem = (position);

            }


        });


        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
              try {

                  mydatabase = openOrCreateDatabase("seproject", 0, null);

                  mydatabase.delete("Store", "Job" + "='" + list.get(CheckedItem) + "'", null);
                  Toast.makeText(Second.this, "Deleted", Toast.LENGTH_SHORT).show();
                 mydatabase.close();
                 /* Intent i = new Intent(Second.this, Second.class);
                  startActivity(i);
                  finish(); Replaced with following*/

                  list.remove(CheckedItem--);

                  adapter.notifyDataSetChanged();



                  }
              catch (Exception e)
              {
                  Toast.makeText(Second.this,"No tasks to delete.", Toast.LENGTH_SHORT).show();
              }



            }
        });
        button2 = (Button) findViewById(R.id.button1);
        button2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
               try {
                   mydatabase = openOrCreateDatabase("seproject", 0, null);
                   mydatabase.delete("Store", "Job" + "='" + list.get(CheckedItem) + "'", null);
                   Intent i = new Intent(Second.this, Edit.class);
                   i.putExtra("job", list.get(CheckedItem));
                   i.putExtra("lat", lat[CheckedItem]);
                   i.putExtra("longt", longt[CheckedItem]);
                   startActivity(i);
                   mydatabase.close();
               }

                catch (Exception e)
                {
                    Toast.makeText(Second.this,"No items to edit.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.second, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        switch (item.getItemId()) {

            case R.id.menu_add:
                Intent i = new Intent(Second.this, MainActivity.class);
                startActivity(i);
                return true;
            case R.id.about:
                // get prompts.xml view
                LayoutInflater li = LayoutInflater.from(this);
                View aboutView = li.inflate(R.layout.about, null);

                final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                        this);

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
            default:
                return super.onOptionsItemSelected(item);
        }
           /* if (id == R.id.action_settings) {
	            return true;
	        }
	       
	        return super.onOptionsItemSelected(item);
	         */
    }
}