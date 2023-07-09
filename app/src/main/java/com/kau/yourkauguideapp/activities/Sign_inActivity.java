package com.kau.yourkauguideapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.kau.yourkauguideapp.utilities.PreferenceManager;
import com.kau.yourkauguideapp.databinding.ActivitySignInBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_inActivity extends AppCompatActivity {

    private ActivitySignInBinding binding;
    private PreferenceManager preferenceManager;
    // Get instance of FirebaseAuth
    FirebaseAuth mAuth = FirebaseAuth.getInstance();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferenceManager = new PreferenceManager(getApplicationContext());
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setListeners();
    }

    private void setListeners() {
        binding.CreateAccount.setOnClickListener(view ->
                startActivity(new Intent(getApplicationContext(),Sign_upActivity.class)));
        binding.buttonLogin.setOnClickListener(view -> {
            if(isValidSignInDetails()){
                signIn();
            }
        });
    }


    private void signIn(){
        loading(true);
// Find the EditText fields for email and password
        EditText emailEditText = binding.editTextEmail;
        EditText passwordEditText = binding.editTextPassword;

// Get the user's email and password from the EditText fields
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

// Call the signInWithEmailAndPassword method to sign in the user
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Navigate to the main activity or do any other action
                            Intent intent =new Intent(getApplicationContext(),ChatActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            String CurntUser=mAuth.getCurrentUser().toString().trim();
                            Toast.makeText(Sign_inActivity.this, CurntUser, Toast.LENGTH_SHORT).show();

                        } else {
                            loading(false);
                            // If sign in fails, display a message to the user.
                            Toast.makeText(Sign_inActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }
    public String ShowToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        return message;
    }
    private boolean isValidSignInDetails(){
        if(binding.editTextEmail.getText().toString().trim().isEmpty()){
            showToast("Enter email");
            return false;

        }else if (!Patterns.EMAIL_ADDRESS.matcher(binding.editTextEmail.getText().toString()).matches()) {
            showToast("Enter Valid email !!");
            return false;

        } else if (binding.editTextPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter password");
            return false;

        }else {
            return true ;
        }
    }

    private void loading (boolean isLoading){
        if(isLoading){
            binding.buttonLogin.setVisibility(View.INVISIBLE);
            binding.ProgressBar.setVisibility(View.VISIBLE);
        }else {
            binding.ProgressBar.setVisibility(View.INVISIBLE);
            binding.buttonLogin.setVisibility(View.VISIBLE);
        }
    }

}




