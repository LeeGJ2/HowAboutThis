package com.example.myapp_20;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.GroupViewHolder>
{
    ArrayList<GroupInfo> arrayList;
    private Context context;

    public GroupAdapter(ArrayList<GroupInfo> arrayList, Context context)
    {
        this.arrayList = arrayList;

        this.context = context;
    }

    @NonNull
    @Override
    public GroupAdapter.GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_item, parent, false);
        GroupViewHolder groupViewHolder = new GroupViewHolder(view);
        return groupViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GroupAdapter.GroupViewHolder holder, int position) {
        holder.tv_people.setText(String.valueOf(arrayList.get(position).getCurPeople()));
        holder.tv_groupname.setText(arrayList.get(position).getGroupName());
        if(arrayList.get(position).isGrouping() == false)
            holder.tv_grouping.setText("모집중");
        else
            holder.tv_grouping.setVisibility(View.GONE);
        holder.groupid = arrayList.get(position).getGroupId();
        holder.grouping = arrayList.get(position).isGrouping();
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class GroupViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_groupname;
        TextView tv_people;
        TextView tv_grouping;
        String groupid;
        boolean grouping;
        public GroupViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.tv_groupname = itemView.findViewById(R.id.tv_groupname);
            this.tv_people = itemView.findViewById(R.id.tv_people);
            this.tv_grouping = itemView.findViewById(R.id.tv_grouping);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    if(grouping == false) {
                        Intent intent = new Intent(context, ApplicationActivity.class);
                        intent.putExtra("groupId", groupid);
                        context.startActivity(intent);
                    }
                    else
                    {
                        Intent intent = new Intent(context, GroupDetailActivity.class);
                        intent.putExtra("groupId", groupid);
                        context.startActivity(intent);
                    }
                }
            });

        }

    }
}
