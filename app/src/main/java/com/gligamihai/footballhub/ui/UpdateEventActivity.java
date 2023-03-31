package com.gligamihai.footballhub.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.gligamihai.footballhub.R;
import com.gligamihai.footballhub.models.Event;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.Calendar;

import static com.gligamihai.footballhub.ui.MainActivity.INTENT_EVENT_ID;

public class UpdateEventActivity extends AppCompatActivity {
    final Calendar calendar = Calendar.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public String id = null;
    EditText updateRecommendedPlayerExperienceLevel;
    EditText updateMatchDayTitle;
    EditText updateEventDate;
    EditText updateEventStartTime;
    EditText updateEventLength;
    EditText updateEventLocation;
    EditText updateNumberOfTeams;
    EditText updateNumberOfPlayersPerTeam;
    EditText updateEventCost;
    Button updateEventButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_event);
        if (getIntent() != null) {
            id = getIntent().getStringExtra(INTENT_EVENT_ID);
        }
        updateRecommendedPlayerExperienceLevel = findViewById(R.id.editTextUpdateRecommendedPlayerLevel);
        updateMatchDayTitle = findViewById(R.id.editTextUpdateMatchDayTitle);
        updateEventDate = findViewById(R.id.editTextUpdateEventDate);
        updateEventStartTime = findViewById(R.id.editTextUpdateEventTime);
        updateEventLength = findViewById(R.id.editTextUpdateEventLength);
        updateEventLocation = findViewById(R.id.editTextUpdateEventLocation);
        updateNumberOfTeams = findViewById(R.id.editTextUpdateEventNumberOfTeams);
        updateNumberOfPlayersPerTeam = findViewById(R.id.editTextUpdateEventNumberOfPlayersPerTeam);
        updateEventCost = findViewById(R.id.editTextUpdateEventCost);
        getEvent(id);
        updateEventButton = findViewById(R.id.updateEventButton);
        updateEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event = new Event();
                event.setOwnerId(FirebaseAuth.getInstance().getUid());
                event.setRecommendedPlayerExperienceLevel(updateRecommendedPlayerExperienceLevel.getText().toString().trim());
                event.setMatchDayTitle(updateMatchDayTitle.getText().toString().trim());
                event.setEventDate(updateEventDate.getText().toString().trim());
                event.setEventStartTime(updateEventStartTime.getText().toString().trim());
                event.setEventLength(Integer.parseInt(updateEventLength.getText().toString().trim()));
                event.setEventLocation(updateEventLocation.getText().toString().trim());
                event.setNumberOfTeams(Integer.parseInt(updateNumberOfTeams.getText().toString().trim()));
                event.setNumberOfPlayersPerTeam(Integer.parseInt(updateNumberOfPlayersPerTeam.getText().toString().trim()));
                event.setEventCost(Integer.parseInt(updateEventCost.getText().toString().trim()));
                if (!event.getOwnerId().isEmpty() && !event.getRecommendedPlayerExperienceLevel().isEmpty() && !event.getMatchDayTitle().isEmpty() && !event.getEventDate().isEmpty() && !event.getEventStartTime().isEmpty() && !String.valueOf(event.getEventLength()).isEmpty() && !event.getEventLocation().isEmpty() && !String.valueOf(event.getNumberOfTeams()).isEmpty() && !String.valueOf(event.getNumberOfPlayersPerTeam()).isEmpty() && !String.valueOf(event.getEventCost()).isEmpty()) {
                    if (event.getRecommendedPlayerExperienceLevel().equalsIgnoreCase("Beginner") || event.getRecommendedPlayerExperienceLevel().equalsIgnoreCase("Intermediate") || event.getRecommendedPlayerExperienceLevel().equalsIgnoreCase("Professional")) {
                        updateEvent(event);
                    } else {
                        Toast.makeText(UpdateEventActivity.this, "Experience level can only be Beginner, Intermediate of Professional", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(UpdateEventActivity.this, "Please make sure that there are no empty fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void goBackToMainActivity(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpdateEventActivity.this, MainActivity.class));
    }

    public void datePicker(View view) {
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        updateEventDate.setText(String.format("%d-%d-%d", dayOfMonth, monthOfYear + 1, year));

                    }
                }, year, month, day);
        datePickerDialog.show();
    }

    public void timePicker(View view) {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {
                        updateEventStartTime.setText(String.format("%d:%d", hourOfDay, minute));
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }

    public void getEvent(String id) {
        DocumentReference documentReference = db.collection("Events")
                .document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                Event event = documentSnapshot.toObject(Event.class);
                updateRecommendedPlayerExperienceLevel.setText(event.getRecommendedPlayerExperienceLevel());
                updateMatchDayTitle.setText(event.getMatchDayTitle());
                updateEventDate.setText(event.getEventDate());
                updateEventStartTime.setText(event.getEventStartTime());
                updateEventLength.setText(String.valueOf(event.getEventLength()));
                updateEventLocation.setText(event.getEventLocation());
                updateNumberOfTeams.setText(String.valueOf(event.getNumberOfTeams()));
                updateNumberOfPlayersPerTeam.setText(String.valueOf(event.getNumberOfPlayersPerTeam()));
                updateEventCost.setText(String.valueOf(event.getEventCost()));
            }
        });
    }

    public void updateEvent(Event event) {
        db.collection("Events")
                .document(id)
                .set(event)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(UpdateEventActivity.this, "Event updated successfully", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            Toast.makeText(UpdateEventActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}