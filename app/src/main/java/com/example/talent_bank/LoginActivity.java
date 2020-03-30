package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Explode;
import android.transition.Fade;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;

public class LoginActivity extends AppCompatActivity {

    private Button mBtnRegister;
    private Button mBtnSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mBtnRegister=findViewById(R.id.btn_register);
        mBtnSignup=findViewById(R.id.btn_signup);
        setListeners();
    }

    private void setListeners(){
        OnClick onClick=new OnClick();
        mBtnRegister.setOnClickListener(onClick);
        mBtnSignup.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent=null;
            switch (v.getId()){
                case R.id.btn_register:
                    //跳转到Register界面
                    startActivity(new Intent(LoginActivity.this,RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                    break;
                case R.id.btn_signup:
                    //跳转到SignUP界面
                    startActivity(new Intent(LoginActivity.this,SignUPActivity.class), ActivityOptions.makeSceneTransitionAnimation(LoginActivity.this).toBundle());
                    break;
            }
        }
    }
}
