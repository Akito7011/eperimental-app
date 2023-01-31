package com.example.trialapp3;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.trialapp3.Models.users;
import com.example.trialapp3.databinding.ActivitySettingsBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class settings extends AppCompatActivity {

    ActivitySettingsBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase firebase;
    FirebaseStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySettingsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        getSupportActionBar().hide();
        storage= FirebaseStorage.getInstance();
        mAuth=FirebaseAuth.getInstance();
        firebase=FirebaseDatabase.getInstance();

        binding.backArrow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(settings.this, MainActivity.class);
                startActivity(intent);
            }
        });
        binding.savebttn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.status.getText().toString().equals(null)&& !binding.txtUSERNAME.getText().toString().equals(null)){

                    String status=binding.status.getText().toString();
                    String Username=binding.txtUSERNAME.getText().toString();
                    HashMap<String,Object> object1=new HashMap<>();
                    object1.put("userName",Username);
                    object1.put("status",status);
                    firebase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid()).updateChildren(object1);
                    Toast.makeText(settings.this, "Status updated", Toast.LENGTH_SHORT).show();

                }
                else{
                    Toast.makeText(settings.this, "Fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        firebase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                        .addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                users User= snapshot.getValue(users.class);
                                Picasso.get().load(User.getPfp()).placeholder(R.drawable.avatar3)
                                        .into(binding.pfp);
                                binding.status.setText(User.getStatus());
                                binding.txtUSERNAME.setText(User.getUSERNAME());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

        binding.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent,45);

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data.getData()!=null){
            Uri sFile=data.getData();
            binding.pfp.setImageURI(sFile);
            final StorageReference reference=storage.getReference().child("profile_img").child(FirebaseAuth.getInstance().getUid());
            reference.putFile(sFile).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    reference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            firebase.getReference().child("Users").child(FirebaseAuth.getInstance().getUid())
                                    .child("pfp").setValue(uri.toString());
                        }
                    });
                }
            });
        }
    }
}