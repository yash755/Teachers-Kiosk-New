package com.example.yash.kiosk;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


    Toolbar toolbar;
    ViewPager pager;
    ViewPagerAdapter adapter;
    SlidingTabLayout tabs;
    CharSequence Titles[]={"Sat","Mon","Tue","Wed","Thu","Fri","Sat","Mon"};
    int Numboftabs =8;


    Userlocalstore userlocalstore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        userlocalstore = new Userlocalstore(this);
        User user = userlocalstore.getloggedInUser();
        System.out.println(user.authkey + "Userlocalstore");


        // Creating The ViewPagerAdapter and Passing Fragment Manager, Titles fot the Tabs and Number Of Tabs.
        adapter =  new ViewPagerAdapter(getSupportFragmentManager(),Titles,Numboftabs);

        // Assigning ViewPager View and setting the adapter
        pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(adapter);

        // Assiging the Sliding Tab Layout View
        tabs = (SlidingTabLayout) findViewById(R.id.tabs);
        tabs.setCustomTabView(R.layout.custom_tab_title, R.id.tabtext);
        tabs.setDistributeEvenly(true);// To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width


        // Setting Custom Color for the Scroll bar indicator of the Tab View
        tabs.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return getResources().getColor(R.color.tabsScrollColor);
            }
        });


        // Setting the ViewPager For the SlidingTabsLayout
        tabs.setViewPager(pager);



        String weekDay = "";

        Calendar c = Calendar.getInstance();
        int dayOfWeek = c.get(Calendar.DAY_OF_WEEK);

        if (Calendar.MONDAY == dayOfWeek) {
            pager.setCurrentItem(1);
            weekDay = "MON";
        } else if (Calendar.TUESDAY == dayOfWeek) {
            pager.setCurrentItem(2);
            weekDay = "TUE";
        } else if (Calendar.WEDNESDAY == dayOfWeek) {
            pager.setCurrentItem(3);
            weekDay = "WED";
        } else if (Calendar.THURSDAY == dayOfWeek) {
            pager.setCurrentItem(4);
            weekDay = "THU";
        } else if (Calendar.FRIDAY == dayOfWeek) {
            pager.setCurrentItem(5);
            weekDay = "FRI";
        } else if (Calendar.SATURDAY == dayOfWeek) {
            pager.setCurrentItem(6);
            weekDay = "SAT";
        } else if (Calendar.SUNDAY == dayOfWeek) {
            pager.setCurrentItem(7);
            weekDay = "SUN";
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
            startActivity(new Intent(this, Settings.class));
            return true;
        }

        else if (id == R.id.logout){
           DatabaseHelper teacher_db = new DatabaseHelper(this);
            Cursor c= teacher_db.getTable();
            if(c.getCount() == 0) {
                teacher_db.removeAll();
                userlocalstore.clearUserdata();
                userlocalstore.setUserloggedIn(false);
                startActivity(new Intent(this, Login.class));
                return true;
            }
            else
                Toast.makeText(getApplicationContext(), "You can't Logout as there some Attendance to be updated on server", Toast.LENGTH_LONG).show();

            return false;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(!authenticate())
            startActivity(new Intent(this,Login.class));
    }

    private boolean authenticate(){
        return userlocalstore.getuserloggedIn();
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

        } else if (id == R.id.notification) {
            startActivity(new Intent(this,Notification.class));

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
