package com.example.talent_bank.TalentBank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.example.talent_bank.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TalentBankFragment2 extends Fragment {
    private Context mContext;
    private View mView;
    private CheckBox mC1;   //C++
    private CheckBox mC2;   //JAVA
    private CheckBox mC3;   //微信小程序开发
    private CheckBox mC4;   //IOS开发
    private CheckBox mC5;   //Android开发

    //以下用于手机存用户信息
    private SharedPreferences TagData;
    private SharedPreferences.Editor TagDataEditor;

    public TalentBankFragment2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_talent_bank2, container, false);
        init();
        return mView;
    }

    public void init() {
        mC1 = mView.findViewById(R.id.talent_bank_cb5);
        mC1.setOnClickListener(new ButtonListener());
        mC2 = mView.findViewById(R.id.talent_bank_cb6);
        mC2.setOnClickListener(new ButtonListener());
        mC3 = mView.findViewById(R.id.talent_bank_cb7);
        mC3.setOnClickListener(new ButtonListener());
        mC4 = mView.findViewById(R.id.talent_bank_cb8);
        mC4.setOnClickListener(new ButtonListener());
        mC5 = mView.findViewById(R.id.talent_bank_cb9);
        mC5.setOnClickListener(new ButtonListener());

        mContext = getActivity();
        TagData = mContext.getSharedPreferences("tag_data",mContext.MODE_PRIVATE);
        TagDataEditor = TagData.edit();
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.talent_bank_cb5:
                    if(!mC1.isChecked()) {
                        TagDataEditor.putBoolean("mC5",false);
                        mC1.setTextColor(0xFF000000);
                    }else  {
                        TagDataEditor.putBoolean("mC5",true);
                        mC1.setTextColor(0xFFFFFFFF);
                    }
                    break;
                case R.id.talent_bank_cb6:
                    if(!mC2.isChecked()) {
                        TagDataEditor.putBoolean("mC6",false);
                        mC2.setTextColor(0xFF000000);
                    }else  {
                        TagDataEditor.putBoolean("mC6",true);
                        mC2.setTextColor(0xFFFFFFFF);
                    }
                    break;
                case R.id.talent_bank_cb7:
                    if(!mC3.isChecked()) {
                        TagDataEditor.putBoolean("mC7",false);
                        mC3.setTextColor(0xFF000000);
                    }else  {
                        TagDataEditor.putBoolean("mC7",true);
                        mC3.setTextColor(0xFFFFFFFF);
                    }
                    break;
                case R.id.talent_bank_cb8:
                    if(!mC4.isChecked()) {
                        TagDataEditor.putBoolean("mC8",false);
                        mC4.setTextColor(0xFF000000);
                    }else  {
                        TagDataEditor.putBoolean("mC8",true);
                        mC4.setTextColor(0xFFFFFFFF);
                    }
                    break;
                case R.id.talent_bank_cb9:
                    if(!mC5.isChecked()) {
                        TagDataEditor.putBoolean("mC9",false);
                        mC5.setTextColor(0xFF000000);
                    }else  {
                        TagDataEditor.putBoolean("mC9",true);
                        mC5.setTextColor(0xFFFFFFFF);
                    }
                    break;
            }
            TagDataEditor.apply();
        }
    }


}
