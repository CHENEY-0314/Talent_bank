package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Fade;
import android.transition.Slide;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.talent_bank.register.RegisterBasedActivity;

public class HandlerActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_handler);
        //显示提示框“三秒后自动跳转”
        Toast toast=Toast.makeText(HandlerActivity.this,"三秒后自动跳转",Toast.LENGTH_SHORT);
        toast.show();
        //进行页面的自动跳转
        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent=new Intent(HandlerActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);  //更改跳转动画
            }
        },3500);
    }
}
