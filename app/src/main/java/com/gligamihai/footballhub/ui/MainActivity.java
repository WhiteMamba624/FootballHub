package com.gligamihai.footballhub.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gligamihai.footballhub.R;
import com.gligamihai.footballhub.Utils.EventAdapter;
import com.gligamihai.footballhub.Utils.Main;
import com.gligamihai.footballhub.models.Event;
import com.gligamihai.footballhub.models.User;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    TextView currentUserEmail;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference documentRef = db.collection("Events");
    private EventAdapter eventAdapter;
    public static final String INTENT_EVENT_ID = "event_id";
    public Event event = new Event();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawerLayout = findViewById(R.id.drawer_layout);
        currentUserEmail = findViewById(R.id.currentUserEmail);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            currentUserEmail.setText(user.getEmail());
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
        setUpRecyclerView();
        FloatingActionButton fab = findViewById(R.id.addEventFab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, AddEventActivity.class));
            }
        });
    }

    public void setUpRecyclerView() {
        Query query = documentRef.orderBy("eventDate", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<Event> options = new FirestoreRecyclerOptions.Builder<Event>()
                .setQuery(query, Event.class)
                .build();
        eventAdapter = new EventAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.events_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(eventAdapter);
        removeSelectedEvent(recyclerView);
    }

    public void removeSelectedEvent(RecyclerView recyclerView) {
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
                String eventID = eventAdapter.getEventId(viewHolder.getBindingAdapterPosition());
                getEventData(eventID);
                if (FirebaseAuth.getInstance().getUid().equals(event.getOwnerId())) {
                    new AlertDialog.Builder(viewHolder.itemView.getContext())
                            .setMessage("Are you sure you want to delete this event?")
                            .setPositiveButton("Yes", (dialog, which) -> eventAdapter.deleteEvent(viewHolder.getBindingAdapterPosition()))
                            .setNegativeButton("No", (dialog, which) -> eventAdapter.notifyItemChanged(viewHolder.getBindingAdapterPosition()))
                            .create()
                            .show();
                } else {
                    Toast.makeText(MainActivity.this, "Only the owner of the event can delete it", Toast.LENGTH_SHORT).show();
                    eventAdapter.notifyItemChanged(viewHolder.getBindingAdapterPosition());
                }
            }
        }).attachToRecyclerView(recyclerView);
        eventAdapter.setOnItemClickListeners(new EventAdapter.OnItemClickListeners() {
            @Override
            public void onItemLongClick(DocumentSnapshot documentSnapshot, int position) {
                String eventID=documentSnapshot.getId();
                getEventData(eventID);
                if (FirebaseAuth.getInstance().getUid().equals(event.getOwnerId())){
                    Intent goToUpdateEventActivity=new Intent(MainActivity.this,UpdateEventActivity.class);
                    goToUpdateEventActivity.putExtra(INTENT_EVENT_ID,eventID);
                    startActivity(goToUpdateEventActivity);
                } else {
                    Toast.makeText(MainActivity.this, "Only the owner of this event can update it", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String eventID=documentSnapshot.getId();
                Intent goToJoinEventActivity=new Intent(MainActivity.this,JoinEventActivity.class);
                goToJoinEventActivity.putExtra(INTENT_EVENT_ID,eventID);
                startActivity(goToJoinEventActivity);
            }
        });
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

    public void getEventData(String docId) {
        DocumentReference documentReference = db.collection("Events")
                .document(docId);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                event = documentSnapshot.toObject(Event.class);
            }
        });

    }

    public void clickMenu(View view) {
        //Open drawer layout
        openDrawer(drawerLayout);
    }

    public static void openDrawer(DrawerLayout drawerLayout) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void clickLogo(View view) {
        //Close drawer
        closeDrawer(drawerLayout);
    }

    public static void closeDrawer(DrawerLayout drawerLayout) {
        //Close drawer layout
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
    }

    public void clickHome(View view) {
        recreate();
    }

    public void clickLogout(View view) {
        AlertDialog.Builder alertLogout = new AlertDialog.Builder(view.getContext());
        alertLogout.setTitle("Logout");
        alertLogout.setMessage("Are you sure you want to log out?");
        alertLogout.setPositiveButton("Yes", (dialog, which) -> {
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(view.getContext(), LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        });
        alertLogout.setNegativeButton("No", (dialog, which) -> {

        });
        alertLogout.show();
    }

    public void clickProfile(View view) {
        startActivity(new Intent(MainActivity.this, ProfileActivity.class));
    }

    public void clickWeather(View view) {
        startActivity(new Intent(MainActivity.this, WeatherActivity.class));
    }

    public void clickPlaces(View view) {
        startActivity(new Intent(MainActivity.this, PlacesActivity.class));
    }

    public void clickCaloriesCalculator(View view) {
        startActivity(new Intent(MainActivity.this, CaloriesCalculatorActivity.class));
    }
}