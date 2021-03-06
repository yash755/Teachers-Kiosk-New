package com.example.yash.kiosk;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.protocol.HTTP;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TimeTableFetch {

    ProgressDialog progressDialog;

    public TimeTableFetch(Context context){


        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Fetching TimeTable");
        progressDialog.setMessage("Working...");


    }


    public void fetchuserdatainbackground(User user, AttendanceArray attendanceArray){
        progressDialog.show();
        new fetchuserdataasynctask(user,attendanceArray).execute();
    }


    public class fetchuserdataasynctask extends AsyncTask<Void, Void, JSONArray> {
        User user;
        AttendanceArray attendanceArray;

        public fetchuserdataasynctask(User user, AttendanceArray attendanceArray){

            this.user = user;
            this.attendanceArray = attendanceArray;
        }

        @Override
        protected JSONArray doInBackground(Void... params) {

            String code = user.teachercode;

            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httppost = new HttpGet("http://teacherkiosk.gauravshukla.xyz:8080/timetable/" + code  );

            JSONArray jsonArray = null;
            try {


                JSONObject jsonobj = new JSONObject();

                System.out.println("I am here");
                jsonobj.put("teachercode", user.teachercode);

               // StringEntity se = new StringEntity(jsonobj.toString());
               // se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));


             //   httppost.setEntity(se);


                HttpResponse response = httpclient.execute(httppost);
                InputStream inputStream = response.getEntity().getContent();
                TimeTableFetch.InputStreamToStringExample str = new TimeTableFetch.InputStreamToStringExample();
                String responseServer = str.getStringFromInputStream(inputStream);

                System.out.println(responseServer + "yuy");
                JSONObject jsonobj1 = new JSONObject(responseServer);
                jsonArray = jsonobj1.getJSONArray("success");

                return jsonArray;

            } catch (Exception e) {
                e.printStackTrace();
            }



            return jsonArray;
        }



        @Override
        protected void onPostExecute(JSONArray jsonArray) {


            progressDialog.dismiss();

            attendanceArray.done(jsonArray);
            super.onPostExecute(jsonArray);

        }
    }

    public static class InputStreamToStringExample {

        public static void main(String[] args) throws IOException {

            // intilize an InputStream
            InputStream is =
                    new ByteArrayInputStream("file content..blah blah".getBytes());

            String result = getStringFromInputStream(is);

            System.out.println(result);
            System.out.println("Done");

        }

        // convert InputStream to String
        static String getStringFromInputStream(InputStream is) {

            BufferedReader br = null;
            StringBuilder sb = new StringBuilder();

            String line;
            try {

                br = new BufferedReader(new InputStreamReader(is));
                while ((line = br.readLine()) != null) {
                    sb.append(line);
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (br != null) {
                    try {
                        br.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return sb.toString();
        }

    }







}
