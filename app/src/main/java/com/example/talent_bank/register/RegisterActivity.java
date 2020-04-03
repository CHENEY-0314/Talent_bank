package com.example.talent_bank.register;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.talent_bank.R;

public class RegisterActivity extends AppCompatActivity {

    private ImageView mImgback;
    private Button mBtnNext;
    private EditText mEditexperience;
    private EditText mEditadvantage;

    private SharedPreferences mShared;     //用于手机暂存用户注册信息
    private SharedPreferences.Editor mEditor;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        //设置页面跳转动画
        getWindow().setExitTransition(new Slide().setDuration(1000));

        mBtnNext=findViewById(R.id.Rbtn_next);
        mImgback=findViewById(R.id.Rimg_back);
        mEditadvantage=findViewById(R.id.R_edt_adantage);
        mEditexperience=findViewById(R.id.R_edt_experience);

        mShared=getSharedPreferences("registerdata",MODE_PRIVATE);
        mEditor=mShared.edit();

        //初始化输入框内容
        mEditexperience.setText(mShared.getString("register_experience",""));
        mEditadvantage.setText(mShared.getString("register_advantage",""));

        //跳转页面（并保存当前页）
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //跳转到最后一个注册页面
                //存储当前页面信息
                mEditor.putString("register_experience",mEditexperience.getText().toString());
                mEditor.putString("register_advantage",mEditadvantage.getText().toString());
                mEditor.apply();
                startActivity(new Intent(RegisterActivity.this, RegisterLastActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
            }
        });
        mImgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //返回上一个注册页面
                //存储当前页面信息
                mEditor.putString("register_experience",mEditexperience.getText().toString());
                mEditor.putString("register_advantage",mEditadvantage.getText().toString());
                mEditor.apply();
                startActivity(new Intent(RegisterActivity.this, RegisterBasedActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //存储当前页面信息
            mEditor.putString("register_experience",mEditexperience.getText().toString());
            mEditor.putString("register_advantage",mEditadvantage.getText().toString());
            mEditor.apply();
            startActivity(new Intent(RegisterActivity.this,RegisterBasedActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
        }
        return super.onKeyDown(keyCode, event);
    }

}
