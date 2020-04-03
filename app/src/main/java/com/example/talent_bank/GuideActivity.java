package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class GuideActivity extends AppCompatActivity implements GestureDetector.OnGestureListener {

    private ViewFlipper mVFActivity;
    private GestureDetector mGestureDetector;
    private TextView tvInNew;
    private String shpName = "SHP_NAME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mVFActivity=findViewById(R.id.vf_activity);
        tvInNew=findViewById(R.id.tvInNew);
        SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = shp.edit();

        boolean fristload = shp.getBoolean("fristload_key",true);
        if (fristload) {  //如果是第一次登录，开启引导界面
            initView();
            GuideActivity.this.finish();  //删除当前页面
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);  //更改跳转动画
            editor.putBoolean("fristload_key",false);
            editor.apply();
        } else {  //反之，直接打开登录界面
            toMain();
            GuideActivity.this.finish();
        }
    }

    @SuppressWarnings("deprecation")
    private void initView() {
        mGestureDetector = new GestureDetector(this);
        //界面跳转
        tvInNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GuideActivity.this,LoginActivity.class), ActivityOptions.makeSceneTransitionAnimation(GuideActivity.this).toBundle());
            }
        });
    }

    private void toMain() {
        Intent intent = new Intent(GuideActivity.this,LoginActivity.class);
        startActivity(intent);
    }

    @Override
    public boolean onDown(MotionEvent e) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX()-e2.getX()>120) {
            mVFActivity.setInAnimation(this, R.anim.in_leftright);
            mVFActivity.setOutAnimation(this, R.anim.out_leftright);
            mVFActivity.showNext();
        } else if (e1.getX()-e2.getY()<-120) {
            mVFActivity.setInAnimation(this, R.anim.in_rightleft);
            mVFActivity.setOutAnimation(this, R.anim.out_rightleft);
            mVFActivity.showPrevious();
        } else {
            return false;
        }
        return true;
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return mGestureDetector.onTouchEvent(event);
    }

}
