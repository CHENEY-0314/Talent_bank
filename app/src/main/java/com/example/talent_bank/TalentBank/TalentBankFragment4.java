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
        return mView;
    }

    public void init() {
        mC1 = mView.findViewById(R.id.talent_bank_cb13);
        mC1.setOnClickListener(new ButtonListener());
        mC2 = mView.findViewById(R.id.talent_bank_cb14);
        mC2.setOnClickListener(new ButtonListener());
        mC3 = mView.findViewById(R.id.talent_bank_cb15);
        mC3.setOnClickListener(new ButtonListener());

        mContext = getActivity();
        TagData = mContext.getSharedPreferences("tag_data",mContext.MODE_PRIVATE);
        TagDataEditor = TagData.edit();
    }

    private class ButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.talent_bank_cb13:
                    if(!mC1.isChecked()) {
                        TagDataEditor.putBoolean("mC13",false);
                        mC1.setTextColor(0xFF000000);
                    }else  {
                        TagDataEditor.putBoolean("mC13",true);
                        mC1.setTextColor(0xFFFFFFFF);
                    }
                    break;
                case R.id.talent_bank_cb14:
                    if(!mC2.isChecked()) {
                        TagDataEditor.putBoolean("mC14",false);
                        mC2.setTextColor(0xFF000000);
                    }else  {
                        TagDataEditor.putBoolean("mC14",true);
                        mC2.setTextColor(0xFFFFFFFF);
                    }
                    break;
                case R.id.talent_bank_cb15:
                    if(!mC3.isChecked()) {
                        TagDataEditor.putBoolean("mC15",false);
                        mC3.setTextColor(0xFF000000);
                    }else  {
                        TagDataEditor.putBoolean("mC15",true);
                        mC3.setTextColor(0xFFFFFFFF);
                    }
                    break;
            }
            TagDataEditor.apply();
        }
    }


}
