package com.gmail.calorious.cyclistdirections.generic;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.gmail.calorious.cyclistdirections.R;

import java.util.List;

public class UsersViewAdapter extends RecyclerView.Adapter<UsersViewHolder> {
    Context context;
    List<User> users;

    public UsersViewAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @NonNull
    @Override
    public UsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new UsersViewHolder(LayoutInflater.from(context).inflate(R.layout.participants_recyclerview_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull UsersViewHolder holder, int position) {
        holder.participant_name.setText(users.get(position).getName());
        holder.participant_number.setText(users.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}
