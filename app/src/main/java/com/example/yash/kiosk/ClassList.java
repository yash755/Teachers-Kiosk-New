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

public class ClassList extends ArrayAdapter<String> {

    DatabaseHelper db = new DatabaseHelper(getContext());


    ArrayList<String> classname = new ArrayList<>();
    ArrayList<String> time      = new ArrayList<>();
    ArrayList<String> venue     = new ArrayList<>();

    ClassList(Context context, ArrayList<String> name, ArrayList<String> name1,ArrayList<String> name2)
    {
        super(context, R.layout.classlist, name);
        classname = name;
        time      = name1;
        venue     = name2;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.classlist, parent, false);
        TextView t1 = (TextView)customView.findViewById(R.id.classname);
        TextView t2 = (TextView)customView.findViewById(R.id.time);
        TextView t3 = (TextView)customView.findViewById(R.id.venue);

        t1.setText(classname.get(position));
        t2.setText(time.get(position));
        t3.setText(venue.get(position));




        return customView;
    }

}
