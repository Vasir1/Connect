package com.purgatorystudios.connect;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/*
TODO: add intent/activity for the logged in users.
 */
public class Home extends AppCompatActivity {

    private TextView status,role,method, lblOnlineUsers;
    SharedPreferences settings;
    NotificationManager manager;
    Notification myNotication;

    classUser[] usersOnline;

    private Handler mHandler = new Handler();
    int timeForNext;
    Context context;
    public  boolean notificationPending=true;
    public boolean appOpen=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=this;
        setContentView(R.layout.activity_home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        status = (TextView)findViewById(R.id.status);
        role = (TextView)findViewById(R.id.role);
        method = (TextView)findViewById(R.id.method);
        lblOnlineUsers= (TextView)findViewById(R.id.lblOnlineUsers);
        settings = getSharedPreferences("MyPreferencesFileName", 0);

        usersOnline=new classUser[2];
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        Log.w("DBD", settings.getInt("id", 0) + " ID in Home");



        new webCheckOnline(this,Home.this,lblOnlineUsers).execute(settings.getString("email", ""));

        Log.w("test", usersOnline.length + " users currently online.");

        //lblOnlineUsers.setText("this works?");
        mHandler.removeCallbacks(mUpdateTimeTask);
        timeForNext=1000;
        mHandler.postDelayed(mUpdateTimeTask, 100);

    }
    public static void myReturn(classUser _temp, int _index){

        //TextView tempTextview=(TextView)this.findViewById(R.id.lblOnlineUsers);
       // usersOnline[_index]=_temp;
    }
    public  void test(String _result){
        String lines[] = _result.split("\\r?\\n");
        //Log.w("test","LINE: "+line);
        // Log.w("test","LINE END ");
        StringBuilder sb = new StringBuilder();
        int arIndex=0;
        for (int i=0;i<lines.length;i+=2){
            classUser temp=new classUser();
            temp.name=lines[i];
            temp.id =Integer.parseInt(lines[i+1]);
            Log.w("test", "temp: " + temp.id + " " + temp.name);
            sb.append(temp.id + " " + temp.name + "\n");




            RelativeLayout layout = (RelativeLayout) findViewById(R.id.relativeLayout);
            Button btnTag = new Button(this);
            RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            //p.addRule(RelativeLayout.BELOW, R.id.lblOnlineUsers);

            //p.addRule(RelativeLayout.BELOW, (LinearLayout)findViewWithTag("1"));

            p.addRule(RelativeLayout.CENTER_HORIZONTAL, 1);

            btnTag.setLayoutParams(p);
            //btnTag.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));

            btnTag.setText(temp.name);
            btnTag.setId(View.generateViewId());

            temp.viewID =btnTag.getId();
            if (arIndex==0) {
                p.addRule(RelativeLayout.BELOW, R.id.lblOnlineUsers);
            }
            else{
                p.addRule(RelativeLayout.BELOW, usersOnline[arIndex-1].viewID);

            }

            //btnTag.setOnClickListener(this);
            //getOnClickDoSomething(btnTag);
            btnTag.setOnClickListener(getOnClickDoSomething(btnTag));
            btnTag.setTag(temp.id);
            layout.addView(btnTag);

            usersOnline[arIndex]=temp;
            arIndex++;

            // clUsersOnline.
        }
        lblOnlineUsers.setText(sb.toString());
        Log.w("test","label set in MAIN!");


        //lblStaticOnline.setText(sb.toString());
        //testToPrivate();

    }

    View.OnClickListener getOnClickDoSomething(final Button button)  {
        return new View.OnClickListener() {
            public void onClick(View v) {
                //button.setText(button.getTag().toString());
                Log.w("test", "clicked");
                classUser temp=getUserWithID(Integer.parseInt(button.getTag().toString()));
                openChat(temp);


            }
        };
    }

    public classUser getUserWithID(int _temp){
        for (int i=0;i<=usersOnline.length;i++){
            if (usersOnline[i].id==_temp){
                return usersOnline[i];
            }


        }
        return null;

    }

