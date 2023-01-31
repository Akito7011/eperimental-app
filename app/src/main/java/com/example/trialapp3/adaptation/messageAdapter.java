package com.example.trialapp3.adaptation;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.trialapp3.Models.chat_model;
import com.example.trialapp3.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class messageAdapter extends RecyclerView.Adapter{


    ArrayList<chat_model> chat_models;
    Context context;
    String recID;

    int sender_view_type=1;
    int receiver_view_type=2;

    public messageAdapter(ArrayList<chat_model> chat_models, Context context) {
        this.chat_models = chat_models;
        this.context = context;
    }

    public messageAdapter(ArrayList<chat_model> chat_models, Context context, String recID) {
        this.chat_models = chat_models;
        this.context = context;
        this.recID = recID;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType==sender_view_type){
            View view= LayoutInflater.from(context).inflate(R.layout.test_sender,parent,false);
            return new SenderChatHolder(view);
        }
        else{
            View view=LayoutInflater.from(context).inflate(R.layout.test_reciever,parent,false);
            return new ReceiverChatHolder(view);
        }
    }

    @Override
    public int getItemViewType(int position) {

    if(chat_models.get(position).getUserId().equals(FirebaseAuth.getInstance().getUid())){
        return sender_view_type;
    }
    else {
        return receiver_view_type;
    }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        chat_model chat_model= chat_models.get(position);


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                new AlertDialog.Builder(context).setTitle("Delete").setMessage("are you sure , you want to delete this message?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                FirebaseDatabase firebase = FirebaseDatabase.getInstance();
                                String senderRoom = FirebaseAuth.getInstance().getUid() + recID;
                                firebase.getReference().child("chats").child(senderRoom).child(chat_model.getMessageID())
                                        .setValue(null);

                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

                return false;
            }
        });




        if(holder.getClass()==SenderChatHolder.class){
            ((SenderChatHolder)holder).sender_msg.setText(chat_model.getMessage());
            Date date = new Date(chat_model.getTimestamp());
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat(" YYYY-MM-dd h:mm a");
            String strdate= simpleDateFormat.format(date);
            ((SenderChatHolder)holder).sender_time.setText(strdate.toString());

        }
        else {
            ((ReceiverChatHolder)holder).receiver_msg.setText(chat_model.getMessage());
            Date date = new Date(chat_model.getTimestamp());
            SimpleDateFormat simpleDateFormat= new SimpleDateFormat(" YYYY-MM-dd h:mm a");
            String strdate= simpleDateFormat.format(date);
            ((ReceiverChatHolder)holder).receiver_time.setText(strdate.toString());
        }

    }

    @Override
    public int getItemCount() {
        return chat_models.size();
    }

    public class ReceiverChatHolder extends RecyclerView.ViewHolder{
        TextView receiver_msg,receiver_time;


        public ReceiverChatHolder(@NonNull View itemView) {
            super(itemView);

            receiver_msg = itemView.findViewById(R.id.recive_text);
            receiver_time=itemView.findViewById(R.id.recive_time);
        }
    }
    public class SenderChatHolder extends RecyclerView.ViewHolder{
        TextView sender_msg, sender_time;

        public SenderChatHolder(@NonNull View itemView) {
            super(itemView);
            sender_msg=itemView.findViewById(R.id.send_chat);
            sender_time=itemView.findViewById(R.id.send_time);


        }
    }

}
