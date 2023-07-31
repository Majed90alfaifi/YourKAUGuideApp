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
                System.exit(0);
            }
        });

        SendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String feedback = userIsuueMsgEdt.getText().toString();

                if (feedback.isEmpty()) {
                    // if the edit text is empty display a toast message.
                    Toast.makeText(FeedbackActivity.this, "الرجاء كتابة استفسارك..", Toast.LENGTH_SHORT).show();
                    return;
                }
                userFeedback(feedback);

                // below line we are setting text in our edit text as empty
                userIsuueMsgEdt.setText("");
                goBack();
            }
        });

        imageBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goBack();
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
        Intent intent = new Intent(this, MainActivity.class);
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
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("users");

        DatabaseReference messageRef = ref.push();
        messageRef.setValue(feedback).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                // Message saved successfully
                Toast.makeText(FeedbackActivity.this, " تم الارسال بنجاح ..\n سيتم التعامل مع رسالتك قريبا ..", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                // Failed to save message
                Toast.makeText(FeedbackActivity.this, "فشل في الارسال..", Toast.LENGTH_SHORT).show();
            }
        });
    }
}