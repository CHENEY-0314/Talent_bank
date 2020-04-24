package com.example.talent_bank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.EditDemand;
import com.example.talent_bank.EditPeopleDemand;
import com.example.talent_bank.R;

import static android.content.Context.MODE_PRIVATE;

public class EditLinearAdapter extends RecyclerView.Adapter<EditLinearAdapter.LinearViewHolder> {
    private String shpName = "projectdata";
    private EditPeopleDemand mContext;


    //以下用于手机存用户信息
    private SharedPreferences UseforProjectData;
    private SharedPreferences.Editor ProjectDataEditor;

    public EditLinearAdapter (EditPeopleDemand context) {
        this.mContext = context;
    }
    @NonNull
    @Override
    public EditLinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_epd_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EditLinearAdapter.LinearViewHolder holder, int position) {

        UseforProjectData=mContext.getSharedPreferences("projectdata",MODE_PRIVATE);

        String member_title=UseforProjectData.getString("member_title","");
        String member_tag=UseforProjectData.getString("member_tag","");
        String remark=UseforProjectData.getString("remark","");

        String[] member_titlestrarr = member_title.split("~");
        String[] remarkstrarr = remark.split("~");
        String[] member_tagstrarr = member_tag.split("~");

        //将各个项目名字写上
        for(int m=0;m<member_titlestrarr.length;m++){
            if(position==m) {
                holder.TEXTmember_title.setText(member_titlestrarr[m]);
                holder.TEXTremark.setText(remarkstrarr[m]);
            String[] tagstrarr = member_tagstrarr[m].split(",");
            for (String s : tagstrarr) {
                if (s.equals("包装设计")) holder.C1.setChecked(true);
                if (s.equals("平面设计")) holder.C2.setChecked(true);
                if (s.equals("UI设计")) holder.C3.setChecked(true);
                if (s.equals("产品设计")) holder.C4.setChecked(true);
                if (s.equals("英语")) holder.C5.setChecked(true);
                if (s.equals("其他外语")) holder.C6.setChecked(true);
                if (s.equals("视频剪辑")) holder.C7.setChecked(true);
                if (s.equals("演讲能力")) holder.C8.setChecked(true);
                if (s.equals("Photoshop")) holder.C9.setChecked(true);
                if (s.equals("PPT制作")) holder.C10.setChecked(true);
                if (s.equals("C")) holder.C11.setChecked(true);
                if (s.equals("JAVA")) holder.C12.setChecked(true);
                if (s.equals("微信小程序开发")) holder.C13.setChecked(true);
                if (s.equals("Android开发")) holder.C14.setChecked(true);
                if (s.equals("IOS开发")) holder.C15.setChecked(true);
            }}
        }
    }

    @Override
    public int getItemCount() {
        //获取当前项目所需人员数量
        SharedPreferences shp = mContext.getSharedPreferences(shpName, Context.MODE_PRIVATE);
        int m=shp.getInt("curr_pj",-1);
        String member_count=shp.getString("count_member","");
        String[] count_memberstrarr = member_count.split("~");

        return Integer.parseInt(count_memberstrarr[m]);
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView TEXTmember_title,TEXTremark;
        private CheckBox C1,C2,C3,C4,C5,C6,C7,C8,C9,C10,C11,C12,C13,C14,C15;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            TEXTmember_title = itemView.findViewById(R.id.rv_epd_member_title);
            TEXTremark = itemView.findViewById(R.id.rv_epd_item_content);

            C1=itemView.findViewById(R.id.rv_epd_cb_1);
            C2=itemView.findViewById(R.id.rv_epd_cb_2);
            C3=itemView.findViewById(R.id.rv_epd_cb_3);
            C4=itemView.findViewById(R.id.rv_epd_cb_4);
            C5=itemView.findViewById(R.id.rv_epd_cb_5);
            C6=itemView.findViewById(R.id.rv_epd_cb_6);
            C7=itemView.findViewById(R.id.rv_epd_cb_7);
            C8=itemView.findViewById(R.id.rv_epd_cb_8);
            C9=itemView.findViewById(R.id.rv_epd_cb_9);
            C10=itemView.findViewById(R.id.rv_epd_cb_10);
            C11=itemView.findViewById(R.id.rv_epd_cb_11);
            C12=itemView.findViewById(R.id.rv_epd_cb_12);
            C13=itemView.findViewById(R.id.rv_epd_cb_13);
            C14=itemView.findViewById(R.id.rv_epd_cb_14);
            C15=itemView.findViewById(R.id.rv_epd_cb_15);
        }
    }

}
