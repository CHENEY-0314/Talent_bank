package com.example.talent_bank.TalentBank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.Adapter.FindingAdapter;
import com.example.talent_bank.Adapter.ReceiveApplyAdapter;
import com.example.talent_bank.Adapter.TablentBankAdapter;
import com.example.talent_bank.Adapter.TalentBankAdapter;
import com.example.talent_bank.R;
import com.example.talent_bank.ReceiveApply;
import com.example.talent_bank.home_page.FindFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private LinearLayout runWebView;

    private CheckBox mC1;   //包装设计
    private CheckBox mC2;   //平面设计
    private CheckBox mC3;   //UI设计
    private CheckBox mC4;   //产品设计
    private CheckBox mC5;   //英语
    private CheckBox mC6;   //其他外语
    private CheckBox mC7;   //视频剪辑
    private CheckBox mC8;   //演讲能力
    private CheckBox mC9;   //Photoshop
    private CheckBox mC10;   //PPT制作
    private CheckBox mC11;   //C++
    private CheckBox mC12;   //JAVA
    private CheckBox mC13;   //微信小程序开发
    private CheckBox mC14;   //Android开发
    private CheckBox mC15;   //IOS开发

    //以下用于手机存用户信息
    private SharedPreferences AllUsersData;
    private SharedPreferences.Editor AllUsersDataEditor;

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

        AllUsersData = getSharedPreferences("all_users_data",MODE_PRIVATE);
        AllUsersDataEditor = AllUsersData.edit();
        AllUsersDataEditor.clear();
        AllUsersDataEditor.apply();
        showProgress(true);
        loadingUsers();

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
    }

    private void initView() {
        imgBack = findViewById(R.id.talent_bank_back);
        imgOpen = findViewById(R.id.talent_bank_more);
        linearLayout = findViewById(R.id.talent_bank_layout);
        mTab = findViewById(R.id.talent_bank_tabLayout);
        mVp = findViewById(R.id.talent_bank_viewPager);
        recyclerView = findViewById(R.id.rv_talent_bank);
        runWebView = findViewById(R.id.talent_bank_loading);


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


    /**
     * Shows the progress UI and hides the login form.
     * 显示进度UI并隐藏登录表单。
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        //在Honeycomb MR2上，我们有ViewPropertyAnimator API，可以实现非常简单的动画。如果可用，请使用这些API淡入进度微调器。
        
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        recyclerView.setVisibility(!show ? View.VISIBLE : View.GONE);
        runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
        runWebView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
        recyclerView.animate().setDuration(shortAnimTime).alpha(
                !show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                recyclerView.setVisibility(!show ? View.VISIBLE : View.GONE);
            }
        });

    }

    //加载项目基本信息到手机暂存
    public void loadingUsers(){
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/GetAllUser";
        String tag = "GetUser";
        //取得请求队列
        RequestQueue GetUser = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        GetUser.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest GetUserrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            AllUsersDataEditor.putString("user_name",jsonObject.getString("name"));
                            AllUsersDataEditor.putString("user_wechart",jsonObject.getString("wechart"));
                            AllUsersDataEditor.putString("user_adress",jsonObject.getString("adress"));
                            AllUsersDataEditor.putString("user_advantage",jsonObject.getString("advantage"));
                            AllUsersDataEditor.putString("user_email",jsonObject.getString("email"));
                            AllUsersDataEditor.putString("user_tag",jsonObject.getString("tag"));
                            AllUsersDataEditor.putString("user_grade",jsonObject.getString("grade"));
                            AllUsersDataEditor.putString("user_number",jsonObject.getString("number"));
                            AllUsersDataEditor.putString("user_experience",jsonObject.getString("experience"));
                            AllUsersDataEditor.apply();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showProgress(false);
                                    recyclerView.setLayoutManager(new LinearLayoutManager(TalentBank.this));
                                    recyclerView.setAdapter(new TalentBankAdapter(TalentBank.this));
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(TalentBank.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(TalentBank.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        //设置Tag标签
        GetUserrequest.setTag(tag);
        //将请求添加到队列中
        GetUser.add(GetUserrequest);
    }
}
