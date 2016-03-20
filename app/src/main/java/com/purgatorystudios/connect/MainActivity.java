package com.purgatorystudios.connect;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

/*


 */
public class MainActivity extends AppCompatActivity {

    private EditText usernameField,passwordField;
    private TextView status,role,method;
    SharedPreferences settings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameField = (EditText)findViewById(R.id.email_address);

        status = (TextView)findViewById(R.id.status);
        role = (TextView)findViewById(R.id.role);
        method = (TextView)findViewById(R.id.method);
        settings = getSharedPreferences("MyPreferencesFileName", 0);
        //If we have saved to the phone that we're logged in we can jump right to home!
        if (settings.getBoolean("loggedIn",false)) {
            Log.w("test2","MainActivity thinks you're not logged in.");
            Intent intent = new Intent(this,Home.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            //

        }
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    public void Login(View view) {

        Log.w("test", "clicked the button!");
        String username = usernameField.getText().toString();
        //String password = passwordField.getText().toString();
        method.setText("Get Method");
       // new SigninActivity(this,status,role,0).execute(username, password);



    }
    public void loginPost(View view){
        String username = usernameField.getText().toString();
        //String password = passwordField.getText().toString();
        method.setText("Post Method");
        new SigninActivity(this,status,role,1).execute(username);
    }
    public static void test(){

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }
        if (id == R.id.btnSignOut) {
            new SignoutActivity(this,status,role,1).execute(usernameField.getText().toString());


        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("test", "onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.i("test", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i("test", "onStop");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i("test", "onRestart");
    }

    @Override
    protected void onDestroy() {
        Log.i("test", "onDestroy");
        //new SignoutActivity(this,status,role,1).execute(usernameField.getText().toString());
        super.onDestroy();

    }
    public void signout(View view){

    }
}
