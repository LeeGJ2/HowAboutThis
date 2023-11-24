package com.example.myapp_20;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class Community extends AppCompatActivity
{

    private DatabaseReference databaseReference;
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private Button but_write;
    private TextView  tv_commnuityname;
    private ImageView iv_recycle;
    private ArrayList<WriteInfo> arrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        tv_commnuityname = findViewById(R.id.tv_communityname);
        tv_commnuityname.setText(getIntent().getExtras().getString("CommunityName"));
        but_write = findViewById(R.id.btn_write);
        recyclerView = findViewById(R.id.rcv_post);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();
        iv_recycle = findViewById(R.id.iv_recycle);

        databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");

        databaseReference.child("Write").addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                arrayList.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    WriteInfo writeInfo = dataSnapshot.getValue(WriteInfo.class);
                    if(writeInfo.getPost().equals((getIntent().getExtras().getString("CommunityName"))))
                        arrayList.add(0, writeInfo);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("Community", String.valueOf(error.toException()));

            }
        });



        adapter = new PostAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);


        ActivityResultLauncher<Intent> launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult o)
            {
                if(o.getResultCode() == 0)
                {
                    databaseReference.child("Write").addListenerForSingleValueEvent(new ValueEventListener()
                    {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot)
                        {
                            arrayList.clear();
                            for(DataSnapshot dataSnapshot : snapshot.getChildren())
                            {
                                WriteInfo writeInfo = dataSnapshot.getValue(WriteInfo.class);
                                if(writeInfo.getPost().equals((getIntent().getExtras().getString("CommunityName"))))
                                    arrayList.add(0, writeInfo);
                            }
                            adapter.notifyDataSetChanged();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Log.e("Community", String.valueOf(error.toException()));

                        }
                    });
                    adapter = new PostAdapter(arrayList, Community.this);
                    recyclerView.setAdapter(adapter);
                }
            }
        });

        but_write.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(Community.this, Write.class);
                intent.putExtra("postname", getIntent().getExtras().getString("CommunityName"));
                launcher.launch(intent);

            }
        });



        iv_recycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                databaseReference.child("Write").addListenerForSingleValueEvent(new ValueEventListener()
                {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        arrayList.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            WriteInfo writeInfo = dataSnapshot.getValue(WriteInfo.class);
                            if(writeInfo.getPost().equals((getIntent().getExtras().getString("CommunityName"))))
                                arrayList.add(0, writeInfo);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Log.e("Community", String.valueOf(error.toException()));

                    }
                });
                adapter = new PostAdapter(arrayList, Community.this);
                recyclerView.setAdapter(adapter);
            }
        });

    }

}