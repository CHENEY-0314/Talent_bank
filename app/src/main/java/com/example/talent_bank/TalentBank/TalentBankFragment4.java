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
public class TalentBankFragment4 extends Fragment {
    private Context mContext;
    private Button button;
    private View mView;
    private CheckBox mC1;   //演讲能力
    private CheckBox mC2;   //英语
    private CheckBox mC3;   //其他外语

    //以下用于手机存用户信息
    private SharedPreferences TagData;
    private SharedPreferences.Editor TagDataEditor;

    public TalentBankFragment4() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.fragment_talent_bank4, container, false);
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
        mC1 = mView.findViewById(R.id.talent_bank_cb13);
        mC2 = mView.findViewById(R.id.talent_bank_cb14);
        mC3 = mView.findViewById(R.id.talent_bank_cb15);
        button = mView.findViewById(R.id.talent_bank4_save);
        mContext = getActivity();
        TagData = mContext.getSharedPreferences("tag_data",mContext.MODE_PRIVATE);
        TagDataEditor = TagData.edit();
    }

    public void LoadingTag() { //保存当前的标签
        if(mC1.isChecked()) {
            TagDataEditor.putBoolean("mC13",true);
        }
        if(mC2.isChecked()) {
            TagDataEditor.putBoolean("mC14",true);
        }
        if(mC3.isChecked()) {
            TagDataEditor.putBoolean("mC15",true);
        }
        TagDataEditor.apply();
    }
}
