package com.example.yash.kiosk;


public class User {
    String authkey,error,success,user,usertype,password,date,teachercode;

    public User(String username,String password,String usertype,String date,String teachercode)
    {

        this.error    = "";
        this.success  = "";
        this.authkey  = "";
        this.usertype = usertype;
        this.date     = date;
        this.user     = username;
        this.password = password;
        this.teachercode=teachercode;
    }

    public User(String error)
    {
        this.date     ="";
        this.error    = error;
        this.success  = "";
        this.authkey  = "";
        this.usertype = "";
        this.user     = "";
        this.password = "";

    }



    public User(String username,String password,String authkey,String success,String usertype,String teachercode)
    {

        this.error    = null;
        this.success  = success;
        this.authkey  = authkey;
        this.usertype = usertype;
        this.user     = username;
        this.password = password;
        this.date     = "";
        this.teachercode=teachercode;
    }



}
