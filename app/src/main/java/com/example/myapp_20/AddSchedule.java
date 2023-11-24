package com.example.myapp_20;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class AddSchedule extends AppCompatActivity {

    private EditText et_date_content;
    private DatabaseReference databaseReference;
    private Button btn_add;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_schedule);
        databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");
        et_date_content = findViewById(R.id.et_date_content);
        btn_add = findViewById(R.id.btn_add);

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScheduleInfo scheduleInfo =  new ScheduleInfo();
                scheduleInfo.setContents(et_date_content.getText().toString());
                scheduleInfo.setDate(getIntent().getExtras().getString("day"));
                scheduleInfo.setGroupId(getIntent().getExtras().getString("groupId"));
                databaseReference.child("Schedule").push().setValue(scheduleInfo);
                setResult(0);
                finish();

            }
        });
    }
}