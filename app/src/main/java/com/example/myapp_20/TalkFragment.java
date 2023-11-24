package com.example.myapp_20;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.checkerframework.checker.units.qual.A;

import java.security.AccessController;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class TalkFragment extends Fragment
{

    public TalkFragment() {
    }

    private RecyclerView rcv_chatroom;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private DatabaseReference databaseReference;
    private FirebaseAuth firebaseAuth;
    private ArrayList<ChatModel> list;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState)
    {
        View view = inflater.inflate(R.layout.fragment_talk,container, false);
        rcv_chatroom = view.findViewById(R.id.rcv_chatroom);
        layoutManager = new LinearLayoutManager(getContext());
        rcv_chatroom.setLayoutManager(layoutManager);

        list = new ArrayList<>();

        firebaseAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");
        databaseReference.child("Chatroom").orderByChild("users/"+firebaseAuth.getCurrentUser().getUid()).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot dataSnapshot : snapshot.getChildren())
                {
                    list.add(dataSnapshot.getValue(ChatModel.class));
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        adapter = new ChatAdapter(list, getContext());
        rcv_chatroom.setAdapter(adapter);


        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                databaseReference.child("Chatroom").orderByChild("users/"+firebaseAuth.getCurrentUser().getUid()).equalTo(true).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        list.clear();
                        for(DataSnapshot dataSnapshot : snapshot.getChildren())
                        {
                            list.add(dataSnapshot.getValue(ChatModel.class));
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

                adapter = new ChatAdapter(list, getContext());
                rcv_chatroom.setAdapter(adapter);
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

    class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>
    {
        private ArrayList<ChatModel> chatModels;
        private Context context;

        public ChatAdapter(ArrayList<ChatModel> chatModels, Context context)
        {
            this.chatModels = chatModels;
            this.context = context;
        }

        @NonNull
        @Override
        public ChatAdapter.ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_item, parent, false);
            ChatViewHolder chatViewHolder = new ChatViewHolder(view);
            return chatViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ChatAdapter.ChatViewHolder holder, int position) {
            holder.tv_chat_title.setText(chatModels.get(position).getChatTitle());
            holder.roomid = chatModels.get(position).getRoomId();
            Map<String, ChatModel.MyComment> commentMap = new TreeMap<>(Collections.reverseOrder());
            commentMap.putAll(chatModels.get(position).comments);
            if(commentMap.keySet().toArray().length >0)
            {
                String lastMessageKey = (String) commentMap.keySet().toArray()[0];
                holder.tv_last_message.setText(chatModels.get(position).comments.get(lastMessageKey).message);
            }
            else
            {
                holder.tv_last_message.setText("");
            }


        }

        @Override
        public int getItemCount() {
            return chatModels.size();
        }

        public class ChatViewHolder extends RecyclerView.ViewHolder
        {
            TextView tv_chat_title;
            TextView tv_last_message;
            String roomid;
            public ChatViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_chat_title = itemView.findViewById(R.id.tv_chat_title);
                tv_last_message = itemView.findViewById(R.id.tv_last_message);
                itemView.setOnClickListener(new View.OnClickListener()
                {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, MessageActivity.class);
                        intent.putExtra("roomName", tv_chat_title.getText().toString());
                        intent.putExtra("roomId", roomid);
                        startActivity(intent);
                    }
                });
            }
        }
    }
}
