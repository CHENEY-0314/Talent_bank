package com.example.talent_bank.user_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.transition.Slide;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.Adapter.MyPublishAdapter;
import com.example.talent_bank.HandlerActivity;
import com.example.talent_bank.LoginActivity;
import com.example.talent_bank.R;
import com.example.talent_bank.SignUPActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import pl.droidsonroids.gif.GifImageView;

public class MyPublishActivity extends AppCompatActivity {

    private ImageView imgBack;
    private RecyclerView mRvMain;
    private TextView textView;

    private LinearLayout runWebView;

    //以下用于手机存用户信息
    private SharedPreferences UseforUserData;
    private SharedPreferences UseforProjectData;
    private SharedPreferences.Editor ProjectDataEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_publish);
        runWebView=findViewById(R.id.MP_loading);
        imgBack=findViewById(R.id.MPimg_back);

        //设置页面跳转动画
        getWindow().setExitTransition(new Slide().setDuration(1000));
        getWindow().setEnterTransition(new Slide().setDuration(1000));


        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                //清空暂存的项目信息
                ProjectDataEditor.clear();
                ProjectDataEditor.apply();
                MyPublishActivity.this.finish();
            }
        });

        UseforUserData=getSharedPreferences("userdata",MODE_PRIVATE);
        UseforProjectData=getSharedPreferences("projectdata",MODE_PRIVATE);
        ProjectDataEditor=UseforProjectData.edit();
        mRvMain = findViewById(R.id.rv_my_publish);
        showProgress(true);
        loadingProject();
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
        runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
        runWebView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    //加载当前用户项目基本信息到手机暂存
    public void loadingProject(){

        final String number=UseforUserData.getString("number","");
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/GetProjectByNumber?number="+number;
        String tag = "GetProject";
        //取得请求队列
        RequestQueue GetProject = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        GetProject.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest GetProjectrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            ProjectDataEditor.putString("pj_id",jsonObject.getString("pj_id"));
                            ProjectDataEditor.putString("pj_name",jsonObject.getString("pj_name"));
                            ProjectDataEditor.putString("pj_introduce",jsonObject.getString("pj_introduce"));
                            ProjectDataEditor.putString("count_member",jsonObject.getString("count_member"));
                            ProjectDataEditor.apply();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showProgress(false);
                                    mRvMain.setLayoutManager(new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL));
                                    mRvMain.setAdapter(new MyPublishAdapter(MyPublishActivity.this)); //对RecyclerView设置适配器
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(MyPublishActivity.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                            MyPublishActivity.this.finish();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(MyPublishActivity.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
                MyPublishActivity.this.finish();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("number", number);
                return params;
            }
        };
        //设置Tag标签
        GetProjectrequest.setTag(tag);
        //将请求添加到队列中
        GetProject.add(GetProjectrequest);

    }

    //重写返回函数
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //清空暂存的项目信息
            ProjectDataEditor.clear();
            ProjectDataEditor.apply();
            MyPublishActivity.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
