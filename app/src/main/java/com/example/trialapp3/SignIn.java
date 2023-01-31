package com.example.trialapp3;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.trialapp3.Models.users;
import com.example.trialapp3.databinding.ActivitySignInBinding;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.FirebaseDatabase;

public class SignIn extends AppCompatActivity {

    ActivitySignInBinding binding;
    ProgressDialog dailog;
    FirebaseAuth mAuth;
    FirebaseDatabase Firedatabase;
    GoogleSignInClient mGoogleSignInClient;

    public SignIn() throws ApiException {
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySignInBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        getSupportActionBar().hide();
        mAuth=FirebaseAuth.getInstance();
        Firedatabase=FirebaseDatabase.getInstance();

        dailog= new ProgressDialog(SignIn.this);
        dailog.setTitle("Login");
        dailog.setMessage("Authenticating ...");
        GoogleSignInOptions GSO=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        mGoogleSignInClient = GoogleSignIn.getClient(this, GSO);
        binding.BTTNSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.txtEMAIL.getText().toString().isEmpty() && !binding.txtpassword.getText().toString().isEmpty()){
                    dailog.show();
                    mAuth.signInWithEmailAndPassword(binding.txtEMAIL.getText().toString(),binding.txtpassword.getText().toString())
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    dailog.dismiss();
                                    if(task.isSuccessful()){
                                        Intent intent=new Intent(SignIn.this,MainActivity.class);
                                        startActivity(intent);

                                    }
                                    else {
                                        Toast.makeText(SignIn.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(SignIn.this, "Enter Credentials", Toast.LENGTH_SHORT).show();
                }

            }
        });
        if(mAuth.getCurrentUser()!=null){
            Intent intent=new Intent(SignIn.this,MainActivity.class);
            startActivity(intent);
        }
        binding.txtclicksignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(SignIn.this,SignUp.class);
                startActivity(intent);
            }
        });
        binding.GOOGLE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signin();
            }
        });
    }
    int RC_SIGN=6;
    private void signin(){
        Intent intent=mGoogleSignInClient.getSignInIntent();
        startActivityForResult(intent,RC_SIGN);
    }
    @Override
    public void onActivityResult(int requestCODE, int resultCODE,Intent data){
        super.onActivityResult(requestCODE,resultCODE,data);
        if(requestCODE== RC_SIGN);
        Task<GoogleSignInAccount> task= GoogleSignIn.getSignedInAccountFromIntent(data);
        try {
            GoogleSignInAccount account = task.getResult(ApiException.class);
            Log.d(TAG,"firebaseWithGoogle: "+account.getId());
            firebaseAuthWithGoogle(account.getIdToken());
            Intent intent= new Intent(SignIn.this,MainActivity.class);
            startActivity(intent);
            Toast.makeText(this, "Sign in successful", Toast.LENGTH_SHORT).show();
        }catch (ApiException e){
            Log.w(TAG, "Google sign in failed",e);
            Toast.makeText(this, "Sign in failed", Toast.LENGTH_SHORT).show();
        }
    }
    private void firebaseAuthWithGoogle(String idToken){
        AuthCredential credential= GoogleAuthProvider.getCredential(idToken,null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    Log.d(TAG, "SignInWithCredentials:success");
                    FirebaseUser user= mAuth.getCurrentUser();
                    users userr = new users();
                    userr.setUserID(user.getUid());
                    userr.setUSERNAME(user.getDisplayName());
                    userr.setPfp(user.getPhotoUrl().toString());
                    Firedatabase.getReference().child("Users").child(user.getUid()).setValue(userr);

                }
                else{
                    Log.w(TAG,"SignInWithCredentials:Failed",task.getException());
                }
            }
        });
    }
}