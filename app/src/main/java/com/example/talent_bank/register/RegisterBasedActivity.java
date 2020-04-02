package com.example.talent_bank.register;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.talent_bank.LoginActivity;
import com.example.talent_bank.R;

import cn.refactor.lib.colordialog.ColorDialog;

public class RegisterBasedActivity extends AppCompatActivity {

    private EditText mEdtname,mEdtnumber,mEdtpassword,mEdtcode;
    private Button mBtnCode;
    private Button mBtnNext;
    private ImageView mImgback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
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
        mImgback=findViewById(R.id.RBimg_back);
        setListeners();
    }
    private void setListeners(){
        RegisterBasedActivity.OnClick onClick=new RegisterBasedActivity.OnClick();
        mImgback.setOnClickListener(onClick);
        mBtnCode.setOnClickListener(onClick);
        mBtnNext.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.RBbtn_code:
                    //获取手机验证码
                    break;
                case R.id.RBbtn_next:
                    //跳转到注册第二界面
                    if(!IsEmpty()) startActivity(new Intent(RegisterBasedActivity.this, RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterBasedActivity.this).toBundle());
                    break;
                case R.id.RBimg_back:
                    //返回上一界面（不保存当前页面数据）
                    ColorDialog dialog = new ColorDialog(RegisterBasedActivity.this);
                    dialog.setTitle("提示");
                    dialog.setColor("#ffffff");//颜色
                    dialog.setContentTextColor("#656565");
                    dialog.setTitleTextColor("#656565");
                    dialog.setContentText("继续返回将丢失当前页面信息，是否确定离开？");
                    dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            startActivity(new Intent(RegisterBasedActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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
            }
        }
    }

    private boolean IsEmpty(){
        if(mEdtname.getText()!=null && mEdtnumber.getText()!=null && mEdtpassword.getText()!=null && mEdtcode.getText()!=null) return false;
        else return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ColorDialog dialog = new ColorDialog(RegisterBasedActivity.this);
            dialog.setTitle("提示");
            dialog.setColor("#ffffff");//颜色
            dialog.setContentTextColor("#656565");
            dialog.setTitleTextColor("#656565");
            dialog.setContentText("继续返回将丢失当前页面信息，是否确定离开？");
            dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                @Override
                public void onClick(ColorDialog dialog) {
                    startActivity(new Intent(RegisterBasedActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    dialog.dismiss();
                }
            })
                    .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            dialog.dismiss();
                        }
                    }).show();
        }
        return super.onKeyDown(keyCode, event);
    }


}
