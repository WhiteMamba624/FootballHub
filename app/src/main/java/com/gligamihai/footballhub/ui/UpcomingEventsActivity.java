package com.gligamihai.footballhub.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gligamihai.footballhub.R;
import com.gligamihai.footballhub.Utils.EventAdapter;
import com.gligamihai.footballhub.models.Event;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class UpcomingEventsActivity extends AppCompatActivity {
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private EventAdapter eventAdapter;
    private CollectionReference documentRef = db.collection("Users").document(FirebaseAuth.getInstance().getUid()).collection("Events");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upcoming_events);
        setUpRecyclerView();
    }

    public void setUpRecyclerView() {
        Query query = documentRef.orderBy("eventDate", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class)
                .build();
        eventAdapter = new EventAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.upcoming_events_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        eventAdapter.startListening();
    }
    @Override
    protected void onStop() {
        super.onStop();
        eventAdapter.stopListening();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(UpcomingEventsActivity.this,MainActivity.class));
    }
}