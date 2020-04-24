package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

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
import com.example.talent_bank.Adapter.MyPublishAdapter;
import com.example.talent_bank.R;

import com.example.talent_bank.Adapter.EditLinearAdapter;
import com.example.talent_bank.user_fragment.MyPublishActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class EditPeopleDemand extends AppCompatActivity {
    private RecyclerView mRvMain;
    private ImageView imgBack;
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
        setContentView(R.layout.activity_edit_people_demand);

        runWebView=findViewById(R.id.EPD_loading);

        UseforUserData=getSharedPreferences("userdata",MODE_PRIVATE);
        UseforProjectData=getSharedPreferences("projectdata",MODE_PRIVATE);
        ProjectDataEditor=UseforProjectData.edit();
        showProgress(true);
        imgBack = findViewById(R.id.EPDimg_back);
        mRvMain = findViewById(R.id.rv_edit_people_demand);
        loadingProject();

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                ProjectDataEditor.putString("remark","");
                ProjectDataEditor.putString("member_title","");
                ProjectDataEditor.putString("member_tag","");
                EditPeopleDemand.this.finish();
            }
        });

    }

    //显示和隐藏加载gif
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

    //加载当前项目需求成员的基本信息到手机暂存
    public void loadingProject(){

        final String number=UseforUserData.getString("number","");
        final String password=UseforUserData.getString("password","");

        final String ALLpj_id=UseforProjectData.getString("pj_id","");
        int Curr_pj=UseforProjectData.getInt("curr_pj",-1);
        String[] ALLpj_idstrarr = ALLpj_id.split("~");
        final String pj_id=ALLpj_idstrarr[Curr_pj];
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/GetProjectMemberByID?number="+number+"&password="+password+"&pj_id="+pj_id;
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
                            ProjectDataEditor.putString("remark",jsonObject.getString("remark"));
                            ProjectDataEditor.putString("member_title",jsonObject.getString("member_title"));
                            ProjectDataEditor.putString("member_tag",jsonObject.getString("member_tag"));
                            ProjectDataEditor.apply();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showProgress(false);
                                    mRvMain.setLayoutManager(new LinearLayoutManager(EditPeopleDemand.this));
                                    mRvMain.setAdapter(new EditLinearAdapter(EditPeopleDemand.this));
                                }
                            },400);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(EditPeopleDemand.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(EditPeopleDemand.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("number", number);
                params.put("password", password);
                params.put("pj_id", pj_id);
                return params;
            }
        };
        //设置Tag标签
        GetProjectMemberrequest.setTag(tag);
        //将请求添加到队列中
        GetProjectMember.add(GetProjectMemberrequest);

    }
}
