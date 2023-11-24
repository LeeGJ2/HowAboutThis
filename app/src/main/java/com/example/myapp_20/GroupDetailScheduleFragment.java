package com.example.myapp_20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.zip.Inflater;

public class GroupDetailScheduleFragment extends Fragment
{
    private DatabaseReference databaseReference;
    private CalendarView cv_group;
    private TextView tv_selectday;
    private String day;
    private Button btn_add_schedule;
    private RecyclerView rcv_schedule;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ScheduleInfo> arrayList;
    private String groupId;

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public GroupDetailScheduleFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_group_detail_schedule, container, false);

        databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");

        arrayList = new ArrayList<>();
        cv_group = view.findViewById(R.id.cv_group);
        tv_selectday = view.findViewById(R.id.tv_selectday);
        btn_add_schedule = view.findViewById(R.id.btn_add_schedule);
        rcv_schedule = view.findViewById(R.id.rcv_schedule);
        layoutManager = new LinearLayoutManager(getContext());
        rcv_schedule.setLayoutManager(layoutManager);
        rcv_schedule.setHasFixedSize(true);

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        day = dateFormat.format(cv_group.getDate());
        tv_selectday.setText(day);

        databaseReference.child("Schedule").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                arrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    ScheduleInfo scheduleInfo = dataSnapshot.getValue(ScheduleInfo.class);
                    if(scheduleInfo.getDate().equals(day) && scheduleInfo.getGroupId().equals(groupId))
                    {
                        arrayList.add(scheduleInfo);
                        adapter.notifyDataSetChanged();
                    }
                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        adapter = new ScheduleAdapter(getContext(), arrayList);
        rcv_schedule.setAdapter(adapter);

        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o)
            {
                if(o.getResultCode() == 0)
                {
                }
            }
        });


        cv_group.setOnDateChangeListener(new CalendarView.OnDateChangeListener()
        {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth)
            {
                tv_selectday.setText(String.format("%d-%d-%d", year, month+1, dayOfMonth));
                day = String.format("%d-%d-%d", year, month+1, dayOfMonth);

                databaseReference.child("Schedule").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            ScheduleInfo scheduleInfo = dataSnapshot.getValue(ScheduleInfo.class);
                            if(scheduleInfo.getDate().equals(day) && scheduleInfo.getGroupId().equals(groupId))
                            {
                                arrayList.add(scheduleInfo);

                            }
                        }
                        adapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                adapter = new ScheduleAdapter(getContext(), arrayList);
                rcv_schedule.setAdapter(adapter);
            }
        });


        btn_add_schedule.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), AddSchedule.class);
                intent.putExtra("groupId", groupId);
                intent.putExtra("day", day);
                launcher.launch(intent);
            }
        });
        return view;
    }

    public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ScheduleViewHolder>
    {
        private Context context;
        private ArrayList<ScheduleInfo> arrayList;

        public ScheduleAdapter(Context context, ArrayList<ScheduleInfo> arrayList) {
            this.context = context;
            this.arrayList = arrayList;
        }

        @NonNull
        @Override
        public ScheduleAdapter.ScheduleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.schedule_item, parent, false);
            ScheduleViewHolder scheduleViewHolder = new ScheduleViewHolder(view);
            return scheduleViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ScheduleAdapter.ScheduleViewHolder holder, int position) {
            holder.tv_schedule_content.setText("· "+arrayList.get(position).getContents());
        }

        @Override
        public int getItemCount(){
            return (arrayList != null ? arrayList.size() : 0);
        }

        public class ScheduleViewHolder extends RecyclerView.ViewHolder
        {
             TextView tv_schedule_content;
            public ScheduleViewHolder(@NonNull View itemView)
            {
                super(itemView);
                tv_schedule_content = itemView.findViewById(R.id.tv_schedule_content);
            }
        }
    }

}
