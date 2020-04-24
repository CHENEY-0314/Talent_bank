package com.example.talent_bank.register;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.talent_bank.R;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {

    private ImageView mImgback;
    private Button mBtnNext;
    private EditText mEditexperience;
    private EditText mEditadvantage;
    private CheckBox mC1;   //包装设计
    private CheckBox mC2;   //平面设计
    private CheckBox mC3;   //UI设计
    private CheckBox mC4;   //产品设计
    private CheckBox mC5;   //英语
    private CheckBox mC6;   //其他外语
    private CheckBox mC7;   //视频剪辑
    private CheckBox mC8;   //演讲能力
    private CheckBox mC9;   //Photoshop
    private CheckBox mC10;   //PPT制作
    private CheckBox mC11;   //C++
    private CheckBox mC12;   //JAVA
    private CheckBox mC13;   //微信小程序开发
    private CheckBox mC14;   //Android开发
    private CheckBox mC15;   //IOS开发

    private String tag="";//用于存储综合能力标签是否被选择

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

        mC1=findViewById(R.id.cb_1);
        mC2=findViewById(R.id.cb_2);
        mC3=findViewById(R.id.cb_3);
        mC4=findViewById(R.id.cb_4);
        mC5=findViewById(R.id.cb_5);
        mC6=findViewById(R.id.cb_6);
        mC7=findViewById(R.id.cb_7);
        mC8=findViewById(R.id.cb_8);
        mC9=findViewById(R.id.cb_9);
        mC10=findViewById(R.id.cb_10);
        mC11=findViewById(R.id.cb_11);
        mC12=findViewById(R.id.cb_12);
        mC13=findViewById(R.id.cb_13);
        mC14=findViewById(R.id.cb_14);
        mC15=findViewById(R.id.cb_15);

        mShared=getSharedPreferences("registerdata",MODE_PRIVATE);
        mEditor=mShared.edit();

        //初始化输入框内容
        mEditexperience.setText(mShared.getString("register_experience",""));
        mEditadvantage.setText(mShared.getString("register_advantage",""));
        settag();//初始化能力标签

        //跳转页面（并保存当前页）
        mBtnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //跳转到最后一个注册页面
                //存储当前页面信息
                updataCheckBoxArray(); //更新tag记录
                mEditor.putString("register_experience",mEditexperience.getText().toString());
                mEditor.putString("register_advantage",mEditadvantage.getText().toString());
                mEditor.putString("register_tag",tag);
                mEditor.apply();
                mBtnNext.setEnabled(false);
                startActivity(new Intent(RegisterActivity.this, RegisterLastActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());}
        });
        mImgback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //返回上一个注册页面
                //存储当前页面信息
                updataCheckBoxArray(); //更新tag记录
                mEditor.putString("register_experience",mEditexperience.getText().toString());
                mEditor.putString("register_advantage",mEditadvantage.getText().toString());
                mEditor.putString("register_tag",tag);
                mEditor.apply();
                startActivity(new Intent(RegisterActivity.this, RegisterBasedActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
            }
        });
    }

    //将tag的记录转化为能力标签
    public void settag(){
        tag=mShared.getString("register_tag","");
        String[] strarr = tag.split(",");
        for (String s : strarr) {
            if (s.equals("包装设计")) mC1.setChecked(true);else mC1.setChecked(false) ;
            if (s.equals("平面设计")) mC2.setChecked(true);
            if (s.equals("UI设计")) mC3.setChecked(true);
            if (s.equals("产品设计")) mC4.setChecked(true);
            if (s.equals("英语")) mC5.setChecked(true);
            if (s.equals("其他外语")) mC6.setChecked(true);
            if (s.equals("视频剪辑")) mC7.setChecked(true);
            if (s.equals("演讲能力")) mC8.setChecked(true);
            if (s.equals("Photoshop")) mC9.setChecked(true);
            if (s.equals("PPT制作")) mC10.setChecked(true);
            if (s.equals("C")) mC11.setChecked(true);
            if (s.equals("JAVA")) mC12.setChecked(true);
            if (s.equals("微信小程序开发")) mC13.setChecked(true);
            if (s.equals("Android开发")) mC14.setChecked(true);
            if (s.equals("IOS开发")) mC15.setChecked(true);
        }
    }

    //将能力标签转化为string字符存储在tag中
    public void updataCheckBoxArray(){
        tag="";
        if(mC1.isChecked())if (tag.length()==0)tag=tag+"包装设计";else tag=tag+",包装设计";  //用于区分的,为英文的
        if(mC2.isChecked())if (tag.length()==0)tag=tag+"平面设计";else tag=tag+",平面设计";
        if(mC3.isChecked())if (tag.length()==0)tag=tag+"UI设计";else tag=tag+",UI设计";
        if(mC4.isChecked())if (tag.length()==0)tag=tag+"产品设计";else tag=tag+",产品设计";
        if(mC5.isChecked())if (tag.length()==0)tag=tag+"英语";else tag=tag+",英语";
        if(mC6.isChecked())if (tag.length()==0)tag=tag+"其他外语";else tag=tag+",其他外语";
        if(mC7.isChecked())if (tag.length()==0)tag=tag+"视频剪辑";else tag=tag+",视频剪辑";
        if(mC8.isChecked())if (tag.length()==0)tag=tag+"演讲能力";else tag=tag+",演讲能力";
        if(mC9.isChecked())if (tag.length()==0)tag=tag+"Photoshop";else tag=tag+",Photoshop";
        if(mC10.isChecked())if (tag.length()==0)tag=tag+"PPT制作";else tag=tag+",PPT制作";
        if(mC11.isChecked())if (tag.length()==0)tag=tag+"C";else tag=tag+",C";
        if(mC12.isChecked())if (tag.length()==0)tag=tag+"JAVA";else tag=tag+",JAVA";
        if(mC13.isChecked())if (tag.length()==0)tag=tag+"微信小程序开发";else tag=tag+",微信小程序开发";
        if(mC14.isChecked())if (tag.length()==0)tag=tag+"Android开发";else tag=tag+",Android开发";
        if(mC15.isChecked())if (tag.length()==0)tag=tag+"IOS开发";else tag=tag+",IOS开发";
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {   //重写返回函数
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            updataCheckBoxArray(); //更新tag记录
            //存储当前页面信息
            mEditor.putString("register_experience",mEditexperience.getText().toString());
            mEditor.putString("register_advantage",mEditadvantage.getText().toString());
            mEditor.putString("register_tag",tag);
            mEditor.apply();
            startActivity(new Intent(RegisterActivity.this,RegisterBasedActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterActivity.this).toBundle());
        }
        return super.onKeyDown(keyCode, event);
    }

    //判断是否已经填好数据
//    public boolean ifEmpty(){
//        if(mEditexperience.getText().toString().equals("")){mEditexperience.setError(getString(R.string.error_empty));return true;}
//        else if(mEditadvantage.getText().toString().equals("")){mEditadvantage.setError(getString(R.string.error_empty));return true;}
//        else return false;
//    }

    //点击空白处收起键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }
    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
