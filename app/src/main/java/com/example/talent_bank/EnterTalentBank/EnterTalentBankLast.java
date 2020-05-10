package com.example.talent_bank.EnterTalentBank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
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
import com.example.talent_bank.register.RegisterLastActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

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
    private SharedPreferences mSha;     //用于手机暂存用户注册信息
    private SharedPreferences.Editor mEdi;

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

        mSha = getSharedPreferences("changedata", MODE_PRIVATE);
        mEdi=mSha.edit();
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

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //点击完成按钮
                if (!Empty()) { //判断grade栏是否为空
                    button.setEnabled(false);
                    if (mETBL_edit_Email.getText().toString().equals("") || mETBL_edit_Wechat.getText().toString().equals("") || mETBL_edit_address.getText().toString().equals("")) { //判断其他三栏是否为空
                        ColorDialog dialog = new ColorDialog(EnterTalentBankLast.this);  //为空则跳出提示框
                        dialog.setTitle("提示");
                        dialog.setColor("#ffffff");//颜色
                        dialog.setContentTextColor("#656565");
                        dialog.setTitleTextColor("#656565");
                        dialog.setContentText("简历信息不完善会影响您的组队成功率，是否确定保存？");
                        dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                JoinTalentBank();  //将信息更新到数据库,执行注册操作
                                dialog.dismiss();
                            }
                        })
                                .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                                    @Override
                                    public void onClick(ColorDialog dialog) {
                                        dialog.dismiss();
                                        button.setEnabled(true);
                                    }
                                }).show();
                    } else {   //信息填写完整，直接注册
                        JoinTalentBank();  //将信息更新到数据库，执行注册操作
                    }
                }

            }

        });
    }

    public boolean Empty(){ //判断grade栏是否为空
        if(mETBL_edit_grade.getText().toString().equals("")){mETBL_edit_grade.setError(getString(R.string.error_empty));return true;}
        else return false;
    }

    //加入人才库
    public void JoinTalentBank(){

        final String number=mShared.getString("number","");
        final String password=mShared.getString("password","");
        final String name=mShared.getString("name","");

        final String advantage=mSha.getString("advantage","");
        final String experience=mSha.getString("experience","");
        final String tags=mSha.getString("tag","");

        final String grade=mETBL_edit_grade.getText().toString();
        final String email=mETBL_edit_Email.getText().toString();
        final String adress=mETBL_edit_address.getText().toString();
        final String wechart=mETBL_edit_Wechat.getText().toString();

        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/JoinTalentBank?number="+number+"&password="+password+"&name="+name+"&advantage="+advantage+"&experience="+experience+"&grade="+grade+"&email="+email+"&adress="+adress+"&wechart="+wechart+"&tag="+tags;
        String tag = "Updata";
        //取得请求队列
        RequestQueue Join = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        Join.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest Joinrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                            mEdi.clear();
                            mEdi.apply();
                            mEditor.putString("advantage",advantage);
                            mEditor.putString("grade",grade);
                            mEditor.putString("experience",experience);
                            mEditor.putString("tag",tags);
                            mEditor.putString("wechart",wechart);
                            mEditor.putString("email",email);
                            mEditor.putString("adress",adress);
                            mEditor.putString("intalent_bank","true");
                            mEditor.apply();
                            Toast toast=Toast.makeText(EnterTalentBankLast.this,null,Toast.LENGTH_SHORT);
                            toast.setText("成功加入");
                            toast.show();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startActivity(new Intent(EnterTalentBankLast.this, MainActivity.class));
                                overridePendingTransition(R.anim.fade_in,R.anim.fade_out);
                                button.setEnabled(true);
                            }
                        },1000);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(EnterTalentBankLast.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
                button.setEnabled(true);
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("number", number);
                params.put("password", password);
                params.put("name", name);
                params.put("advantage", advantage);
                params.put("experience", experience);
                params.put("tags", tags);
                params.put("grade", grade);
                params.put("email", email);
                params.put("adress", adress);
                params.put("wechart", wechart);
                return params;
            }
        };
        //设置Tag标签
        Joinrequest.setTag(tag);
        //将请求添加到队列中
        Join.add(Joinrequest);
    }

}