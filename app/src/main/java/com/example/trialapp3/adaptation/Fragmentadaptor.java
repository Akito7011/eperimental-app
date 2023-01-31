package com.example.trialapp3.adaptation;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.trialapp3.Fragements.Call_vcFragment;
import com.example.trialapp3.Fragements.ChatViewFragment;
import com.example.trialapp3.Fragements.stories_statusFragment;

public class Fragmentadaptor extends FragmentPagerAdapter {
    public Fragmentadaptor(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0: return new ChatViewFragment();
            case 1: return new stories_statusFragment();
            case 2: return new Call_vcFragment();
            default:return new ChatViewFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        String title= "hero";
        if(position == 0){
            title= "CHATS";
        }
        if(position == 1){
            title= "Status";
        }
        if(position == 2){
            title= "Calls";
        }
        return title;
    }
}

