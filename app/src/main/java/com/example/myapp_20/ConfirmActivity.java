package com.example.myapp_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ConfirmActivity extends AppCompatActivity {
    private Button btn_confirm;
    private Button btn_refuse;
    private DatabaseReference databaseReference;
    private String groupId;
    private String applicantId;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm);

        groupId = getIntent().getExtras().getString("groupId");
        applicantId = getIntent().getExtras().getString("applicantId");

        btn_confirm = findViewById(R.id.btn_confirm);
        btn_refuse = findViewById(R.id.btn_refuese);

        databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");

        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseReference.child("Application").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot aItem : snapshot.getChildren())
                        {
                            Application application = aItem.getValue(Application.class);
                            if(application.getGroupId().equals(groupId) && application.getApplicantId().equals(applicantId))
                            {
                                DatabaseReference aDatabaseRef = aItem.getRef();
                                databaseReference.child("Group").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot)
                                    {
                                        for(DataSnapshot gItem : snapshot.getChildren())
                                        {
                                            if(gItem.getKey().equals(groupId))
                                            {
                                                DatabaseReference gDatabaseRef = gItem.getRef();
                                                GroupInfo groupInfo = gItem.getValue(GroupInfo.class);
                                                groupInfo.users.put(applicantId, true);
                                                groupInfo.setCurPeople(groupInfo.getCurPeople()+1);
                                                if(groupInfo.getMaxPeople() == groupInfo.getCurPeople())
                                                {
                                                    groupInfo.setGrouping(true);
                                                    ChatModel chatModel = new ChatModel();
                                                    chatModel.setChatTitle(groupInfo.getGroupName());
                                                    chatModel.users = groupInfo.users;
                                                    DatabaseReference cDatabaseRef = databaseReference.child("Chatroom").push();
                                                    chatModel.setRoomId(cDatabaseRef.getKey().toString());
                                                    cDatabaseRef.setValue(chatModel);
                                                }
                                                gDatabaseRef.setValue(groupInfo);

                                                databaseReference.child("Write").addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot s2) {
                                                        for(DataSnapshot dataSnapshot2 : s2.getChildren())
                                                        {
                                                            WriteInfo writeInfo = dataSnapshot2.getValue(WriteInfo.class);
                                                            if(writeInfo.getGroupId().equals(groupId))
                                                            {
                                                                writeInfo.setCurPeople(writeInfo.getCurPeople()+1);
                                                                DatabaseReference wDatabaseRef = dataSnapshot2.getRef();
                                                                wDatabaseRef.setValue(writeInfo);
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError error) {

                                                    }
                                                });

                                            }
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                                aDatabaseRef.removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                finish();
            }
        });

        btn_refuse.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                databaseReference.child("Application").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            Application application = dataSnapshot.getValue(Application.class);
                            if(application.getGroupId().equals(groupId) && applicantId.equals(application.getApplicantId()))
                            {
                                DatabaseReference rdatabaseRef = dataSnapshot.getRef();
                                rdatabaseRef.removeValue();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
                finish();
            }
        });
    }
}