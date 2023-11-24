package com.example.myapp_20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    private ImageView iv_main_home;
    private ImageView iv_main_post;
    private ImageView iv_main_schedule;
    private ImageView iv_main_group;
    private ImageView iv_main_talk;

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

        iv_main_home = findViewById(R.id.iv_main_home);
        iv_main_post = findViewById(R.id.iv_main_post);
        iv_main_schedule = findViewById(R.id.iv_main_schedule);
        iv_main_talk = findViewById(R.id.iv_main_talk);
        iv_main_group = findViewById(R.id.iv_main_group);



        iv_main_home.setOnClickListener(new View.OnClickListener()
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

        iv_main_post.setOnClickListener(new View.OnClickListener()
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

        iv_main_schedule.setOnClickListener(new View.OnClickListener()
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

        iv_main_group.setOnClickListener(new View.OnClickListener()
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

        iv_main_talk.setOnClickListener(new View.OnClickListener()
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