package com.example.yash.kiosk;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class StudentList extends AppCompatActivity {



    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Mark Attendance");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        final DatabaseHelper db = new DatabaseHelper(this);
        db.createTable();

        Cursor cr = db.getInformation();
        System.out.print(cr.getCount());




        cr.moveToFirst();
        final ArrayList<String> enno = new ArrayList<>();
        while (!cr.isAfterLast()) {
            enno.add(cr.getString(cr.getColumnIndex("eno")));
            cr.moveToNext();

        }

        cr.moveToFirst();
        final ArrayList<String> names = new ArrayList<>();
        while (!cr.isAfterLast()) {
            names.add(cr.getString(cr.getColumnIndex("name")));
            cr.moveToNext();
        }
        cr.close();


        ListAdapter adpt = new Students(this, enno, names);
        final ListView li = (ListView) findViewById(R.id.listView);
        li.setAdapter(adpt);




        li.setOnTouchListener(new SwipeDetector());

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                Cursor cr = db.vfetch();
                cr.moveToFirst();
                String tags = null;

                while (!cr.isAfterLast()) {
                    tags = cr.getString(cr.getColumnIndex("vstatus")).toString();
                    cr.moveToNext();
                }

                System.out.println(tags);
                cr.close();

                if (SwipeDetector.swipeDetected()) {

                    if (SwipeDetector.getAction() == SwipeDetector.Action.RL) {
                        arg1.setBackgroundColor(Color.parseColor("#FFFFA654"));
                       db.onAttendanceInsert(enno.get(position), "a");


                        if(tags.equals("1")) {
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(400);
                        }



                    } else if (SwipeDetector.getAction() == SwipeDetector.Action.LR) {


                        arg1.setBackgroundColor(Color.parseColor("#87CEFA"));
                        db.onAttendanceInsert(enno.get(position), "p");

                        if(tags.equals("1")) {
                            Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                            v.vibrate(100);
                        }


                    }
                    else
                        Toast.makeText(StudentList.this, "Wrong Press", Toast.LENGTH_SHORT).show();
                }
            }
        };

        li.setOnItemClickListener(listener);






    }

    public boolean onOptionsItemSelected(MenuItem item){
        Toast.makeText(getApplicationContext(), "Attendance Saved!!!", Toast.LENGTH_SHORT).show();
        Intent myIntent = new Intent(getApplicationContext(),MarkAttendance.class);
        startActivityForResult(myIntent, 0);
        return true;

    }





}
