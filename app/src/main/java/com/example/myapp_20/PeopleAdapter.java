package com.example.myapp_20;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PeopleAdapter extends RecyclerView.Adapter<PeopleAdapter.PeopleViewHolder>
{
    private ArrayList<UserAcc> arrayList;
    private Context context;

    public PeopleAdapter(ArrayList<UserAcc> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PeopleAdapter.PeopleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_item, parent, false);
        PeopleAdapter.PeopleViewHolder peopleViewHolder = new PeopleViewHolder(view);

        return peopleViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleAdapter.PeopleViewHolder holder, int position)
    {
        holder.tv_useremail.setText(arrayList.get(position).getEmailId());
        holder.tv_username.setText(arrayList.get(position).getNickname());
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PeopleViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_username;
        TextView tv_useremail;
        public PeopleViewHolder(@NonNull View itemView)
        {
            super(itemView);
            tv_username = itemView.findViewById(R.id.tv_username);
            tv_useremail = itemView.findViewById(R.id.tv_useremail);
        }
    }
}