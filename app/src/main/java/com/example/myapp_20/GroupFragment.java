package com.example.myapp_20;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewTreeViewModelKt;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class GroupFragment extends Fragment
{

    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private RecyclerView recyclerView;
    private ArrayList<GroupInfo> arrayList;
    private RecyclerView.Adapter adapter;

    public GroupFragment() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        recyclerView = view.findViewById(R.id.rcv_group);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        arrayList = new ArrayList<>();
        databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");
        firebaseAuth = FirebaseAuth.getInstance();

        databaseReference.child("Group").orderByChild("users/"+firebaseAuth.getCurrentUser().getUid()).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                arrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    GroupInfo groupInfo = dataSnapshot.getValue(GroupInfo.class);
                    arrayList.add(groupInfo);
                }
                adapter.notifyDataSetChanged();

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("GroupFragment", String.valueOf(error.toException()));
            }
        });

        adapter = new GroupAdapter(arrayList, view.getContext());
        recyclerView.setAdapter(adapter);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                databaseReference.child("Group").orderByChild("users/"+firebaseAuth.getCurrentUser().getUid()).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            GroupInfo groupInfo = dataSnapshot.getValue(GroupInfo.class);
                            arrayList.add(groupInfo);
                        }
                        adapter.notifyDataSetChanged();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("GroupFragment", String.valueOf(error.toException()));
                    }
                });

                adapter = new GroupAdapter(arrayList, view.getContext());
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }
}