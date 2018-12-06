package com.example.haiba.androidlabs;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatWindow extends Activity {

    protected static final String ACTIVITY_NAME = "ChatWindow";
    ListView list;
    EditText edit;
    Button send;
    ArrayList<String> chatMessage = new ArrayList<>();
    ChatAdapter messageAdapter;
    ChatDatabaseHelper chatHelper = new ChatDatabaseHelper(this);
    SQLiteDatabase database;
    boolean frameExist;
    Cursor cursor;
    FrameLayout frame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_window);

        list = (ListView)findViewById(R.id.list);
        edit = (EditText)findViewById(R.id.editText);
        send = (Button)findViewById(R.id.send);
        frame = findViewById(R.id.frame);

        if(frame == null){
            frameExist = false;
        }else{
            frameExist = true;
        }

        database = chatHelper.getWritableDatabase();
        cursor= database.query(ChatDatabaseHelper.TABLE_NAME, ChatDatabaseHelper.ALL_COLUMNS,null,
                null,null, null, null);

        cursor.moveToFirst();

        while(!cursor.isAfterLast() ) {
            String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
            chatMessage.add(message);

            Log.i(ACTIVITY_NAME, "SQL MESSAGE:" + message);
            cursor.moveToNext();
        }

        Log.i(ACTIVITY_NAME, "Cursor's column count=" + cursor.getColumnCount());
        for(int i=0;i<cursor.getColumnCount();i++){
            Log.i(ACTIVITY_NAME,"The column name is " + cursor.getColumnName(i));
        }

        messageAdapter = new ChatAdapter(this);
        list.setAdapter(messageAdapter);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String msg = edit.getText().toString();
                chatMessage.add(msg);

                ContentValues cv = new ContentValues();
                cv.put(ChatDatabaseHelper.KEY_MESSAGE, msg);
                database.insert(ChatDatabaseHelper.TABLE_NAME,null, cv);

                messageAdapter.notifyDataSetChanged();
                edit.setText("");
            }
        });

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                id = messageAdapter.getItemId(position);


                if(frameExist){
                    MessageFragment messageFragment = new MessageFragment();
                    Bundle bundle = new Bundle();

                    bundle.putLong("id",id);
                    bundle.putString("message",messageAdapter.getItem(position));
                    messageFragment.setArguments(bundle);

                    FragmentManager manager = getFragmentManager();
                    FragmentTransaction transaction = manager.beginTransaction();
                    transaction.replace(R.id.frame,messageFragment).commit();
                }else{
                    //phone layout
                    Intent intent = new Intent(ChatWindow.this,MessageDetails.class);
                    intent.putExtra("id",id);
                    intent.putExtra("message",messageAdapter.getItem(position));
                    startActivityForResult(intent,100);
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode,int resultCode, Intent data){
        if(requestCode == 100){
            if(resultCode == RESULT_OK) {
                Long ID = data.getLongExtra("delete", -1);
                deleteMessage(ID);
            }
        }
    }

    public void deleteMessage(long id){
        database = chatHelper.getWritableDatabase();
        database.execSQL("DELETE FROM " + ChatDatabaseHelper.TABLE_NAME + " WHERE " + ChatDatabaseHelper.KEY_ID + " = " + id);
        chatMessage = new ArrayList<>();
        cursor = database.query(ChatDatabaseHelper.TABLE_NAME, new String[]{ChatDatabaseHelper.KEY_ID,ChatDatabaseHelper.KEY_MESSAGE},null,null,null,null,null);

        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String message = cursor.getString(cursor.getColumnIndex(ChatDatabaseHelper.KEY_MESSAGE));
                chatMessage.add(message);
                cursor.moveToNext();
            }
        }

        messageAdapter.notifyDataSetChanged();
    }


    private class ChatAdapter extends ArrayAdapter<String>{
        private ChatAdapter(Context ctx){
            super(ctx,0);
        }

        @Override
        public int getCount(){
            return chatMessage.size();
        }

        @Override
        public String getItem(int position){
            return chatMessage.get(position);
        }

        @Override
        public View getView(int position,View convertView,ViewGroup parent){
            LayoutInflater inflater = ChatWindow.this.getLayoutInflater();
            View result = null;
            if(position%2 == 0)
                result = inflater.inflate(R.layout.chat_row_incoming,null);
            else
                result = inflater.inflate(R.layout.chat_row_outgoing,null);

            TextView message = (TextView)result.findViewById(R.id.message_text);
            message.setText(getItem(position));
            return result;
        }

        @Override
        public long getItemId(int position){
            cursor.moveToPosition(position);
            return cursor.getLong(cursor.getColumnIndex(ChatDatabaseHelper.KEY_ID));

        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        database.close();

    }

}
