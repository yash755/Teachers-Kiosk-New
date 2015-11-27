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


public class Mon extends Fragment {



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
            time.add(cr.getString(cr.getColumnIndex("time")));
            cr.moveToNext();
        }

        cr.moveToFirst();
        final ArrayList<String> venue = new ArrayList<>();
        while (!cr.isAfterLast()) {
            venue.add(cr.getString(cr.getColumnIndex("venue")));
            cr.moveToNext();
        }
        cr.close();






        View v = inflater.inflate(R.layout.mon,container,false);

        ListAdapter adpt = new ClassList(getActivity(),classname,time,venue);
        ListView lv = (ListView)v.findViewById(R.id.monday);
        lv.setAdapter(adpt);


        return v;
    }
}