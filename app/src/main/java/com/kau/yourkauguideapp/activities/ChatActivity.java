package com.kau.yourkauguideapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.kau.yourkauguideapp.ChatbotModel;
import com.kau.yourkauguideapp.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.kau.yourkauguideapp.ChatsModel;
import com.kau.yourkauguideapp.MessageRVAdapter;


import org.checkerframework.checker.nullness.qual.NonNull;
import org.tensorflow.lite.Interpreter;

import java.util.ArrayList;
import java.util.Arrays;


public class ChatActivity extends AppCompatActivity {
    // creating variables for our
    // widgets in xml file.
    private RecyclerView chatsRV;
    private FrameLayout sendMsgIB;
    private EditText userMsgEdt;
    private final String USER_KEY = "user";
    private final String BOT_KEY = "bot";
    private ImageView imageBack;
    private ImageView imageInfo;

    private ProgressBar progressBar ;

    private DatabaseReference mDatabase;

    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    private ChatbotModel chatbotModel;

    // creating a variable for array list and adapter class.
    private ArrayList<ChatsModel> chatsModelArrayList;
    private MessageRVAdapter messageRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        // on below line we are initializing all our views.
        chatsRV = findViewById(R.id.chatRecyclerview);
        sendMsgIB = findViewById(R.id.layoutSend);
        userMsgEdt = findViewById(R.id.inputMessage);
        progressBar = findViewById(R.id.ProgressBar);
        imageBack = findViewById(R.id.imageBack);
        imageInfo = findViewById(R.id.imageInfo);

        progressBar.setVisibility(View.GONE);

        imageBack.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                goBack();

            }
        });

        imageInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                openFeedbackPage();
                // Get input tensor information

            }
        });

        // Create an instance of the ChatbotModel class
        chatbotModel = new ChatbotModel(this);

        // creating a new array list
        chatsModelArrayList = new ArrayList<>();

        // adding on click listener for send message button.
        sendMsgIB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking if the message entered
                // by user is empty or not.
                if (userMsgEdt.getText().toString().isEmpty()) {
                    // if the edit text is empty display a toast message.
                    Toast.makeText(ChatActivity.this, "Please enter your message...", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Classify the user's input using the ChatbotModel class
                String tag = chatbotModel.getTag(userMsgEdt.getText().toString());

                // Generate a response based on the tag
                String response = generateResponse(tag);

                // Add the user's input and the generated response to the chat list
                chatsModelArrayList.add(new ChatsModel(userMsgEdt.getText().toString(), USER_KEY));
                chatsModelArrayList.add(new ChatsModel(response, BOT_KEY));
                messageRVAdapter.notifyDataSetChanged();

                // Scroll to the bottom of the chat list
                chatsRV.scrollToPosition(chatsModelArrayList.size() - 1);

                // Clear the user's input
                userMsgEdt.setText("");
            }
        });

        // on below line we are initializing our adapter class and passing our array list to it.
        messageRVAdapter = new MessageRVAdapter(chatsModelArrayList, this);

        // below line we are creating a variable for our linear layout manager.
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ChatActivity.this, RecyclerView.VERTICAL, false);

        // below line is to set layout
        // manager to our recycler view.
        chatsRV.setLayoutManager(linearLayoutManager);

        // below line we are setting
        // adapter to our recycler view.
        chatsRV.setAdapter(messageRVAdapter);
    }

    // Load the TensorFlow Lite model file from the assets folder

    void goBack(){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        mAuth.signOut();
        Toast.makeText(ChatActivity.this, "You are sign Out ..", Toast.LENGTH_SHORT).show();
    }

    void openFeedbackPage(){

        // Check if the user is authenticated
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser user = auth.getCurrentUser();
        if(user == null) {
            // If the user is not authenticated, redirect to the login screen
            Intent intent = new Intent(this, Sign_inActivity.class);
            startActivity(intent);
            Toast.makeText(ChatActivity.this, "You should log in to send feedback..", Toast.LENGTH_SHORT).show();
        } else {
            // If the user is authenticated, allow access to the page
            Intent intent = new Intent(this, FeedbackActivity.class);
            startActivity(intent);
        }
    }

    // Generate a response based on the tag returned by the ChatbotModel
    private String generateResponse(String tag) {
        System.out.println("Generate bot response from tags");
        switch (tag) {
            case "البرامج الأكاديمية":
                return "Response for البرامج الأكاديمية";
            case "التخصصات":
                return "Response for التخصصات";
            case "الخطة الدراسية":
                return "Response for الخطة الدراسية";
            case "الخطة الدراسية تقنية معلومات ":
                return "Response for الخطة الدراسية تقنية معلومات ";
            case "الخطة الدراسية نظم معلومات ":
                return "Response for الخطة الدراسية نظم معلومات ";
            case "المؤهلات المطلوبة":
                return "Response for المؤهلات المطلوبة";
            case "ايميل دكتور حسام":
                return "Response for ايميل دتكور حسام";
            case "ايميل دكتور علي الزهراني":
                return "Response for ايميل دكتور علي الزهراني";
            case "ايميل دكتور وجدي الغامدي":
                return "Response for ايميل دكتور وجدي الغامدي";
            case "تحية":
                return "Response for تحية";
            case "تحية صباحية":
                return "Response for تحية صباحية";
            case "تحية مسائية":
                return "Response for تحية مسائية";
            case "شروط القبول":
                return "Response for شروط القبول";
            default:
                return "I'm sorry, I don't know how to respond to that.";
        }
    }

}



