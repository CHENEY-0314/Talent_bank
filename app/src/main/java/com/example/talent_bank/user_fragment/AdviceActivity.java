package com.example.talent_bank.user_fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.HandlerActivity;
import com.example.talent_bank.LoginActivity;
import com.example.talent_bank.R;
import com.example.talent_bank.SignUPActivity;
import com.example.talent_bank.register.RegisterBasedActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.refactor.lib.colordialog.ColorDialog;

public class AdviceActivity extends AppCompatActivity {

    private ImageView imgBack;
    private EditText mAdvice;
    private Button mSubmit;
    private TextView mNum;

    private SharedPreferences mSharedPreferences;

    private SharedPreferences mShared;     //用于手机暂存用户注册信息
    private SharedPreferences.Editor mEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advice);

        imgBack=findViewById(R.id.Adviceimg_back);
        mAdvice=findViewById(R.id.Advice_edt);
        mSubmit=findViewById(R.id.Advice_btn);
        mSubmit.setEnabled(false);
        mNum=findViewById(R.id.Advice_num);

        mSharedPreferences=getSharedPreferences("userdata",MODE_PRIVATE);

        mShared=getSharedPreferences("advicedata",MODE_PRIVATE);
        mEditor=mShared.edit();

        mAdvice.setText(mShared.getString("advice",""));

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                if(mSubmit.isEnabled()){
                    ColorDialog dialog = new ColorDialog(AdviceActivity.this);
                dialog.setTitle("提示");
                dialog.setColor("#ffffff");//颜色
                dialog.setContentTextColor("#656565");
                dialog.setTitleTextColor("#656565");
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
                dialog.setContentText("是否保存已有编辑");
                dialog.setPositiveListener("保存", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        mEditor.putString("advice", String.valueOf(mAdvice.getText()));
                        AdviceActivity.this.finish();
                        dialog.dismiss();
                    }
                })
                        .setNegativeListener("不保存", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                AdviceActivity.this.finish();
                                dialog.dismiss();
                            }
                        }).show();
            }else AdviceActivity.this.finish();
            }
        });

        mAdvice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {  //监听输入框字数变化
                mNum.setText(String.valueOf(s.length())+"/300");
                if(s.length()>15){
                    mSubmit.setEnabled(true);
                }else mSubmit.setEnabled(false);
            }
        });

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //点击提交按钮
                ColorDialog dialog = new ColorDialog(AdviceActivity.this);
                dialog.setTitle("提示");
                dialog.setColor("#ffffff");//颜色
                dialog.setContentTextColor("#656565");
                dialog.setTitleTextColor("#656565");
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
                dialog.setContentText("是否确认编辑完毕，并提交");
                dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        SubmitAdvice();
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

    }

    //提交意见到云端
    private void SubmitAdvice(){
        final String advice= String.valueOf(mAdvice.getText());
        final String number=mSharedPreferences.getString("number","");
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/AdviceServlet?number="+number+"&advice="+advice;
        String tag = "SubAdvice";
        //取得请求队列
        RequestQueue SubAdvice = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        SubAdvice.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest SubAdvicerequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("结果");
                            if(jsonObject.getString("Result").equals("提交成功")){
                                Toast toast=Toast.makeText(AdviceActivity.this,null,Toast.LENGTH_SHORT);
                                toast.setText("感谢您的意见，我们会继续努力");
                                toast.show();
                            }
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AdviceActivity.this.finish();
                                }
                            },1000);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(AdviceActivity.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(AdviceActivity.this,"无网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("number", number);
                params.put("advice", advice);
                return params;
            }
        };
        //设置Tag标签
        SubAdvicerequest.setTag(tag);
        //将请求添加到队列中
        SubAdvice.add(SubAdvicerequest);
    }

    //重写返回函数
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            if(mSubmit.isEnabled()){
            ColorDialog dialog = new ColorDialog(AdviceActivity.this);
            dialog.setTitle("提示");
            dialog.setColor("#ffffff");//颜色
            dialog.setContentTextColor("#656565");
            dialog.setTitleTextColor("#656565");
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
            dialog.setContentText("是否保存已有编辑");
            dialog.setPositiveListener("保存", new ColorDialog.OnPositiveListener() {
                @Override
                public void onClick(ColorDialog dialog) {
                    mEditor.putString("advice", String.valueOf(mAdvice.getText()));
                    AdviceActivity.this.finish();
                    dialog.dismiss();
                }
            })
                    .setNegativeListener("不保存", new ColorDialog.OnNegativeListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            AdviceActivity.this.finish();
                            dialog.dismiss();
                        }
                    }).show();
        }else AdviceActivity.this.finish();
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
                mAdvice.clearFocus();
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
