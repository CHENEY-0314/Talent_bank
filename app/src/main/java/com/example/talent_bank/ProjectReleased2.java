package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.talent_bank.Adapter.EditLinearAdapter;
import com.example.talent_bank.Adapter.ProjectReleasedAdapter;

public class ProjectReleased2 extends AppCompatActivity {
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
        setContentView(R.layout.activity_project_released2);

        imgBack = findViewById(R.id.PR2_back);
        mRvMain = findViewById(R.id.rv_project_released2);
        mRvMain.setLayoutManager(new LinearLayoutManager(ProjectReleased2.this));
        mRvMain.setAdapter(new ProjectReleasedAdapter(ProjectReleased2.this));

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                ProjectReleased2.this.finish();
            }
        });
    }
}
