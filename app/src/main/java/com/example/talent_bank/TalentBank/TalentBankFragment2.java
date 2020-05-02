package com.example.talent_bank.TalentBank;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.example.talent_bank.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TalentBankFragment2 extends Fragment {
    private Context mContext;
    private Button button;
    private View mView;
    private CheckBox mC1;   //C++
    private CheckBox mC2;   //JAVA
    private CheckBox mC3;   //微信小程序开发
    private CheckBox mC4;   //Android开发
    private CheckBox mC5;   //IOS开发

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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingTag();
            }
        });
        return mView;
    }

    public void init() {
        mC1 = mView.findViewById(R.id.talent_bank_cb5);
        mC2 = mView.findViewById(R.id.talent_bank_cb6);
        mC3 = mView.findViewById(R.id.talent_bank_cb7);
        mC4 = mView.findViewById(R.id.talent_bank_cb8);
        mC5 = mView.findViewById(R.id.talent_bank_cb9);
        button = mView.findViewById(R.id.talent_bank2_save);
        mContext = getActivity();
        TagData = mContext.getSharedPreferences("tag_data",mContext.MODE_PRIVATE);
        TagDataEditor = TagData.edit();
    }

    public void LoadingTag() { //保存当前的标签
        if(mC1.isChecked()) {
            TagDataEditor.putBoolean("mC5",true);
        }
        if(mC2.isChecked()) {
            TagDataEditor.putBoolean("mC6",true);
        }
        if(mC3.isChecked()) {
            TagDataEditor.putBoolean("mC7",true);
        }
        if(mC4.isChecked()) {
            TagDataEditor.putBoolean("mC8",true);
        }
        if(mC5.isChecked()) {
            TagDataEditor.putBoolean("mC9",true);
        }
        TagDataEditor.apply();
    }
}
