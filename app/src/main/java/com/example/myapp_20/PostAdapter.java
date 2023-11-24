package com.example.myapp_20;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    private ArrayList<WriteInfo> arrayList;
    private Context context;

    public PostAdapter(ArrayList<WriteInfo> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.post_item, parent, false);
        PostViewHolder postViewHolder = new PostViewHolder(view);

        return postViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, int position)
    {
        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_content.setText(arrayList.get(position).getDetail());
        holder.tv_time.setText(arrayList.get(position).getTime());
        holder.id = arrayList.get(position).getWriteId();
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PostViewHolder extends RecyclerView.ViewHolder
    {
        TextView tv_title;
        TextView tv_content;
        TextView tv_time;
        String id;
        public PostViewHolder(@NonNull View itemView)
        {
            super(itemView);
            this.tv_title = itemView.findViewById(R.id.tv_groupname);
            this.tv_content = itemView.findViewById(R.id.tv_schedule_content);
            this.tv_time = itemView.findViewById(R.id.tv_people);

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    Intent intent = new Intent(context, DetailActivity.class);
                    intent.putExtra("WriteId", id);
                    context.startActivity(intent);
                }
            });
        }
    }
}
