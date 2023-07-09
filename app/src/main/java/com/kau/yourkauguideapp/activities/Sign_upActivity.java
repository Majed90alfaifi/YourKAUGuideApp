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
import com.kau.yourkauguideapp.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Sign_upActivity extends AppCompatActivity {

private ActivitySignUpBinding binding;
private PreferenceManager preferenceManager;
FirebaseAuth mAuth = FirebaseAuth.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferenceManager = new PreferenceManager(getApplicationContext());
        setListeners();
    }

    private void setListeners (){
        binding.buttonAlreadyHaveAnAccount.setOnClickListener(view -> onBackPressed());
        binding.buttonSignUp.setOnClickListener(view -> {
            if (isValidSignUpDetails()){
                signup();
            }
        });
    }

    private void showToast(String message){
        Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
    }

    private void signup() {
        loading(true);
        // Find the EditText fields for email and password
        EditText emailEditText = binding.editTextEmail;
        EditText passwordEditText = binding.editTextPassword;

        // Get the user's email and password from the EditText fields
        String email = emailEditText.getText().toString().trim();
        String password = passwordEditText.getText().toString().trim();

        // Call the createUserWithEmailAndPassword method to create a new user with the provided email and password
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign up success, update UI with the signed-up user's information.
                            FirebaseUser user = mAuth.getCurrentUser();
                            // Navigate to the main activity or do any other action
                            Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            String currentUser = mAuth.getCurrentUser().toString().trim();
                            Toast.makeText(Sign_upActivity.this, "Sign up successful. User: " + currentUser, Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        loading(false);
                        // Handle any exceptions that occurred during the sign-up process
                        e.printStackTrace();
                        Toast.makeText(Sign_upActivity.this, "Sign up failed. Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }



    private boolean isValidSignUpDetails(){
          if (binding.editTextEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter Your Email");
            return false;
        }

        else if (!Patterns.EMAIL_ADDRESS.matcher(binding.editTextEmail.getText().toString()).matches()) {
            showToast("Enter correct Email !!");
            return false;
        }

        else if (binding.editTextPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Your Password");
            return false;
        }else {
            return true ;
        }
    }


    private void loading (boolean isLoading){
        if(isLoading){
            binding.buttonSignUp.setVisibility(View.INVISIBLE);
            binding.ProgressBar.setVisibility(View.VISIBLE);
        }else {
            binding.ProgressBar.setVisibility(View.INVISIBLE);
            binding.buttonSignUp.setVisibility(View.VISIBLE);
        }
    }



}

