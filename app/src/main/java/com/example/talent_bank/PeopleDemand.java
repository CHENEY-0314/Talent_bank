package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.talent_bank.Adapter.PeopleDemandAdapter;
import com.example.talent_bank.Adapter.ReceiveApplyAdapter;

public class PeopleDemand extends AppCompatActivity {
    private ImageView imgBack;
    private RecyclerView mRvMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_demand);

        imgBack=findViewById(R.id.PDimg_back);
        mRvMain = findViewById(R.id.rv_people_demand);

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                PeopleDemand.this.finish();
            }
        });
        mRvMain.setLayoutManager(new LinearLayoutManager(PeopleDemand.this));
        mRvMain.setAdapter(new PeopleDemandAdapter(PeopleDemand.this));
    }
}
