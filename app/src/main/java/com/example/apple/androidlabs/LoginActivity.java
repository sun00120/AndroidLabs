package com.example.apple.androidlabs;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.i(ACTIVITY_NAME,"In onCreate()");

        final EditText loginEmail = findViewById(R.id.LoginText);
        final Button loginButton = findViewById(R.id.LoginButton);
        final SharedPreferences shPrefs = getSharedPreferences("Email", Context.MODE_PRIVATE);

        String emailName = shPrefs.getString("DefaultEmail","email@domain.com");
        loginEmail.setText(emailName);

        loginButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                    SharedPreferences.Editor editor =  shPrefs.edit();
                    String userInput = loginEmail.getText().toString();
                    editor.putString("DefaultEmail",userInput);
                    editor.commit();
                    Intent intent = new Intent(LoginActivity.this, StartActivity.class);
                    startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME,"In onResume()");
    }

    @Override
    protected void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME,"In onStart()");
    }

    @Override
    protected void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME,"In onPause()");
    }

    @Override
    protected void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME,"In onStop()");
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME,"In onDestroy()");
    }

}