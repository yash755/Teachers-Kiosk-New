package com.example.yash.kiosk;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class Settings extends AppCompatActivity {


    TextView name,eno,vibration,status;
    DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Settings");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);



        Userlocalstore userlocalstore;
        userlocalstore = new Userlocalstore(this);
        User user = userlocalstore.getloggedInUser();

        db = new DatabaseHelper(this);


        name = (TextView)findViewById(R.id.name);
        eno  = (TextView)findViewById(R.id.eno);

        vibration = (TextView)findViewById(R.id.vibration);
        status    = (TextView)findViewById(R.id.status);

        name.setText(user.teachercode);
        eno.setText(user.user);

        System.out.println(user.authkey + "key");

      Cursor cr = db.vfetch();



        cr.moveToFirst();

        String tags = null;

        while (!cr.isAfterLast()) {
            tags = cr.getString(cr.getColumnIndex("vstatus")).toString();
            cr.moveToNext();
        }

        cr.close();

        if (tags.equals("1")) {

            status.setText("Enabled");
        } else {

            status.setText("Disabled");

        }

        status.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Cursor cr = db.vfetch();

                cr.moveToFirst();

                String tags = null;

                while (!cr.isAfterLast()) {
                    tags = cr.getString(cr.getColumnIndex("vstatus")).toString();
                    cr.moveToNext();
                }

                System.out.println(tags + "Tags");
                cr.close();


                if (tags.equals("1")) {

                    db.vinsert("2", "vibration");
                    status.setText("Disabled");
                } else {

                    db.vinsert("1", "vibration");
                    status.setText("Enabled");

                }


            }
        });

        vibration.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

              Cursor cr = db.vfetch();

                cr.moveToFirst();

                String tags = null;


                while (!cr.isAfterLast()) {
                    tags = cr.getString(cr.getColumnIndex("vstatus")).toString();
                    cr.moveToNext();
                }

                System.out.println(tags);
                cr.close();

                System.out.println(tags + "Tags");

                if (tags.equals("1")) {

                    db.vinsert("2", "vibration");
                    status.setText("Disabled");
                } else {

                    db.vinsert("1", "vibration");
                    status.setText("Enabled");

                }



            }
        });





    }

    public boolean onOptionsItemSelected(MenuItem item){
        finish();
        return true;

    }

}
