package com.example.haiba.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    private static final String MY_PREF = "MY_PREF";
    private static final String DEFAULT_EMAIL_KEY = "DEFAULT_EMAIL_KEY";
    private SharedPreferences pref;
    private EditText email;
    private Button loginBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME, "In onCreate()");

        loginBtn = (Button)findViewById(R.id.login);
        email = (EditText) findViewById(R.id.loginName);
        //initial or get SharedPreference
        pref = getSharedPreferences(MY_PREF, Context.MODE_PRIVATE);

        //get value from SharedPref and save it into a variable
        String defaultEmail = pref.getString(DEFAULT_EMAIL_KEY,"email@domain.com");
        email.setText(defaultEmail);

        //login button click listener
        //inside login button click listener
        //take value from emailEditText(email)
        //save it back to SharedPreference
        //next transit to StartActivity
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor edit = pref.edit();
                String input = email.getText().toString();
                edit.putString(DEFAULT_EMAIL_KEY,input);
                edit.commit();
                Intent intent = new Intent(LoginActivity.this,StartActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");

    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
