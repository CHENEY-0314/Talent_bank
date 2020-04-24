package com.example.talent_bank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.HandlerActivity;
import com.example.talent_bank.MainActivity;
import com.example.talent_bank.ProjectReleased2;
import com.example.talent_bank.R;
import com.example.talent_bank.SignUPActivity;
import com.example.talent_bank.user_fragment.MyPublishActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ProjectReleasedAdapter extends RecyclerView.Adapter<ProjectReleasedAdapter.LinearViewHolder> {

    private String[] SaveMember_titleData;
    private String[] SaveMember_tagData;
    private String[] SaveRemarkData;

    private Button btn_publich;
    private String shpName = "pj_publish";
    private String shpforUserdata = "userdata";

    private ProjectReleased2 mContext;
    public ProjectReleasedAdapter (ProjectReleased2 context) {
        this.mContext = context;
    }
    @NonNull
    @Override
    public ProjectReleasedAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProjectReleasedAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_pr_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ProjectReleasedAdapter.LinearViewHolder holder, final int position) {
        final SharedPreferences shp = mContext.getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=shp.edit();

        btn_publich=mContext.findViewById(R.id.pr_btn_publish);

        SaveMember_titleData=new String[shp.getInt("demandNum_key",1)];
        SaveRemarkData=new String[shp.getInt("demandNum_key",1)];
        SaveMember_tagData=new String[shp.getInt("demandNum_key",1)];


//        String member_title=shp.getString("member_title","");
//        String[] member_titlestrarr = member_title.split("~");
//        String remark=shp.getString("remark","");
//        String[] remarkstrarr = remark.split("~");
//
//        String member_tag=shp.getString("member_tag","");
//        String[] member_tagstrarr = member_tag.split("~");
//
//        if(!member_title.equals("")|!remark.equals("")|!member_tag.equals("")){
//
//        for(int item_i=0;item_i<shp.getInt("demandNum_key",1);item_i++){  //根据用户以往所填写的信息初始化到填写框内
//            if(position==item_i) {
//                holder.mmember_title.setText(member_titlestrarr[item_i]);
//                holder.mremark.setText(remarkstrarr[item_i]);
//                String[] strarr = member_tagstrarr[item_i].split(",");
//                for (String s : strarr) {
//                    if (s.equals("包装设计")) holder.C1.setChecked(true);
//                    if (s.equals("平面设计")) holder.C2.setChecked(true);
//                    if (s.equals("UI设计")) holder.C3.setChecked(true);
//                    if (s.equals("产品设计")) holder.C4.setChecked(true);
//                    if (s.equals("英语")) holder.C5.setChecked(true);
//                    if (s.equals("其他外语")) holder.C6.setChecked(true);
//                    if (s.equals("视频剪辑")) holder.C7.setChecked(true);
//                    if (s.equals("演讲能力")) holder.C8.setChecked(true);
//                    if (s.equals("Photoshop")) holder.C9.setChecked(true);
//                    if (s.equals("PPT制作")) holder.C10.setChecked(true);
//                    if (s.equals("C")) holder.C11.setChecked(true);
//                    if (s.equals("JAVA")) holder.C12.setChecked(true);
//                    if (s.equals("微信小程序开发")) holder.C13.setChecked(true);
//                    if (s.equals("Android开发")) holder.C14.setChecked(true);
//                    if (s.equals("IOS开发")) holder.C15.setChecked(true);
//                }
//            } }
//        }

        //点击保存  将数据存储到本地数组对应位置  且将保存改为编辑  将各部分取消编辑状态

        holder.save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(holder.save.getText()=="EDIT"){  //改为编辑状态
                    holder.save.setClickable(false);
                    holder.save.setText("SAVE");

                    holder.mmember_title.setEnabled(true);
                    holder.mremark.setEnabled(true);
                    holder.C1.setClickable(true);holder.C2.setClickable(true);holder.C3.setClickable(true);holder.C4.setClickable(true);
                    holder.C5.setClickable(true);holder.C6.setClickable(true);holder.C7.setClickable(true);holder.C8.setClickable(true);
                    holder.C9.setClickable(true);holder.C10.setClickable(true);holder.C11.setClickable(true);holder.C12.setClickable(true);
                    holder.C13.setClickable(true);holder.C14.setClickable(true);holder.C15.setClickable(true);
                    holder.save.setClickable(true);
                }
                else {  //改为已经保存状态
                    holder.save.setClickable(false);
                    String tag="";
                    if(holder.C1.isChecked())if (tag.length()==0)tag=tag+"包装设计";else tag=tag+",包装设计";  //用于区分的,为英文的
                    if(holder.C2.isChecked())if (tag.length()==0)tag=tag+"平面设计";else tag=tag+",平面设计";
                    if(holder.C3.isChecked())if (tag.length()==0)tag=tag+"UI设计";else tag=tag+",UI设计";
                    if(holder.C4.isChecked())if (tag.length()==0)tag=tag+"产品设计";else tag=tag+",产品设计";
                    if(holder.C5.isChecked())if (tag.length()==0)tag=tag+"英语";else tag=tag+",英语";
                    if(holder.C6.isChecked())if (tag.length()==0)tag=tag+"其他外语";else tag=tag+",其他外语";
                    if(holder.C7.isChecked())if (tag.length()==0)tag=tag+"视频剪辑";else tag=tag+",视频剪辑";
                    if(holder.C8.isChecked())if (tag.length()==0)tag=tag+"演讲能力";else tag=tag+",演讲能力";
                    if(holder.C9.isChecked())if (tag.length()==0)tag=tag+"Photoshop";else tag=tag+",Photoshop";
                    if(holder.C10.isChecked())if (tag.length()==0)tag=tag+"PPT制作";else tag=tag+",PPT制作";
                    if(holder.C11.isChecked())if (tag.length()==0)tag=tag+"C";else tag=tag+",C";
                    if(holder.C12.isChecked())if (tag.length()==0)tag=tag+"JAVA";else tag=tag+",JAVA";
                    if(holder.C13.isChecked())if (tag.length()==0)tag=tag+"微信小程序开发";else tag=tag+",微信小程序开发";
                    if(holder.C14.isChecked())if (tag.length()==0)tag=tag+"Android开发";else tag=tag+",Android开发";
                    if(holder.C15.isChecked())if (tag.length()==0)tag=tag+"IOS开发";else tag=tag+",IOS开发";

                    if(holder.mmember_title.getText().toString().equals("")){
                        Toast toast=Toast.makeText(mContext,null,Toast.LENGTH_SHORT);
                        toast.setText("请填写队友职务");
                        toast.show();
                        holder.save.setClickable(true);

                    }else if(holder.mremark.getText().toString().equals("")){
                        Toast toast=Toast.makeText(mContext,null,Toast.LENGTH_SHORT);
                        toast.setText("请填写其他要求");
                        toast.show();
                        holder.save.setClickable(true);

                    }else if(tag.equals("")){
                        Toast toast=Toast.makeText(mContext,null,Toast.LENGTH_SHORT);
                        toast.setText("请填写能力标签");
                        toast.show();
                        holder.save.setClickable(true);
                    }else{
                        holder.save.setText("EDIT");
                        SaveMember_titleData[position]=holder.mmember_title.getText().toString();
                        SaveRemarkData[position]=holder.mremark.getText().toString();

                        holder.mmember_title.setEnabled(false);
                        holder.mremark.setEnabled(false);
                        holder.C1.setClickable(false);holder.C2.setClickable(false);holder.C3.setClickable(false);holder.C4.setClickable(false);
                        holder.C5.setClickable(false);holder.C6.setClickable(false);holder.C7.setClickable(false);holder.C8.setClickable(false);
                        holder.C9.setClickable(false);holder.C10.setClickable(false);holder.C11.setClickable(false);holder.C12.setClickable(false);
                        holder.C13.setClickable(false);holder.C14.setClickable(false);holder.C15.setClickable(false);
                        SaveMember_tagData[position]=tag;
                        holder.save.setClickable(true);
                    }
            }
            }

        });

        btn_publich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int demandNum_key=shp.getInt("demandNum_key",1);
                String count_member=""+demandNum_key;

                btn_publich.setText("正在创建中.");
                btn_publich.setEnabled(false);

                String AFTERmember_title="",AFTERremark="",AFTERmember_tag="";

                boolean IsSave=true;

                //当有card没有点击save的时候，对应的位置显示null
                //当有card点击save但没有数据时，对应的位置显示""

                for(int itemi=0;itemi<demandNum_key;itemi++){
                    if(SaveMember_titleData[itemi]==null){
                        IsSave=false;
                        break;
                    }
                }

                if(!IsSave){
                    Toast toast=Toast.makeText(mContext,null,Toast.LENGTH_SHORT);
                    toast.setText("请点击SAVE保存数据");
                    toast.show();
                    btn_publich.setEnabled(true);
                    btn_publich.setText("发布");
                }else {
                    //将所有需求间隔~合并到一起
                    for(int iii=0;iii<demandNum_key;iii++){
                        if(iii==0) AFTERmember_title=SaveMember_titleData[iii];
                        else AFTERmember_title=AFTERmember_title+"~"+SaveMember_titleData[iii];
                        if(iii==0) AFTERremark=SaveRemarkData[iii];
                        else AFTERremark=AFTERremark+"~"+SaveRemarkData[iii];
                        if(iii==0) AFTERmember_tag=SaveMember_tagData[iii];
                        else AFTERmember_tag=AFTERmember_tag+"~"+SaveMember_tagData[iii];
                    }

                    String pj_name=shp.getString("pj_name","");
                    String pj_introduce=shp.getString("pj_introduce","");
                    try_Publish_project(pj_name,pj_introduce,count_member,AFTERmember_title,AFTERremark,AFTERmember_tag);  //存入数据库
                }

            }
        });

    }

    //将项目数据存储到数据库
    public void try_Publish_project(final String pj_name,final String pj_introduce,final String count_member,final String member_title,final String remark,final String member_tag){

        final SharedPreferences shp = mContext.getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor=shp.edit();
        final SharedPreferences mSha = mContext.getApplication().getSharedPreferences(shpforUserdata, Context.MODE_PRIVATE);

        final String pj_boss_phone=mSha.getString("number","");
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/CreatProjectServlet?pj_name="+pj_name+"&pj_boss_phone="+pj_boss_phone+"&count_member="+count_member+"&pj_introduce="+pj_introduce+"&member_tag="+member_tag+"&remark="+remark+"&member_title="+member_title;
        String tag = "Publish_project";
        //取得请求队列
        RequestQueue Pub_pj = Volley.newRequestQueue(mContext);
        //防止重复请求，所以先取消tag标识的请求队列
        Pub_pj.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest Publishrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("结果");
                            String result = jsonObject.getString("Result");
                            editor.clear();
                            editor.apply();
                            Toast toast=Toast.makeText(mContext,null,Toast.LENGTH_SHORT);
                            toast.setText("创建成功,请到”我的发布“查看");
                            toast.show();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    mContext.startActivity(new Intent(mContext, MainActivity.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                                    mContext.overridePendingTransition(0,R.anim.fade_out);
                                }
                            },1300);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            btn_publich.setEnabled(true);
                            btn_publich.setText("发布");
                            Toast.makeText(mContext,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(mContext,"请退出重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("pj_name", pj_name);
                params.put("pj_introduce", pj_introduce);
                params.put("count_member", count_member);
                params.put("member_title", member_title);
                params.put("remark", remark);
                params.put("pj_boss_phone", pj_boss_phone);
                params.put("member_tag", member_tag);
                return params;
            }
        };
        //设置Tag标签
        Publishrequest.setTag(tag);
        //将请求添加到队列中
        Pub_pj.add(Publishrequest);
    }


    @Override
    public int getItemCount() {
        SharedPreferences shp = mContext.getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        int x = shp.getInt("demandNum_key",1);
        return x;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private EditText mmember_title;
        private CheckBox C1,C2,C3,C4,C5,C6,C7,C8,C9,C10,C11,C12,C13,C14,C15;
        private EditText mremark;
        private Button save;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mmember_title=itemView.findViewById(R.id.pr_member_title);
            mremark=itemView.findViewById(R.id.pr_item_content);
            save=itemView.findViewById(R.id.rv_pr_btnsava);

            C1=itemView.findViewById(R.id.prcb_1);
            C2=itemView.findViewById(R.id.prcb_2);
            C3=itemView.findViewById(R.id.prcb_3);
            C4=itemView.findViewById(R.id.prcb_4);
            C5=itemView.findViewById(R.id.prcb_5);
            C6=itemView.findViewById(R.id.prcb_6);
            C7=itemView.findViewById(R.id.prcb_7);
            C8=itemView.findViewById(R.id.prcb_8);
            C9=itemView.findViewById(R.id.prcb_9);
            C10=itemView.findViewById(R.id.prcb_10);
            C11=itemView.findViewById(R.id.prcb_11);
            C12=itemView.findViewById(R.id.prcb_12);
            C13=itemView.findViewById(R.id.prcb_13);
            C14=itemView.findViewById(R.id.prcb_14);
            C15=itemView.findViewById(R.id.prcb_15);

        }
    }
}
