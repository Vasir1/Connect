package com.purgatorystudios.connect;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class pollForUnreadThreads extends AsyncTask<String,Void,String> {

    Context context;
    Home home;
    int to, from;
    String message;
    boolean success=false;

    public pollForUnreadThreads(Context _context, Home _home, int _yourID) {
        //Log.w("DBD", "Made it to polling!");
        context=_context;
        home=_home;
        to=_yourID;
        //from=_theirID;
       // notifications=_notifications;
       // Log.w("DBD",to+" - "+from);


        // message=_message;

    }
    @Override
    protected String doInBackground(String... arg0) {

        Log.w("test", "Seeing if there's unopened conversations!");
        try {
            //String username = (String) arg0[0];
            //Log.w("test", "username: "+username);
            //String password = (String) arg0[1];

            String link = "http://kylepfef.com/Secrets/androidCheckForAllUnread.php";
            String data = URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(to), "UTF-8");
           // data += "&" + URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(from), "UTF-8");
            //data += "&" + URLEncoder.encode("message", "UTF-8") + "=" + URLEncoder.encode(message, "UTF-8");

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
                if (line.equals("null")){
                    success=false;
                    sb.append("null");
                    continue;

                }

                //Log.w("DBDPolling", line.toString());
                //sb.append(line);
                //if (line.equals("success")){



                // }
                 line=line.replaceAll(patternRegex, "\n");

                String lines[] = line.split("\\r?\\n");

                for (int i=0;i<lines.length;i++){
                    sb.append(lines[i]+"\n");
                    //buil
                    //build the loop to accumulate into stringbuilder

                }

                success=true;
                //return "errr";
                Log.w("DBDPolling","Unread ID: "+sb.toString());


            }


            //Log.w("DBD",sb.toString());
            /*
            if (sb.toString()!=null){
                try {
                    int n = Integer.parseInt(sb.toString());
                } catch (NumberFormatException e) {
                    //error
                }
                success=true;
            }
            if(sb.toString().isEmpty()){
                Log.w("DBD","empty");
                success=false;
            }*/

            return sb.toString();
            //return "error, line is null ";
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }





    @Override
    protected void onPostExecute(String result){

        home.Reset();
        if(success)
            home.displayNotification(result);

    }

    protected void onPreExecute() {

    }
}
