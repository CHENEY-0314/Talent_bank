package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.talent_bank.register.RegisterBasedActivity;

public class LoginActivity extends AppCompatActivity {

    private Button mBtnRegister;
    private Button mBtnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBtnRegister=findViewById(R.id.btn_register);
        mBtnSignup=findViewById(R.id.btn_signEnter);

        setListeners();
    }

    private void setListeners(){  //声明点击事件的函数
        OnClick onClick=new OnClick();
        mBtnRegister.setOnClickListener(onClick);
        mBtnSignup.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{  //点击事件判断

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.btn_register:
                    //跳转到RegisterBased界面
                    startActivity(new Intent(LoginActivity.this,RegisterBasedActivity.class), ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                    break;
                case R.id.btn_signEnter:
                    //跳转到SignUP界面
                    startActivity(new Intent(LoginActivity.this,SignUPActivity.class), ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                    break;
            }
        }
    }
}
