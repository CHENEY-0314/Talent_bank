package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.TalentBank.OthersBiographical;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ApplyerBiographical extends AppCompatActivity {

    private ImageView imgBack;

    String usertag;   //能力标签

    private TextView madvantage,mexperience,mgrade,mwechart,memail,madress;
    private CheckBox mC1,mC2,mC3,mC4,mC5,mC6,mC7,mC8,mC9,mC10,mC11,mC12,mC13,mC14,mC15;

    //以下用于手机存用户信息
    private SharedPreferences ReceiveApplyData;
    private SharedPreferences.Editor ReceiveApplyDataEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_applyer_biographical);


        imgBack=findViewById(R.id.ARBimg_back);

        madvantage=findViewById(R.id.ARB_text_advantage);
        mexperience=findViewById(R.id.ARB_text_experience);
        mgrade=findViewById(R.id.ARB_text_grade);
        mwechart=findViewById(R.id.ARB_text_wechart);
        memail=findViewById(R.id.ARB_text_email);
        madress=findViewById(R.id.ARB_text_adress);

        mC1=findViewById(R.id.ARB_cb_1);mC2=findViewById(R.id.ARB_cb_2);mC3=findViewById(R.id.ARB_cb_3);
        mC4=findViewById(R.id.ARB_cb_4);mC5=findViewById(R.id.ARB_cb_5);mC6=findViewById(R.id.ARB_cb_6);
        mC7=findViewById(R.id.ARB_cb_7);mC8=findViewById(R.id.ARB_cb_8);mC9=findViewById(R.id.ARB_cb_9);
        mC10=findViewById(R.id.ARB_cb_10);mC11=findViewById(R.id.ARB_cb_11);mC12=findViewById(R.id.ARB_cb_12);
        mC13=findViewById(R.id.ARB_cb_13);mC14=findViewById(R.id.ARB_cb_14);mC15=findViewById(R.id.ARB_cb_15);

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                ApplyerBiographical.this.finish();
            }
        });

        ReceiveApplyData = getSharedPreferences("receive_apply_data", MODE_PRIVATE);
        getData(ReceiveApplyData.getString("curr_user",""));

    }


    //初始化用户能力标签
    private void gettag(){
        mC1.setVisibility(View.GONE); mC2.setVisibility(View.GONE);mC3.setVisibility(View.GONE);
        mC4.setVisibility(View.GONE);mC5.setVisibility(View.GONE);mC6.setVisibility(View.GONE);
        mC7.setVisibility(View.GONE);mC8.setVisibility(View.GONE);mC9.setVisibility(View.GONE);
        mC10.setVisibility(View.GONE);mC11.setVisibility(View.GONE);mC12.setVisibility(View.GONE);
        mC13.setVisibility(View.GONE);mC14.setVisibility(View.GONE);mC15.setVisibility(View.GONE);

        String[] tagArray=usertag.split(",");
        for (String s : tagArray) {
            if (s.equals("包装设计")) mC1.setVisibility(View.VISIBLE);
            if (s.equals("平面设计")) mC2.setVisibility(View.VISIBLE);
            if (s.equals("UI设计")) mC3.setVisibility(View.VISIBLE);
            if (s.equals("产品设计")) mC4.setVisibility(View.VISIBLE);
            if (s.equals("英语")) mC5.setVisibility(View.VISIBLE);
            if (s.equals("其他外语")) mC6.setVisibility(View.VISIBLE);
            if (s.equals("视频剪辑")) mC7.setVisibility(View.VISIBLE);
            if (s.equals("演讲能力")) mC8.setVisibility(View.VISIBLE);
            if (s.equals("Photoshop")) mC9.setVisibility(View.VISIBLE);
            if (s.equals("PPT制作")) mC10.setVisibility(View.VISIBLE);
            if (s.equals("C")) mC11.setVisibility(View.VISIBLE);
            if (s.equals("JAVA")) mC12.setVisibility(View.VISIBLE);
            if (s.equals("微信小程序开发")) mC13.setVisibility(View.VISIBLE);
            if (s.equals("Android开发")) mC14.setVisibility(View.VISIBLE);
            if (s.equals("IOS开发")) mC15.setVisibility(View.VISIBLE);
        }
    }

    //获取当前用户信息
    private void getData(final String number){
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/GetOtherdataServlet?number="+number+"&code=2020508";
        String tag = "Get";
        //取得请求队列
        RequestQueue Get = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        Get.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest Getrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            madvantage.setText(jsonObject.getString("advantage"));
                            mexperience.setText(jsonObject.getString("experience"));
                            usertag=jsonObject.getString("tag");
                            mgrade.setText(jsonObject.getString("grade"));
                            mwechart.setText(jsonObject.getString("wechart"));
                            memail.setText(jsonObject.getString("email"));
                            madress.setText(jsonObject.getString("adress"));
                            gettag();
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(ApplyerBiographical.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(ApplyerBiographical.this,"无网络连接,请稍后重试！",Toast.LENGTH_SHORT).show();
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
        Getrequest.setTag(tag);
        //将请求添加到队列中
        Get.add(Getrequest);
    }



}
