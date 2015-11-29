package com.example.yash.kiosk;


import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendanceUpload extends ArrayAdapter<String> {

    DatabaseHelper db = new DatabaseHelper(getContext());


    ArrayList<String> list1 = new ArrayList<>();


    AttendanceUpload(Context context, ArrayList<String> name)
    {
        super(context, R.layout.attendancelist, name);
        list1 = name;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.uploadlist, parent, false);
        TextView t1 = (TextView)customView.findViewById(R.id.up);


        String str = list1.get(position).toString();
        String res = str.substring(16, 18);
        String res1 = str.substring(18, 20);
        String res2 = str.substring(20,24);


        t1.setText(res + "-" + res1 + "-" + res2 );


        return customView;
    }
}