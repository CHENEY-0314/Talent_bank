package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
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
import com.example.talent_bank.user_fragment.SetUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import okhttp3.OkHttpClient;

import static com.example.talent_bank.user_fragment.ChangeImageActivity.convertIconToString;

public class ProjectContentsApply extends AppCompatActivity {
    private ImageView imgBack;
    private Button button;
    private TextView textView ,project_name,project_content;
    private LinearLayout mCollect,mCollected;

    private int Curr_PJ;//用于标识点进来的项目是哪个项目

    private String pj_id;  //当前项目的pj_id
    private String number,password; //当前用户

    private SharedPreferences AllProjectData;
    private SharedPreferences.Editor AllProjectDataEditor;
    private SharedPreferences SharedUsers;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_contents_apply);

        imgBack = findViewById(R.id.PCAimg_back);
        button = findViewById(R.id.PCA_btn_detail);
        textView = findViewById(R.id.PCA_num);
        project_name = findViewById(R.id.PCA_text_name);
        project_content = findViewById(R.id.PCA_text_content);
        mCollect=findViewById(R.id.PCA_collect);
        mCollected=findViewById(R.id.PCA_collected);

        AllProjectData = getSharedPreferences("all_project_data",MODE_PRIVATE);
        AllProjectDataEditor = AllProjectData.edit();
        SharedUsers=getSharedPreferences("userdata",MODE_PRIVATE);

        number=SharedUsers.getString("number","");
        password=SharedUsers.getString("password","");

        Curr_PJ = AllProjectData.getInt("curr_pj",0);  //当前项目的pj_id
        String Spj_id=AllProjectData.getString("pj_id","");
        String[] pj_idArray=Spj_id.split("~");
        pj_id=pj_idArray[Curr_PJ];

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                //清除当前项目的标记符curr_pj
                AllProjectDataEditor.putInt("curr_pj",-1);
                AllProjectDataEditor.apply();
                ProjectContentsApply.this.finish();
            }
        });

        //初始化当前项目信息
        Initializing();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //点击查看队友要求
                Intent intent = new Intent(ProjectContentsApply.this,PeopleDemand.class);
                startActivity(intent);
            }
        });

        mCollect.setOnClickListener(new View.OnClickListener() {  //点击收藏
            @Override
            public void onClick(View v) {
                mCollect.setClickable(false);
                Collected();
                mCollect.setClickable(true);
            }
        });

        mCollected.setOnClickListener(new View.OnClickListener() {  //取消收藏
            @Override
            public void onClick(View v) {
                mCollected.setClickable(false);
                Collected();
                mCollected.setClickable(true);
            }
        });
    }

    //用于初始化当前项目信息
    public void Initializing(){
        String count_member = AllProjectData.getString("count_member","");
        String pj_name = AllProjectData.getString("pj_name","");
        String pj_introduce = AllProjectData.getString("pj_introduce","");

        String[] count_memberstrarr = count_member.split("~");
        String[] pj_namestrarr = pj_name.split("~");
        String[] pj_introducestrarr = pj_introduce.split("~");

        project_name.setText(pj_namestrarr[Curr_PJ]);
        project_content.setText(pj_introducestrarr[Curr_PJ]);
        String textNum = "参与人数：" + String.valueOf(count_memberstrarr[Curr_PJ]) + " 人";
        textView.setText(textNum);

        HaveCollected();  //更新当前项目是否收藏
    }

    //用于前往数据库判断是否已经收藏
    public void HaveCollected(){
        String url="http://47.107.125.44:8080/Talent_bank/servlet/IfHaveCollectPjServlet?number="+number+"&pj_id="+pj_id;
        String tag = "HaveCollect";
        //取得请求队列
        RequestQueue HaveCollect = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        HaveCollect.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest HaveCollectrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            if(jsonObject.getString("result").equals("已收藏")){
                                mCollect.setVisibility(View.GONE);
                                mCollected.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(ProjectContentsApply.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(ProjectContentsApply.this,"无网络连接,请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("number", number);
                params.put("pj_id", pj_id);
                return params;
            }
        };
        //设置Tag标签
        HaveCollectrequest.setTag(tag);
        //将请求添加到队列中
        HaveCollect.add(HaveCollectrequest);

    }

    //改变收藏状态
    public void Collected(){
        String url="http://47.107.125.44:8080/Talent_bank/servlet/CollectionServlet?number="+number+"&pj_id="+pj_id+"&password="+password;
        String tag = "Collect";
        //取得请求队列
        RequestQueue Collect = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        Collect.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest Collectrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            if(jsonObject.getString("result").equals("取消收藏成功")){
                                mCollected.setVisibility(View.GONE);
                                mCollect.setVisibility(View.VISIBLE);
                            }else{
                                mCollect.setVisibility(View.GONE);
                                mCollected.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(ProjectContentsApply.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(ProjectContentsApply.this,"无网络连接,请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("number", number);
                params.put("pj_id", pj_id);
                params.put("password", password);

                return params;
            }
        };
        //设置Tag标签
        Collectrequest.setTag(tag);
        //将请求添加到队列中
        Collect.add(Collectrequest);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {   //重写返回函数
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //清除当前项目的标记符curr_pj
            AllProjectDataEditor.putInt("curr_pj",-1);
            AllProjectDataEditor.apply();
            ProjectContentsApply.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }

}
