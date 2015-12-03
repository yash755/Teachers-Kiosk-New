package com.example.yash.kiosk;

import android.content.Context;
import android.content.SharedPreferences;

class Userlocalstore{

    public static final String SP_Name = "userDetails";
    SharedPreferences userLocalDatabase;

    public Userlocalstore(Context context)
    {
        userLocalDatabase = context.getSharedPreferences(SP_Name,0);
    }

    public void userData(User user)
    {
        SharedPreferences.Editor speditor = userLocalDatabase.edit();
        System.out.println("Authkey" + user.authkey);
        speditor.putString("authkey",user.authkey);
        speditor.putString("usertype",user.usertype);
        speditor.putString("password",user.password);
        speditor.putString("user",user.user);
        speditor.putString("teachercode",user.teachercode);
        speditor.commit();
    }


    public User getloggedInUser(){
        String name = userLocalDatabase.getString("user", "");
        String usertype = userLocalDatabase.getString("usertype","");
        String authkey = userLocalDatabase.getString("authkey","");
        String password = userLocalDatabase.getString("password","");
        String teacher  = userLocalDatabase.getString("teachercode","");

        System.out.println("author" + authkey);
        User storedUser = new User(name,password,usertype,authkey,teacher,"test","test2");
        return storedUser;

    }

    public void setUserloggedIn(boolean loggedIn){
        SharedPreferences.Editor speditor = userLocalDatabase.edit();
        speditor.putBoolean("loggedIn",loggedIn);
        speditor.commit();

    }

    public boolean getuserloggedIn(){

        if(userLocalDatabase.getBoolean("loggedIn",false) == true)
            return true;
        else
            return false;
    }

    public void clearUserdata(){
        SharedPreferences.Editor speditor = userLocalDatabase.edit();
        speditor.clear();

    }
}