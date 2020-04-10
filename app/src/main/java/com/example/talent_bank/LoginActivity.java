package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.talent_bank.register.RegisterBasedActivity;

public class LoginActivity extends AppCompatActivity {

    private Button mBtnRegister;
    private Button mBtnSignup;

    private SharedPreferences preferences;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBtnRegister=findViewById(R.id.btn_register);
        mBtnSignup=findViewById(R.id.btn_signEnter);
        preferences=getSharedPreferences("userdata",MODE_PRIVATE);
        mEditor=preferences.edit();
        IfHaveLogin();
        setListeners();
    }

    //判断是否曾经登录过
    private  void IfHaveLogin(){
        String number=preferences.getString("number","");
        String auto=preferences.getString("auto","");

        // Check for a valid number.
        if ("".equals(number)) {  //可以增加一个判断账户是否存在
            return;
        }

        if("false".equals(preferences.getString("auto",""))){
            return;
        }

        mEditor.putString("auto","true");
        mEditor.apply();
        startActivity(new Intent(LoginActivity.this, HandlerActivity.class));
        LoginActivity.this.finish();
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
