package com.example.talent_bank.MyApply;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

import com.example.talent_bank.R;

public abstract class MyDialog extends Dialog implements View.OnClickListener {
    private Activity activity;
    private RelativeLayout btnPickBySelect, btnPickByTake;

    public MyDialog(Activity activity) {
        super(activity, R.style.ActionSheetDialogStyle);
        this.activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_dialog);

        btnPickBySelect = findViewById(R.id.btnPickBySelect);
        btnPickByTake = findViewById(R.id.btnPickByTake);

        btnPickBySelect.setOnClickListener(this);
        btnPickByTake.setOnClickListener(this);

        setViewLocation();
        setCanceledOnTouchOutside(true);//外部点击取消
    }

    /**
     * 设置dialog位于屏幕底部
     */
    private void setViewLocation(){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;

        Window window = this.getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.x = 0;
        lp.y = height;
        lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
        lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        // 设置显示位置
        onWindowAttributesChanged(lp);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPickBySelect:
                btnPickBySelect();
                this.cancel();
                break;
            case R.id.btnPickByTake:
                btnPickByTake();
                this.cancel();
                break;
        }
    }

    public abstract void btnPickBySelect();//按下第一个按钮的事件
    public abstract void btnPickByTake();  //按下第二个按钮的事件

}