    public void openChat(classUser _userToTalkTo){
        Intent intent = new Intent(this,Conversation.class);
       // intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        Bundle b = new Bundle();
        b.putInt("id", _userToTalkTo.id); //Your id
        b.putString("name", _userToTalkTo.name);

        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_loggedin, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent("com.purgatorystudios.connect.Home");


            PendingIntent pendingIntent = PendingIntent.getActivity(Home.this, 1, getIntent(), 0);

            Notification.Builder builder = new Notification.Builder(Home.this);

            builder.setAutoCancel(true);
            builder.setTicker("this is ticker text");
            builder.setContentTitle("Connect Notification");
            builder.setContentText("You have a new message");
            builder.setSmallIcon(R.drawable.lightbulb);
            builder.setContentIntent(pendingIntent);
            builder.setOngoing(false);
            builder.setSubText("This is subtext...");   //API level 16
            builder.setNumber(100);
            builder.build();

            myNotication = builder.getNotification();
            manager.notify(11, myNotication);
            /*
            NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
            Notification notify=new Notification(R.drawable.lightbulb,"Notification",System.currentTimeMillis());
            PendingIntent pending= PendingIntent.getActivity(getApplicationContext(), 0, new Intent(), 0);

            //notify.
            notify.setLatestEventInfo(getApplicationContext(),"subject","body text",pending);
            notif.notify(0, notify);
            */
            //return true;
        }
        if (id == R.id.btnSignOut) {

            new SignoutActivity(this,status,role,1).execute(settings.getString("email",""));


        }

        return super.onOptionsItemSelected(item);
    }


    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {




            new pollForUnreadThreads(context,Home.this,settings.getInt("id", 0)).execute(settings.getString("email", ""));

            mHandler.postDelayed(mUpdateTimeTask, timeForNext);


        }
    };
    public void Reset(){
        for (int i=0;i<usersOnline.length;i++){
            Button tempButton = (Button) findViewById(usersOnline[i].viewID);

            tempButton.setBackgroundColor(Color.WHITE);
            tempButton.setTextColor(Color.BLACK);
        }

    }

    public void displayNotification(String _result){

        if (!appOpen) {
            Log.w("DBD", "notification found!(calling from Home)");
            //It is set back to false on pause

            Intent notifyIntent = new Intent(context, Home.class);
            notifyIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            Notification.Builder builder = new Notification.Builder(Home.this);


            builder.setAutoCancel(true);
            builder.setTicker("this is ticker text");
            builder.setContentTitle("New message");
            builder.setContentText("You have a new message");
            builder.setSmallIcon(R.drawable.lightbulb);
            builder.setContentIntent(pendingIntent);
            builder.setOngoing(false);
            // builder.setSubText("This is subtext...");   //API level 16
            builder.setNumber(1);
            builder.build();

            myNotication = builder.getNotification();
            manager.notify(11, myNotication);
            notificationPending = true;
            //mHandler.postDelayed(mUpdateTimeTask, timeForNext);
        }
        else {
           //

            String lines[] = _result.split("\\r?\\n");

            for (int i=0;i<lines.length;i++) {
                classUser temp = getUserWithID(Integer.parseInt(lines[i]));
                Button tempButton = (Button) findViewById(temp.viewID);
                tempButton.setBackgroundColor(Color.RED);
                tempButton.setTextColor(Color.WHITE);
            }

        }
        mHandler.postDelayed(mUpdateTimeTask, timeForNext);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("test", "onPause");
        mHandler.removeCallbacks(mUpdateTimeTask);
        timeForNext=10000;
        notificationPending=false;
        appOpen=false;
       // mHandler.postDelayed(mUpdateTimeTask, 10000);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("test", "onStop");
        mHandler.removeCallbacks(mUpdateTimeTask);
        timeForNext=10000;
        notificationPending=false;
        appOpen=false;
        //mHandler.postDelayed(mUpdateTimeTask, 10000);
    }

    protected void onResume() {
        super.onResume();
        Log.i("test", "onResume");
        appOpen=true;
    }

}
