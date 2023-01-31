package com.example.trialapp3;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.trialapp3.adaptation.Fragmentadaptor;
import com.example.trialapp3.databinding.ActivityMainBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    FirebaseAuth mAuth;
    FirebaseDatabase FireDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding =  ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        mAuth= FirebaseAuth.getInstance();
        binding.viewPager.setAdapter(new Fragmentadaptor(getSupportFragmentManager()));
        binding.tabLayout.setupWithViewPager(binding.viewPager);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menu_inflater = getMenuInflater();
        menu_inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()){
            case R.id.SETTINGS:
                Intent intent3=new Intent(MainActivity.this,settings.class);
                startActivity(intent3);
                break;
            case R.id.GRP_CHAT:
                //Toast.makeText(this, "GRP CHAT MODE ON", Toast.LENGTH_SHORT).show();
                Intent intent2=new Intent(MainActivity.this,GroupChat.class);
                break;
            case R.id.LOGOUT:
                mAuth.signOut();
                Intent intent = new Intent(MainActivity.this,SignIn.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}