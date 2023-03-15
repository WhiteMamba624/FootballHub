package com.gligamihai.footballhub.Utils;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.gligamihai.footballhub.R;
import com.gligamihai.footballhub.models.Event;
import com.gligamihai.footballhub.models.User;

import org.jetbrains.annotations.NotNull;

public class UserAdapter extends FirestoreRecyclerAdapter<User, UserAdapter.UserHolder> {

    public UserAdapter(@NonNull FirestoreRecyclerOptions<User> options){super(options);}

    @Override
    protected void onBindViewHolder(@NonNull UserHolder holder, int position, @NonNull User user) {
        holder.userFullName.setText(user.getName());
        holder.userPlayingPosition.setText(user.getPlayingPosition());
        holder.userExperienceLevel.setText(user.getExperienceLevel());
        holder.userPreferredFoot.setText(user.getPreferredFoot());
    }

    @NonNull
    @NotNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        return new UserHolder(v);
    }


    class UserHolder extends RecyclerView.ViewHolder{
        TextView userFullName;
        TextView userPlayingPosition;
        TextView userExperienceLevel;
        TextView userPreferredFoot;

        public UserHolder(View itemView){
            super(itemView);
            userFullName=itemView.findViewById(R.id.textViewUserFullName);
            userPlayingPosition=itemView.findViewById(R.id.textViewPlayingPosition);
            userExperienceLevel=itemView.findViewById(R.id.textViewUserExperienceLevel);
            userPreferredFoot=itemView.findViewById(R.id.textViewUserPreferredFoot);
        }
    }

}
