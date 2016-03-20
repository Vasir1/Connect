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

public class pollForMessages extends AsyncTask<String,Void,String> {

    Context context;
    Conversation conversation;
    int to, from;
    String message;
    boolean success=false;
    boolean notifications=false; //may be able to remove this...yeah probly best

    public pollForMessages(Context _context, Conversation _conversation, int _yourID, int _theirID, boolean _notifications) {
        //Log.w("DBD", "Made it to polling!");
        context=_context;
        conversation=_conversation;
        to=_yourID;
        from=_theirID;
        notifications=_notifications;
        Log.w("DBD",to+" - "+from);


        // message=_message;

    }
    @Override
    protected String doInBackground(String... arg0) {

        Log.w("test", "Checking who is online!");
        try {
            //String username = (String) arg0[0];
            //Log.w("test", "username: "+username);
            //String password = (String) arg0[1];

            String link = "http://kylepfef.com/Secrets/androidCheckForUnread.php";
            String data = URLEncoder.encode("to", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(to), "UTF-8");
            data += "&" + URLEncoder.encode("from", "UTF-8") + "=" + URLEncoder.encode(String.valueOf(from), "UTF-8");
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

                Log.w("DBD", line.toString());
                sb.append(line);
                //if (line.equals("success")){



               // }
                // line=line.replaceAll(patternRegex, "\n");

                //return "errr";


            }

            Log.w("DBD",sb.toString());
            if (sb.toString()!=null){
                success=true;
            }
            if(sb.toString().isEmpty()){
                Log.w("DBD","empty");
                success=false;
            }
            return sb.toString();
            //return "error, line is null ";
        } catch (Exception e) {
            return new String("Exception: " + e.getMessage());
        }
    }





    @Override
    protected void onPostExecute(String result){

        if(success)
            conversation.updateReceivedText(result);

    }

    protected void onPreExecute() {

    }
}
