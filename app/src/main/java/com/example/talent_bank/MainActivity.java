package com.example.talent_bank;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.talent_bank.Adapter.ViewPagerAdapterForNav;
import com.example.talent_bank.home_page.UserFragment;
import com.example.talent_bank.home_page.MainFragment;
import com.example.talent_bank.home_page.NewsFragment;
import com.example.talent_bank.home_page.FindFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    //Fragment集合
    List<Fragment> mFragments;
    //组件
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    //适配器
    private ViewPagerAdapterForNav mViewPagerAdapterForNav;
    private MenuItem menuItem;

    private long exitTime=0;  //用于判断两次点击退出主页的事件间隔

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //实例化组件
        initElement();
        //初始化Fragment集合
        initFragments();
        //导航栏与ViewPager联动
        //给底部导航栏和ViewPager设置监听器
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (menuItem != null) {
                    menuItem.setChecked(false);
                } else {
                    bottomNavigationView.getMenu().getItem(0).setChecked(false);
                }
                menuItem = bottomNavigationView.getMenu().getItem(position);
                menuItem.setChecked(true);

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        //为ViewPager设置适配器
        mViewPagerAdapterForNav = new ViewPagerAdapterForNav(getSupportFragmentManager());
        viewPager.setAdapter(mViewPagerAdapterForNav);
        mViewPagerAdapterForNav.setFragment(mFragments);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        bottomNavigationView.setItemTextAppearanceInactive(R.style.bottom_normal_text);
    }

    private void initElement() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView2);
        viewPager = findViewById(R.id.viewPager);
    }

    private void initFragments() {
        mFragments = new ArrayList<>();
        mFragments.add(MainFragment.newInstance());
        mFragments.add(FindFragment.newInstance());
        mFragments.add(NewsFragment.newInstance());
        mFragments.add(UserFragment.newInstance());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.mainFragment:
                    viewPager.setCurrentItem(0,false);
                    return true;
                case R.id.talkingFragment:
                    viewPager.setCurrentItem(1,false);
                    return true;
                case R.id.newsFragment:
                    viewPager.setCurrentItem(2,false);
                    return true;
                case R.id.homeFragment:
                    viewPager.setCurrentItem(3,false);
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {   //重写返回函数以实现点击两次退出APP
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exit() {
        if ((System.currentTimeMillis() - exitTime) > 2000) {
            Toast toast=Toast.makeText(MainActivity.this,null,Toast.LENGTH_SHORT);
            toast.setText("再按一次退出程序");
            toast.show();
            exitTime = System.currentTimeMillis();
        } else {
            finish();
            overridePendingTransition(R.anim.fade_in,R.anim.fade_out);  //更改跳转动画
        }
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
