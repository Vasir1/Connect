package com.purgatorystudios.connect;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

//reference: http://android-developers.blogspot.ca/2007/11/stitch-in-time.html
public class Conversation extends AppCompatActivity {

    public int id;
    public String name;
    public  StringBuilder sbDisplayedText;
    public EditText text;
    public TextView tvChat;
    SharedPreferences settings;
    private Handler mHandler = new Handler();
    long mStartTime;
    Context context;
    int timeForNext;
    boolean notifications=false;


    NotificationManager manager;
    Notification myNotication;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        context=this;

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        text=(EditText) findViewById(R.id.editText);
        tvChat=(TextView) findViewById(R.id.textView3);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        Bundle b = getIntent().getExtras();
        id = b.getInt("id");
        name=b.getString("name");

        setTitle(name);
        settings = getSharedPreferences("MyPreferencesFileName", 0);
        sbDisplayedText = new StringBuilder();

        mStartTime = System.currentTimeMillis();
        mHandler.removeCallbacks(mUpdateTimeTask);
        timeForNext=1000;
        new pollForMessages(context,Conversation.this,settings.getInt("id", 0), id, true).execute(settings.getString("email", ""));
        mHandler.postDelayed(mUpdateTimeTask, 400);
    }
    public void submit(View view){
       // Log.w("DBD", text.getText().toString());
        String _temp=text.getText().toString();
        Log.w("DBD",_temp);
        /*sbDisplayedText.append(text.getText().toString() + "\n");
        tvChat.setText(sbDisplayedText.toString());*/


        //this context, this activity, YOUR id, THEIR id, message
        new sendMessage(this,Conversation.this,settings.getInt("id", 0),id,text.getText().toString()).execute(settings.getString("email", ""));


    }
    public void updateText(String _text){

        sbDisplayedText.append("You: "+_text + "\n");
        tvChat.setText(sbDisplayedText.toString());
        text.setText("");
    }
    public void updateReceivedText(String _text){
        if (notifications) {
            Intent notifyIntent = new Intent(context, Conversation.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(Conversation.this);


            builder.setAutoCancel(true);
            builder.setTicker("this is ticker text");
            builder.setContentTitle(name);
            builder.setContentText("You have a new message");
            builder.setSmallIcon(R.drawable.lightbulb);
            builder.setContentIntent(pendingIntent);
            builder.setOngoing(false);
            // builder.setSubText("This is subtext...");   //API level 16
            builder.setNumber(1);
            builder.build();

            myNotication = builder.getNotification();
            manager.notify(11, myNotication);
        }

        //name + ": " + 
        sbDisplayedText.append(_text + "\n");
        tvChat.setText(sbDisplayedText.toString());
    }

    View.OnClickListener mStartListener = new View.OnClickListener() {
        public void onClick(View v) {
            if (mStartTime == 0L) {
                mStartTime = System.currentTimeMillis();
                mHandler.removeCallbacks(mUpdateTimeTask);
                mHandler.postDelayed(mUpdateTimeTask, 1000);
            }
        }
    };

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            //return;
            //Log.w("DBD","loop");

            //this context, this activity, YOUR id
            new pollForMessages(context,Conversation.this,settings.getInt("id", 0), id, false).execute(settings.getString("email", ""));

            /*
            final long start = mStartTime;
            long millis = SystemClock.uptimeMillis() - start;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds     = seconds % 60;

            if (seconds < 10) {
                Log.w("DBD","" + minutes + ":0" + seconds);
               // mTimeLabel.setText("" + minutes + ":0" + seconds);
            } else {
                Log.w("DBD","" + minutes + ":" + seconds);
                //mTimeLabel.setText("" + minutes + ":" + seconds);
            }
            */

           // mHandler.postAtTime(this,
                   // start + (((minutes * 60) + seconds + 1) * 1000));
            mHandler.postDelayed(mUpdateTimeTask, timeForNext);
        }
    };

    @Override
    public void onBackPressed(){
        Log.i("test", "onBackPressed");
        mHandler.removeCallbacks(mUpdateTimeTask);
        super.onBackPressed();

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("test", "onPause");
        mHandler.removeCallbacks(mUpdateTimeTask);
        timeForNext=10000;
        notifications=true;
        mHandler.postDelayed(mUpdateTimeTask, 10000);
    }
    protected void onResume() {
        super.onResume();
        Log.i("test", "onResume");
        notifications=false;
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("test", "onStop");
        mHandler.removeCallbacks(mUpdateTimeTask);
        timeForNext=10000;
        notifications=true;
       // mHandler.postDelayed(mUpdateTimeTask, 10000);
    }

}
