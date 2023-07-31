package com.kau.yourkauguideapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.kau.yourkauguideapp.R;
import com.kau.yourkauguideapp.ChatsModel;
import com.kau.yourkauguideapp.MessageRVAdapter;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity {
    // creating variables for our xml file.
    private RecyclerView chatsRV;
    private FrameLayout sendMsgIB;
    private EditText userMsgEdt;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";
    private ImageView imageBack;
    private ImageView imageInfo;
    private ProgressBar progressBar;
    // creating a variable for array list and adapter class.
    private ArrayList<ChatsModel> chatsModelArrayList;
    private MessageRVAdapter messageRVAdapter;

    private String StrResponse = "";
    //private String URL  = "http://192.168.1.49:5000/getresponse";  local host
    private final String URL = "https://yourkauguide.pythonanywhere.com/getresponse";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // on below line we are initializing all our views.
        chatsRV = findViewById(R.id.chatRecyclerview);
        sendMsgIB = findViewById(R.id.layoutSend);
        userMsgEdt = findViewById(R.id.inputMessage);
        progressBar = findViewById(R.id.MainProgressBar);
        imageBack = findViewById(R.id.imageBack);
        imageInfo = findViewById(R.id.imageInfo);
        progressBar.setVisibility(View.GONE);


        imageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFeedbackPage();

            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(true);
            }
        });


        // creating a new array list
        chatsModelArrayList = new ArrayList<>();

        //ADD DEFAULT BOT MESSAGE BEFORE THE USER SEND ANYTHING
        String DefaultBotResponse = "تفضل كيف ممكن أساعدك ؟";
        chatsModelArrayList.add(new ChatsModel(DefaultBotResponse, BOT_KEY));


        sendMsgIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMsg = userMsgEdt.getText().toString();
                if (!userMsg.isEmpty()) {
                }
                ProsesMsg(userMsg);
            }
        });


        // on below line we are initializing our adapter class and passing our array list to it.

        messageRVAdapter = new MessageRVAdapter(chatsModelArrayList, this, MainActivity.this);
        // below line we are creating a variable for our linear layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, RecyclerView.VERTICAL, false);

        // below line is to set layout
        // manager to our recycler view.
        chatsRV.setLayoutManager(linearLayoutManager);

        // below line we are setting
        // adapter to our recycler view.
        chatsRV.setAdapter(messageRVAdapter);

    }

    void openFeedbackPage() {

        Intent intent = new Intent(this, FeedbackActivity.class);
        startActivity(intent);
    }

    public void ProsesMsg(String userMsg) {
        // Clear the user's input

        userMsgEdt.setText("");

        // checking if the message entered
        // by user is empty or not.
        if (userMsg.isEmpty()) {
            // if the edit text is empty display a toast message.
            Toast.makeText(MainActivity.this, "الرجاء ادخال رسالة...", Toast.LENGTH_SHORT).show();
            return;
        }
        // Show the progress bar
        setProgressBarVISABLE();

        RequestParams params = new RequestParams("msg", userMsg);

        chatsModelArrayList.add(new ChatsModel(userMsg, USER_KEY));
        new AsyncHttpClient().post(URL, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {


                StrResponse = new String(responseBody);


                chatsModelArrayList.add(new ChatsModel(StrResponse, BOT_KEY));
                messageRVAdapter.notifyDataSetChanged();

                // Scroll to the bottom of the chat list
                chatsRV.scrollToPosition(chatsModelArrayList.size() - 1);
                setProgressBarGONE();


            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                StrResponse = "عذرا هنالك خطأ في الشبكة !";
                chatsModelArrayList.add(new ChatsModel(StrResponse, BOT_KEY));
                chatsRV.scrollToPosition(chatsModelArrayList.size() - 1);
                userMsgEdt.setText("");
            }
        });

    }

    public void setProgressBarVISABLE() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void setProgressBarGONE() {
        // Hide the progress bar
        progressBar.setVisibility(View.GONE);
    }
}