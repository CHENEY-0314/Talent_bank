package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
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
import android.widget.Toast;

import com.example.talent_bank.register.RegisterActivity;
import com.example.talent_bank.register.RegisterBasedActivity;
import com.example.talent_bank.register.RegisterLastActivity;

import cn.refactor.lib.colordialog.ColorDialog;

public class ProjectContents extends AppCompatActivity {
    private ImageView imgBack;
    private ImageView imgMore;
    private Button button1,button2,button3;
    private TextView textView;
    private String shpName = "SHP_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_contents);
        //设置head的img监听
        imgBack = findViewById(R.id.PCimg_back);
        imgMore = findViewById(R.id.PCimg_more);
        button1 = findViewById(R.id.PC_btn_num);
        button2 = findViewById(R.id.PC_btn_demand);
        button3 = findViewById(R.id.PC_btn_apply);
        textView = findViewById(R.id.PC_num);

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //点击跳转界面
                startActivity(new Intent(ProjectContents.this,EditProjectNum.class), ActivityOptions.makeSceneTransitionAnimation(ProjectContents.this).toBundle());
            }
        });

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //点击跳转界面
                startActivity(new Intent(ProjectContents.this,EditPeopleDemand.class), ActivityOptions.makeSceneTransitionAnimation(ProjectContents.this).toBundle());
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //点击跳转界面
                startActivity(new Intent(ProjectContents.this,ReceiveApply.class), ActivityOptions.makeSceneTransitionAnimation(ProjectContents.this).toBundle());
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                ProjectContents.this.finish();
            }
        });

        SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        String textNum = "参与人数：" + String.valueOf(shp.getInt("editNum_key",0)) + "人";
        textView.setText(textNum);

        imgMore.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击右上角更多的按钮
                new MyDialog(ProjectContents.this){
                    @Override
                    public void btnPickByTake(){
                        //点击弹窗”结束招聘“时做的事
                        ColorDialog dialog = new ColorDialog(ProjectContents.this);
                        dialog.setTitle("提示");
                        dialog.setColor("#ffffff");//颜色
                        dialog.setContentTextColor("#656565");
                        dialog.setTitleTextColor("#656565");
                        dialog.setContentText("是否确定结束招聘？结束项目招聘后将不能再开启。");
                        dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                //结束招聘的操作
                                dialog.dismiss();
                            }
                        })
                                .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                                    @Override
                                    public void onClick(ColorDialog dialog) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }
                    @Override
                    public void btnPickBySelect() {
                        //点击弹窗”删除项目“时做的事
                        ColorDialog dialog = new ColorDialog(ProjectContents.this);
                        dialog.setTitle("提示");
                        dialog.setColor("#ffffff");//颜色
                        dialog.setContentTextColor("#656565");
                        dialog.setTitleTextColor("#656565");
                        dialog.setContentText("是否确定删除项目？删除后将不能再找回。");
                        dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                //删除项目的操作
                                dialog.dismiss();
                            }
                        })
                                .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                                    @Override
                                    public void onClick(ColorDialog dialog) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }

                }.show();
            }
        });


    }

    @Override
    protected void onRestart() {
        super.onRestart();
        textView = findViewById(R.id.PC_num);
        SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        String textNum = "参与人数：" + String.valueOf(shp.getInt("editNum_key",0)) + "人";
        textView.setText(textNum);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {   //重写返回函数
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            ProjectContents.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
