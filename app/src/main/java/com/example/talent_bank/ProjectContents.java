package com.example.talent_bank;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.Adapter.MyPublishAdapter;
import com.example.talent_bank.TalentBank.TalentBank;
import com.example.talent_bank.register.RegisterActivity;
import com.example.talent_bank.register.RegisterLastActivity;
import com.example.talent_bank.user_fragment.MyPublishActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.refactor.lib.colordialog.ColorDialog;

public class ProjectContents extends AppCompatActivity {
    private ImageView imgBack;
    private ImageView imgMore;
    private Button button2,button3,button4;
    private TextView TXETcount_member;
    private String shpName = "SHP_NAME";

    private TextView TEXTpj_name;
    private TextView TEXTpj_introduce;

    private int Curr_PJ;//用于标识点进来的项目是哪个项目

    private SharedPreferences UseforProjectData;
    private SharedPreferences UseforUser;
    private SharedPreferences.Editor ProjectDataEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置屏幕上方状态栏颜色
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0及以上
            Window window = getWindow();
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//状态栏字体颜色设置为黑色这个是Android 6.0才出现的属性
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_contents);
        //设置head的img监听
        imgBack = findViewById(R.id.PCimg_back);
        imgMore = findViewById(R.id.PCimg_more);
        button2 = findViewById(R.id.PC_btn_demand);
        button3 = findViewById(R.id.PC_btn_apply);
        TXETcount_member = findViewById(R.id.PC_num);
        TEXTpj_name=findViewById(R.id.PC_pj_name);
        TEXTpj_introduce=findViewById(R.id.PC_pj_introduce);
        button4 = findViewById(R.id.PC_enter);

        UseforUser=getSharedPreferences("userdata",MODE_PRIVATE);
        UseforProjectData=getSharedPreferences("projectdata",MODE_PRIVATE);
        ProjectDataEditor=UseforProjectData.edit();

        Curr_PJ=UseforProjectData.getInt("curr_pj",0);

        button2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //点击跳转查看需求队友
                startActivity(new Intent(ProjectContents.this, EditPeopleDemand.class), ActivityOptions.makeSceneTransitionAnimation(ProjectContents.this).toBundle());
            }
        });

        button3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //点击跳转查看申请
                SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
                int x = shp.getInt("receiveApplyNum_key",3);
                if (x == 0) {
                    startActivity(new Intent(ProjectContents.this, ReceiveApply.class), ActivityOptions.makeSceneTransitionAnimation(ProjectContents.this).toBundle());
                } else {
                    startActivity(new Intent(ProjectContents.this, ReceiveNullApply.class), ActivityOptions.makeSceneTransitionAnimation(ProjectContents.this).toBundle());
                }
            }
        });

        button4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {  //点击跳转界面
                startActivity(new Intent(ProjectContents.this, TalentBank.class), ActivityOptions.makeSceneTransitionAnimation(ProjectContents.this).toBundle());
            }
        });

        imgBack.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击上方返回按钮
                //清除当前项目的标记符curr_pj
                ProjectDataEditor.putInt("curr_pj",-1);
                ProjectDataEditor.apply();
                ProjectContents.this.finish();
            }
        });

        //初始化当前项目信息
        Initializing();

        //点击右上角更多的按钮
        imgMore.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {
                new MyDialog(ProjectContents.this){
                    @Override
                    public void btnPickByTake(){
                        //点击弹窗修改项目时做的事
                    }
                    @Override
                    public void btnPickBySelect() {
                        //点击弹窗”删除项目“时做的事
                        ColorDialog dialog = new ColorDialog(ProjectContents.this);
                        dialog.setTitle("提示");
                        dialog.setColor("#ffffff");//颜色
                        dialog.setContentTextColor("#656565");
                        dialog.setTitleTextColor("#656565");
                        dialog.setContentText("是否确定删除项目？删除后将不能再找回。");
                        dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                //删除项目的操作
                                DeleteProject();
                                dialog.dismiss();
                            }
                        })
                                .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                                    @Override
                                    public void onClick(ColorDialog dialog) {
                                        dialog.dismiss();
                                    }
                                }).show();
                    }

                }.show();
            }
        });
    }

    //用于初始化当前项目信息
    public void Initializing(){
        String count_member=UseforProjectData.getString("count_member","");
        String pj_name=UseforProjectData.getString("pj_name","");
        String pj_introduce=UseforProjectData.getString("pj_introduce","");

        String[] count_memberstrarr = count_member.split("~");
        String[] pj_namestrarr = pj_name.split("~");
        String[] pj_introducestrarr = pj_introduce.split("~");

        TEXTpj_name.setText(pj_namestrarr[Curr_PJ]);
        TEXTpj_introduce.setText(pj_introducestrarr[Curr_PJ]);
        String textNum = "参与人数：" + String.valueOf(count_memberstrarr[Curr_PJ]) + " 人";
        TXETcount_member.setText(textNum);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        TXETcount_member = findViewById(R.id.PC_num);
        String count_member=UseforProjectData.getString("count_member","");
        String[] count_memberstrarr = count_member.split("~");
        String textNum = "参与人数：" + String.valueOf(count_memberstrarr[Curr_PJ]) + " 人";
        TXETcount_member.setText(textNum);
    }

    //删除项目
    public void DeleteProject(){

        final String number=UseforUser.getString("number","");
        final String password=UseforUser.getString("password","");
        final String allpj_id=UseforProjectData.getString("pj_id","");
        String[] pj_idstrarr = allpj_id.split("~");
        final String pj_id=pj_idstrarr[Curr_PJ];

        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/DeleteProject?number="+number+"&password="+password+"&pj_id="+pj_id;
        String tag = "DeleteProject";
        //取得请求队列
        RequestQueue DeleteProject = Volley.newRequestQueue(this);
        //防止重复请求，所以先取消tag标识的请求队列
        DeleteProject.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest DeleteProjectrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("结果");
                            String result = jsonObject.getString("Result");
                            if (result.equals("删除成功")) {
                                Toast toast=Toast.makeText(ProjectContents.this,null,Toast.LENGTH_SHORT);
                                toast.setText("删除成功");
                                toast.show();
                                Handler mHandler = new Handler();
                                mHandler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        startActivity(new Intent(ProjectContents.this, MyPublishActivity.class));
                                        overridePendingTransition(R.anim.slide_in,R.anim.slide_out);  //设置跳转动画
                                        ProjectContents.this.finish();
                                    }
                                },500);
                            } else {
                                if (result.equals("删除失败")){
                                    Toast toast=Toast.makeText(ProjectContents.this,null,Toast.LENGTH_SHORT);
                                    toast.setText("请稍后重试");
                                    toast.show();
                                }
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(ProjectContents.this,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(ProjectContents.this,"请稍后重试！",Toast.LENGTH_SHORT).show();
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
        DeleteProjectrequest.setTag(tag);
        //将请求添加到队列中
        DeleteProject.add(DeleteProjectrequest);
    };


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {   //重写返回函数
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            //清除当前项目的标记符curr_pj
            ProjectDataEditor.putInt("curr_pj",-1);
            ProjectDataEditor.apply();
            ProjectContents.this.finish();
        }
        return super.onKeyDown(keyCode, event);
    }
}
