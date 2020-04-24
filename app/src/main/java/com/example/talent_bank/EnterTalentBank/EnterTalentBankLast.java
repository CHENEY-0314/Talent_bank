package com.example.talent_bank.EnterTalentBank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.talent_bank.R;
import com.example.talent_bank.register.RegisterLastActivity;

import cn.refactor.lib.colordialog.ColorDialog;

public class EnterTalentBankLast extends AppCompatActivity {
    private ImageView imgBack;
    private Button button;

    private EditText mETBL_edit_grade;
    private EditText mETBL_edit_Wechat;
    private EditText mETBL_edit_Email;
    private EditText mETBL_edit_address;

    private SharedPreferences mShared;     //用于手机暂存用户注册信息
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_talent_bank_last);

        //设置页面跳转动画
        getWindow().setEnterTransition(new Slide().setDuration(1000));
        getWindow().setExitTransition(new Slide().setDuration(1000));

        mETBL_edit_grade=findViewById(R.id.ETBL_edit_grade);
        mETBL_edit_Wechat=findViewById(R.id.ETBL_edit_Wechat);
        mETBL_edit_Email=findViewById(R.id.ETBL_edit_Email);
        mETBL_edit_address=findViewById(R.id.ETBL_edit_address);


        imgBack = findViewById(R.id.ETBL_back);
        button = findViewById(R.id.ETBL_btn_complete);
        mShared = getSharedPreferences("userdata", MODE_PRIVATE);
        mEditor = mShared.edit();

        //初始化输入框内容
        mETBL_edit_grade.setText(mShared.getString("grade", ""));
        mETBL_edit_Wechat.setText(mShared.getString("wechart", ""));
        mETBL_edit_Email.setText(mShared.getString("email", ""));
        mETBL_edit_address.setText(mShared.getString("adress", ""));


        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                EnterTalentBankLast.this.finish();
            }
        });

        button.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击完成按钮
                if (!Empty()) { //判断grade栏是否为空
                    if (mETBL_edit_Email.getText().toString().equals("") || mETBL_edit_Wechat.getText().toString().equals("") || mETBL_edit_address.getText().toString().equals("")) { //判断其他三栏是否为空
                        ColorDialog dialog = new ColorDialog(EnterTalentBankLast.this);  //为空则跳出提示框
                        dialog.setTitle("提示");
                        dialog.setColor("#ffffff");//颜色
                        dialog.setContentTextColor("#656565");
                        dialog.setTitleTextColor("#656565");
                        dialog.setContentText("简历信息不完善会影响您的组队成功率，是否确定保存并离开？");
                        dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                //JoinTalentBank();  //将信息更新到数据库,执行注册操作
                                dialog.dismiss();
                            }
                        })
                                .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                                    @Override
                                    public void onClick(ColorDialog dialog) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    } else {   //信息填写完整，直接注册
                        //JoinTalentBank();  //将信息更新到数据库，执行注册操作
                    }
                }

            }

        });
    }

    public boolean Empty(){ //判断grade栏是否为空
        if(mETBL_edit_grade.getText().toString().equals("")){mETBL_edit_grade.setError(getString(R.string.error_empty));return true;}
        else return false;
    }

}