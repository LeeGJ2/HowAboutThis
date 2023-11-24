package com.example.myapp_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Write extends AppCompatActivity
{

    private FirebaseAuth firebaseAuth;
    private DatabaseReference mDatabaseRef;
    private Button btn_upload;
    private EditText et_title, et_group, et_str, et_people;
    private Intent curintent;
    private UserAcc userAcc;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        curintent = getIntent();
        setContentView(R.layout.activity_write);

        firebaseAuth = FirebaseAuth.getInstance();

        mDatabaseRef = FirebaseDatabase.getInstance().getReference("HowAboutThis");

        btn_upload = findViewById(R.id.btn_upload);


        et_title = findViewById(R.id.et_title);
        et_group = findViewById(R.id.et_group);
        et_str = findViewById(R.id.et_str);
        et_people = findViewById(R.id.et_people);


        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        mDatabaseRef.child("UserAccount").child(firebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener()
        {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot)
            {
                userAcc = snapshot.getValue(UserAcc.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error)
            {
                Log.e("Write", String.valueOf(error.toException()));
            }
        });



        btn_upload.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                long now = System.currentTimeMillis();
                Date date = new Date(now);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy-MM-dd hh:mm");
                String gettime = simpleDateFormat.format(date);
                WriteInfo writeInfo = new WriteInfo();
                GroupInfo groupInfo = new GroupInfo();
                if(Integer.valueOf(et_people.getText().toString()) >= 2) {
                    if (firebaseAuth.getCurrentUser() != null) {
                        DatabaseReference greference = mDatabaseRef.child("Group").push();
                        DatabaseReference wreference = mDatabaseRef.child("Write").push();

                        groupInfo.setGroupId(greference.getKey().toString());
                        groupInfo.users.put(firebaseAuth.getCurrentUser().getUid(), true);
                        groupInfo.setMaxPeople(Integer.valueOf(et_people.getText().toString()));
                        groupInfo.setCurPeople(1);
                        groupInfo.setGroupName(et_group.getText().toString());
                        groupInfo.setGrouping(false);
                        groupInfo.setTime(gettime);

                        writeInfo.setWriteId(wreference.getKey().toString());
                        writeInfo.setGroupId(greference.getKey().toString());
                        writeInfo.setTitle(et_title.getText().toString());
                        writeInfo.setDetail(et_str.getText().toString());
                        writeInfo.setGroup(et_group.getText().toString());
                        writeInfo.setPost(curintent.getExtras().getString("postname"));
                        writeInfo.setWriter(userAcc.getNickname());
                        writeInfo.setWriterUid(userAcc.getIdToken());
                        writeInfo.setCurPeople(1);
                        writeInfo.setMaxPeople(Integer.valueOf(et_people.getText().toString()));
                        writeInfo.setTime(gettime);


                        greference.setValue(groupInfo);

                        wreference.setValue(writeInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(Write.this, "업로드 완료", Toast.LENGTH_SHORT).show();
                            }
                        });
                        setResult(0);
                        finish();
                    }
                }
                else
                {
                    Toast.makeText(Write.this, "2명 이상의 인원을 입력하세요", Toast.LENGTH_SHORT).show();
                }


            }
        });

    }
}