package com.example.trialapp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.trialapp3.Models.chat_model;
import com.example.trialapp3.adaptation.messageAdapter;
import com.example.trialapp3.databinding.ActivityGroupChatBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;

public class GroupChat extends AppCompatActivity {

    ActivityGroupChatBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityGroupChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GroupChat.this,MainActivity.class);
                startActivity(intent);
            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final ArrayList<chat_model> chatmodels= new ArrayList<>();
        final String senderID= FirebaseAuth.getInstance().getUid();
        binding.USERrecycleview.setText("grp chat");

        final messageAdapter chatadapter= new messageAdapter(chatmodels,this);
        binding.chatboxrecycler.setAdapter(chatadapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatboxrecycler.setLayoutManager(layoutManager);

        database.getReference().child("grp chat").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                chatmodels.clear();
                for(DataSnapshot dataSnapshot:snapshot.getChildren()){
                    chat_model model= dataSnapshot.getValue(chat_model.class);
                    chatmodels.add(model);
                }
                chatadapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        binding.sendbttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String mesg=binding.entermsg.getText().toString();
                final chat_model model= new chat_model(senderID,mesg);
                model.setTimestamp(new Date().getTime());
                binding.entermsg.setText("");
                database.getReference().child("grp chat").push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                    }
                });
            }
        });
    }
}