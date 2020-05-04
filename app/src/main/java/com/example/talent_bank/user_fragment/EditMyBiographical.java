package com.example.talent_bank.user_fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.text.Editable;
import android.text.TextWatcher;
import android.transition.Slide;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.R;
import com.example.talent_bank.SignUPActivity;
import com.example.talent_bank.register.RegisterLastActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.refactor.lib.colordialog.ColorDialog;

public class EditMyBiographical extends AppCompatActivity {

    private EditText madvantage,mexperience,mgrade,mwechart,memail,madress;
    private CheckBox mC1,mC2,mC3,mC4,mC5,mC6,mC7,mC8,mC9,mC10,mC11,mC12,mC13,mC14,mC15;
    private Button mComplet;
    private ImageView mBack;

    private TextView mtxtadvantage,mtxtexperience;

    //以下用于手机存用户信息
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    String tag; //记录能力标签

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_my_biographical);

        getWindow().setEnterTransition(new Slide().setDuration(1000));
        getWindow().setExitTransition(new Slide().setDuration(1000));

        madvantage=findViewById(R.id.EMB_edt_adantage);
        mexperience=findViewById(R.id.EMB_edt_experience);
        mgrade=findViewById(R.id.EMBeditGrade);
        mwechart=findViewById(R.id.EMBeditWechat);
        memail=findViewById(R.id.EMBeditEmail);
        madress=findViewById(R.id.EMBeditAdress);

        mtxtadvantage=findViewById(R.id.EMB_textAdvantage_Num);
        mtxtexperience=findViewById(R.id.EMB_textExperience_Num);

        mComplet=findViewById(R.id.EMBbtn_next);
        mBack=findViewById(R.id.EMBimg_back);

        mC1=findViewById(R.id.EMBcb_1);mC2=findViewById(R.id.EMBcb_2);mC3=findViewById(R.id.EMBcb_3);
        mC4=findViewById(R.id.EMBcb_4);mC5=findViewById(R.id.EMBcb_5);mC6=findViewById(R.id.EMBcb_6);
        mC7=findViewById(R.id.EMBcb_7);mC8=findViewById(R.id.EMBcb_8);mC9=findViewById(R.id.EMBcb_9);
        mC10=findViewById(R.id.EMBcb_10);mC11=findViewById(R.id.EMBcb_11);mC12=findViewById(R.id.EMBcb_12);
        mC13=findViewById(R.id.EMBcb_13);mC14=findViewById(R.id.EMBcb_14);mC15=findViewById(R.id.EMBcb_15);

        mSharedPreferences=getSharedPreferences("userdata",MODE_PRIVATE);
        mEditor=mSharedPreferences.edit();

        //初始化信息
        madvantage.setText(mSharedPreferences.getString("advantage",""));
        mexperience.setText(mSharedPreferences.getString("experience",""));
        mgrade.setText(mSharedPreferences.getString("grade",""));
        mwechart.setText(mSharedPreferences.getString("wechart",""));
        memail.setText(mSharedPreferences.getString("email",""));
        madress.setText(mSharedPreferences.getString("adress",""));
        gettag();

        mtxtadvantage.setText(madvantage.length()+"/150");
        mtxtexperience.setText(mexperience.length()+"/150");

        madvantage.addTextChangedListener(new TextWatcher() {  //监听字数变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mtxtadvantage.setText(String.valueOf(s.length())+"/150");
            }
        });
        mexperience.addTextChangedListener(new TextWatcher() {  //监听字数变化
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {
                mtxtexperience.setText(String.valueOf(s.length())+"/150");
            }
        });

        mComplet.setOnClickListener(new View.OnClickListener() {  //点击完成
            @Override
            public void onClick(View v) {
                mComplet.setEnabled(false);
                if(!Empty()){ //判断grade栏是否为空
                    if(memail.getText().toString().equals("")||mwechart.getText().toString().equals("")||madress.getText().toString().equals("")||madvantage.getText().toString().equals("")||mexperience.getText().toString().equals("")){ //判断其他三栏是否为空
                        ColorDialog dialog = new ColorDialog(EditMyBiographical.this);  //为空则跳出提示框
                        dialog.setTitle("提示");
                        dialog.setColor("#ffffff");//颜色
                        dialog.setContentTextColor("#656565");
                        dialog.setTitleTextColor("#656565");
                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
                        dialog.setContentText("简历信息不完善会影响您的组队成功率，是否确定保存并离开？");
                        dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                updataCheckBoxArray();
                                new Thread(new Runnable() {
                                    public void run() {
                                        Updata();  //将信息更新到数据库,执行注册操作
                                    }
                                }).start();
                                dialog.dismiss();
                            }
                        }).setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                dialog.dismiss();
                            }
                        }).show();}
                    else {   //信息填写完整，直接注册
                        updataCheckBoxArray();
                        new Thread(new Runnable() {
                            public void run() {
                                Updata();  //将信息更新到数据库,执行注册操作
                            }
                        }).start();
                    }}
                mComplet.setEnabled(true);
            }
        });

        mBack.setOnClickListener(new View.OnClickListener() {  //点击上方按钮返回
            @Override
            public void onClick(View v) {
                EditMyBiographical.this.finish();
            }
        });

    }

    //初始化用户能力标签
    private void gettag(){
        String Alltag=mSharedPreferences.getString("tag","");
        String[] tagArray=Alltag.split(",");
        for (String s : tagArray) {
            if (s.equals("包装设计")) mC1.setChecked(true);
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

    public boolean Empty(){ //判断grade栏是否为空
        if(mgrade.getText().toString().equals("")){mgrade.setError(getString(R.string.error_empty));return true;}
        else return false;
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

    //更新个人简历
    private void Updata(){

        final String password=mSharedPreferences.getString("password","");
        final String number=mSharedPreferences.getString("number","");
        final String advantage=madvantage.getText().toString();
        final String experience=mexperience.getText().toString();
        final String grade=mgrade.getText().toString();
        final String wechart=mwechart.getText().toString();
        final String email=memail.getText().toString();
        final String adress=madress.getText().toString();

        //showProgress(true);

        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/UpUserdataServlet?number="+number+"&password="+password+"&advantage="+advantage+"&experience="+experience+"&grade="+grade+"&tag="+tag+"&email="+email+"&adress="+adress+"&wechart="+wechart;
        String statetag = "ReNewUserdata";
        //取得请求队列
        RequestQueue ReNewUserdatarequestQueue = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        ReNewUserdatarequestQueue.cancelAll(statetag);

        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest ReNewUserdatarequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("结果");
                            String result = jsonObject.getString("Result");
                            if (result.equals("更新成功")) {
                                mEditor.putString("advantage",advantage);
                                mEditor.putString("grade",grade);
                                mEditor.putString("experience",experience);
                                mEditor.putString("tag",tag);
                                mEditor.putString("wechart",wechart);
                                mEditor.putString("email",email);
                                mEditor.putString("adress",adress);
                                mEditor.apply();
                                Toast toast=Toast.makeText(EditMyBiographical.this,null,Toast.LENGTH_SHORT);
                                toast.setText("更新成功");
                                toast.show();
                                EditMyBiographical.this.finish();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(EditMyBiographical.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                            Log.e("TAG", e.getMessage(), e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(EditMyBiographical.this,"无网络连接,请稍后再试！",Toast.LENGTH_SHORT).show();
                Log.e("TAG", error.getMessage(), error);
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("number", number);
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
        ReNewUserdatarequest.setTag(statetag);
        //将请求添加到队列中
        ReNewUserdatarequestQueue.add(ReNewUserdatarequest);

    }

    /**
     * Shows the progress UI and hides the login form.
     * 显示进度UI并隐藏登录表单。
     */
//    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
//    private void showProgress(final boolean show) {
//        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
//
//        mBtnComplet.setVisibility(show ? View.GONE : View.VISIBLE);
//        mBtnComplet.animate().setDuration(shortAnimTime).alpha(
//                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mBtnComplet.setVisibility(show ? View.GONE : View.VISIBLE);
//            }
//        });
//
//        runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
//        runWebView.animate().setDuration(shortAnimTime).alpha(
//                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
//            }
//        });
//    }

    //点击空白处收起键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
                madvantage.clearFocus();
                mexperience.clearFocus();
                memail.clearFocus();
                madress.clearFocus();
                mgrade.clearFocus();
                mwechart.clearFocus();
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
