package com.gligamihai.footballhub.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gligamihai.footballhub.R;
import com.gligamihai.footballhub.Utils.EventAdapter;
import com.gligamihai.footballhub.Utils.UserAdapter;
import com.gligamihai.footballhub.models.Event;
import com.gligamihai.footballhub.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.jetbrains.annotations.NotNull;

import static com.gligamihai.footballhub.ui.MainActivity.INTENT_EVENT_ID;

public class JoinEventActivity extends AppCompatActivity {
    public String id = null;
    public int numberOfPlayers=0;
    private UserAdapter userAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference documentRef;
    private DocumentReference playerDocumentRef;
    public User user = new User();
    public Event event=new Event();
    Button userJoinEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_event);
        userJoinEvent = findViewById(R.id.joinEventButton);
        if (getIntent() != null) {
            id = getIntent().getStringExtra(INTENT_EVENT_ID);
            getUserData();
            getEventData();
            documentRef = db.collection("Events").document(id).collection("Players");
            playerDocumentRef = db.collection("Events").document(id).collection("Players").document(FirebaseAuth.getInstance().getUid());
            setUpRecyclerView();
            documentRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        numberOfPlayers=task.getResult().size();
                        //Toast.makeText(JoinEventActivity.this, "Number of players is "+numberOfPlayers, Toast.LENGTH_SHORT).show();
                    }
                }
            });
            playerDocumentRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if (documentSnapshot.exists()) {
                            userJoinEvent.setText("Leave event");
                        }
                    }
                }
            });
            userJoinEvent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String buttonText=userJoinEvent.getText().toString();
                    if(buttonText.equals("Join event") && (numberOfPlayers<=event.getNumberOfTeams()*event.getNumberOfPlayersPerTeam())) {
                        db.collection("Events").document(id).collection("Players").document(FirebaseAuth.getInstance().getUid())
                                .set(user)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(JoinEventActivity.this, "User joined event successfully", Toast.LENGTH_SHORT).show();
                                            userJoinEvent.setText("Leave event");
                                        } else {
                                            Toast.makeText(JoinEventActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }else if(buttonText.equals("Leave event")){
                        AlertDialog.Builder alertLeaveEvent = new AlertDialog.Builder(JoinEventActivity.this);
                        alertLeaveEvent.setTitle("Leave event");
                        alertLeaveEvent.setMessage("Are you sure you want to leave this event?");
                        alertLeaveEvent.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                db.collection("Events").document(id).collection("Players").document(FirebaseAuth.getInstance().getUid())
                                        .delete()
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused) {
                                                Toast.makeText(JoinEventActivity.this, "You have left this event successfully", Toast.LENGTH_SHORT).show();
                                                userJoinEvent.setText("Join event");
                                            }
                                        });
                            }
                        });
                        alertLeaveEvent.setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                        alertLeaveEvent.show();
                    }
                }
            });
        }

    }


    public void setUpRecyclerView() {
        Query query = documentRef.orderBy("name", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<User> options = new FirestoreRecyclerOptions.Builder<User>()
                .setQuery(query, User.class)
                .build();
        userAdapter = new UserAdapter(options);
        RecyclerView recyclerView = findViewById(R.id.users_joined_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(userAdapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        userAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        userAdapter.stopListening();
    }

    public void getUserData() {
        DocumentReference docRef = db.collection("Users")
                .document(FirebaseAuth.getInstance().getUid());
        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                user = documentSnapshot.toObject(User.class);
            }
        });
    }

    public void getEventData(){
        DocumentReference documentReference=db.collection("Events")
                .document(id);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                event=documentSnapshot.toObject(Event.class);
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(JoinEventActivity.this,MainActivity.class));
    }
}