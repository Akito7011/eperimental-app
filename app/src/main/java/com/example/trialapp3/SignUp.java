package com.example.trialapp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Toast;

import com.example.trialapp3.Models.users;
import com.example.trialapp3.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class SignUp extends AppCompatActivity {
    ActivitySignUpBinding binding;
    FirebaseDatabase database;
    ProgressDialog dialogbox;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mAuth = FirebaseAuth.getInstance();
        database=FirebaseDatabase.getInstance();

        getSupportActionBar().hide();

        dialogbox= new ProgressDialog(SignUp.this);
        dialogbox.setTitle("Account creation ....");
        dialogbox.setMessage("Account creation in process");

        binding.BTTNSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!binding.txtUSERNAME.getText().toString().isEmpty() && !binding.txtEMAIL.getText().toString().isEmpty() && !binding.txtpassword.getText().toString().isEmpty()){
                    dialogbox.show();
                    mAuth.createUserWithEmailAndPassword(binding.txtEMAIL.getText().toString(),binding.txtpassword.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            dialogbox.dismiss();
                            if(task.isSuccessful()){
                                users user= new users(binding.txtUSERNAME.getText().toString(),binding.txtEMAIL.getText().toString(),binding.txtpassword.getText().toString());
                                String id = task.getResult().getUser().getUid();
                                database.getReference().child("Users").child(id).setValue(user);
                                Toast.makeText(SignUp.this, "Sign Up successful", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(SignUp.this, task.getException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
                else {
                    Toast.makeText(SignUp.this, "Required fields are empty", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.OLDUSER.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SignUp.this,SignIn.class);
                startActivity(intent);

            }
        });

    }
}