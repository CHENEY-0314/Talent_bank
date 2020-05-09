package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
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
import com.example.talent_bank.Adapter.EditLinearAdapter;
import com.example.talent_bank.Adapter.PeopleDemandAdapter;
import com.example.talent_bank.Adapter.ReceiveApplyAdapter;
import com.example.talent_bank.TalentBank.TalentBank;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class PeopleDemand extends AppCompatActivity {
    private ImageView imgBack;
    private RecyclerView mRvMain;
    private LinearLayout runWebView;

    //以下用于手机存用户信息
    private SharedPreferences AllProjectData;
    private SharedPreferences.Editor AllProjectDataEditor;

    private SharedPreferences UserData;

    private SharedPreferences data;
    private SharedPreferences.Editor dataEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people_demand);

        runWebView = findViewById(R.id.people_demand_loading);
        imgBack = findViewById(R.id.PDimg_back);
        mRvMain = findViewById(R.id.rv_people_demand);

        AllProjectData = getSharedPreferences("all_project_data",MODE_PRIVATE);
        AllProjectDataEditor = AllProjectData.edit();

        data = getSharedPreferences("SHP_NAME",MODE_PRIVATE);
        dataEditor = data.edit();

        UserData = getSharedPreferences("userdata",MODE_PRIVATE);
        showProgress(true);
        loadingProject();

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                AllProjectDataEditor.putString("remark","");
                AllProjectDataEditor.putString("member_title","");
                AllProjectDataEditor.putString("member_tag","");
                PeopleDemand.this.finish();
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
        mRvMain.setVisibility(!show ? View.VISIBLE : View.GONE);
        runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
        runWebView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
        mRvMain.animate().setDuration(shortAnimTime).alpha(
                !show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                mRvMain.setVisibility(!show ? View.VISIBLE : View.GONE);
            }
        });
    }

    //加载当前项目需求成员的基本信息到手机暂存
    public void loadingProject(){

        final String ALLpj_id = AllProjectData.getString("pj_id","");
        int Curr_pj = AllProjectData.getInt("curr_pj",-1);
        String[] ALLpj_idstrarr = ALLpj_id.split("~");
        final String pj_id = ALLpj_idstrarr[Curr_pj];
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/GetProjectMemberByID?pj_id="+pj_id;
        String tag = "GetProjectMember";
        //取得请求队列
        RequestQueue GetProjectMember = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        GetProjectMember.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest GetProjectMemberrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            AllProjectDataEditor.putString("remark",jsonObject.getString("remark"));
                            AllProjectDataEditor.putString("member_title",jsonObject.getString("member_title"));
                            AllProjectDataEditor.putString("member_tag",jsonObject.getString("member_tag"));
                            AllProjectDataEditor.apply();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showProgress(false);
                                    mRvMain.setLayoutManager(new LinearLayoutManager(PeopleDemand.this));
                                    mRvMain.setAdapter(new PeopleDemandAdapter(PeopleDemand.this));
                                }
                            },400);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(PeopleDemand.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(PeopleDemand.this,"无网络连接,请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("pj_id", pj_id);
                return params;
            }
        };
        //设置Tag标签
        GetProjectMemberrequest.setTag(tag);
        //将请求添加到队列中
        GetProjectMember.add(GetProjectMemberrequest);

    }

    public void SendApply(final String apply_project_id, final String apply_receive_number, final String apply_job, final String apply_project_title, final String apply_project_content,final String apply_project_count_member) {

        final String apply_send_name = UserData.getString("name","");
        final String apply_send_number = UserData.getString("number","");
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/CreatApplyServlet?apply_send_number="+apply_send_number+"&apply_send_name="+apply_send_name+"&apply_receive_number="+apply_receive_number+"&apply_project_id="+apply_project_id+"&apply_job="+apply_job+"&apply_project_title="+apply_project_title+"&apply_project_content="+apply_project_content+"&apply_project_count_member="+apply_project_count_member;
        String tag = "SendApply";
        //取得请求队列
        RequestQueue SendApply = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        SendApply.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest SendApplyrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response);
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(PeopleDemand.this,"发送申请成功",Toast.LENGTH_SHORT).show();
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(PeopleDemand.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(PeopleDemand.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("apply_send_number", apply_send_number);
                params.put("apply_send_name", apply_send_name);
                params.put("apply_receive_number", apply_receive_number);
                params.put("apply_project_id", apply_project_id);
                params.put("apply_job", apply_job);
                params.put("apply_project_title", apply_project_title);
                params.put("apply_project_content", apply_project_content);
                params.put("apply_project_count_member", apply_project_count_member);
                return params;
            }
        };
        //设置Tag标签
        SendApplyrequest.setTag(tag);
        //将请求添加到队列中
        SendApply.add(SendApplyrequest);

    }

    public void SendNews(final String news_number,String project_title,String project_job) {

        final String news_send_name = UserData.getString("name","");
        final String news_send_number = UserData.getString("number","");
        final String news_send_content = "("+news_send_name+")对你的项目("+project_title+")感兴趣，向你的项目职位“"+project_job+"”发出申请。请求与你沟通。";

        //获得当前系统的时间方法
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        Date date = new Date(System.currentTimeMillis());
        final String news_time = simpleDateFormat.format(date);

        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/CreatNewsServlet?news_send_number="+news_send_number+"&news_send_name="+news_send_name+"&news_number="+news_number+"&news_send_content="+news_send_content+"&news_time="+news_time;
        String tag = "SendNews";
        //取得请求队列
        RequestQueue SendNews = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        SendNews.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest SendNewsrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response);
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(PeopleDemand.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(PeopleDemand.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("news_send_number", news_send_number);
                params.put("news_send_name", news_send_name);
                params.put("news_number", news_number);
                params.put("news_send_content", news_send_content);
                params.put("news_time", news_time);
                return params;
            }
        };
        //设置Tag标签
        SendNewsrequest.setTag(tag);
        //将请求添加到队列中
        SendNews.add(SendNewsrequest);

    }

    //用于前往数据库判断是否已经申请
    public void HaveCollected(final String apply_project_id, final String apply_send_number, final String apply_job){
        dataEditor.clear();
        dataEditor.apply();

        String url="http://47.107.125.44:8080/Talent_bank/servlet/IfHaveApplyJobServlet?apply_project_id="+apply_project_id+"&apply_send_number="+apply_send_number+"&apply_job="+apply_job;
        String tag = "HaveApply";
        //取得请求队列
        RequestQueue HaveApply = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        HaveApply.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest HaveApplyrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            if(jsonObject.getString("result").equals("已申请")){
                                dataEditor.putString("ifApply","true");
                                dataEditor.apply();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(PeopleDemand.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(PeopleDemand.this,"无网络连接,请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("apply_project_id", apply_project_id);
                params.put("apply_send_number", apply_send_number);
                params.put("apply_job", apply_job);
                return params;
            }
        };
        //设置Tag标签
        HaveApplyrequest.setTag(tag);
        //将请求添加到队列中
        HaveApply.add(HaveApplyrequest);
    }


}
