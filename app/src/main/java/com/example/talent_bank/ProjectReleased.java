package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ProjectReleased extends AppCompatActivity {
    private ImageView imgBack;
    private Button button,btn1,btn2;
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
        setContentView(R.layout.activity_project_released);

        imgBack = findViewById(R.id.pr_back);
        button = findViewById(R.id.pr_released);
        btn1 = findViewById(R.id.PR_btn1);
        btn2 = findViewById(R.id.PR_btn2);
        textView = findViewById(R.id.PR_num);

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                ProjectReleased.this.finish();
            }
        });
        button.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                Intent intent = new Intent(ProjectReleased.this,ProjectReleased2.class);
                startActivity(intent);
            }
        });

        final SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shp.edit();
        editor.putInt("demandNum_key",0);  //初始化为0
        editor.apply();
        btn1.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击"-"按钮
                int x = shp.getInt("demandNum_key",0) - 1;
                editor.putInt("demandNum_key",x);
                editor.apply();
                String textNum = String.valueOf(shp.getInt("demandNum_key",0));
                textView.setText(textNum);
            }
        });
        btn2.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击"+"按钮
                int x = shp.getInt("demandNum_key",0) + 1;
                editor.putInt("demandNum_key",x);
                editor.apply();
                String textNum = String.valueOf(shp.getInt("demandNum_key",0));
                textView.setText(textNum);
            }
        });

    }


    //点击空白处收起键盘
    @Override
    public boolean dispatchTouchEvent(MotionEvent me) {
        if (me.getAction() == MotionEvent.ACTION_DOWN) {  //把操作放在用户点击的时候
            View v = getCurrentFocus();      //得到当前页面的焦点,ps:有输入框的页面焦点一般会被输入框占据
            if (isShouldHideKeyboard(v, me)) { //判断用户点击的是否是输入框以外的区域
                hideKeyboard(v.getWindowToken());   //收起键盘
            }
        }
        return super.dispatchTouchEvent(me);
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v
     * @param event
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        if (v != null && (v instanceof EditText)) {  //判断得到的焦点控件是否包含EditText
            int[] l = {0, 0};
            v.getLocationInWindow(l);
            int left = l[0],    //得到输入框在屏幕中上下左右的位置
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                // 点击位置如果是EditText的区域，忽略它，不收起键盘。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略
        return false;
    }
    /**
     * 获取InputMethodManager，隐藏软键盘
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            InputMethodManager im = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            im.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }
}
