package com.example.talent_bank.EnterTalentBank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.transition.Slide;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.talent_bank.R;
import com.example.talent_bank.SignUPActivity;
import com.example.talent_bank.register.RegisterActivity;
import com.example.talent_bank.register.RegisterLastActivity;

public class EnterTalentBank extends AppCompatActivity {
    private ImageView imgBack;
    private Button button;

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
    private SharedPreferences Userfochange;     //用于手机暂存用户注册信息
    private SharedPreferences.Editor UserfochangeEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_talent_bank);

        imgBack = findViewById(R.id.ETB_back);
        button = findViewById(R.id.ETB_btn_next);

        mEditadvantage=findViewById(R.id.ETB_edt_advantage);
        mEditexperience=findViewById(R.id.ETB_edt_experience);

        mC1=findViewById(R.id.ETB_cb_1);
        mC2=findViewById(R.id.ETB_cb_2);
        mC3=findViewById(R.id.ETB_cb_3);
        mC4=findViewById(R.id.ETB_cb_4);
        mC5=findViewById(R.id.ETB_cb_5);
        mC6=findViewById(R.id.ETB_cb_6);
        mC7=findViewById(R.id.ETB_cb_7);
        mC8=findViewById(R.id.ETB_cb_8);
        mC9=findViewById(R.id.ETB_cb_9);
        mC10=findViewById(R.id.ETB_cb_10);
        mC11=findViewById(R.id.ETB_cb_11);
        mC12=findViewById(R.id.ETB_cb_12);
        mC13=findViewById(R.id.ETB_cb_13);
        mC14=findViewById(R.id.ETB_cb_14);
        mC15=findViewById(R.id.ETB_cb_15);

        mShared=getSharedPreferences("userdata",MODE_PRIVATE);
        mEditor=mShared.edit();

        Userfochange=getSharedPreferences("changedata",MODE_PRIVATE);
        UserfochangeEditor=Userfochange.edit();

        //初始化输入框内容
        mEditexperience.setText(mShared.getString("experience",""));
        mEditadvantage.setText(mShared.getString("advantage",""));
        settag();//初始化能力标签

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                EnterTalentBank.this.finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击进入下一页面
                //存储当前页面信息
                updataCheckBoxArray(); //更新tag记录
                if(!ifEmpty()){
                UserfochangeEditor.putString("experience",mEditexperience.getText().toString());
                UserfochangeEditor.putString("advantage",mEditadvantage.getText().toString());
                UserfochangeEditor.putString("tag",tag);
                UserfochangeEditor.apply();
                startActivity(new Intent(EnterTalentBank.this, EnterTalentBankLast.class), ActivityOptions.makeSceneTransitionAnimation(EnterTalentBank.this).toBundle());
            }}
        });
    }

    //将tag的记录转化为能力标签
    public void settag(){
        tag=mShared.getString("tag","");
        String[] strarr = tag.split(",");
        for (String s : strarr) {
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

    //判断是否已经填好数据
    public boolean ifEmpty(){
        if(mEditexperience.getText().toString().equals("")){mEditexperience.setError(getString(R.string.error_empty));return true;}
        else if(mEditadvantage.getText().toString().equals("")){mEditadvantage.setError(getString(R.string.error_empty));return true;}
        else if(tag.equals("")){
            Toast toast=Toast.makeText(EnterTalentBank.this,null,Toast.LENGTH_SHORT);
            toast.setText("请选择您的能力标签");
            toast.show();return true;}
        else return false;
    }

}
