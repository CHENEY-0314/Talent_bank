package com.example.talent_bank.user_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.talent_bank.R;
import com.example.talent_bank.StaggeredGridAdapter;

public class MyPublishActivity extends AppCompatActivity {

    private ImageView imgBack;
    private RecyclerView mRvMain;
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish);

        imgBack=findViewById(R.id.MPimg_back);

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                MyPublishActivity.this.finish();
            }
        });

        mRvMain = findViewById(R.id.rv_my_publish);
        mRvMain.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
        mRvMain.setAdapter(new StaggeredGridAdapter(MyPublishActivity.this)); //对RecyclerView设置适配器
    }
}
