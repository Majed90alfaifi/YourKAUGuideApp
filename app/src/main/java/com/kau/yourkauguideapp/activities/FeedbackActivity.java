package com.kau.yourkauguideapp.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.kau.yourkauguideapp.R;

public class FeedbackActivity extends AppCompatActivity {

    private EditText userIsuueMsgEdt;
    private Button SendButton;
    private Button TwitterButton;
    private ImageView imageBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        userIsuueMsgEdt=findViewById(R.id.issueEditText);
        SendButton=findViewById(R.id.sendButton);
        TwitterButton=findViewById(R.id.TwitterButton);
        imageBack = findViewById(R.id.FimageBack);


        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goBack();
            }
        });

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (userIsuueMsgEdt.getText().toString().isEmpty()) {
                    // if the edit text is empty display a toast message.
                    Toast.makeText(FeedbackActivity.this, "Please enter your Issue..", Toast.LENGTH_SHORT).show();
                    return;
                }
                String feedback = userIsuueMsgEdt.getText().toString();
                userFeedback(feedback);

                // below line we are setting text in our edit text as empty
                userIsuueMsgEdt.setText("");
            }
        });

        TwitterButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent;

                // If the Twitter app is installed, open the app
                if (isTwitterAppInstalled()) {
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("twitter://user?screen_name=FCITKAU"));
                } else {
                    // If the Twitter app is not installed, open the Twitter website in a web browser
                    intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://twitter.com/FCITKAU"));
                }

                startActivity(intent);
            }
        });

    }

    void goBack(){
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }
    private boolean isTwitterAppInstalled() {
        PackageManager pm = getPackageManager();
        try {
            pm.getPackageInfo("com.twitter.android", 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    private void userFeedback(String feedback){
        //send the issue to DB
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        String userId = auth.getCurrentUser().getUid(); // get the current user's ID from Firebase Authentication
        DatabaseReference ref = database.getReference("users").child(userId); // create a reference to the user's data in Firebase

        DatabaseReference messageRef = ref.child("feedback"); // create a child reference called "feedback" under the user's data

        messageRef.setValue(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // message saved successfully
                Toast.makeText(FeedbackActivity.this, "message saved successfully..", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // failed to save message
                Toast.makeText(FeedbackActivity.this, "failed to save message..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
