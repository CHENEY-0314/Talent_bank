package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import cn.refactor.lib.colordialog.ColorDialog;


public class RegisterLastActivity extends AppCompatActivity {

    private ImageView mImgback;
    private Button mBtnComplet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setExitTransition(new Slide().setDuration(1000));
        setContentView(R.layout.activity_register_last);
        mBtnComplet = findViewById(R.id.RLbtn_complet);
        mImgback = findViewById(R.id.RLimg_back);
        setListeners();
    }

    private void setListeners() {
        RegisterLastActivity.OnClick onClick = new RegisterLastActivity.OnClick();
        mBtnComplet.setOnClickListener(onClick);
        mImgback.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.RLbtn_complet:
                    ColorDialog dialog = new ColorDialog(RegisterLastActivity.this);
                    dialog.setTitle("提示");
                    dialog.setColor("#ffffff");//颜色
                    dialog.setContentTextColor("#656565");
                    dialog.setTitleTextColor("#656565");
                    dialog.setContentText("简历信息不完善会影响您的组队成功率，是否确定保存并离开？");
                    dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            startActivity(new Intent(RegisterLastActivity.this, SignUPActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            dialog.dismiss();
                        }
                    })
                            .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
                case R.id.RLimg_back:
                    //返回上一界面（保存当前页面数据）
                    startActivity(new Intent(RegisterLastActivity.this, RegisterActivity.class));
                    break;
            }
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            startActivity(new Intent(RegisterLastActivity.this, RegisterActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterLastActivity.this).toBundle());
        }
        return super.onKeyDown(keyCode, event);
    }
}
