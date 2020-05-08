package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.example.talent_bank.Adapter.MyApplyAdapter;
import com.example.talent_bank.Adapter.ReceiveApplyAdapter;
import com.example.talent_bank.TalentBank.TalentBank;
import com.example.talent_bank.user_fragment.MyApplyActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReceiveApply extends AppCompatActivity {
    private RecyclerView mRvMain;
    private ImageView imgBack;
    private LinearLayout runWebView;

    //以下用于手机存用户信息
    private SharedPreferences ReceiveApplyData;
    private SharedPreferences.Editor ReceiveApplyDataEditor;

    private SharedPreferences ProjectData;

    private SharedPreferences UserData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_receive_apply);

        imgBack = findViewById(R.id.RAimg_back);
        mRvMain = findViewById(R.id.rv_receive_apply);
        mRvMain.setLayoutManager(new LinearLayoutManager(ReceiveApply.this));
        mRvMain.setAdapter(new ReceiveApplyAdapter(ReceiveApply.this));
        runWebView = findViewById(R.id.receive_apply_loading);
        ReceiveApplyData = getSharedPreferences("receive_apply_data",MODE_PRIVATE);
        ReceiveApplyDataEditor = ReceiveApplyData.edit();
        ProjectData = getSharedPreferences("projectdata",MODE_PRIVATE);
        ReceiveApplyDataEditor.clear();
        ReceiveApplyDataEditor.apply();

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                ReceiveApply.this.finish();
            }
        });

        showProgress(true);
        loadingReceiveApply();
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

    //加载收到的申请信息到手机暂存
    public void loadingReceiveApply(){
        String projectId = ProjectData.getString("pj_id","");
        int curr_pj = ProjectData.getInt("curr_pj",0);
        String[] projectIdStrarr = projectId.split("~");
        final String apply_project_id = projectIdStrarr[curr_pj];
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/GetApplyByPcIdServlet?apply_project_id="+apply_project_id;
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
                            ReceiveApplyDataEditor.putString("apply_id",jsonObject.getString("apply_id"));
                            ReceiveApplyDataEditor.putString("apply_send_number",jsonObject.getString("apply_send_number"));
                            ReceiveApplyDataEditor.putString("apply_send_name",jsonObject.getString("apply_send_name"));
                            ReceiveApplyDataEditor.putString("apply_receive_number",jsonObject.getString("apply_receive_number"));
                            ReceiveApplyDataEditor.putString("apply_project_id",jsonObject.getString("apply_project_id"));
                            ReceiveApplyDataEditor.putString("apply_job",jsonObject.getString("apply_job"));
                            ReceiveApplyDataEditor.putString("apply_project_title",jsonObject.getString("apply_project_title"));
                            ReceiveApplyDataEditor.putString("apply_project_content",jsonObject.getString("apply_project_content"));
                            ReceiveApplyDataEditor.putString("apply_project_count_member",jsonObject.getString("apply_project_count_member"));
                            ReceiveApplyDataEditor.apply();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showProgress(false);
                                    mRvMain.setLayoutManager(new LinearLayoutManager(ReceiveApply.this));
                                    mRvMain.setAdapter(new ReceiveApplyAdapter(ReceiveApply.this));
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(ReceiveApply.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(ReceiveApply.this,"无网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("apply_project_id", apply_project_id);
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
                                    ReceiveApplyDataEditor.clear();
                                    ReceiveApplyDataEditor.apply();
                                    loadingReceiveApply();
                                    Toast.makeText(ReceiveApply.this,"操作成功！",Toast.LENGTH_SHORT).show();
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(ReceiveApply.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(ReceiveApply.this,"无网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
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


    public void SendNewsAgree(final String news_number,String apply_job,String project_name) {
        UserData = getSharedPreferences("userdata",MODE_PRIVATE);
        final String news_send_name = UserData.getString("name","");
        final String news_send_number = UserData.getString("number","");
        final String news_send_content = "恭喜你，项目经理("+news_send_name+")同意了你的申请，项目："+project_name+"，职位："+apply_job+"请与他沟通联系。";
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
                                    Toast.makeText(ReceiveApply.this,"发送消息成功",Toast.LENGTH_SHORT).show();
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(ReceiveApply.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(ReceiveApply.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
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

    public void SendNewsDisagree(final String news_number,String apply_job,String project_name) {
        UserData = getSharedPreferences("userdata",MODE_PRIVATE);
        final String news_send_name = UserData.getString("name","");
        final String news_send_number = UserData.getString("number","");
        final String news_send_content = "很遗憾的通知你，项目经理("+news_send_name+")拒绝了你的申请，项目："+project_name+"，职位："+apply_job;
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
                                    Toast.makeText(ReceiveApply.this,"发送消息成功",Toast.LENGTH_SHORT).show();
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(ReceiveApply.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(ReceiveApply.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
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
}
