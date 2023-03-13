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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;

public class AddEventActivity extends AppCompatActivity {
    final Calendar calendar = Calendar.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    EditText recommendedPlayerExperienceLevel;
    EditText matchDayTitle;
    EditText eventDate;
    EditText eventStartTime;
    EditText eventLength;
    EditText eventLocation;
    EditText numberOfTeams;
    EditText numberOfPlayersPerTeam;
    EditText eventCost;
    Button addEventButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);
        recommendedPlayerExperienceLevel=findViewById(R.id.editTextRecommendedPlayerLevel);
        matchDayTitle=findViewById(R.id.editTextMatchDayTitle);
        eventDate=findViewById(R.id.editTextEventDate);
        eventStartTime=findViewById(R.id.editTextEventTime);
        eventLength=findViewById(R.id.editTextEventLength);
        eventLocation=findViewById(R.id.editTextEventLocation);
        numberOfTeams=findViewById(R.id.editTextEventNumberOfTeams);
        numberOfPlayersPerTeam=findViewById(R.id.editTextEventNumberOfPlayersPerTeam);
        eventCost=findViewById(R.id.editTextEventCost);
        addEventButton=findViewById(R.id.addEventButton);
        addEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Event event=new Event();
                event.setOwnerId(FirebaseAuth.getInstance().getUid());
                event.setRecommendedPlayerExperienceLevel(recommendedPlayerExperienceLevel.getText().toString().trim());
                event.setMatchDayTitle(matchDayTitle.getText().toString().trim());
                event.setEventDate(eventDate.getText().toString().trim());
                event.setEventStartTime(eventStartTime.getText().toString().trim());
                event.setEventLength(Integer.parseInt(eventLength.getText().toString().trim()));
                event.setEventLocation(eventLocation.getText().toString().trim());
                event.setNumberOfTeams(Integer.parseInt(numberOfTeams.getText().toString().trim()));
                event.setNumberOfPlayersPerTeam(Integer.parseInt(numberOfPlayersPerTeam.getText().toString().trim()));
                event.setEventCost(Integer.parseInt(numberOfPlayersPerTeam.getText().toString().trim()));
                addEvent(event);
            }
        });

    }

    public void addEvent(Event event){
        db.collection("Events")
                .add(event)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(AddEventActivity.this, "Event added successfully", Toast.LENGTH_SHORT).show();
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull  Exception e) {
                        Toast.makeText(AddEventActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void goBackToMainActivity(View view) {
        onBackPressed();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddEventActivity.this,MainActivity.class));
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
                        eventDate.setText(String.format("%d-%d-%d", dayOfMonth, monthOfYear + 1, year));

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
                        eventStartTime.setText(String.format("%d:%d", hourOfDay, minute));
                    }
                }, hour, minute, true);
        timePickerDialog.show();
    }
}