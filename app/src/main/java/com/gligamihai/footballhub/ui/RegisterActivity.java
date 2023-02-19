package com.gligamihai.footballhub.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gligamihai.footballhub.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        EditText email = findViewById(R.id.editTextEmail);
        EditText password = findViewById(R.id.editTextPassword);
        EditText name = findViewById(R.id.editTextName);
        EditText phoneNumber = findViewById(R.id.editTextMobile);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            if (isValidEmail(email.getText().toString().trim()) && isValidPassword(password.getText().toString().trim()) && !name.getText().toString().trim().isEmpty() && !phoneNumber.getText().toString().trim().isEmpty()) {
                registerUser(email.getText().toString().trim(), password.getText().toString().trim());
            } else if (email.getText().toString().trim().isEmpty() || password.getText().toString().trim().isEmpty() || name.getText().toString().trim().isEmpty() || phoneNumber.getText().toString().isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please make sure that there are no empty fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Invalid email or password format", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void registerUser(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {

                            onBackPressed();
                        } else {
                            Toast.makeText(RegisterActivity.this, task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean isValidEmail(String email) {
        if (email == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
        }
    }

    public boolean isValidPassword(String password) {
        if (password.length() >= 8) {
            Pattern letter = Pattern.compile("[a-zA-z]");
            Pattern digit = Pattern.compile("[0-9]");
            Pattern special = Pattern.compile("[!@#$%&*()_+=|<>?{}\\[\\]~-]");
            Matcher hasLetter = letter.matcher(password);
            Matcher hasDigit = digit.matcher(password);
            Matcher hasSpecial = special.matcher(password);

            return hasLetter.find() && hasDigit.find() && hasSpecial.find();

        } else
            return false;
    }

    public void goToLoginScreen(View view) {
        onBackPressed();
    }
}