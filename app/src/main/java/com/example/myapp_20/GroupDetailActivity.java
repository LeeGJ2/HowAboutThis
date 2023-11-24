package com.example.myapp_20;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class GroupDetailActivity extends AppCompatActivity
{
    ImageView iv_home;
    ImageView iv_schedule;
    ImageView iv_people;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_detail);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        GroupDetailHomeFragment detailHomeFragment = new GroupDetailHomeFragment();
        detailHomeFragment.setGroupId(getIntent().getExtras().getString("groupId"));
        fragmentTransaction.replace(R.id.frameLayout, detailHomeFragment);
        fragmentTransaction.commit();

        iv_home = findViewById(R.id.iv_home);
        iv_schedule = findViewById(R.id.iv_schedule);
        iv_people = findViewById(R.id.iv_people);

        iv_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                GroupDetailHomeFragment groupDetailHomeFragment = new GroupDetailHomeFragment();
                groupDetailHomeFragment.setGroupId(getIntent().getExtras().getString("groupId"));
                transaction.replace(R.id.frameLayout, groupDetailHomeFragment);
                transaction.commit();
            }
        });

        iv_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                GroupDetailScheduleFragment groupDetailScheduleFragment = new GroupDetailScheduleFragment();
                groupDetailScheduleFragment.setGroupId(getIntent().getExtras().getString("groupId"));
                transaction.replace(R.id.frameLayout, groupDetailScheduleFragment);
                transaction.commit();
            }
        });

        iv_people.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                GroupDetailPeopleFragment groupDetailPeopleFragment = new GroupDetailPeopleFragment();
                groupDetailPeopleFragment.setGroupId(getIntent().getExtras().getString("groupId"));
                transaction.replace(R.id.frameLayout, groupDetailPeopleFragment);
                transaction.commit();

            }
        });
    }
}