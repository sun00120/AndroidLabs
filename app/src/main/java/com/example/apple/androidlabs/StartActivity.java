package com.example.apple.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {
    protected static final String ACTIVITY_NAME = "StartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        Button button = findViewById(R.id.ImButton);

        button.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, ListItemsActivity.class);
                startActivityForResult(intent, 50);
            }
        });

        Button chatButton = findViewById(R.id.ChatButton);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                Intent intent = new Intent(StartActivity.this, ChatWindowActivity.class);
                startActivity(intent);
            }
        });

        Button weatherButton =findViewById(R.id.weatherButton);
        weatherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(ACTIVITY_NAME,"User clicked Weather Button");
                Intent intent=new Intent(StartActivity.this, WeatherForecast.class);
                startActivity(intent);
            }
        });
    }

    protected void onActivityResult(int requestCode, int responseCode, Intent data) {
        if (requestCode == 50 && responseCode == Activity.RESULT_OK) {
            Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult()");
            String messagePassed = data.getStringExtra("Response");
            CharSequence text ="ListItemsActivity passed:";
            int duration = Toast.LENGTH_LONG;
            Toast toast = Toast.makeText(this, text+messagePassed, duration);
            toast.show();
        }
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
