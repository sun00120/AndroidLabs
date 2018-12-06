package com.example.haiba.androidlabs;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.app.FragmentManager;

public class MessageDetails extends Activity {





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);

        Intent intent = getIntent();
        Bundle bundle = getIntent().getExtras();
        MessageFragment messageFragment = new MessageFragment();

        bundle.putLong("id",intent.getLongExtra("id",99));
        bundle.putString("message",intent.getStringExtra("message"));
        bundle.putBoolean("phone",true);
        messageFragment.setArguments(bundle);

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        ft.replace(R.id.detail,messageFragment).commit();
    }
}
