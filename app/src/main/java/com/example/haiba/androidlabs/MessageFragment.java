package com.example.haiba.androidlabs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class MessageFragment extends Fragment {

    private TextView messageTab;
    private TextView idTab;
    private Button delete;
    private Bundle bundle;
    private boolean frameExist;

    public MessageFragment() {
        super();
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        bundle = this.getArguments();
        frameExist = bundle.getBoolean("phone");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.message_fragment, container, false);
        delete = (Button)view.findViewById(R.id.delete);
        messageTab = view.findViewById(R.id.fragment_message);
        idTab = view.findViewById(R.id.idNumber);

        messageTab.setText(bundle.getString("message"));
        idTab.setText("ID: " + bundle.getLong("id"));

        delete.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if (!frameExist) {
                    ChatWindow chatWindow = (ChatWindow)getActivity();
                    chatWindow.deleteMessage(bundle.getLong("id"));
                    getFragmentManager().beginTransaction().remove(MessageFragment.this).commit();
                } else {
                    Intent intent = new Intent(getActivity(), ChatWindow.class);
                    intent.putExtra("delete", bundle.getLong("id"));

                    MessageDetails messageDetails = (MessageDetails)getActivity();
                    messageDetails.setResult(-1,intent);
                    messageDetails.finish();
                }
            }
        });

        return view;
    }


}
