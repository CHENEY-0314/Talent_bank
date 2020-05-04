package com.example.talent_bank.user_fragment;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.talent_bank.R;
import com.example.talent_bank.register.RegisterActivity;
import com.example.talent_bank.register.RegisterBasedActivity;

import static com.example.talent_bank.user_fragment.ChangeImageActivity.convertStringToIcon;

public class MyBiographicalActivity extends AppCompatActivity {

    private ImageView imgBack,mimgmore;

    private TextView madvantage,mexperience,mgrade,mwechart,memail,madress;
    private CheckBox mC1,mC2,mC3,mC4,mC5,mC6,mC7,mC8,mC9,mC10,mC11,mC12,mC13,mC14,mC15;

    //以下用于手机存用户信息
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

   @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_biographical);

        imgBack=findViewById(R.id.MBimg_back);
        mimgmore=findViewById(R.id.MBimg_more);

        madvantage=findViewById(R.id.MB_text_advantage);
        mexperience=findViewById(R.id.MB_text_experience);
        mgrade=findViewById(R.id.MB_text_grade);
        mwechart=findViewById(R.id.MB_text_wechart);
        memail=findViewById(R.id.MB_text_email);
        madress=findViewById(R.id.MB_text_adress);

        mC1=findViewById(R.id.MB_cb_1);mC2=findViewById(R.id.MB_cb_2);mC3=findViewById(R.id.MB_cb_3);
        mC4=findViewById(R.id.MB_cb_4);mC5=findViewById(R.id.MB_cb_5);mC6=findViewById(R.id.MB_cb_6);
        mC7=findViewById(R.id.MB_cb_7);mC8=findViewById(R.id.MB_cb_8);mC9=findViewById(R.id.MB_cb_9);
        mC10=findViewById(R.id.MB_cb_10);mC11=findViewById(R.id.MB_cb_11);mC12=findViewById(R.id.MB_cb_12);
        mC13=findViewById(R.id.MB_cb_13);mC14=findViewById(R.id.MB_cb_14);mC15=findViewById(R.id.MB_cb_15);

       mSharedPreferences=getSharedPreferences("userdata",MODE_PRIVATE);

       //初始化信息
       madvantage.setText(mSharedPreferences.getString("advantage",""));
       mexperience.setText(mSharedPreferences.getString("experience",""));
       mgrade.setText(mSharedPreferences.getString("grade",""));
       mwechart.setText(mSharedPreferences.getString("wechart",""));
       memail.setText(mSharedPreferences.getString("email",""));
       madress.setText(mSharedPreferences.getString("adress",""));
       gettag();


       imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                MyBiographicalActivity.this.finish();
            }
        });

       mimgmore.setOnClickListener(new View.OnClickListener() { //点击上方右侧更多
           @Override
           public void onClick(View v) {

               startActivity(new Intent(MyBiographicalActivity.this, EditMyBiographical.class), ActivityOptions.makeSceneTransitionAnimation(MyBiographicalActivity.this).toBundle());

//               new MBDialog(MyBiographicalActivity.this){
//                   @Override
//                   public void btnPickByTake(){
//
//                   }
//               }.show();
           }
       });

    }

    //初始化用户能力标签
    private void gettag(){
       mC1.setVisibility(View.GONE); mC2.setVisibility(View.GONE);mC3.setVisibility(View.GONE);
       mC4.setVisibility(View.GONE);mC5.setVisibility(View.GONE);mC6.setVisibility(View.GONE);
       mC7.setVisibility(View.GONE);mC8.setVisibility(View.GONE);mC9.setVisibility(View.GONE);
       mC10.setVisibility(View.GONE);mC11.setVisibility(View.GONE);mC12.setVisibility(View.GONE);
       mC13.setVisibility(View.GONE);mC14.setVisibility(View.GONE);mC15.setVisibility(View.GONE);

       String Alltag=mSharedPreferences.getString("tag","");
       String[] tagArray=Alltag.split(",");
       for (String s : tagArray) {
            if (s.equals("包装设计")) mC1.setVisibility(View.VISIBLE);
            if (s.equals("平面设计")) mC2.setVisibility(View.VISIBLE);
            if (s.equals("UI设计")) mC3.setVisibility(View.VISIBLE);
            if (s.equals("产品设计")) mC4.setVisibility(View.VISIBLE);
            if (s.equals("英语")) mC5.setVisibility(View.VISIBLE);
            if (s.equals("其他外语")) mC6.setVisibility(View.VISIBLE);
            if (s.equals("视频剪辑")) mC7.setVisibility(View.VISIBLE);
            if (s.equals("演讲能力")) mC8.setVisibility(View.VISIBLE);
            if (s.equals("Photoshop")) mC9.setVisibility(View.VISIBLE);
            if (s.equals("PPT制作")) mC10.setVisibility(View.VISIBLE);
            if (s.equals("C")) mC11.setVisibility(View.VISIBLE);
            if (s.equals("JAVA")) mC12.setVisibility(View.VISIBLE);
            if (s.equals("微信小程序开发")) mC13.setVisibility(View.VISIBLE);
            if (s.equals("Android开发")) mC14.setVisibility(View.VISIBLE);
            if (s.equals("IOS开发")) mC15.setVisibility(View.VISIBLE);
       }
    }

    //回到该页面时自动更新
    @Override
    public void onStart() {
        super.onStart();
        //初始化信息
        madvantage.setText(mSharedPreferences.getString("advantage",""));
        mexperience.setText(mSharedPreferences.getString("experience",""));
        mgrade.setText(mSharedPreferences.getString("grade",""));
        mwechart.setText(mSharedPreferences.getString("wechart",""));
        memail.setText(mSharedPreferences.getString("email",""));
        madress.setText(mSharedPreferences.getString("adress",""));
        gettag();
    }

}
