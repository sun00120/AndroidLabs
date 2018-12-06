package com.example.apple.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;

public class ChatWindowActivity extends Activity {

    protected static final String ACTIVITY_NAME="ChatWindow";
    final ArrayList<String> chatArray = new ArrayList<>();
    SQLiteDatabase chatDB;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);
        final ListView listView = findViewById(R.id.chatView);
        final EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.sendButton);
        final ChatAdapter chatAdapter = new ChatAdapter(this);
        final ContentValues contentValues = new ContentValues();
        listView.setAdapter(chatAdapter);

        ChatDatabaseHelper chatDatabaseHelper = new ChatDatabaseHelper(this);
        chatDB = chatDatabaseHelper.getWritableDatabase();

        Cursor cursor = chatDB.query(ChatDatabaseHelper.TABLE_NAME, new String[]{ChatDatabaseHelper.KEY_ID, ChatDatabaseHelper.KEY_MESSAGE},
                null,null,null,null,null);

        if(cursor.moveToFirst()) {
            do{
                String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                chatArray.add(message);
                Log.i(ACTIVITY_NAME, "SQL MESSAGE: " + cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE)));
                cursor.moveToNext();
            }while (!cursor.isAfterLast());
        }

        Log.i(ACTIVITY_NAME,"Cursor's column count: " + cursor.getColumnCount());
        for(int i = 0; i < cursor.getColumnCount(); i++){
            Log.i(ACTIVITY_NAME, "Name of column: "+ cursor.getColumnName(i));
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String chatString = editText.getText().toString();
                chatArray.add(chatString);
                chatAdapter.notifyDataSetChanged();
                editText.setText("");
                contentValues.put("message",chatString);
                chatDB.insert(ChatDatabaseHelper.TABLE_NAME,null,contentValues);

            }
        });

    }

    protected void onDestroy(){
        super.onDestroy();
        chatDB.close();
    }

    private class ChatAdapter extends ArrayAdapter<String> {
        public ChatAdapter(Context ctx) {
            super(ctx, 0);
        }

        public int getCount() {
            return chatArray.size();
        }

        public String getItem(int position) {
            return chatArray.get(position);
        }

        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = ChatWindowActivity.this.getLayoutInflater();

            View result = null;
            if (position % 2 == 0) {
                result = inflater.inflate(R.layout.chat_row_incoming, null);

            } else {
                result = inflater.inflate(R.layout.chat_row_outgoing, null);
            }
            TextView message =(TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }

        public long getItemId(int position) {
            return position;
        }
    }
}