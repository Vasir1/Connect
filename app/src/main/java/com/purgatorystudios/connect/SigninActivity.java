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
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;


public class SigninActivity  extends AsyncTask<String,Void,String> {
    private TextView statusField, roleField;
    private Context context;
    private int byGetOrPost = 0;
    public  String email;

    SharedPreferences settings;
    boolean loggedIn=false;

    //flag 0 means get and 1 means post.(By default it is get.)
    public SigninActivity(Context context, TextView statusField, TextView roleField, int flag) {
        this.context = context;
        this.statusField = statusField;
        this.roleField = roleField;
        byGetOrPost = flag;
        Log.w("test", "In new activity!");
    }

    protected void onPreExecute() {

    }

    @Override
    protected String doInBackground(String... arg0) {
        if (byGetOrPost == 0) { //means by Get Method

            try {
                String username = (String) arg0[0];
                String password = (String) arg0[1];
                //String link = "http://myphpmysqlweb.hostei.com/login.php?username="+username+"& password="+password;

                String link = "http://www.google.com";
                URL url = new URL(link);

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                InputStream is = conn.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                String webPage = "", data = "";

                while ((data = reader.readLine()) != null) {
                    webPage += data + "\n";
                }
                /*
                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));*/

                /*
                StringBuffer sb = new StringBuffer("");
                String line="";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }
                in.close();
                return sb.toString();
                */
                return webPage.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        } else {
            Log.w("test", "Signing in by POST!");
            try {
                String username = (String) arg0[0];
                email=(String) arg0[0];
                Log.w("test", "username: "+username);
                //String password = (String) arg0[1];

                String link = "http://kylepfef.com/Secrets/android.php";
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
               /* while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    Log.w("test",line);
                    break;
                }*/
                //Log.w("test",reader.readLine());
                if (reader.readLine().equals("success <br />")){
                    loggedIn=true;
                    Log.w("test","logged in! string parsed"+loggedIn);
                }
                //Log.w("test", reader.readLine());
                return reader.readLine();
                //return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        }

    }



    @Override
    protected void onPostExecute(String result){
        if (loggedIn==false)
            return;
        this.statusField.setText("Login Successful");
        this.roleField.setText(result);

        settings = context.getSharedPreferences("MyPreferencesFileName", 0);
        SharedPreferences.Editor preferencesEditor = settings.edit();
        preferencesEditor.putBoolean("loggedIn", true); //
        preferencesEditor.putString("email",email);
        preferencesEditor.commit();


/*
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);*/
        Intent intent = new Intent(context,Home.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
    /*

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
    }*/
}
