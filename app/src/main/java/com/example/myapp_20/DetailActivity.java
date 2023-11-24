package com.example.myapp_20;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class DetailActivity extends AppCompatActivity
{
    private TextView tv_title;
    private TextView tv_name;
    private TextView tv_communityname;
    private TextView tv_time;
    private TextView tv_content;
    private TextView tv_curpeople;
    private EditText et_apply;
    private Button btn_apply;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private WriteInfo writeInfo;
    private GroupInfo groupInfo;
    private String groupId;
    private String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        tv_title = findViewById(R.id.tv_title);
        tv_name = findViewById(R.id.tv_name);
        tv_communityname = findViewById(R.id.tv_communityname);
        tv_time = findViewById(R.id.tv_time);
        tv_content = findViewById(R.id.tv_content);
        tv_curpeople = findViewById(R.id.tv_curpeople);

        et_apply = findViewById(R.id.et_apply);
        btn_apply = findViewById(R.id.btn_apply);
        firebaseAuth = FirebaseAuth.getInstance();


        databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");

        databaseReference.child("Write").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if (dataSnapshot.getKey().equals(getIntent().getExtras().getString("WriteId")))
                    {
                        writeInfo = dataSnapshot.getValue(WriteInfo.class);
                        tv_title.setText(writeInfo.getTitle());
                        tv_name.setText(writeInfo.getWriter());
                        tv_communityname.setText(writeInfo.getPost());
                        tv_time.setText(writeInfo.getTime());
                        tv_content.setText(writeInfo.getDetail());
                        groupId = writeInfo.getGroupId();
                        if(firebaseAuth.getCurrentUser().getUid().equals(writeInfo.getWriterUid())) {
                            et_apply.setEnabled(false);
                            et_apply.setVisibility(View.INVISIBLE);
                            btn_apply.setEnabled(false);
                            btn_apply.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("Group").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    if(dataSnapshot.getKey().equals(groupId))
                    {
                        groupInfo = dataSnapshot.getValue(GroupInfo.class);
                        for(int i = 0; i < groupInfo.users.size(); i++)
                        {
                            
                        }
                        tv_curpeople.setText(groupInfo.getCurPeople() + "/" + groupInfo.getMaxPeople());
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        databaseReference.child("UserAccount").child(firebaseAuth.getCurrentUser().getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                UserAcc acc = snapshot.getValue(UserAcc.class);
                Toast.makeText(DetailActivity.this, acc.getNickname(), Toast.LENGTH_SHORT);
                userName = acc.getNickname();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        btn_apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Application application = new Application();
                application.setApplicant(userName);
                application.setApplicantId(firebaseAuth.getUid());
                application.setGroupId(groupId);
                application.setApplyMessage(et_apply.getText().toString());
                databaseReference.child("Application").push().setValue(application);
                Toast.makeText(DetailActivity.this, "가입신청 완료", Toast.LENGTH_SHORT).show();
                finish();
            }
        });






    }
}