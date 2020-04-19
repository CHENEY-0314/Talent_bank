package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditDemand extends AppCompatActivity {
    private ImageView imgBack;
    private Button btn;
    private EditText editText;
    private String shpName = "SHP_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_demand);
        imgBack = findViewById(R.id.EDimg_back);
        btn = findViewById(R.id.ED_save);
        editText = findViewById(R.id.ED_edit);

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                EditDemand.this.finish();
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击保存按钮
                SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = shp.edit();
                String x = String.valueOf(editText.getText());
                editor.putString("editDemand_key",x);
                editor.apply();
                Intent intent = new Intent(EditDemand.this,EditPeopleDemand.class);
                startActivity(intent);
            }
        });
    }
}
