package com.purgatorystudios.connect;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Conversation extends AppCompatActivity {

    public int id;
    public String name;
    public  StringBuilder sbDisplayedText;
    public EditText text;
    public TextView tvChat;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

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
        sbDisplayedText = new StringBuilder();
    }
    public void submit(View view){
        Log.w("DBD", text.getText().toString());
        String _temp=text.getText().toString();
        Log.w("DBD",_temp);
        /*sbDisplayedText.append(text.getText().toString() + "\n");
        tvChat.setText(sbDisplayedText.toString());*/

        settings = getSharedPreferences("MyPreferencesFileName", 0);
        //this context, this activity, YOUR id, THEIR id, message
        new sendMessage(this,Conversation.this,settings.getInt("id", 0),id,text.getText().toString()).execute(settings.getString("email", ""));


    }
    public void updateText(String _text){

        sbDisplayedText.append(_text + "\n");
        tvChat.setText(sbDisplayedText.toString());
    }

}
