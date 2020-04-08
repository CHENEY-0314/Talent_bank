package com.example.talent_bank.home_page;

import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.talent_bank.MyApplyActivity;
import com.example.talent_bank.MyBiographicalActivity;
import com.example.talent_bank.MyCollectionActivity;
import com.example.talent_bank.MyPublishActivity;
import com.example.talent_bank.SetUpActivity;
import com.example.talent_bank.viewmodel.HomeViewModel;
import com.example.talent_bank.R;

public class HomeFragment extends Fragment {

    private HomeViewModel mViewModel;
    private View mView;
    private LinearLayout mSetUp;
    private LinearLayout mMyPublish;
    private LinearLayout mMyApply;
    private LinearLayout mMyCollection;
    private LinearLayout mMyBiographical;

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.home_fragment, container, false);
        initView();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

    private void initView() {
        mMyPublish=mView.findViewById(R.id.home_btn_MyPublish);
        mMyPublish.setOnClickListener(new ButtonListener());
        mMyApply=mView.findViewById(R.id.home_btn_MyApply);
        mMyApply.setOnClickListener(new ButtonListener());
        mMyCollection=mView.findViewById(R.id.home_btn_MyCollection);
        mMyCollection.setOnClickListener(new ButtonListener());
        mMyBiographical=mView.findViewById(R.id.home_btn_MyBiographical);
        mMyBiographical.setOnClickListener(new ButtonListener());
        mSetUp=mView.findViewById(R.id.home_btn_setup);
        mSetUp.setOnClickListener(new ButtonListener());
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.home_btn_MyPublish:
                    Intent intent= new Intent(getActivity(), MyPublishActivity.class);
                    startActivity(intent);
                    break;
                case R.id.home_btn_MyApply:
                    Intent intent1= new Intent(getActivity(), MyApplyActivity.class);
                    startActivity(intent1);
                    break;
                case R.id.home_btn_MyCollection:
                    Intent intent2= new Intent(getActivity(), MyCollectionActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.home_btn_MyBiographical:
                    Intent intent3= new Intent(getActivity(), MyBiographicalActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.home_btn_setup:
                    Intent intent4= new Intent(getActivity(), SetUpActivity.class);
                    startActivity(intent4);
                    break;
            }
        }
    }

}
