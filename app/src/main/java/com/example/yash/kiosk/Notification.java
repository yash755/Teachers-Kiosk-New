package com.example.yash.kiosk;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
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
    DatabaseHelper db;

    // EditText message;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final EditText message = (EditText) findViewById(R.id.message);
        final TextView   text  = (TextView)findViewById(R.id.selectlist);


        final Dialog dialog = new Dialog(Notification.this);



        message.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {



                if ((actionId == EditorInfo.IME_ACTION_DONE)) {
                    //Toast.makeText(getActivity(), "call",45).show();
                    // hide virtual keyboard
                    InputMethodManager imm = (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(text.getWindowToken(), 0);
                    dialog.setCancelable(true);
                    dialog.setTitle("Swipe To Select Tags");
                    dialog.show();

                    return true;
                }
                return false;

            }
        });

      db = new DatabaseHelper(this);

        Cursor cr = db.getbatch();


        final ArrayList<String> batch = new ArrayList<>();
        batch.add("B1B2B3");
        batch.add("B1");
        batch.add("B2");
        batch.add("GDG");


        final ArrayList<String> value = new ArrayList<>();
        value.add("0");
        value.add("0");
        value.add("0");
        value.add("0");


/*
        for (int i=0;i<cr.getCount();i++){

            db.settag(batch.get(i),"0");
            System.out.println("The Count:" + batch.get(i));

        }

        cr.close();

        cr = db.gettag();

        System.out.println("The Count:" + cr.getCount());

        cr.moveToFirst();
        final ArrayList<String> notifylist = new ArrayList<>();
        while (!cr.isAfterLast()) {
            notifylist.add(cr.getString(cr.getColumnIndex("tags")));
            cr.moveToNext();
        }

        cr.moveToFirst();
        ArrayList<String> value = new ArrayList<>();
        while (!cr.isAfterLast()) {
            notifylist.add(cr.getString(cr.getColumnIndex("tvalue")));
            cr.moveToNext();
        }

        cr.close();
*/

        dialog.setContentView(R.layout.listdialog);

        ListAdapter adpt = new Notify(this,batch);
        final ListView li = (ListView) dialog.findViewById(R.id.listView2);
        li.setAdapter(adpt);


        Button submit = (Button) dialog.findViewById(R.id.button);

        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                dialog.dismiss();

                String mess = message.getText().toString();
                ArrayList<String> tags = new ArrayList<>();

                for(int i=0;i<4;i++){

                    if(value.get(i).equals("1"))
                    {

                     tags.add(batch.get(i).toString());

                    }

                }

                sendrequest(mess, tags);



                // log.show();

            }
        });
                li.setOnTouchListener(new SwipeDetector());

                AdapterView.OnItemClickListener listener = new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> arg0, View arg1, int position,
                                            long arg3) {

                        if (SwipeDetector.swipeDetected()) {

                            if (SwipeDetector.getAction() == SwipeDetector.Action.LR) {

                                value.set(position,"1");
                              //  db.settag(notifylist.get(position), "1");
                                arg1.setBackgroundColor(Color.parseColor("#7EB9E2EF"));

                            } else if (SwipeDetector.getAction() == SwipeDetector.Action.RL) {


                                value.set(position,"0");
                                //  db.settag(notifylist.get(position), "0");
                                arg1.setBackgroundColor(Color.WHITE);


                            }
                        } else
                            Toast.makeText(Notification.this, "Wrong Press", Toast.LENGTH_SHORT).show();

                    }
                };

                li.setOnItemClickListener(listener);


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
                } else if (id == R.id.logout) {
                    DatabaseHelper teacher_db = new DatabaseHelper(this);
                    Cursor c = teacher_db.getTable();
                    if (c.getCount() == 0) {
                        Userlocalstore userlocalstore;
                        userlocalstore = new Userlocalstore(this);
                        teacher_db.removeAll();
                        userlocalstore.clearUserdata();
                        userlocalstore.setUserloggedIn(false);
                        startActivity(new Intent(this, Login.class));
                        return true;
                    } else
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

                    startActivity(new Intent(this, MarkAttendance.class));

                } else if (id == R.id.show_timetable) {

                    startActivity(new Intent(this, MainActivity.class));

                } else if (id == R.id.upload_attendance) {

                    startActivity(new Intent(this, UploadAttendance.class));

                } else if (id == R.id.notification) {
                    startActivity(new Intent(this, Notification.class));

                }

                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                drawer.closeDrawer(GravityCompat.START);
                return true;
            }


            public void fetch() {


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

            public void inserthere(JSONArray jsonArray) {

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


            public boolean isConnected() {
                ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
                return networkInfo != null && networkInfo.isConnected();
            }



            public void sendrequest(String mess, ArrayList<String> tags){


                System.out.println();
                NotificationRequest notificationRequest = new NotificationRequest(this);
                notificationRequest.fetchuserdatainbackground(mess,tags,new GetUserCallBack() {
                            @Override
                            public void done(User returneduser) {

                                System.out.println(returneduser.error + "Error");
                                showerrormessage(returneduser.error);

                            }
                        }
                );
            }


    private void showerrormessage(String error){
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(Notification.this);
        dialogbuilder.setMessage(error);
        dialogbuilder.setPositiveButton("OKAY", null);
        dialogbuilder.show();
    }

        }