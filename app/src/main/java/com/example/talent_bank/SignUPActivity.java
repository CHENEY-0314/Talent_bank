package com.example.talent_bank;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.InputType;
import android.transition.Slide;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.register.RegisterLastActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;


public class SignUPActivity extends AppCompatActivity {

    private CheckBox mCbxhidepsa;
    private Button mBtnsignup;
    private ImageView imgback;
    private EditText medtname;
    private EditText medtpassword;
    private CheckBox mCbxrempas;

    private GifImageView runWebView;
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     * 跟踪登录任务以确保我们可以根据请求取消它。
     */
    private UserLoginTask mAuthTask = null;

    //以下用于手机存用户信息
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private SharedPreferences ForSignup;
    private SharedPreferences.Editor mEdi;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上才能设置状态栏颜色
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        getWindow().setEnterTransition(new Slide().setDuration(1000));  //设置退场动画

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_u_p);

        runWebView =findViewById(R.id.gv_error);

        imgback=findViewById(R.id.SP_img_back);
        mCbxrempas=findViewById(R.id.checkBox_rempassword);
        mCbxhidepsa=findViewById(R.id.SP_check_showpassword);
        mBtnsignup=findViewById(R.id.btn_signup);
        medtname=findViewById(R.id.SPedt_number);
        medtpassword=findViewById(R.id.SPedt_password);
        mSharedPreferences=getSharedPreferences("userdata",MODE_PRIVATE);
        mEditor=mSharedPreferences.edit();

        ForSignup=getSharedPreferences("signup",MODE_PRIVATE);
        mEdi=ForSignup.edit();

        //初始化用户名和密码输入框
        medtname.setText(ForSignup.getString("number",""));
        medtpassword.setText(ForSignup.getString("password",""));

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
                overridePendingTransition(0,R.anim.slide_out);
            }
        });

        mBtnsignup.setOnClickListener(new View.OnClickListener() {  //登录，检查账号密码是否正确
            @Override
            public void onClick(View v) {
                attemptLogin();
            }
        });
    }

    /**
     * Attempts to sign in or register the account specified by the login form.
     * 尝试登录或注册登录表单指定的帐户。
     * errors are presented and no actual login attempt is made.
     * 出现错误并且没有进行实际的登录尝试。
     */
    private void attemptLogin() {

        String number=medtname.getText().toString();
        String password=medtpassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.如果用户输入密码，请检查有效的密码。
        if ("".equals(password) || !isPasswordValid(password)) {
            medtpassword.setError(getString(R.string.error_invalid_password));
            focusView = medtpassword;
            cancel = true;
        }

        // Check for a valid number.检查一个有效的账号。
        if ("".equals(number) || !isNumberValid(number)) {
            medtname.setError(getString(R.string.error_invalid_number));
            focusView = medtname;
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
            showProgress(true);
            mAuthTask = new UserLoginTask(number, password);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() >= 8 && password.length()<=16;
    }
    private boolean isNumberValid(String number) {
        //TODO: Replace this with your own logic
        return number.length() == 11;
    }


    /**
     * Shows the progress UI and hides the login form.
     * 显示进度UI并隐藏登录表单。
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        //在Honeycomb MR2上，我们有ViewPropertyAnimator API，可以实现非常简单的动画。如果可用，请使用这些API淡入进度微调器。
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mBtnsignup.setVisibility(show ? View.GONE : View.VISIBLE);
        mBtnsignup.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mBtnsignup.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
        runWebView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     * 表示用于验证用户的异步登录/注册任务。
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mmNumber;
        private final String mmPassword;

        UserLoginTask(String number, String password) {
            mmNumber = number;
            mmPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.
            //尝试对网络服务进行身份验证。
            try {
                LoginRequest(mmNumber,mmPassword);
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false);
            if (success) {
                /*
                Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                */
            } else {
                runWebView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }

    public void LoginRequest(final String account, final String password) {

        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/LoginServlet?number="+account+"&password="+password;
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
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("params");
                            String result = jsonObject.getString("Result");
                            if (result.equals("success")) {
                                //自动保存账号，密码
                                if(mCbxrempas.isChecked()){
                                    mEdi.putString("number",medtname.getText().toString());
                                    mEdi.putString("password",medtpassword.getText().toString());
                                    mEdi.apply();
                                }else{
                                    mEdi.putString("number",medtname.getText().toString());
                                    mEdi.putString("password","");    //如没有选择则清空password文件
                                    mEdi.apply();
                                }
                                //将用户名和密码存储到userdata，以便后续使用
                                mEditor.putString("number",medtname.getText().toString());
                                mEditor.putString("password",medtpassword.getText().toString());
                                //实现页面跳转（并清空页面栈）
                                mEditor.putString("auto","true");
                                mEditor.apply();
                                UpdateUserdate(account,password);
                                Handler mHandler = new Handler();
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(SignUPActivity.this, HandlerActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                        overridePendingTransition(R.anim.fade_in,R.anim.fade_out);  //设置跳转动画
                                    }
                                },1800);
                            } else {
                                if (result.equals("failed"))
                                    Toast.makeText(SignUPActivity.this,"账户或密码错误！",Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(SignUPActivity.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(SignUPActivity.this,"请稍后再试！",Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("number", account);
                params.put("password", password);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(tag);
        //将请求添加到队列中
        requestQueue.add(request);
    }

    public void UpdateUserdate(final String number, final String password){  //更新当前登录的用户信息
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/GetUserdata?number="+number+"&password="+password;
        String tag = "Updata";
        //取得请求队列
        RequestQueue Updata = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        Updata.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest Updatarequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            mEditor.putString("name",jsonObject.getString("name"));
                            mEditor.putString("advantage",jsonObject.getString("advantage"));
                            mEditor.putString("grade",jsonObject.getString("grade"));
                            mEditor.putString("experience",jsonObject.getString("experience"));
                            mEditor.putString("tag",jsonObject.getString("tag"));
                            mEditor.putString("wechart",jsonObject.getString("wechart"));
                            mEditor.putString("email",jsonObject.getString("email"));
                            mEditor.putString("adress",jsonObject.getString("adress"));
                            mEditor.apply();
                            Toast toast=Toast.makeText(SignUPActivity.this,null,Toast.LENGTH_SHORT);
                            toast.setText("欢迎你:"+jsonObject.getString("name"));
                            toast.show();
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(SignUPActivity.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(SignUPActivity.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("number", number);
                params.put("password", password);
                return params;
            }
        };
        //设置Tag标签
        Updatarequest.setTag(tag);
        //将请求添加到队列中
        Updata.add(Updatarequest);
    }

    //重写返回函数
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(SignUPActivity.this, LoginActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
            overridePendingTransition(0,R.anim.slide_out);
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
