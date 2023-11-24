package com.example.myapp_20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApplicationActivity extends AppCompatActivity
{
    private DatabaseReference databaseReference;
    private RecyclerView rcv_applicaiton;
    private RecyclerView.Adapter adapter;
    private ArrayList<Application> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        arrayList = new ArrayList<>();

        setContentView(R.layout.activity_application);
        rcv_applicaiton = findViewById(R.id.rcv_application);
        rcv_applicaiton.setHasFixedSize(true);
        rcv_applicaiton.setLayoutManager(new LinearLayoutManager(this));

        databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                databaseReference.child("Application").addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            Application application = dataSnapshot.getValue(Application.class);
                            if(application.getGroupId().equals(getIntent().getExtras().getString("groupId")))
                            {
                                arrayList.add(application);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                adapter = new ApplicationAdapter(arrayList, ApplicationActivity.this);
                rcv_applicaiton.setAdapter(adapter);
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName)
            {
                databaseReference.child("Group").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot: snapshot.getChildren())
                        {
                            if(dataSnapshot.getKey().equals(getIntent().getExtras().getString("groupId")))
                            {
                                GroupInfo groupInfo = dataSnapshot.getValue(GroupInfo.class);
                                if(groupInfo.isGrouping())
                                {
                                    finish();
                                }
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                databaseReference.child("Application").addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            Application application = dataSnapshot.getValue(Application.class);
                            if(application.getGroupId().equals(getIntent().getExtras().getString("groupId")))
                            {
                                arrayList.add(application);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                adapter = new ApplicationAdapter(arrayList, ApplicationActivity.this);
                rcv_applicaiton.setAdapter(adapter);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
                databaseReference.child("Application").addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            Application application = dataSnapshot.getValue(Application.class);
                            if(application.getGroupId().equals(getIntent().getExtras().getString("groupId")))
                            {
                                arrayList.add(application);
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                adapter = new ApplicationAdapter(arrayList, ApplicationActivity.this);
                rcv_applicaiton.setAdapter(adapter);
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Application").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    arrayList.clear();
                    Application application = dataSnapshot.getValue(Application.class);
                    if(application.getGroupId().equals(getIntent().getExtras().getString("groupId")))
                    {
                        arrayList.add(application);
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new ApplicationAdapter(arrayList, this);
        rcv_applicaiton.setAdapter(adapter);

    }
}