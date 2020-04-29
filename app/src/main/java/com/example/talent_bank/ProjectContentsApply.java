package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.talent_bank.user_fragment.SetUpActivity;

public class ProjectContentsApply extends AppCompatActivity {
    private ImageView imgBack;
    private Button button;
    private TextView textView ,project_name,project_content;

    private int Curr_PJ;//用于标识点进来的项目是哪个项目

    private SharedPreferences AllProjectData;
    private SharedPreferences.Editor AllProjectDataEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_contents_apply);

        imgBack = findViewById(R.id.PCAimg_back);
        button = findViewById(R.id.PCA_btn_detail);
        textView = findViewById(R.id.PCA_num);
        project_name = findViewById(R.id.PCA_text_name);
        project_content = findViewById(R.id.PCA_text_content);

        AllProjectData = getSharedPreferences("all_project_data",MODE_PRIVATE);
        AllProjectDataEditor = AllProjectData.edit();

        Curr_PJ = AllProjectData.getInt("curr_pj",0);

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                //清除当前项目的标记符curr_pj
                AllProjectDataEditor.putInt("curr_pj",-1);
                AllProjectDataEditor.apply();
                ProjectContentsApply.this.finish();
            }
        });

        //初始化当前项目信息
        Initializing();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //点击查看队友要求
                Intent intent = new Intent(ProjectContentsApply.this,PeopleDemand.class);
                startActivity(intent);
            }
        });
    }

    //用于初始化当前项目信息
    public void Initializing(){
        String count_member = AllProjectData.getString("count_member","");
        String pj_name = AllProjectData.getString("pj_name","");
        String pj_introduce = AllProjectData.getString("pj_introduce","");

        String[] count_memberstrarr = count_member.split("~");
        String[] pj_namestrarr = pj_name.split("~");
        String[] pj_introducestrarr = pj_introduce.split("~");

        project_name.setText(pj_namestrarr[Curr_PJ]);
        project_content.setText(pj_introducestrarr[Curr_PJ]);
        String textNum = "参与人数：" + String.valueOf(count_memberstrarr[Curr_PJ]) + " 人";
        textView.setText(textNum);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {   //重写返回函数
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //清除当前项目的标记符curr_pj
            AllProjectDataEditor.putInt("curr_pj",-1);
            AllProjectDataEditor.apply();
            ProjectContentsApply.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
