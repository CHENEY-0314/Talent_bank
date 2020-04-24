package com.example.talent_bank.TalentBank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.talent_bank.Adapter.ReceiveApplyAdapter;
import com.example.talent_bank.Adapter.TablentBankAdapter;
import com.example.talent_bank.Adapter.TalentBankAdapter;
import com.example.talent_bank.R;
import com.example.talent_bank.ReceiveApply;

import java.util.ArrayList;

import q.rorbin.verticaltablayout.VerticalTabLayout;
import q.rorbin.verticaltablayout.adapter.TabAdapter;
import q.rorbin.verticaltablayout.widget.ITabView;

public class TalentBank extends AppCompatActivity {
    private RecyclerView recyclerView;
    private LinearLayout linearLayout;
    private ImageView imgBack,imgOpen;
    private String shpName = "SHP_NAME";
    private VerticalTabLayout mTab;
    private ViewPager mVp;
    private ArrayList<Fragment> mFragmentsList;
    private ArrayList<String> mTitlesList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_talent_bank);

        initView();
        initData();

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                TalentBank.this.finish();
            }
        });
        final SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = shp.edit();
        //初始化key“ifOpenTable_key”的值为false，每次打开这个界面，table应该处于关闭状态
        editor.putBoolean("ifOpenTable_key",false);
        editor.apply();
        //初始化图标的src
        imgOpen.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);
        //初始化默认打开时，Table处于关闭状态
        linearLayout.setVisibility(View.GONE);
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        linearLayout.animate().setDuration(shortAnimTime).alpha(
                0 ).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                linearLayout.setVisibility( View.GONE );
            }
        });

        imgOpen.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                boolean x = shp.getBoolean("ifOpenTable_key",false);
                if (x) { //在处于打开的状态下按下按钮
                    imgOpen.setImageResource(R.drawable.ic_arrow_drop_down_black_24dp);  //改变图标
                    editor.putBoolean("ifOpenTable_key",false);
                    editor.apply();
                    //隐藏Table
                    linearLayout.setVisibility(View.GONE);
                    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
                    linearLayout.animate().setDuration(shortAnimTime).alpha(
                            0 ).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            linearLayout.setVisibility( View.GONE );
                        }
                    });
                } else {  //在处于关闭状态下按下按钮
                    imgOpen.setImageResource(R.drawable.ic_arrow_drop_up_black_24dp);  //改变图标
                    editor.putBoolean("ifOpenTable_key",true);
                    editor.apply();
                    //打开显示Table
                    linearLayout.setVisibility(View.VISIBLE);
                    int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
                    linearLayout.animate().setDuration(shortAnimTime).alpha(
                            1 ).setListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            linearLayout.setVisibility( View.VISIBLE );
                        }
                    });
                }
            }
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(TalentBank.this));
        recyclerView.setAdapter(new TalentBankAdapter(TalentBank.this));
    }

    private void initView() {
        imgBack = findViewById(R.id.talent_bank_back);
        imgOpen = findViewById(R.id.talent_bank_more);
        linearLayout = findViewById(R.id.talent_bank_layout);
        mTab = findViewById(R.id.talent_bank_tabLayout);
        mVp = findViewById(R.id.talent_bank_viewPager);
        recyclerView = findViewById(R.id.rv_talent_bank);

        mTitlesList = new ArrayList<>(); //初始化Tab栏数据
        mTitlesList.add("设计");
        mTitlesList.add("开发");
        mTitlesList.add("办公");
        mTitlesList.add("其他");

        mFragmentsList = new ArrayList<>(); //添加Fragment
        mFragmentsList.add(new TalentBankFragment1());
        mFragmentsList.add(new TalentBankFragment2());
        mFragmentsList.add(new TalentBankFragment3());
        mFragmentsList.add(new TalentBankFragment4());

        //创建适配器
        TablentBankAdapter myPagerAdapter = new TablentBankAdapter(getSupportFragmentManager());
        myPagerAdapter.SetSubFragments(mFragmentsList,mTitlesList);
        mVp.setAdapter(myPagerAdapter);
        mTab.setupWithViewPager(mVp);
    }

    private void initData() {
        mTab.setTabAdapter(new TabAdapter() {
            @Override
            public int getCount() {
                return mTitlesList.size();
            }

            @Override
            public ITabView.TabBadge getBadge(int position) {
                return null;
            }

            @Override
            public ITabView.TabIcon getIcon(int position) {
                return null;
            }

            @Override
            public ITabView.TabTitle getTitle(int position) {
                ITabView.TabTitle title = new ITabView.TabTitle.Builder()
                        .setContent(mTitlesList.get(position))  //设置数据
                        .build();
                return title;
            }

            @Override
            public int getBackground(int position) {
                return 0;   //选中的背景颜色
            }
        });

    }
}
