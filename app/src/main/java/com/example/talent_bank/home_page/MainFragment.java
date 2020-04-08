package com.example.talent_bank.home_page;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.talent_bank.MyPublishActivity;
import com.example.talent_bank.ProjectReleased;
import com.example.talent_bank.viewmodel.MainViewModel;
import com.example.talent_bank.R;

public class MainFragment extends Fragment {
    private View mView;
    private CardView enterTable;
    private CardView publishProject;

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView =  inflater.inflate(R.layout.main_fragment, container, false);
        initView();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    private void initView() {
        enterTable = mView.findViewById(R.id.main_btn_entertable);
        enterTable.setOnClickListener(new ButtonListener());
        publishProject = mView.findViewById(R.id.main_btn_publish);
        publishProject.setOnClickListener(new ButtonListener());
    }

    public class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_btn_entertable:
                    break;
                case R.id.main_btn_publish:
                    Intent intent1 = new Intent(getActivity(), ProjectReleased.class);
                    startActivity(intent1);
                    break;
            }
        }
    }

}
