package com.example.fsdf;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by MAHE on 5/24/2015.
 */
public class marrayadapter extends ArrayAdapter<String> {


    public marrayadapter(Context context, ArrayList list) {
        super(context,R.layout.mcustomview, list);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View customView = convertView;
        if(customView==null)
        {
            LayoutInflater amansinflater= LayoutInflater.from(getContext());
            customView = amansinflater.inflate(R.layout.mcustomview, parent, false);
        }

        String singleTask=getItem(position).toString();
        System.out.println("Here lies :   " + singleTask);
        TextView rowtext = (TextView)customView.findViewById(R.id.rowtext);

        rowtext.setText(singleTask);

        return customView;
    }
}
