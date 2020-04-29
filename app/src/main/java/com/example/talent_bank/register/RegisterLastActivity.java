package com.example.talent_bank.register;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
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
import com.example.talent_bank.MainActivity;
import com.example.talent_bank.R;
import com.example.talent_bank.SignUPActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.refactor.lib.colordialog.ColorDialog;
import pl.droidsonroids.gif.GifImageView;


public class RegisterLastActivity extends AppCompatActivity {

    private ImageView mImgback;
    private Button mBtnComplet;
    private EditText mEdtGrade;
    private EditText mEdtWechart;
    private EditText mEdtEmail;
    private EditText mEdtAddress;

    private GifImageView runWebView;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     * 跟踪登录任务以确保我们可以根据请求取消它。
     */
    private UserRegisterTask mAuthTask = null;

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
        mEdtGrade=findViewById(R.id.RLeditGrade);
        mEdtWechart=findViewById(R.id.RLeditWechat);
        mEdtEmail=findViewById(R.id.RLeditEmail);
        mEdtAddress=findViewById(R.id.RLeditAdress);

        runWebView =findViewById(R.id.RL_loading);

        mShared=getSharedPreferences("registerdata",MODE_PRIVATE);
        mEditor=mShared.edit();

        //初始化输入框内容
        mEdtGrade.setText(mShared.getString("register_Grade",""));
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
                    if(!Empty()){ //判断grade栏是否为空
                        if(mEdtEmail.getText().toString().equals("")||mEdtWechart.getText().toString().equals("")||mEdtAddress.getText().toString().equals("")){ //判断其他三栏是否为空
                            ColorDialog dialog = new ColorDialog(RegisterLastActivity.this);  //为空则跳出提示框
                            dialog.setTitle("提示");
                            dialog.setColor("#ffffff");//颜色
                            dialog.setContentTextColor("#656565");
                            dialog.setTitleTextColor("#656565");
                            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
                            dialog.setContentText("简历信息不完善会影响您的组队成功率，是否确定保存并离开？");
                            dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            register();  //将信息更新到数据库,执行注册操作
                            dialog.dismiss();
                        }
                            }).setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();}
                        else {   //信息填写完整，直接注册
                            register();  //将信息更新到数据库，执行注册操作
                            }}
                    break;
                case R.id.RLimg_back:
                    //返回上一界面（保存当前页面数据）
                    mEditor.putString("register_Grade",mEdtGrade.getText().toString());
                    mEditor.putString("register_Email",mEdtEmail.getText().toString());
                    mEditor.putString("register_Wechart",mEdtWechart.getText().toString());
                    mEditor.putString("register_Address",mEdtAddress.getText().toString());
                    mEditor.apply();
                    startActivity(new Intent(RegisterLastActivity.this, RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterLastActivity.this).toBundle());
                    break;
            }
        }
    }

    public boolean Empty(){ //判断grade栏是否为空
        if(mEdtGrade.getText().toString().equals("")){mEdtGrade.setError(getString(R.string.error_empty));return true;}
        else return false;
    }

    /**
     * Shows the progress UI and hides the login form.
     * 显示进度UI并隐藏登录表单。
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        mBtnComplet.setVisibility(show ? View.GONE : View.VISIBLE);
        mBtnComplet.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mBtnComplet.setVisibility(show ? View.GONE : View.VISIBLE);
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

    public void register(){  //将数据上传到数据库

        String name=mShared.getString("register_name","");
        String number=mShared.getString("register_number","");
        String password=mShared.getString("register_password","");
        String advantage=mShared.getString("register_advantage","");
        String experience=mShared.getString("register_experience","");
        String tag=mShared.getString("register_tag","");
        String grade=mEdtGrade.getText().toString();
        String wechart=mEdtWechart.getText().toString();
        String email=mEdtEmail.getText().toString();
        String adress=mEdtAddress.getText().toString();

        showProgress(true);
        mAuthTask = new UserRegisterTask(name,number, password,advantage,experience,grade,wechart,email,adress,tag);
        mAuthTask.execute((Void) null);
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     * 表示用于验证用户的异步注册任务。
     */
    public class UserRegisterTask extends AsyncTask<Void, Void, Boolean> {

        private final String rName;
        private final String rNumber;
        private final String rPassword;
        private final String radvantage;
        private final String rexperience;
        private final String rgrade;
        private final String rwechart;
        private final String remail;
        private final String radress;
        private final String rtag;

        UserRegisterTask(String name,String number, String password,String advantage,String experience,String grade,String wechart,String email,String adress,String tag) {
            rName=name;
            rNumber = number;
            rPassword = password;
            radvantage = advantage;
            rexperience = experience;
            rgrade = grade;
            rwechart = wechart;
            remail = email;
            radress = adress;
            rtag = tag;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            //尝试对网络服务进行身份验证。
            try {
                RegisterRequest(rName,rNumber,rPassword,radvantage,rexperience,rgrade,rwechart,remail,radress,rtag);
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

    public void RegisterRequest(final String name,final String number,final String password,final String advantage,final String experience,final String grade,final String wechart,final String email,final String adress,final String tag) {

        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/RegisterServlet?number="+number+"&name="+name+"&password="+password+"&advantage="+advantage+"&experience="+experience+"&grade="+grade+"&tags="+tag+"&email="+email+"&adress="+adress+"&wechart="+wechart;
        String statetag = "Login";
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
                            if (result.equals("注册成功")) {
                                mEditor.clear();
                                mEditor.commit();
                                Toast toast=Toast.makeText(RegisterLastActivity.this,null,Toast.LENGTH_SHORT);
                                toast.setText("注册成功");
                                toast.show();
                                startActivity(new Intent(RegisterLastActivity.this, SignUPActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            } else {
                                if (result.equals("账号已存在")){
                                    Toast toast=Toast.makeText(RegisterLastActivity.this,null,Toast.LENGTH_SHORT);
                                    toast.setText("账号已存在");
                                    toast.show();
                                }
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(RegisterLastActivity.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(RegisterLastActivity.this,"请稍后再试！",Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("name", name);
                params.put("number", number);
                params.put("password", password);
                params.put("advantage", advantage);
                params.put("experience", experience);
                params.put("tags", tag);
                params.put("grade", grade);
                params.put("email", email);
                params.put("adress", adress);
                params.put("wechart", wechart);
                return params;
            }
        };

        //设置Tag标签
        request.setTag(statetag);
        //将请求添加到队列中
        requestQueue.add(request);
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            mEditor.putString("register_Grade",mEdtGrade.getText().toString());
            mEditor.putString("register_Email",mEdtEmail.getText().toString());
            mEditor.putString("register_Wechart",mEdtWechart.getText().toString());
            mEditor.putString("register_Address",mEdtAddress.getText().toString());
            mEditor.apply();
            startActivity(new Intent(RegisterLastActivity.this, RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterLastActivity.this).toBundle());
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
