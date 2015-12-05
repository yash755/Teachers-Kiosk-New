package com.example.yash.kiosk;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
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
import java.util.ArrayList;
import java.util.Date;


public class NotificationRequest {

    ProgressDialog progressDialog;

    public NotificationRequest(Context context) {

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Working....");
        progressDialog.setMessage("Please Wait");


    }


    public void fetchuserdatainbackground(String mess,ArrayList<String> tags, GetUserCallBack userCallBack) {
        progressDialog.show();
        new fetchuserdataasynctask(mess, tags, userCallBack).execute();
    }


    public class fetchuserdataasynctask extends AsyncTask<Void, Void, User> {

        String mess;
        ArrayList<String> tags;
        GetUserCallBack userCallBack;


        public fetchuserdataasynctask(String mess, ArrayList<String> tags, GetUserCallBack userCallBack) {

            this.mess = mess;
            this.tags = tags;
            this.userCallBack = userCallBack;


        }

        @Override
        protected User doInBackground(Void... voids) {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://probase.anip.xyz:8080/pushnotification");
            User returneduser = null;

            try {

                JSONObject jsonobj = new JSONObject();
                jsonobj.put("msg", mess);
                jsonobj.put("tags", new JSONArray(tags));



                StringEntity se = new StringEntity(jsonobj.toString());
                se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));


                httppost.setEntity(se);


                HttpResponse response = httpclient.execute(httppost);


                InputStream inputStream = response.getEntity().getContent();
                ServerRequest.InputStreamToStringExample str = new ServerRequest.InputStreamToStringExample();
                String responseServer = str.getStringFromInputStream(inputStream);


                System.out.println(responseServer + "Response Recieved");


                JSONObject jsonobj1 = new JSONObject(responseServer);

                System.out.println(jsonobj1.toString() + "Response Recieved123" + jsonobj1.length());

                if (jsonobj1.length() == 1) {

                    String error = (String) jsonobj1.get("success");
                    returneduser = new User(error);
                    System.out.println(error + "Response");
                    // returneduser = new User("13103485","yash&9654195909","shdhddud","yes","s","SAN");

                }
                else
                {
                   // String error = (String) jsonobj1.get("success");
                    returneduser = new User("Something Went Wrong!!!");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return returneduser;
        }


        @Override
        protected void onPostExecute(User returneduser) {


            progressDialog.dismiss();
            userCallBack.done(returneduser);


            super.onPostExecute(returneduser);
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


