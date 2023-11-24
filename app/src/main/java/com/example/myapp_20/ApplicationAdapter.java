package com.example.myapp_20;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ApplicationAdapter extends RecyclerView.Adapter<ApplicationAdapter.ApplicationViewHolder> {

    private ArrayList<Application> arrayList;
    private Context context;

    public ApplicationAdapter(ArrayList<Application> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public ApplicationAdapter.ApplicationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_detail_item, parent, false);
        ApplicationAdapter.ApplicationViewHolder applicationViewHolder = new ApplicationAdapter.ApplicationViewHolder(view);
        return applicationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApplicationAdapter.ApplicationViewHolder holder, int position) {
        holder.tv_applicant.setText(arrayList.get(position).getApplicant());
        holder.tv_applymessage.setText(arrayList.get(position).getApplyMessage());
        holder.groupId = arrayList.get(position).getGroupId();
        holder.applicantId = arrayList.get(position).getApplicantId();
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class ApplicationViewHolder extends RecyclerView.ViewHolder
    {
        private TextView tv_applicant;
        private TextView tv_applymessage;
        private DatabaseReference databaseReference;
        private String groupId;
        private String applicantId;
        public ApplicationViewHolder(@NonNull View itemView)
        {
            super(itemView);
            databaseReference = FirebaseDatabase.getInstance().getReference("HowAboutThis");
            tv_applicant = itemView.findViewById(R.id.tv_applicant);
            tv_applymessage = itemView.findViewById(R.id.tv_applymessage);
            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, ConfirmActivity.class);
                    intent.putExtra("groupId", groupId);
                    intent.putExtra("applicantId", applicantId);
                    context.startActivity(intent);
                }
            });
        }
    }
}
