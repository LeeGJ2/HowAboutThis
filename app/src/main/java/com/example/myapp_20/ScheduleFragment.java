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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ScheduleFragment extends Fragment
{

    public ScheduleFragment() {
    }

    private CalendarView cv_main;
    private TextView tv_mainday;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private RecyclerView.Adapter adapter;
    private ArrayList<ScheduleInfo> arrayList;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private String day;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_schedule,container, false);
        arrayList = new ArrayList<>();
        cv_main = view.findViewById(R.id.cv_main);
        tv_mainday = view.findViewById(R.id.tv_mainday);
        recyclerView = view.findViewById(R.id.rcv_schedule);
        layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);

        databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");
        firebaseAuth = FirebaseAuth.getInstance();

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        day = simpleDateFormat.format(cv_main.getDate());

        databaseReference.child("Group").orderByChild("users/"+firebaseAuth.getCurrentUser().getUid()).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                arrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    GroupInfo groupInfo = dataSnapshot.getValue(GroupInfo.class);
                    databaseReference.child("Schedule").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                            {
                                ScheduleInfo scheduleInfo = dataSnapshot1.getValue(ScheduleInfo.class);
                                if(scheduleInfo.getDate().equals(day))
                                    arrayList.add(scheduleInfo);
                                adapter.notifyDataSetChanged();
                            }
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

        adapter = new MainScheduleAdapter(arrayList, getContext());
        recyclerView.setAdapter(adapter);

        cv_main.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                day = String.format("%d-%d-%d", year, month+1, dayOfMonth);
                databaseReference.child("Group").orderByChild("users/"+firebaseAuth.getCurrentUser().getUid()).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            GroupInfo groupInfo = dataSnapshot.getValue(GroupInfo.class);
                            databaseReference.child("Schedule").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for(DataSnapshot dataSnapshot1 : snapshot.getChildren())
                                    {
                                        ScheduleInfo scheduleInfo = dataSnapshot1.getValue(ScheduleInfo.class);
                                        if(scheduleInfo.getDate().equals(day))
                                            arrayList.add(scheduleInfo);
                                        adapter.notifyDataSetChanged();
                                    }
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
                adapter = new MainScheduleAdapter(arrayList, getContext());
                recyclerView.setAdapter(adapter);
            }
        });

        return view;
    }

    class MainScheduleAdapter extends RecyclerView.Adapter<MainScheduleAdapter.MainScheduleViewHolder>
    {
        ArrayList<ScheduleInfo> arrayList;
        private Context context;

        public MainScheduleAdapter(ArrayList<ScheduleInfo> arrayList, Context context) {
            this.arrayList = arrayList;
            this.context = context;
        }

        @NonNull
        @Override
        public MainScheduleAdapter.MainScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
            MainScheduleViewHolder mainScheduleViewHolder = new MainScheduleViewHolder(view);
            return mainScheduleViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MainScheduleAdapter.MainScheduleViewHolder holder, int position)
        {
            holder.tv_schedule_content.setText("· "+arrayList.get(position).getContents());
        }

        @Override
        public int getItemCount() {
            return (arrayList != null ? arrayList.size() : 0);
        }

        public class MainScheduleViewHolder extends RecyclerView.ViewHolder {
            TextView tv_schedule_content;
            public MainScheduleViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_schedule_content = itemView.findViewById(R.id.tv_schedule_content);
            }
        }
    }
}
