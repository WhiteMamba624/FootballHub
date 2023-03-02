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
import com.gligamihai.footballhub.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        FirebaseApp.initializeApp(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        EditText email = findViewById(R.id.editTextEmail);
        EditText password = findViewById(R.id.editTextPassword);
        EditText name = findViewById(R.id.editTextName);
        EditText phoneNumber = findViewById(R.id.editTextMobile);
        EditText experienceLevel= findViewById(R.id.editTextExperienceLevel);
        EditText height=findViewById(R.id.editTextHeight);
        EditText playingPosition=findViewById(R.id.editTextPlayingPosition);
        EditText preferredFoot=findViewById(R.id.editTextPreferredFoot);
        EditText weight=findViewById(R.id.editTextWeight);
        Button registerButton = findViewById(R.id.registerButton);

        registerButton.setOnClickListener(v -> {
            User user = new User(name.getText().toString().trim(), email.getText().toString().trim(), password.getText().toString().trim(), phoneNumber.getText().toString().trim(),Integer.parseInt(height.getText().toString().trim()),Float.parseFloat(weight.getText().toString().trim()),preferredFoot.getText().toString().trim(),playingPosition.getText().toString().trim(),experienceLevel.getText().toString().trim());
            if (isValidEmail(user.getEmail()) && isValidPassword(user.getPassword()) && !user.getName().isEmpty() && !user.getPhoneNumber().isEmpty()) {
                registerUser(user);
            } else if (user.getEmail().isEmpty() || user.getPassword().isEmpty() || user.getName().isEmpty() || user.getPhoneNumber().isEmpty()) {
                Toast.makeText(RegisterActivity.this, "Please make sure that there are no empty fields", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(RegisterActivity.this, "Invalid email or password format", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerUser(User user) {
        mAuth.createUserWithEmailAndPassword(user.getEmail(), user.getPassword()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            addUserToDataBase(user);
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

    private void addUserToDataBase(User user) {
        user.setPassword("");
        db.collection("Users").document(mAuth.getInstance().getUid())
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(RegisterActivity.this, "User successfully registered", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
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