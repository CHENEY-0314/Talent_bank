package com.example.talent_bank.MyApply;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

import com.example.talent_bank.R;
import com.example.talent_bank.ReceiveApplyAdapter;

public class ReceiveApply extends AppCompatActivity {
    private RecyclerView mRvMain;
    private ImageView imgBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_apply);

        imgBack = findViewById(R.id.RAimg_back);
        mRvMain = findViewById(R.id.rv_receive_apply);
        mRvMain.setLayoutManager(new LinearLayoutManager(ReceiveApply.this));
        mRvMain.setAdapter(new ReceiveApplyAdapter(ReceiveApply.this));

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                ReceiveApply.this.finish();
            }
        });
    }
}
