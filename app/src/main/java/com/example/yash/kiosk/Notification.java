package com.example.yash.kiosk;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Notification extends AppCompatActivity  implements NavigationView.OnNavigationItemSelectedListener {

    Userlocalstore userlocalstore;
   // EditText message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        EditText message = (EditText) findViewById(R.id.message);
        message.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                boolean handled = false;
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    // TODO do something
                    handled = true;
                }
                return handled;
            }
        });




        ArrayList<String> notifylist = new ArrayList<>();
        notifylist.add("B1B2B3");
        notifylist.add("B4B5B6");

        ArrayList<String> value = new ArrayList<>();
        notifylist.add("jh");
        notifylist.add("jkljl");

        ListAdapter adpt = new Notify(this, notifylist,value);
        final ListView li = (ListView) findViewById(R.id.listll);
        li.setAdapter(adpt);

        li.setOnTouchListener(new SwipeDetector());

        AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                    long arg3) {

                if (SwipeDetector.swipeDetected()) {

                    if (SwipeDetector.getAction() == SwipeDetector.Action.LR) {
                        arg1.setBackgroundColor(Color.YELLOW);

                    }
                    else if (SwipeDetector.getAction() == SwipeDetector.Action.RL) {
                        arg1.setBackgroundColor(Color.WHITE);


                    }
                }
                else
                    Toast.makeText(Notification.this, "Wrong Press", Toast.LENGTH_SHORT).show();

            }
        };

        li.setOnItemClickListener(listener);


        userlocalstore = new Userlocalstore(this);

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
