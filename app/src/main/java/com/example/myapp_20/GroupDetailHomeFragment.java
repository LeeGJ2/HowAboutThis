package com.example.myapp_20;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Set;

public class GroupDetailHomeFragment extends Fragment
{
    private String groupId;
    private TextView tv_group_title;
    private DatabaseReference databaseReference;

    private ArrayList<UserAcc> arrayList;
    private ArrayList<ScheduleInfo> scheduleInfos;
    private RecyclerView rcv_group_people;
    private RecyclerView rcv_group_date;
    private RecyclerView.Adapter adapter;
    private RecyclerView.Adapter adapter2;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.LayoutManager layoutManager2;
    public GroupDetailHomeFragment() {
    }

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_group_detail_home,container, false);
        tv_group_title = view.findViewById(R.id.tv_group_title);
        rcv_group_people = view.findViewById(R.id.rcv_group_people);
        rcv_group_date = view.findViewById(R.id.rcv_group_date);

        layoutManager = new LinearLayoutManager(getContext());
        rcv_group_people.setLayoutManager(layoutManager);
        rcv_group_people.setHasFixedSize(true);

        layoutManager2 = new LinearLayoutManager(getContext());
        rcv_group_date.setLayoutManager(layoutManager2);
        rcv_group_date.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");
        arrayList = new ArrayList<>();
        scheduleInfos = new ArrayList<>();

        databaseReference.child("Group").child(groupId).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                arrayList.clear();

                GroupInfo groupInfo = snapshot.getValue(GroupInfo.class);
                tv_group_title.setText(groupInfo.getGroupName());
                Set set = groupInfo.users.keySet();
                Iterator iterator = set.iterator();
                while(iterator.hasNext())
                {
                    String key = (String) iterator.next();
                    databaseReference.child("UserAccount").child(key).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            UserAcc userAcc = snapshot.getValue(UserAcc.class);
                            arrayList.add(userAcc);
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Schedule").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ScheduleInfo scheduleInfo = dataSnapshot.getValue(ScheduleInfo.class);
                    if(scheduleInfo.getGroupId().equals(groupId)) {
                        scheduleInfos.add(scheduleInfo);
                        Collections.sort(scheduleInfos, new Comparator<ScheduleInfo>() {
                            @Override
                            public int compare(ScheduleInfo o1, ScheduleInfo o2)
                            {
                                return  o1.getDate().hashCode() - o2.getDate().hashCode();
                            }
                        });
                    }
                }
                adapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new GroupHomeAdapter(arrayList, getContext());
        rcv_group_people.setAdapter(adapter);

        adapter2 = new GroupDateAdapter(scheduleInfos, getContext());
        rcv_group_date.setAdapter(adapter2);

        return view;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }


    class GroupDateAdapter extends RecyclerView.Adapter<GroupDateAdapter.GroupDateViewHolder>
    {
        ArrayList<ScheduleInfo> arrayList;
        private  Context context;

        public GroupDateAdapter(ArrayList<ScheduleInfo> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public GroupDateAdapter.GroupDateViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_schedule_item, parent, false);
            GroupDateAdapter.GroupDateViewHolder groupDateViewHolder = new GroupDateAdapter.GroupDateViewHolder(view);
            return groupDateViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull GroupDateAdapter.GroupDateViewHolder holder, int position) {
            holder.tv_day.setText(arrayList.get(position).getDate());
            holder.tv_date.setText(arrayList.get(position).getContents());
        }

        @Override
        public int getItemCount() {
            return (arrayList != null ? arrayList.size() : 0);
        }

        public class GroupDateViewHolder extends RecyclerView.ViewHolder {
            TextView tv_day;
            TextView tv_date;
            public GroupDateViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_day = itemView.findViewById(R.id.tv_day);
                tv_date = itemView.findViewById(R.id.tv_date);
            }
        }
    }
    class GroupHomeAdapter extends RecyclerView.Adapter<GroupHomeAdapter.GroupHomeViewHolder>
    {
        ArrayList<UserAcc> arrayList;
        private Context context;

        public GroupHomeAdapter(ArrayList<UserAcc> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public GroupHomeAdapter.GroupHomeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_user_item, parent, false);
            GroupHomeAdapter.GroupHomeViewHolder groupHomeViewHolder = new GroupHomeViewHolder(view);
            return groupHomeViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull GroupHomeAdapter.GroupHomeViewHolder holder, int position)
        {
            holder.tv_username.setText(arrayList.get(position).getNickname());
        }

        @Override
        public int getItemCount() {
            return (arrayList != null ? arrayList.size() : 0);
        }

        public class GroupHomeViewHolder extends RecyclerView.ViewHolder
        {
            TextView tv_username;
            public GroupHomeViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_username = itemView.findViewById(R.id.tv_username);
            }
        }
    }



}
