package com.example.yash.kiosk;


import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class Notify extends ArrayAdapter<String> {

        DatabaseHelper db = new DatabaseHelper(getContext());


        ArrayList<String> list1 = new ArrayList<>();


        Notify(Context context, ArrayList<String> name)
        {
        super(context, R.layout.list, name);
        list1 = name;
        }

@Override
public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.list, parent, false);
        TextView t1  = (TextView)customView.findViewById(R.id.tv5);


        t1.setText(list1.get(position));



        return customView;
        }
}
