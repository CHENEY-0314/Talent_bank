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
import com.example.talent_bank.SignUPActivity;

import cn.refactor.lib.colordialog.ColorDialog;


public class RegisterLastActivity extends AppCompatActivity {

    private ImageView mImgback;
    private Button mBtnComplet;
    private EditText mEdtQQ;
    private EditText mEdtWechart;
    private EditText mEdtEmail;
    private EditText mEdtAddress;

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
        setContentView(R.layout.activity_register_last);
        //设置页面跳转动画
        getWindow().setExitTransition(new Slide().setDuration(1000));

        mBtnComplet = findViewById(R.id.RLbtn_complet);
        mImgback = findViewById(R.id.RLimg_back);
        mEdtQQ=findViewById(R.id.RLeditQQ);
        mEdtWechart=findViewById(R.id.RLeditWechat);
        mEdtEmail=findViewById(R.id.RLeditEmail);
        mEdtAddress=findViewById(R.id.RLeditAdress);

        mShared=getSharedPreferences("registerdata",MODE_PRIVATE);
        mEditor=mShared.edit();

        //初始化输入框内容
        mEdtQQ.setText(mShared.getString("register_QQ",""));
        mEdtWechart.setText(mShared.getString("register_Wechart",""));
        mEdtEmail.setText(mShared.getString("register_Email",""));
        mEdtAddress.setText(mShared.getString("register_Address",""));

        setListeners();
    }

    private void setListeners() {
        RegisterLastActivity.OnClick onClick = new RegisterLastActivity.OnClick();
        mBtnComplet.setOnClickListener(onClick);
        mImgback.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.RLbtn_complet:
                    ColorDialog dialog = new ColorDialog(RegisterLastActivity.this);
                    dialog.setTitle("提示");
                    dialog.setColor("#ffffff");//颜色
                    dialog.setContentTextColor("#656565");
                    dialog.setTitleTextColor("#656565");
                    dialog.setContentText("简历信息不完善会影响您的组队成功率，是否确定保存并离开？");
                    dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            mEditor.putString("register_QQ",mEdtQQ.getText().toString());
                            mEditor.putString("register_Email",mEdtEmail.getText().toString());
                            mEditor.putString("register_Wechart",mEdtWechart.getText().toString());
                            mEditor.putString("register_Address",mEdtAddress.getText().toString());
                            mEditor.apply();
                            startActivity(new Intent(RegisterLastActivity.this, SignUPActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            dialog.dismiss();
                        }
                    })
                            .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
                case R.id.RLimg_back:
                    //返回上一界面（保存当前页面数据）
                    mEditor.putString("register_QQ",mEdtQQ.getText().toString());
                    mEditor.putString("register_Email",mEdtEmail.getText().toString());
                    mEditor.putString("register_Wechart",mEdtWechart.getText().toString());
                    mEditor.putString("register_Address",mEdtAddress.getText().toString());
                    mEditor.apply();
                    startActivity(new Intent(RegisterLastActivity.this, RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterLastActivity.this).toBundle());
                    break;
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            mEditor.putString("register_QQ",mEdtQQ.getText().toString());
            mEditor.putString("register_Email",mEdtEmail.getText().toString());
            mEditor.putString("register_Wechart",mEdtWechart.getText().toString());
            mEditor.putString("register_Address",mEdtAddress.getText().toString());
            mEditor.apply();
            startActivity(new Intent(RegisterLastActivity.this, RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterLastActivity.this).toBundle());
        }
        return super.onKeyDown(keyCode, event);
    }

}
