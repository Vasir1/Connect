package com.purgatorystudios.connect;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class webCheckOnline  extends AsyncTask<String,Void,String> {
private TextView statusField, roleField, lblOnlineUsers;
private Context context;
    public Home home;
private int byGetOrPost = 0;
        SharedPreferences settings;

    //POST
    public webCheckOnline(Context context, TextView onlineUsers) {
        this.context = context;
        this.statusField = statusField;
        this.roleField = roleField;
        this.lblOnlineUsers=onlineUsers;


        Log.w("test", "In new activity!");
    }
    public webCheckOnline(Context context, Home _home, TextView onlineUsers) {
        this.context = context;
        this.home=_home;
        this.statusField = statusField;
        this.roleField = roleField;
        this.lblOnlineUsers=onlineUsers;
        //clUsersOnline=_clUsersOnline;
        //this.context.



        Log.w("test", "In new activity!");
    }



    @Override
    protected String doInBackground(String... arg0) {

            Log.w("test", "Checking who is online!");
            try {
                String username = (String) arg0[0];
                Log.w("test", "username: "+username);
                //String password = (String) arg0[1];

                String link = "http://kylepfef.com/Secrets/androidcheckonline.php";
                String data = URLEncoder.encode("username", "UTF-8") + "=" + URLEncoder.encode(username, "UTF-8");
                // data += "&" + URLEncoder.encode("password", "UTF-8") + "=" + URLEncoder.encode(password, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                //Boolean classControl=false;
                String patternRegex = "(?i)<br */?>";
                //String titleNameRegex = result.replaceAll(patternRegex, "\n");
                while ((line = reader.readLine()) != null) {
                    line=line.replaceAll(patternRegex, "\n");

                    return line;

                    /*
                    //if (classControl==false){
                    String lines[] = line.split("\\r?\\n");
                    //Log.w("test","LINE: "+line);
                   // Log.w("test","LINE END ");
                    int arIndex=0;
                    for (int i=0;i<lines.length;i+=2){
                        classUser temp=new classUser();
                        temp.name=lines[i];
                        temp.id=Integer.parseInt(lines[i+1]);
                        Log.w("test", "temp: " + temp.id + " " + temp.name);
                        sb.append(temp.id + " " + temp.name + "\n");

                        Home.myReturn(temp, arIndex);
                        arIndex++;

                       // clUsersOnline.
                    }*/



                        //temp.name=name;
                      //  Log.w("test","name: "+temp.name);
                       // String id=reader.readLine().replaceAll(patternRegex,"\n");
                      //  Log.w("test","ID: "+id);
                       // temp.id=Integer.parseInt(id);
                       // Log.w("test",temp.id+" "+temp.name);


                   // }
                   // sb.append(line);
                    //break;
                }
                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
    }





    @Override
    protected void onPostExecute(String result){

        home.test(result);
       // context.testToPrivate();
       // Home.test(result);

        //String patternRegex = "(?i)<br */?>";
       // String titleNameRegex = result.replaceAll(patternRegex, "\n");
       // this.lblOnlineUsers.setText(titleNameRegex);


       // this.statusField.setText("Login Successful");
       // this.roleField.setText(result);

        /*
        settings = context.getSharedPreferences("MyPreferencesFileName", 0);
        SharedPreferences.Editor preferencesEditor = settings.edit();
        preferencesEditor.putBoolean("loggedIn", false);
        preferencesEditor.commit();

        Intent intent = new Intent(context,MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
        */
    }

    protected void onPreExecute() {

    }
}
