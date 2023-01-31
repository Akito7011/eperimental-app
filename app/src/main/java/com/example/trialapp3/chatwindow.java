package com.example.trialapp3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.trialapp3.Models.chat_model;
import com.example.trialapp3.adaptation.messageAdapter;
import com.example.trialapp3.databinding.ActivityChatwindowBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

public class chatwindow extends AppCompatActivity {

    ActivityChatwindowBinding binding;
    FirebaseDatabase firebase;
    FirebaseAuth FireAuth;
    FirebaseStorage storage;
    String checker="",urlmy="";
    Intent data1;
    Uri fileUri;
    StorageTask upload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatwindowBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getSupportActionBar().hide();
        firebase = FirebaseDatabase.getInstance();
        FireAuth = FirebaseAuth.getInstance();

        final String senderID = FireAuth.getUid();
        String receiverID = getIntent().getStringExtra("userId");
        String userName = getIntent().getStringExtra("userName");
        String profilePic = getIntent().getStringExtra("profileIMAGE");

        binding.USERrecycleview.setText(userName);
        Picasso.get().load(profilePic).placeholder(R.drawable.avatar3).into(binding.profileIMAGE);
        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(chatwindow.this, MainActivity.class);
                startActivity(intent);
            }
        });

        final ArrayList<chat_model> chatmodels= new ArrayList<>();
        final  messageAdapter chatadapter=new messageAdapter(chatmodels,this,receiverID);
        binding.chatboxrecycler.setAdapter(chatadapter);
        LinearLayoutManager layoutManager=new LinearLayoutManager(this);
        binding.chatboxrecycler.setLayoutManager(layoutManager);
        final String senderRoom= senderID+receiverID;
        final String receiverRoom= receiverID+senderID;

        firebase.getReference().child("chats").child(senderRoom).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                chatmodels.clear();
                for (DataSnapshot snapshot1:snapshot.getChildren()){
                    chat_model model= snapshot1.getValue(chat_model.class);
                    model.setMessageID(snapshot1.getKey());
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
                if (binding.entermsg.getText().toString().isEmpty()) {
                    Toast.makeText(chatwindow.this, "No message to send", Toast.LENGTH_SHORT).show();

                }
                else {
                    String message = binding.entermsg.getText().toString();
                    final chat_model model = new chat_model(senderID, message);
                    model.setTimestamp(new Date().getTime());
                    binding.entermsg.setText("");
                    firebase.getReference().child("chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            firebase.getReference().child("chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                }
                            });
                        }
                    });

                }
            }
        });
        binding.senditem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CharSequence options[]= new CharSequence[]{

                        "Image",
                        "Pdf File",
                        "Others",
                };
                AlertDialog.Builder builder= new AlertDialog.Builder(chatwindow.this);
                builder.setTitle("Select File");

                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(which==0){
                            checker="Image";
                            Intent intent=new Intent();
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(Intent.createChooser(intent,"select image"),438);
                        }
                        if (which==1){
                            checker="Pdf File";

                        }
                        if (which==2){
                            checker="Others";

                        }

                    }
                }).show();


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==438 && resultCode==RESULT_OK && data!=null && data.getData()!=null){

            fileUri=data.getData();
            if(!checker.equals("Image")){

            }
            else if (checker.equals("Image")){

                StorageReference storageReference=FirebaseStorage.getInstance().getReference().child("Image Files");
                String message = binding.senditem.toString();
                final String senderID = FireAuth.getUid();
                String receiverID = getIntent().getStringExtra("userId");
                final chat_model model = new chat_model(senderID, message);
                final String senderRoom= senderID+receiverID;
                final String receiverRoom= receiverID+senderID;
                model.setTimestamp(new Date().getTime());
                firebase.getReference().child("chats").child(senderRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        firebase.getReference().child("chats").child(receiverRoom).push().setValue(model).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {

                            }
                        });
                    }
                });





            }
            else {
                Toast.makeText(this, "Zero item selected", Toast.LENGTH_SHORT).show();
            }

        }
    }
}