package com.example.talent_bank.user_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.talent_bank.Adapter.MyApplyAdapter;
import com.example.talent_bank.Adapter.ReceiveApplyAdapter;
import com.example.talent_bank.R;

public class MyApplyActivity extends AppCompatActivity {
    private RecyclerView mRvMain;
    private ImageView imgBack;
    private String shpName = "SHP_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apply);

        imgBack=findViewById(R.id.MAimg_back);
        mRvMain = findViewById(R.id.rv_my_apply);

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                MyApplyActivity.this.finish();
            }
        });
        //为了方便程序测试，暂时用以下代码初始化Num的值，之后需要删除
        SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();
        editor.putInt("myApplyNum_key",3);
        editor.apply();

        mRvMain.setLayoutManager(new LinearLayoutManager(MyApplyActivity.this));
        mRvMain.setAdapter(new MyApplyAdapter(MyApplyActivity.this));


    }
}
