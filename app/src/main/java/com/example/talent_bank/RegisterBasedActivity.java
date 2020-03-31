package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class RegisterBasedActivity extends AppCompatActivity {

    private EditText mEdtname,mEdtnumber,mEdtpassword,mEdtcode;
    private Button mBtnCode;
    private Button mBtnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_based);
        getWindow().setEnterTransition(new Slide().setDuration(1000));
        getWindow().setExitTransition(new Slide().setDuration(1000));
        mBtnCode=findViewById(R.id.RBbtn_code);
        mBtnNext=findViewById(R.id.RBbtn_next);
        mEdtname=findViewById(R.id.RBeditName);
        mEdtnumber=findViewById(R.id.RBeditNumber);
        mEdtpassword=findViewById(R.id.RBeditPassword);
        mEdtcode=findViewById(R.id.RBeditCode);
        setListeners();
    }
    private void setListeners(){
        RegisterBasedActivity.OnClick onClick=new RegisterBasedActivity.OnClick();
        mBtnCode.setOnClickListener(onClick);
        mBtnNext.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=null;
            switch (v.getId()){
                case R.id.RBbtn_code:
                    //获取手机验证码
                    break;
                case R.id.RBbtn_next:
                    //跳转到注册第二界面
                    if(!IsEmpty()) startActivity(new Intent(RegisterBasedActivity.this,RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterBasedActivity.this).toBundle());
                    break;
            }
        }
    }

    private boolean IsEmpty(){
        if(mEdtname.getText()!=null && mEdtnumber.getText()!=null && mEdtpassword.getText()!=null && mEdtcode.getText()!=null) return false;
        else return true;
    }

}
