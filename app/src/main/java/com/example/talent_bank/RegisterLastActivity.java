package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.transition.Fade;
import android.view.View;
import android.widget.Button;
import cn.refactor.lib.colordialog.ColorDialog;


public class RegisterLastActivity extends AppCompatActivity {

    private Button mBtnComplet;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_last);
        mBtnComplet=findViewById(R.id.RLbtn_complet);
        getWindow().setExitTransition(new Fade().setDuration(1000));
        setListeners();
    }
    private void setListeners(){
        RegisterLastActivity.OnClick onClick=new RegisterLastActivity.OnClick();
        mBtnComplet.setOnClickListener(onClick);
    }

    private class OnClick implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent=null;
            if (v.getId() == R.id.RLbtn_complet) {//跳转到主页
                ColorDialog dialog = new ColorDialog(RegisterLastActivity.this);
                dialog.setTitle("提示");
                dialog.setColor("#ffffff");//颜色
                dialog.setContentTextColor("#656565");
                dialog.setTitleTextColor("#656565");
                dialog.setContentText("简历信息不完善会影响您的组队成功率，是否确定保存并离开？");
                dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        startActivity(new Intent(RegisterLastActivity.this, SignUPActivity.class), ActivityOptions.makeSceneTransitionAnimation(RegisterLastActivity.this).toBundle());
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
        }
    }
}
