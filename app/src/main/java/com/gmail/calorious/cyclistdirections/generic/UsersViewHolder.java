package com.gmail.calorious.cyclistdirections.generic;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.calorious.cyclistdirections.R;

public class UsersViewHolder extends RecyclerView.ViewHolder {
    TextView participant_name, participant_number;
    public UsersViewHolder(@NonNull View itemView) {
        super(itemView);
        participant_name = itemView.findViewById(R.id.participants_recyclerview_name);
        participant_number = itemView.findViewById(R.id.participants_recyclerview_number);
    }
}
