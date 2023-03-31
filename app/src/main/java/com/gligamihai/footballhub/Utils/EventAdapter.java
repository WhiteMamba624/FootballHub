package com.gligamihai.footballhub.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gligamihai.footballhub.R;
import com.gligamihai.footballhub.models.Event;
import com.google.firebase.firestore.DocumentSnapshot;

import org.jetbrains.annotations.NotNull;

public class EventAdapter extends FirestoreRecyclerAdapter<Event, EventAdapter.EventHolder> {
    public OnItemClickListeners listener;

    public EventAdapter(@NonNull FirestoreRecyclerOptions<Event> options){super(options);}

    @Override
    protected void onBindViewHolder(@NonNull  EventHolder holder, int position, @NonNull Event event) {

        holder.eventTitle.setText(event.getMatchDayTitle());
        holder.recommendedPlayerLevel.setText(event.getRecommendedPlayerExperienceLevel());
        holder.eventDate.setText(event.getEventDate());
        holder.eventTime.setText(event.getEventStartTime());
        holder.eventLength.setText(String.valueOf(event.getEventLength()) +" hours");
        holder.eventLocation.setText(event.getEventLocation());
        holder.eventPlayersAndTeams.setText(String.valueOf(event.getNumberOfPlayersPerTeam())+" players x "+String.valueOf(event.getNumberOfTeams()));
        holder.eventPrice.setText(String.valueOf(event.getEventCost())+ " lei");
    }

    @NonNull
    @NotNull
    @Override
    public EventHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.event_item, parent, false);
        return new EventHolder(v);
    }

    public void deleteEvent(int position) {
        getSnapshots().getSnapshot(position).getReference().delete();
    }
    public String getEventId(int position){
        return getSnapshots().getSnapshot(position).getId();
    }


    class EventHolder extends RecyclerView.ViewHolder{
        TextView eventTitle;
        TextView recommendedPlayerLevel;
        TextView eventDate;
        TextView eventTime;
        TextView eventLength;
        TextView eventLocation;
        TextView eventPlayersAndTeams;
        TextView eventFreeSpots;
        TextView eventPrice;
        ImageView eventImage;

        public EventHolder(View itemView){
            super(itemView);
            eventTitle=itemView.findViewById(R.id.textViewEventTitle);
            recommendedPlayerLevel=itemView.findViewById(R.id.textViewRecommendedPlayerLevel);
            eventDate=itemView.findViewById(R.id.textViewEventDate);
            eventTime=itemView.findViewById(R.id.textViewEventTime);
            eventLength=itemView.findViewById(R.id.textViewEventLength);
            eventLocation=itemView.findViewById(R.id.textViewEventLocation);
            eventPlayersAndTeams=itemView.findViewById(R.id.textViewPlayersAndTeams);
            eventFreeSpots=itemView.findViewById(R.id.textViewFreeSpots);
            eventPrice=itemView.findViewById(R.id.textViewEventPrice);
            eventImage=itemView.findViewById(R.id.imageViewEvent);
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemLongClick(getSnapshots().getSnapshot(position), position);
                    }
                    return true;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getBindingAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }

    }

    public interface OnItemClickListeners {
        void onItemLongClick(DocumentSnapshot documentSnapshot, int position);

        void onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public void setOnItemClickListeners(OnItemClickListeners listener) {
        this.listener = listener;
    }

}
