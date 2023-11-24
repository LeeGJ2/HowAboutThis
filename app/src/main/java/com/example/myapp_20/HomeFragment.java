package com.example.myapp_20;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
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

public class HomeFragment extends Fragment
{
    public HomeFragment()
    {
    }

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;

    ArrayList<GroupInfo> groups;
    private RecyclerView rcv_group;
    private RecyclerView.LayoutManager layoutManager_group;
    private RecyclerView.Adapter adapter_group;

    ArrayList<ScheduleInfo> schedules;
    private RecyclerView rcv_schedule;
    private RecyclerView.LayoutManager layoutManager_schedule;
    private RecyclerView.Adapter adapter_schedule;

    ArrayList<String> posts;
    private RecyclerView rcv_post;
    private RecyclerView.LayoutManager layoutManager_post;
    private RecyclerView.Adapter adapter_post;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_home,container, false);
        firebaseAuth  = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");

        rcv_group = view.findViewById(R.id.rcv_group);
        layoutManager_group = new LinearLayoutManager(getContext());
        rcv_group.setLayoutManager(layoutManager_group);
        rcv_group.setHasFixedSize(true);
        groups = new ArrayList<>();

        rcv_schedule = view.findViewById(R.id.rcv_schedule);
        layoutManager_schedule = new LinearLayoutManager(getContext());
        rcv_schedule.setLayoutManager(layoutManager_schedule);
        rcv_schedule.setHasFixedSize(true);
        schedules = new ArrayList<>();

        rcv_post = view.findViewById(R.id.rcv_post);
        layoutManager_post = new LinearLayoutManager(getContext());
        rcv_post.setLayoutManager(layoutManager_post);
        rcv_post.setHasFixedSize(true);
        posts = new ArrayList<>();
        posts.add("자유 게시판");
        posts.add("대회/공모전 게시판");
        posts.add("대외활동 게시판");
        posts.add("스터디 게시판");



        databaseReference.child("Group").orderByChild("users/"+firebaseAuth.getCurrentUser().getUid()).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                groups.clear();
                schedules.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    GroupInfo groupInfo = dataSnapshot.getValue(GroupInfo.class);
                    databaseReference.child("Schedule").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                            {
                                ScheduleInfo scheduleInfo = dataSnapshot1.getValue(ScheduleInfo.class);
                                if(scheduleInfo.getGroupId().equals(groupInfo.getGroupId()))
                                {
                                    schedules.add(scheduleInfo);
                                    Collections.sort(schedules, new Comparator<ScheduleInfo>() {
                                        @Override
                                        public int compare(ScheduleInfo o1, ScheduleInfo o2)
                                        {
                                            return o1.getDate().hashCode() - o2.getDate().hashCode();
                                        }
                                    });
                                    if(schedules.size() > 4) schedules.remove(schedules.size()-1);
                                    adapter_schedule.notifyDataSetChanged();
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    groups.add(groupInfo);

                    if(groups.size() > 4) groups.remove(groups.size()-1);
                }
                adapter_group.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter_group = new CurGroupAdapter(groups, getContext());
        rcv_group.setAdapter(adapter_group);

        adapter_schedule = new ScheduleAdapter(schedules, getContext());
        rcv_schedule.setAdapter(adapter_schedule);

        return view;
    }

    class CurGroupAdapter extends RecyclerView.Adapter<CurGroupAdapter.CurGroupViewHolder>
    {
        ArrayList<GroupInfo> arrayList;
        private  Context context;

        public CurGroupAdapter(ArrayList<GroupInfo> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public CurGroupAdapter.CurGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cur_group_item, parent, false);
            CurGroupAdapter.CurGroupViewHolder curGroupViewHolder = new CurGroupAdapter.CurGroupViewHolder(view);
            return curGroupViewHolder;

        }

        @Override
        public void onBindViewHolder(@NonNull CurGroupAdapter.CurGroupViewHolder holder, int position) {
            holder.tv_curgroup.setText(arrayList.get(position).getGroupName());
        }

        @Override
        public int getItemCount() {
            return (arrayList != null ? arrayList.size() : 0);
        }

        public class CurGroupViewHolder extends RecyclerView.ViewHolder {
            TextView tv_curgroup;
            public CurGroupViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_curgroup = itemView.findViewById(R.id.tv_curgroup);
            }
        }
    }

    class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>
    {
        ArrayList<ScheduleInfo> arrayList;
        private Context context;

        public ScheduleAdapter(ArrayList<ScheduleInfo> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_schedule_item, parent, false);
            ScheduleAdapter.ScheduleViewHolder scheduleViewHolder = new ScheduleViewHolder(view);
            return scheduleViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ScheduleAdapter.ScheduleViewHolder holder, int position) {
            holder.tv_day.setText(arrayList.get(position).getDate());
            holder.tv_date.setText(arrayList.get(position).getContents());
        }

        @Override
        public int getItemCount() {
            return (arrayList != null ? arrayList.size() : 0);
        }

        public class ScheduleViewHolder extends RecyclerView.ViewHolder {
            TextView tv_day;
            TextView tv_date;
            public ScheduleViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_day = itemView.findViewById(R.id.tv_day);
                tv_date = itemView.findViewById(R.id.tv_date);
            }
        }
    }
}
