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

public class Students extends ArrayAdapter<String> {

    DatabaseHelper db = new DatabaseHelper(getContext());


    ArrayList<String> list1 = new ArrayList<>();
    ArrayList<String> list2 = new ArrayList<>();

    Students(Context context, ArrayList<String> name, ArrayList<String> name1)
    {
        super(context, R.layout.attendancelist, name);
        list1 = name;
        list2 = name1;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.attendancelist, parent, false);
        TextView t1 = (TextView)customView.findViewById(R.id.enno);
        TextView t2 = (TextView)customView.findViewById(R.id.names);

        t1.setText(list1.get(position));
        t2.setText(list2.get(position));


        Cursor cr =db.getStatus(t1.getText().toString());
        System.out.println(cr.getCount());

        if(cr.getCount() == 0)
            customView.setBackgroundColor(Color.WHITE);
        else if (cr.getCount() == 1) {
            // customView.setBackgroundColor(Color.RED);
            cr.moveToFirst();
            do{
                String data=cr.getString(cr.getColumnIndex("status"));
                if(data.equals("p"))
                    customView.setBackgroundColor(Color.parseColor("#87CEFA"));
                else
                    customView.setBackgroundColor(Color.parseColor("#FFFFA654"));
            }while(cr.moveToNext());


        }

        return customView;
    }
}
