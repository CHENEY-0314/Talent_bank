package com.example.talent_bank;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import com.example.talent_bank.home_page.HomeFragment;
import com.example.talent_bank.home_page.MainFragment;
import com.example.talent_bank.home_page.NewsFragment;
import com.example.talent_bank.home_page.TalkingFragment;
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
        mFragments.add(TalkingFragment.newInstance());
        mFragments.add(NewsFragment.newInstance());
        mFragments.add(HomeFragment.newInstance());
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            menuItem = item;
            switch (item.getItemId()) {
                case R.id.mainFragment:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.talkingFragment:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.newsFragment:
                    viewPager.setCurrentItem(2);
                    return true;
                case R.id.homeFragment:
                    viewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };



}
