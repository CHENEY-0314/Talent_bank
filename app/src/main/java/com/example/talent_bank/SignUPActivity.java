package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.InputType;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class SignUPActivity extends AppCompatActivity {

    private CheckBox mCbxhidepsa;
    private Button mBtnsignup;
    private ImageView imgback;
    private EditText medtname;
    private EditText medtpassword;
    private CheckBox mCbxrempas;

    //以下用于手机存用户信息
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上才能设置状态栏颜色
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        getWindow().setEnterTransition(new Slide().setDuration(1000));  //设置退场动画

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_u_p);


        imgback=findViewById(R.id.SP_img_back);
        mCbxrempas=findViewById(R.id.checkBox_rempassword);
        mCbxhidepsa=findViewById(R.id.SP_check_showpassword);
        mBtnsignup=findViewById(R.id.btn_signup);
        medtname=findViewById(R.id.SPedt_number);
        medtpassword=findViewById(R.id.SPedt_password);
        mSharedPreferences=getSharedPreferences("userdata",MODE_PRIVATE);
        mEditor=mSharedPreferences.edit();

        //初始化用户名和密码输入框
        medtname.setText(mSharedPreferences.getString("number",""));
        medtpassword.setText(mSharedPreferences.getString("password",""));

        mCbxhidepsa.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击显示、隐藏密码
                    //记住光标开始的位置
                    int pos = medtpassword.getSelectionStart();
                    if(medtpassword.getInputType()!= (InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)){//隐藏密码
                        medtpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    }else{//显示密码
                        medtpassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    }
                medtpassword.setSelection(pos);
            }
        });

        imgback.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                startActivity(new Intent(SignUPActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
            }
        });

        mBtnsignup.setOnClickListener(new View.OnClickListener() {  //登录，检查账号密码是否正确
            @Override
            public void onClick(View v) {
                Toast toast=Toast.makeText(SignUPActivity.this,"登陆成功",Toast.LENGTH_SHORT);
                toast.show();
                new Handler(new Handler.Callback() {   //等待登录成功显示完成之后跳转
                    @Override
                    public boolean handleMessage(Message msg) {  //记住用户名和密码（判断是否选择了记住密码）
                        if(mCbxrempas.isChecked()){
                            mEditor.putString("number",medtname.getText().toString());
                            mEditor.putString("password",medtpassword.getText().toString());
                            mEditor.apply();
                        }else{
                            mEditor.putString("number",medtname.getText().toString());
                            mEditor.putString("password","");    //如没有选择则清空password文件
                            mEditor.apply();
                        }
                        //实现页面跳转（并清空页面栈）
                        startActivity(new Intent(SignUPActivity.this, HandlerActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);  //设置跳转动画
                        return false;
                    }
                }).sendEmptyMessageDelayed(0,1500);//表示延迟1.5秒发送任务

            }
        });
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {   //重写返回函数
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(SignUPActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

}
