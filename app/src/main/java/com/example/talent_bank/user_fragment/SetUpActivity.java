package com.example.talent_bank.user_fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.talent_bank.LoginActivity;
import com.example.talent_bank.R;

import java.util.Objects;

import cn.refactor.lib.colordialog.ColorDialog;

public class SetUpActivity extends AppCompatActivity {

    private ImageView imgBack;
    private LinearLayout exitlogin;
    private LinearLayout aboutus;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_up);

        imgBack=findViewById(R.id.Setup_back);
        exitlogin=findViewById(R.id.Setup_exitlogin);
        aboutus=findViewById(R.id.Setup_aboutus);

        mSharedPreferences = getSharedPreferences("userdata", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                SetUpActivity.this.finish();
            }
        });
        exitlogin.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击注销账号
                ColorDialog dialog = new ColorDialog(SetUpActivity.this);
                dialog.setTitle("提示");
                dialog.setColor("#ffffff");//颜色
                dialog.setContentTextColor("#656565");
                dialog.setTitleTextColor("#656565");
                dialog.setContentText("是否注销账号？");
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
                dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        mEditor.putString("auto", "false");
                        mEditor.putString("intalent_bank", "");
                        mEditor.putString("userimage", "");
                        mEditor.apply();
                        Intent intent3 = new Intent(SetUpActivity.this, LoginActivity.class);
                        startActivity(intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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
        });
        aboutus.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击关于本APP
                Intent intent= new Intent(SetUpActivity.this, AboutUsActivity.class);
                startActivity(intent);
            }
        });
    }
}
