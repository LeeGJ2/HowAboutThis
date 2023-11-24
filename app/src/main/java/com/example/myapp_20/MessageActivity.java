package com.example.myapp_20;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    Map<String, UserAcc> users = new HashMap<>();
    String roomId;
    EditText et_message;
    TextView tv_room;
    String uid;

    private FirebaseAuth firebaseAuth;
    private DatabaseReference databaseReference;
    private ValueEventListener valueEventListener;
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    private RecyclerView rcv_message;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_message);
        et_message = findViewById(R.id.et_message);
        tv_room = findViewById(R.id.tv_room);
        ImageView iv_message = findViewById(R.id.iv_message);
        rcv_message = findViewById(R.id.rcv_message);
        layoutManager = new LinearLayoutManager(this);
        rcv_message.setLayoutManager(layoutManager);

        tv_room.setText(getIntent().getExtras().getString("roomName"));
        roomId = getIntent().getExtras().getString("roomId");


        databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");
        firebaseAuth = FirebaseAuth.getInstance();
        uid = firebaseAuth.getCurrentUser().getUid();
        rcv_message.setAdapter(new MessageAdapter());

        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                rcv_message.setAdapter(new MessageAdapter());
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                rcv_message.setAdapter(new MessageAdapter());
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

        iv_message.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                ChatModel.MyComment comment = new ChatModel.MyComment();
                comment.uid = firebaseAuth.getCurrentUser().getUid();
                comment.message = et_message.getText().toString();
                comment.timestamp = ServerValue.TIMESTAMP;

                System.out.println(roomId);
                databaseReference.child("Chatroom").child(roomId).child("comments").push().setValue(comment).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        et_message.setText("");
                        rcv_message.setAdapter(new MessageAdapter());
                    }
                });


            }
        });

    }

    class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>
    {
        ArrayList<ChatModel.MyComment> comments;
        Context context;

        public MessageAdapter()
        {
            comments = new ArrayList<>();
            FirebaseDatabase.getInstance().getReference("HowAboutThis").child("Chatroom").child(roomId).child("comments").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    comments.clear();
                    for(DataSnapshot item : snapshot.getChildren())
                    {
                        comments.add(item.getValue(ChatModel.MyComment.class));
                    }

                    //메세지가 갱신
                    notifyDataSetChanged();
                    rcv_message.scrollToPosition(comments.size()-1);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }

        @NonNull
        @Override
        public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.message_item, parent, false);
            MessageViewHolder messageViewHolder = new MessageViewHolder(view);
            return messageViewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
            holder.tv_send_message.setText(comments.get(position).message);


            ChatModel.MyComment myComment = comments.get(position);
            FirebaseDatabase.getInstance().getReference("HowAboutThis").child("UserAccount").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot)
                {
                    for (DataSnapshot item : snapshot.getChildren()) {
                        UserAcc userAcc = item.getValue(UserAcc.class);
                        if (userAcc.getIdToken().equals(myComment.uid))
                        {
                            holder.tv_sender.setText(userAcc.getNickname());
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

            if(comments.get(position).uid.equals(uid))
            {
                holder.tv_send_message.setBackgroundResource(R.drawable.bubble_right);
                holder.linearLayout_destination.setVisibility(View.INVISIBLE);
                holder.linearLayout_main.setGravity(Gravity.RIGHT);
            }
            else
            {
                holder.tv_send_message.setBackgroundResource(R.drawable.bubble_left);
                holder.linearLayout_destination.setVisibility(View.VISIBLE);
                holder.linearLayout_main.setGravity(Gravity.LEFT);
            }
        }

        @Override
        public int getItemCount() {
            return comments.size();
        }

        public class MessageViewHolder extends RecyclerView.ViewHolder {
            public TextView tv_sender;
            public TextView tv_send_message;
            public LinearLayout linearLayout_destination;
            public LinearLayout linearLayout_main;
            public MessageViewHolder(@NonNull View itemView) {
                super(itemView);
                tv_sender = itemView.findViewById(R.id.tv_sender);
                tv_send_message = itemView.findViewById(R.id.tv_send_message);
                linearLayout_destination = itemView.findViewById(R.id.linearlayout_destination);
                linearLayout_main = itemView.findViewById(R.id.linearlayout_main);
            }
        }
    }


}