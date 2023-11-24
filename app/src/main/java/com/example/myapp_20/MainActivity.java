package com.example.myapp_20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

public class MainActivity extends AppCompatActivity {

    private Button but_home;
    private Button but_post;
    private Button but_alarm;
    private Button but_schedule;
    private Button but_talk;
    private CalendarView calendarView;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        HomeFragment homeFragment = new HomeFragment();
        fragmentTransaction.replace(R.id.frameLayout, homeFragment);
        fragmentTransaction.commit();

        but_home = findViewById(R.id.btn_home);
        but_post = findViewById(R.id.btn_post);
        but_schedule = findViewById(R.id.btn_schedule);
        but_alarm = findViewById(R.id.btn_group);
        but_talk = findViewById(R.id.btn_talk);



        but_home.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                HomeFragment homeFragment = new HomeFragment();
                transaction.replace(R.id.frameLayout, homeFragment);
                transaction.commit();

            }
        });

        but_post.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                PostFragment postFragment = new PostFragment();
                transaction.replace(R.id.frameLayout, postFragment);
                transaction.commit();


            }
        });

        but_schedule.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                ScheduleFragment scheduleFragment = new ScheduleFragment();
                transaction.replace(R.id.frameLayout, scheduleFragment);
                transaction.commit();
            }
        });

        but_alarm.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                GroupFragment groupFragment = new GroupFragment();
                transaction.replace(R.id.frameLayout, groupFragment);
                transaction.commit();
            }
        });

        but_talk.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                TalkFragment talkFragment = new TalkFragment();
                transaction.replace(R.id.frameLayout, talkFragment);
                transaction.commit();
            }
        });

    }
}