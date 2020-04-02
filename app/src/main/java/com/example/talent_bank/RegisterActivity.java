package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class RegisterActivity extends AppCompatActivity {

    private ImageView mImgback;
    private Button mBtnNext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getWindow().setExitTransition(new Slide().setDuration(1000));
        mBtnNext=findViewById(R.id.Rbtn_next);
        mImgback=findViewById(R.id.Rimg_back);
        //跳转页面（并保存当前页）
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //跳转到最后一个注册页面
                startActivity(new Intent(RegisterActivity.this,RegisterLastActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
            }
        });
        mImgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //返回上一个注册页面
                startActivity(new Intent(RegisterActivity.this,RegisterBasedActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(RegisterActivity.this,RegisterBasedActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
        }
        return super.onKeyDown(keyCode, event);
    }
}
