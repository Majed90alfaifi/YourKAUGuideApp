package com.kau.yourkauguideapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.kau.yourkauguideapp.R;
import com.kau.yourkauguideapp.chatbot_TfliteModel;

import org.tensorflow.lite.Interpreter;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

public class MainActivity extends AppCompatActivity {
    Button signInBtn;
    Button signUpBtn;
    Button signAsGuist;
    chatbot_TfliteModel chatbottfliteModel;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        signInBtn = (Button)findViewById(R.id.button_sign_in);
        signUpBtn = (Button)findViewById(R.id.button_sign_up);
        signAsGuist=(Button)findViewById(R.id.button_sign_as_guist);

        signInBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openSignInPage();
            }
        });

        signUpBtn.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openSignupPage();
            }
        });

        signAsGuist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               openChatPage();
            }
        });


    }

        void openSignInPage(){
        chatbottfliteModel= new chatbot_TfliteModel(this);
            Intent intent = new Intent(this, Sign_inActivity.class);
            startActivity(intent);
        }

        void openSignupPage(){
            Intent intent = new Intent(this, Sign_upActivity.class);
            startActivity(intent);
        }

        void openChatPage(){
            Intent intent = new Intent(this, ChatActivity.class);
            startActivity(intent);
        }
}