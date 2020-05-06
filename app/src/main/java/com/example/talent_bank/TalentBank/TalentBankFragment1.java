package com.example.talent_bank.TalentBank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.talent_bank.R;
import com.example.talent_bank.home_page.MainFragment;

/**
 * A simple {@link Fragment} subclass.
 */
public class TalentBankFragment1 extends Fragment {
    private Context mContext;

    private View mView;
    private CheckBox mC1;   //包装设计
    private CheckBox mC2;   //平面设计
    private CheckBox mC3;   //UI设计
    private CheckBox mC4;   //产品设计

    //以下用于手机存用户信息
    private SharedPreferences TagData;
    private SharedPreferences.Editor TagDataEditor;

    public TalentBankFragment1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_talent_bank1, container, false);
        init();
        return mView;
    }

    public void init() {
        mC1 = mView.findViewById(R.id.talent_bank_cb1);
        mC1.setOnClickListener(new ButtonListener());
        mC2 = mView.findViewById(R.id.talent_bank_cb2);
        mC2.setOnClickListener(new ButtonListener());
        mC3 = mView.findViewById(R.id.talent_bank_cb3);
        mC3.setOnClickListener(new ButtonListener());
        mC4 = mView.findViewById(R.id.talent_bank_cb4);
        mC4.setOnClickListener(new ButtonListener());

        mContext = getActivity();
        TagData = mContext.getSharedPreferences("tag_data",mContext.MODE_PRIVATE);
        TagDataEditor = TagData.edit();
    }

    //点击checkbox时出发事件
    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.talent_bank_cb1:
                    if(!mC1.isChecked()) {
                        TagDataEditor.putBoolean("mC1",false);
                        mC1.setTextColor(0xFF000000);
                    }else  {
                        TagDataEditor.putBoolean("mC1",true);
                        mC1.setTextColor(0xFFFFFFFF);
                    }
                    break;
                case R.id.talent_bank_cb2:
                    if(!mC2.isChecked()) {
                        TagDataEditor.putBoolean("mC2",false);
                        mC2.setTextColor(0xFF000000);
                    }else  {
                        TagDataEditor.putBoolean("mC2",true);
                        mC2.setTextColor(0xFFFFFFFF);
                    }
                    break;
                case R.id.talent_bank_cb3:
                    if(!mC3.isChecked()) {
                        TagDataEditor.putBoolean("mC3",false);
                        mC3.setTextColor(0xFF000000);
                    }else  {
                        TagDataEditor.putBoolean("mC3",true);
                        mC3.setTextColor(0xFFFFFFFF);
                    }
                    break;
                case R.id.talent_bank_cb4:
                    if(!mC4.isChecked()) {
                        TagDataEditor.putBoolean("mC4",false);
                        mC4.setTextColor(0xFF000000);
                    }else  {
                        TagDataEditor.putBoolean("mC4",true);
                        mC4.setTextColor(0xFFFFFFFF);
                    }
                    break;
            }
            TagDataEditor.apply();
        }
    }

}
