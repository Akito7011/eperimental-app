package com.example.trialapp3.Fragements;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.trialapp3.Models.users;
import com.example.trialapp3.R;
import com.example.trialapp3.adaptation.user_list_adapt;
import com.example.trialapp3.databinding.FragmentChatViewBinding;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ChatViewFragment extends Fragment {

    public ChatViewFragment() {
        // Required empty public constructor
    }
     FragmentChatViewBinding binding;
    ArrayList<users> list= new ArrayList<>();
    FirebaseDatabase firedata;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding= FragmentChatViewBinding.inflate(inflater,container,false);
        firedata=FirebaseDatabase.getInstance();
        user_list_adapt adapter= new user_list_adapt(list,getContext());
        binding.USERrecycleview.setAdapter(adapter);
        LinearLayoutManager layoutmngr= new LinearLayoutManager(getContext());
        binding.USERrecycleview.setLayoutManager(layoutmngr);
        firedata.getReference().child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
                    users Users=dataSnapshot.getValue(users.class);
                    Users.setUserID(dataSnapshot.getKey());
                    if(!Users.getUserID().equals(FirebaseAuth.getInstance().getUid())){
                        list.add(Users);
                    }


                }
                adapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return binding.getRoot();
    }
}