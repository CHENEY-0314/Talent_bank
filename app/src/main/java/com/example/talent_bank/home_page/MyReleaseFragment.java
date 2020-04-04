package com.example.talent_bank.home_page;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.talent_bank.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyReleaseFragment extends Fragment {

    public MyReleaseFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_my_release, container, false);
    }
}
