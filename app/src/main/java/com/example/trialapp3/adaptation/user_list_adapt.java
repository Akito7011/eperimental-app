package com.example.trialapp3.adaptation;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trialapp3.Models.users;
import com.example.trialapp3.R;
import com.example.trialapp3.chatwindow;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class user_list_adapt extends RecyclerView.Adapter<user_list_adapt.ViewHolder> {

    ArrayList<users>list;
    Context context;

    public user_list_adapt(ArrayList<users> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.testing_users_list,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        users Users= list.get(position);
        Picasso.get().load(Users.getPfp()).placeholder(R.drawable.avatar3).into(holder.pfp);
        holder.userName.setText(Users.getUSERNAME());
        FirebaseDatabase.getInstance().getReference().child("chats").child(FirebaseAuth.getInstance()
                .getUid()+Users.getUserID()).orderByChild("timestamp").limitToLast(1)
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.hasChildren()){
                                    for (DataSnapshot snapshot1:snapshot.getChildren()){
                                        holder.lastMSG.setText(snapshot1.child("message").getValue().toString());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(context, chatwindow.class);
                intent.putExtra("userId",Users.getUserID());
                intent.putExtra("profileIMAGE",Users.getPfp());
                intent.putExtra("userName",Users.getUSERNAME());

                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView pfp;
        TextView userName, lastMSG;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
                pfp = itemView.findViewById(R.id.profileIMAGE);
                userName=itemView.findViewById(R.id.user_name);
                lastMSG=itemView.findViewById(R.id.last_msg);
        }
    }
}
