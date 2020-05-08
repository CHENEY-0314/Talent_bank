package com.example.talent_bank.user_fragment;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
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
import com.example.talent_bank.Adapter.FindingAdapter;
import com.example.talent_bank.Adapter.MyCollectionAdapter;
import com.example.talent_bank.Adapter.MyPublishAdapter;
import com.example.talent_bank.R;
import com.example.talent_bank.home_page.FindFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MyCollectionActivity extends AppCompatActivity {

    private ImageView imgBack;
    private RecyclerView mRvMain;
    private LinearLayout runWebView;

    private TextView mtext;
    private SharedPreferences AllProjectData;
    private SharedPreferences.Editor AllProjectDataEditor;

    private SharedPreferences ShareUserData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_collection);

        mRvMain = findViewById(R.id.rv_my_collection);

        AllProjectData =getSharedPreferences("all_project_data",MODE_PRIVATE);
        AllProjectDataEditor = AllProjectData.edit();

        AllProjectDataEditor.clear();
        AllProjectDataEditor.apply();

        runWebView = findViewById(R.id.my_collection_loading);
        mtext=findViewById(R.id.MP_textview_end);
        showProgress(true);

        ShareUserData=getSharedPreferences("userdata",MODE_PRIVATE);

        imgBack=findViewById(R.id.MCimg_back);

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                MyCollectionActivity.this.finish();
            }
        });

        //加载收藏
        loadingCollection();

    }

    /**
     * Shows the progress UI and hides the login form.
     * 显示进度UI并隐藏登录表单。
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        mRvMain.setVisibility(!show ? View.VISIBLE : View.GONE);
        runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    //加载除了自己发布的项目的基本信息到手机暂存
    private void loadingCollection(){

        String number=ShareUserData.getString("number","");
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/GetCollectionServlet?number="+number;
        String tag = "GetCollection";
        //取得请求队列
        RequestQueue GetCollection = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        GetCollection.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest GetCollectionrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            AllProjectDataEditor.putString("pj_id",jsonObject.getString("pj_id"));
                            AllProjectDataEditor.putString("pj_name",jsonObject.getString("pj_name"));
                            AllProjectDataEditor.putString("pj_introduce",jsonObject.getString("pj_introduce"));
                            AllProjectDataEditor.putString("count_member",jsonObject.getString("count_member"));
                            AllProjectDataEditor.putString("pj_boss_phone",jsonObject.getString("pj_boss_phone"));
                            AllProjectDataEditor.apply();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showProgress(false);
                                    if(!AllProjectData.getString("pj_id", "").equals("null")) {
                                        mRvMain.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
                                        mRvMain.setAdapter(new MyCollectionAdapter(MyCollectionActivity.this)); //对RecyclerView设置适配器
                                    }else{
                                        mtext.setText("列表空空如也，快去发现页面收藏项目吧！");
                                    }
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(MyCollectionActivity.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(MyCollectionActivity.this,"无网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        //设置Tag标签
        GetCollectionrequest.setTag(tag);
        //将请求添加到队列中
        GetCollection.add(GetCollectionrequest);
    }



}
