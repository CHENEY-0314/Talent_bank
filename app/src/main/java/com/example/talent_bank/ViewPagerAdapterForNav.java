package com.example.talent_bank;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapterForNav extends FragmentStatePagerAdapter {
    private List<Fragment> mFragment = new ArrayList<>();

    public ViewPagerAdapterForNav(@NonNull FragmentManager fm) {
        super(fm);
    }
    @Override
    public Fragment getItem(int position) {
        return mFragment.get(position);
    }

    @Override
    public int getCount() {
        if (mFragment == null) {
            return 0;
        } else {
            return mFragment.size();
        }
    }

    public void setFragment(List<Fragment> fragments) {
        this.mFragment = fragments;
        notifyDataSetChanged();
    }
}
