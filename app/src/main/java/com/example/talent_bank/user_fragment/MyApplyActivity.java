package com.example.talent_bank.user_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.Adapter.MyApplyAdapter;
import com.example.talent_bank.Adapter.NewsAdapter;
import com.example.talent_bank.Adapter.ReceiveApplyAdapter;
import com.example.talent_bank.R;
import com.example.talent_bank.home_page.NewsFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyApplyActivity extends AppCompatActivity {
    private RecyclerView mRvMain;
    private ImageView imgBack;
    private LinearLayout runWebView;

    //以下用于手机存用户信息
    private SharedPreferences AllApplyData;
    private SharedPreferences.Editor AllApplyDataEditor;

    private SharedPreferences UserData;

    private SharedPreferences AllProjectData;
    private SharedPreferences.Editor AllProjectDataEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_apply);

        imgBack=findViewById(R.id.MAimg_back);
        mRvMain = findViewById(R.id.rv_my_apply);
        runWebView = findViewById(R.id.my_apply_loading);
        UserData = getSharedPreferences("userdata",MODE_PRIVATE);
        AllApplyData = getSharedPreferences("all_apply_data",MODE_PRIVATE);
        AllApplyDataEditor = AllApplyData.edit();
        AllProjectData = getSharedPreferences("all_project_data",MODE_PRIVATE);
        AllProjectDataEditor = AllProjectData.edit();
        AllApplyDataEditor.clear();
        AllApplyDataEditor.apply();
        AllProjectDataEditor.clear();
        AllProjectDataEditor.apply();

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                MyApplyActivity.this.finish();
            }
        });

        showProgress(true);
        loadingApply();
    }

    /**
     * Shows the progress UI and hides the login form.
     * 显示进度UI并隐藏登录表单。
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        mRvMain.setVisibility(!show ? View.VISIBLE : View.GONE);
        runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    //加载消息基本信息到手机暂存
    public void loadingApply(){
        final String number = UserData.getString("number","");
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/GetApplyByNumberServlet?apply_send_number="+number;
        String tag = "GetApply";
        //取得请求队列
        RequestQueue GetApply = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        GetApply.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest GetApplyrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            AllApplyDataEditor.putString("apply_id",jsonObject.getString("apply_id"));
                            AllApplyDataEditor.putString("apply_send_number",jsonObject.getString("apply_send_number"));
                            AllApplyDataEditor.putString("apply_send_name",jsonObject.getString("apply_send_name"));
                            AllApplyDataEditor.putString("apply_receive_number",jsonObject.getString("apply_receive_number"));
                            AllApplyDataEditor.putString("apply_project_id",jsonObject.getString("apply_project_id"));
                            AllApplyDataEditor.putString("apply_job",jsonObject.getString("apply_job"));
                            AllApplyDataEditor.putString("apply_project_title",jsonObject.getString("apply_project_title"));
                            AllApplyDataEditor.putString("apply_project_content",jsonObject.getString("apply_project_content"));
                            AllApplyDataEditor.putString("apply_project_count_member",jsonObject.getString("apply_project_count_member"));
                            AllApplyDataEditor.apply();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AllProjectDataEditor.putString("pj_id",AllApplyData.getString("apply_project_id",""));
                                    AllProjectDataEditor.putString("pj_name",AllApplyData.getString("apply_project_title",""));
                                    AllProjectDataEditor.putString("pj_introduce",AllApplyData.getString("apply_project_content",""));
                                    AllProjectDataEditor.putString("count_member",AllApplyData.getString("apply_project_count_member",""));
                                    AllProjectDataEditor.putString("pj_boss_phone",AllApplyData.getString("apply_receive_number",""));
                                    AllProjectDataEditor.apply();
                                    showProgress(false);
                                    mRvMain.setLayoutManager(new LinearLayoutManager(MyApplyActivity.this));
                                    mRvMain.setAdapter(new MyApplyAdapter(MyApplyActivity.this));
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(MyApplyActivity.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(MyApplyActivity.this,"无网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("apply_send_number", number);
                return params;
            }
        };
        //设置Tag标签
        GetApplyrequest.setTag(tag);
        //将请求添加到队列中
        GetApply.add(GetApplyrequest);
    }

    //删除指定的申请
    public void deleteApply(final String apply_id){
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/DeleteApplyServlet?apply_id="+apply_id;
        String tag = "DeleteNews";
        //取得请求队列
        RequestQueue DeleteApply = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        DeleteApply.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest DeleteApplyrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response);
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AllApplyDataEditor.clear();
                                    AllApplyDataEditor.apply();
                                    loadingApply();
                                    Toast.makeText(MyApplyActivity.this,"撤销申请成功！",Toast.LENGTH_SHORT).show();
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(MyApplyActivity.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(MyApplyActivity.this,"无网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("apply_id", apply_id);
                return params;
            }
        };
        //设置Tag标签
        DeleteApplyrequest.setTag(tag);
        //将请求添加到队列中
        DeleteApply.add(DeleteApplyrequest);
    }

}
