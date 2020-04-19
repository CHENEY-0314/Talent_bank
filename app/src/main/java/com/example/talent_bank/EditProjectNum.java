package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.SavedStateViewModelFactory;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.talent_bank.viewmodel.HomeViewModel;

public class EditProjectNum extends AppCompatActivity {
    TextView textView;
    Button btn1,btn2;

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
        setContentView(R.layout.activity_edit_project_num);
        textView = findViewById(R.id.ED_num);
        btn1 = findViewById(R.id.ED_btn1);
        btn2 = findViewById(R.id.ED_btn2);

        imgBack = findViewById(R.id.EPNimg_back);
        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                EditProjectNum.this.finish();
            }
        });

        final SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shp.edit();
        String textNum = String.valueOf(shp.getInt("editNum_key",0));

        textView.setText(textNum);
        btn1.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击"-"按钮
                int x = shp.getInt("editNum_key",0) - 1;
                editor.putInt("editNum_key",x);
                editor.apply();
                String textNum = String.valueOf(shp.getInt("editNum_key",0));
                textView.setText(textNum);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击"+"按钮
                int x = shp.getInt("editNum_key",0) + 1;
                editor.putInt("editNum_key",x);
                editor.apply();
                String textNum = String.valueOf(shp.getInt("editNum_key",0));
                textView.setText(textNum);
            }
        });

    }


}
