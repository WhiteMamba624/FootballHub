package com.gligamihai.footballhub.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.gligamihai.footballhub.R;
import com.gligamihai.footballhub.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText updateName;
    EditText updatePhoneNumber;
    EditText updateExperienceLevel;
    EditText updateHeight;
    EditText updatePlayingPosition;
    EditText updatePreferredFoot;
    EditText updateWeight;
    Button updateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        updateName = findViewById(R.id.editTextInfoName);
        updatePhoneNumber = findViewById(R.id.editTextInfoMobile);
        updateExperienceLevel = findViewById(R.id.editTextInfoExperienceLevel);
        updateHeight = findViewById(R.id.editTextInfoHeight);
        updatePlayingPosition = findViewById(R.id.editTextInfoPlayingPosition);
        updatePreferredFoot = findViewById(R.id.editTextInfoPreferredFoot);
        updateWeight = findViewById(R.id.editTextInfoWeight);
        updateButton = findViewById(R.id.updateButton);
        getUserData();
        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User user = new User();
                user.setName(updateName.getText().toString().trim());
                user.setEmail(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                user.setPassword("");
                user.setPhoneNumber(updatePhoneNumber.getText().toString().trim());
                user.setExperienceLevel(updateExperienceLevel.getText().toString().trim());
                user.setHeight(Integer.parseInt(updateHeight.getText().toString().trim()));
                user.setPlayingPosition(updatePlayingPosition.getText().toString().trim());
                user.setPreferredFoot(updatePreferredFoot.getText().toString().trim());
                user.setWeight(Float.parseFloat(updateWeight.getText().toString().trim()));
                if (!user.getEmail().isEmpty() && !user.getName().isEmpty() && !user.getPhoneNumber().isEmpty() && !user.getExperienceLevel().isEmpty() && !updateWeight.getText().toString().isEmpty() && !updateHeight.getText().toString().isEmpty() && !user.getPlayingPosition().isEmpty() && !user.getPreferredFoot().isEmpty()) {
                    if (user.getPlayingPosition().equalsIgnoreCase("Striker") || user.getPlayingPosition().equalsIgnoreCase("Midfielder") || user.getPlayingPosition().equalsIgnoreCase("Defender") || user.getPlayingPosition().equalsIgnoreCase("Goalkeeper")) {
                        if (user.getExperienceLevel().equalsIgnoreCase("Beginner") || user.getExperienceLevel().equalsIgnoreCase("Intermediate") || user.getExperienceLevel().equalsIgnoreCase("Professional")) {
                            if (user.getPreferredFoot().equalsIgnoreCase("Left") || user.getPreferredFoot().equalsIgnoreCase("Right") || user.getPreferredFoot().equalsIgnoreCase("Both")) {
                                updateUser(user);
                            } else {
                                Toast.makeText(ProfileActivity.this, "Preferred foot can only be Right, Left or Both ", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(ProfileActivity.this, "Experience level can only be Beginner, Intermediate of Professional", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(ProfileActivity.this, "Playing position can only be Striker, Midfielder, Defender or Goalkeeper", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Please make sure that there are no empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goToMainActivity(View view) {
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
    }

    public void getUserData() {
        DocumentReference docRef = db.collection("Users")
                .document(FirebaseAuth.getInstance().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                updateName.setText(user.getName());
                updatePhoneNumber.setText(user.getPhoneNumber());
                updateExperienceLevel.setText(user.getExperienceLevel());
                updateHeight.setText(String.valueOf(user.getHeight()));
                updatePlayingPosition.setText(user.getPlayingPosition());
                updatePreferredFoot.setText(user.getPreferredFoot());
                updateWeight.setText(String.valueOf(user.getWeight()));
            }
        });
    }

    public void updateUser(User user) {
        db.collection("Users")
                .document(FirebaseAuth.getInstance().getUid())
                .set(user)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Toast.makeText(ProfileActivity.this, "User successfully updated", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(ProfileActivity.this, MainActivity.class));
                    } else {
                        Toast.makeText(ProfileActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}