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

/**
 * A simple {@link Fragment} subclass.
 */
public class TalentBankFragment1 extends Fragment {
    private Context mContext;
    private Button button;
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
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadingTag();
            }
        });
        return mView;
    }

    public void init() {
        mC1 = mView.findViewById(R.id.talent_bank_cb1);
        mC2 = mView.findViewById(R.id.talent_bank_cb2);
        mC3 = mView.findViewById(R.id.talent_bank_cb3);
        mC4 = mView.findViewById(R.id.talent_bank_cb4);
        button = mView.findViewById(R.id.talent_bank1_save);
        mContext = getActivity();
        TagData = mContext.getSharedPreferences("tag_data",mContext.MODE_PRIVATE);
        TagDataEditor = TagData.edit();
    }

    public void LoadingTag() { //保存当前的标签
        if(mC1.isChecked()) {
            TagDataEditor.putBoolean("mC1",true);
        }
        if(mC2.isChecked()) {
            TagDataEditor.putBoolean("mC2",true);
        }
        if(mC3.isChecked()) {
            TagDataEditor.putBoolean("mC3",true);
        }
        if(mC4.isChecked()) {
            TagDataEditor.putBoolean("mC4",true);
        }
        TagDataEditor.apply();
    }

}
