package com.example.yash.kiosk;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class Login extends AppCompatActivity implements View.OnClickListener{


    Button login;
    EditText username,password,teachercode;
    RadioGroup radiousertype;
    RadioButton radioButton;

    Userlocalstore userlocalstore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.t1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Login");


        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        teachercode = (EditText)findViewById(R.id.teachercode);

        login = (Button) findViewById(R.id.login);
        radiousertype = (RadioGroup) findViewById(R.id.radiousertype);

        userlocalstore = new Userlocalstore(this);

        if(userlocalstore.getuserloggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
        }
        else
            login.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.login:
                if(isConnected()) {
                    if(!validate())
                        Toast.makeText(getApplicationContext(), "Fill All Credentials", Toast.LENGTH_SHORT).show();
                    else {

                        int selectedId = radiousertype.getCheckedRadioButtonId();
                        radioButton = (RadioButton) findViewById(selectedId);

                        String usertype =radioButton.getText().toString();
                        String usernames = username.getText().toString();
                        String passwords = password.getText().toString();
                        String teachercodes = teachercode.getText().toString();

                        if(usertype.equals("Teacher"))
                            usertype = "E";
                        else
                            usertype = "S";

                        System.out.println(usertype);

                        User user = new User(usernames, passwords,usertype,"24-02-1995",teachercodes);

                        authenticate(user);

                    }
                }
                else
                    Toast.makeText(getApplicationContext(), "You are Not Connected", Toast.LENGTH_SHORT).show();
                break;


        }


    }


    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }


    private void authenticate(User user) {

        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchuserdatainbackground(user, new GetUserCallBack() {
                    @Override
                    public void done(User returneduser) {
                        if(returneduser.error == null) {

                            loguserin(returneduser);

                        }
                        else{
                            System.out.println(returneduser.error);
                            showerrormessage(returneduser.error);
                        }
                    }
                }
        );
    }

    private void showerrormessage(String error){
        AlertDialog.Builder dialogbuilder = new AlertDialog.Builder(Login.this);
        dialogbuilder.setMessage(error);
        dialogbuilder.setPositiveButton("OKAY",null);
        dialogbuilder.show();
    }

    private void loguserin(User returneduser ){

        userlocalstore.userData(returneduser);
        userlocalstore.setUserloggedIn(true);

        startActivity(new Intent(this, MainActivity.class));

    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isConnected();
    }


    private boolean validate() {
        return !username.getText().toString().trim().equals("") && !password.getText().toString().trim().equals("")
                && !teachercode.getText().toString().trim().equals("");
    }

}


