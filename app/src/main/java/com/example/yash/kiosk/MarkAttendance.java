package com.example.yash.kiosk;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MarkAttendance extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Userlocalstore userlocalstore;
    ListView list;
    TextView ttv;
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mark_attendance);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Select Class");


        DatabaseHelper db = new DatabaseHelper(this);
        userlocalstore = new Userlocalstore(this);

        String weekDay = "";


        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);


        switch (day) {
            case Calendar.MONDAY:
                weekDay = "MON";

            case Calendar.TUESDAY:
                weekDay = "TUE";

            case Calendar.WEDNESDAY:
                weekDay = "WED";

            case Calendar.THURSDAY:
                weekDay = "THU";

            case Calendar.FRIDAY:
                weekDay = "FRI";

            case Calendar.SATURDAY:
                weekDay = "SAT";

            case Calendar.SUNDAY:
                weekDay = "SUN";
        }

        Cursor cr = db.getTT("MON");
        count = cr.getCount();

        System.out.println("The count is" + cr.getCount() + weekDay);

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



        if(count != 0) {
            String t =  "Select from Today's Classes!!";
            ttv = (TextView) findViewById(R.id.ttv);
            ttv.setText(t);
            ListAdapter adpt = new ClassList(this, classname, time, venue);
            list = (ListView) findViewById(R.id.lt);
            list.setAdapter(adpt);
        }
        else {

            String t =  "No Classes Today!!";
            ttv = (TextView) findViewById(R.id.ttv);
            ttv.setText(t);

        }

        if(count!= 0) {

            AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                        long arg3) {

                    Intent intent = new Intent(getApplicationContext(), StudentList.class);
                    startActivity(intent);

                }
            };

            list.setOnItemClickListener(listener);


        }




        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

    }




    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        else if (id == R.id.logout){
            userlocalstore.clearUserdata();
            userlocalstore.setUserloggedIn(false);
            startActivity(new Intent(this, Login.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.fetch_data) {
            if (isConnected()) {
                fetch();
            } else {
                Toast.makeText(getApplicationContext(), "You are Not Conncted!!!", Toast.LENGTH_SHORT).show();
            }
            // Handle the camera action
        } else if (id == R.id.mark_attendance) {

            startActivity(new Intent(this,MarkAttendance.class));

        } else if (id == R.id.show_timetable) {

            startActivity(new Intent(this,MainActivity.class));

        } else if (id == R.id.upload_attendance) {

            startActivity(new Intent(this,UploadAttendance.class));

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    public void fetch(){
        User user = userlocalstore.getloggedInUser();
        AttendanceFetch attendanceFetch = new AttendanceFetch(this);
        attendanceFetch.fetchuserdatainbackground(user, new AttendanceArray() {
                    @Override
                    public void done(JSONArray jsonArray) {


                        if (jsonArray.length() > 0)
                            inserthere(jsonArray);
                        else
                            Toast.makeText(getApplicationContext(), "Sorry try after sometime!!!", Toast.LENGTH_SHORT).show();


                    }
                }
        );


    }

    public void inserthere(JSONArray jsonArray){

        DatabaseHelper teacher_db = new DatabaseHelper(this);

        for (int i = 0; i < jsonArray.length(); i++) {

            try {
                JSONObject object = jsonArray.getJSONObject(i);
                String batch = (String) object.get("batch");
                String eno = (String) object.get("eno");
                String name = (String) object.get("name");
                teacher_db.insertdata(batch, eno, name);
            } catch (JSONException e) {
                Log.e("SAMPLE", "error getting result " + i, e);
            }
        }
        Toast.makeText(getApplicationContext(), "Data is successfully updated!!!", Toast.LENGTH_SHORT).show();
        // startActivity(new Intent(this, AttendanceListActivity.class));
    }


    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }




}







