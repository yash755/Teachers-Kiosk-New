package com.example.yash.kiosk;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;


public class TimeTable extends Fragment {




    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {



        Bundle bundle = this.getArguments();
        int i = bundle.getInt("Int",0);


        System.out.print("I am everywhere" + i);

        final DatabaseHelper db = new DatabaseHelper(getActivity());

        String names[] = {" ","MON","TUE","WED","THU","FRI","SAT"," "};

        Cursor cr = db.getTT(names[i]);

        cr.moveToFirst();
        final ArrayList<String> classname = new ArrayList<>();
        while (!cr.isAfterLast()) {
            classname.add(cr.getString(cr.getColumnIndex("sub")));
            cr.moveToNext();

        }


        cr.moveToFirst();
        final ArrayList<String> time = new ArrayList<>();
        while (!cr.isAfterLast()) {

            if(cr.getString(cr.getColumnIndex("time")).equals("9") || cr.getString(cr.getColumnIndex("time")).equals("10") || cr.getString(cr.getColumnIndex("time")).equals("11"))
            {
                time.add(cr.getString(cr.getColumnIndex("time")) + "AM");
                cr.moveToNext();
            }

            else if (cr.getString(cr.getColumnIndex("time")).equals("12"))
            {
                time.add(cr.getString(cr.getColumnIndex("time")) + "NOON");
                cr.moveToNext();
            }
            else if (cr.getString(cr.getColumnIndex("time")).equals("13"))
            {
                time.add("1PM");
                cr.moveToNext();
            }
            else if (cr.getString(cr.getColumnIndex("time")).equals("14"))
            {
                time.add("2PM");
                cr.moveToNext();
            }
            else if (cr.getString(cr.getColumnIndex("time")).equals("15"))
            {
                time.add("3PM");
                cr.moveToNext();
            }
            else if (cr.getString(cr.getColumnIndex("time")).equals("14"))
            {
                time.add("4PM");
                cr.moveToNext();
            }

        }

        cr.moveToFirst();
        final ArrayList<String> venue = new ArrayList<>();
        while (!cr.isAfterLast()) {
            venue.add(cr.getString(cr.getColumnIndex("venue")));
            cr.moveToNext();
        }
        cr.close();






        View v = inflater.inflate(R.layout.timetable,container,false);

        ListAdapter adpt = new ClassList(getActivity(),classname,time,venue);
        ListView lv = (ListView)v.findViewById(R.id.tt);
        lv.setAdapter(adpt);


        return v;
    }
}
