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
import android.widget.EditText;
import android.widget.ImageView;

import com.example.talent_bank.LoginActivity;
import com.example.talent_bank.R;

import cn.refactor.lib.colordialog.ColorDialog;

public class RegisterBasedActivity extends AppCompatActivity {

    private SharedPreferences mShared;     //用于手机暂存用户注册信息
    private SharedPreferences.Editor mEditor;
    private EditText mEdtname,mEdtnumber,mEdtpassword,mEdtcode;
    private Button mBtnCode;
    private Button mBtnNext;
    private ImageView mImgback;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_based);

        //设置页面切换进出场动画
        getWindow().setEnterTransition(new Slide().setDuration(1000));
        getWindow().setExitTransition(new Slide().setDuration(1000));

        mBtnCode=findViewById(R.id.RBbtn_code);
        mBtnNext=findViewById(R.id.RBbtn_next);
        mEdtname=findViewById(R.id.RBeditName);
        mEdtnumber=findViewById(R.id.RBeditNumber);
        mEdtpassword=findViewById(R.id.RBeditPassword);
        mEdtcode=findViewById(R.id.RBeditCode);
        mImgback=findViewById(R.id.RBimg_back);

        mShared=getSharedPreferences("registerdata",MODE_PRIVATE);
        mEditor=mShared.edit();

        //初始化输入框内容
        mEdtname.setText(mShared.getString("register_name",""));
        mEdtnumber.setText(mShared.getString("register_number",""));
        mEdtpassword.setText(mShared.getString("register_password",""));
        mEdtcode.setText(mShared.getString("register_code",""));

        //监听点击事件
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
                    if(!IsEmpty()) ToRegisterActivity();
                    break;
                case R.id.RBimg_back:
                    //返回上一界面（不保存当前页面数据）
                    ColorDialog dialog = new ColorDialog(RegisterBasedActivity.this);
                    dialog.setTitle("提示");
                    dialog.setColor("#ffffff");//颜色
                    dialog.setContentTextColor("#656565");
                    dialog.setTitleTextColor("#656565");
                    dialog.setContentText("继续返回将丢失当前所有注册信息，是否确定离开？");
                    dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            mEditor.putString("register_Email","");
                            mEditor.putString("register_Address","");
                            mEditor.putString("register_Wechart","");
                            mEditor.putString("register_QQ","");
                            mEditor.putString("register_name","");   //清理先前存储的信息
                            mEditor.putString("register_number","");
                            mEditor.putString("register_password","");
                            mEditor.putString("register_code","");
                            mEditor.putString("register_experience","");
                            mEditor.putString("register_advantage","");
                            mEditor.apply();
                            startActivity(new Intent(RegisterBasedActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
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

    //前往下一注册页面
    private  void ToRegisterActivity(){
        //存储当前页面信息
        mEditor.putString("register_name",mEdtname.getText().toString());
        mEditor.putString("register_number",mEdtnumber.getText().toString());
        mEditor.putString("register_password",mEdtpassword.getText().toString());
        mEditor.putString("register_code",mEdtcode.getText().toString());
        mEditor.apply();
        //前往下一页面
        startActivity(new Intent(RegisterBasedActivity.this, RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterBasedActivity.this).toBundle());
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
            dialog.setContentText("继续返回将丢失当前所有注册信息，是否确定离开？");
            dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                @Override
                public void onClick(ColorDialog dialog) {
                    mEditor.putString("register_Email","");
                    mEditor.putString("register_Address","");
                    mEditor.putString("register_Wechart","");
                    mEditor.putString("register_QQ","");
                    mEditor.putString("register_name","");   //清理先前存储的信息
                    mEditor.putString("register_number","");
                    mEditor.putString("register_password","");
                    mEditor.putString("register_code","");
                    mEditor.putString("register_experience","");
                    mEditor.putString("register_advantage","");
                    mEditor.apply();
                    startActivity(new Intent(RegisterBasedActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
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
