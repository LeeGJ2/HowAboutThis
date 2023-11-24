package com.example.myapp_20;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class PostFragment extends Fragment
{
    private View view;
    private Button btn_every;
    private Button btn_contest;
    private Button btn_external;
    private Button btn_hobby;
    private Button btn_study;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        view = inflater.inflate(R.layout.fragment_post, container, false);
        btn_every = view.findViewById(R.id.but_every);
        btn_contest = view.findViewById(R.id.but_contest);
        btn_external = view.findViewById(R.id.but_external);
        btn_hobby = view.findViewById(R.id.but_hobby);
        btn_study = view.findViewById(R.id.but_study);


        btn_every.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Intent intent = new Intent(getActivity(), Community.class);
                intent.putExtra("CommunityName", btn_every.getText().toString());
                startActivity(intent);
            }
        });

        btn_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Community.class);
                intent.putExtra("CommunityName", btn_contest.getText().toString());
                startActivity(intent);
            }
        });

        btn_external.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Community.class);
                intent.putExtra("CommunityName", btn_external.getText().toString());
                startActivity(intent);
            }
        });

        view.findViewById(R.id.but_hobby).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Community.class);
                intent.putExtra("CommunityName", btn_hobby.getText().toString());
                startActivity(intent);
            }
        });

        view.findViewById(R.id.but_study).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), Community.class);
                intent.putExtra("CommunityName", btn_study.getText().toString());
                startActivity(intent);
            }
        });

        return view;
    }
}
