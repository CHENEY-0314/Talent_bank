package com.example.talent_bank.register;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.transition.Slide;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.HandlerActivity;
import com.example.talent_bank.LoginActivity;
import com.example.talent_bank.MainActivity;
import com.example.talent_bank.R;
import com.example.talent_bank.SignUPActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.ColorDialog;

public class RegisterBasedActivity extends AppCompatActivity {

    private SharedPreferences mShared;     //用于手机暂存用户注册信息
    private SharedPreferences.Editor mEditor;
    private EditText mEdtname,mEdtnumber,mEdtpassword,mEdtcode;
    private Button mBtnCode;
    private Button mBtnNext;
    private ImageView mImgback;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     * 跟踪登录任务以确保我们可以根据请求取消它。
     */
    private RegisterBasedActivity.UserExitTask mAuthTask = null;

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
                    IfExit();
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
                    dialog.setContentText("继续返回将丢失当前已填写的注册信息，是否确定离开？");
                    dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            mEditor.clear();
                            mEditor.commit();
                            startActivity(new Intent(RegisterBasedActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            overridePendingTransition(0,R.anim.slide_out);
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

    //判断是否正确输入值
    private boolean IsEmpty(){
        if( !"".contentEquals(mEdtname.getText())){
            if(!"".contentEquals(mEdtnumber.getText())){
                if(!"".contentEquals(mEdtpassword.getText())&&mEdtpassword.getText().length()>7){
                    if(!"".contentEquals(mEdtcode.getText())){
                        return false;
                    }else {mEdtcode.setError(getString(R.string.error_empty)); return true;}
                }else {mEdtpassword.setError(getString(R.string.reg_password_mark)); return true;}
            }else {mEdtnumber.setError(getString(R.string.error_empty)); return true;}
        } else {mEdtname.setError(getString(R.string.error_empty)); return true;}
    }

    //判断账号是否已经注册
    private  void IfExit(){

        boolean cancel = false;
        View focusView = null;

        String number=mEdtnumber.getText().toString();

        // Check for a valid number.检查一个有效的账号。
        if ("".equals(number) || !isNumberValid(number)) {
            mEdtnumber.setError(getString(R.string.error_empty));
            focusView = mEdtnumber;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            //有一个错误;不要尝试登录并首先关注
            // form field with an error.
            //表单字段有错误。
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            //显示进度微调，并启动后台任务
            // perform the user login attempt.
            //执行用户登录尝试。
            mAuthTask = new UserExitTask(number);
            mAuthTask.execute((Void) null);
        }

    }

    //判断手机号是否正确
    private boolean isNumberValid(String number) {
        return number.length() == 11;
    }

    //调用验证的函数
    public class UserExitTask extends AsyncTask<Void, Void, Boolean> {

        private final String mmNumber;

        UserExitTask(String number) {
            mmNumber = number;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //尝试对网络服务进行身份验证。
            try {
                IsExitRequest(mmNumber);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                /*
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                */
            } else {
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
        }
    }

    public void IsExitRequest(final String account) {

        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/isExistServlet?number="+account;
        String tag = "Login";
        //取得请求队列
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        requestQueue.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest request = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("结果");
                            String result = jsonObject.getString("Result");
                            if (result.equals("账号未注册")) {
                                timer.start();  //开始60秒倒计时
                                //这里发送验证码

                            } else {
                                if (result.equals("账号已存在")) {
                                    Toast toast=Toast.makeText(RegisterBasedActivity.this,null,Toast.LENGTH_SHORT);
                                    toast.setText("手机号已注册");
                                    toast.show();
                                    mEdtnumber.setError(getString(R.string.error_haveexit_number));
                                }
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast toast=Toast.makeText(RegisterBasedActivity.this,null,Toast.LENGTH_SHORT);
                            toast.setText("无网络连接");
                            toast.show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast toast=Toast.makeText(RegisterBasedActivity.this,null,Toast.LENGTH_SHORT);
                toast.setText("请稍后再试");
                toast.show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("number", account);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);
        //将请求添加到队列中
        requestQueue.add(request);
    }

    //重写返回函数
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
                    mEditor.clear();
                    mEditor.commit();
                    startActivity(new Intent(RegisterBasedActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                    overridePendingTransition(0,R.anim.slide_out);
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

    //倒计时60秒,这里不直接写60000,而用1000*60是因为后者看起来更直观,每走一步是1000毫秒也就是1秒
    CountDownTimer timer = new CountDownTimer(1000 * 60, 1000) {
        @SuppressLint("DefaultLocale")
        @Override
        public void onTick(long millisUntilFinished) {
            mBtnCode.setEnabled(false);
            mBtnCode.setText(String.format("已发送(%d)",millisUntilFinished/1000));
        }

        @Override
        public void onFinish() {
            mBtnCode.setEnabled(true);
            mBtnCode.setText("重新获取");
        }
    };

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
