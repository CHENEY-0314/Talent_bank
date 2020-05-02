package com.example.talent_bank.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.PeopleDemand;
import com.example.talent_bank.ProjectContents;
import com.example.talent_bank.R;

import java.util.Objects;

import cn.refactor.lib.colordialog.ColorDialog;

import static android.content.Context.MODE_PRIVATE;

public class PeopleDemandAdapter extends RecyclerView.Adapter<PeopleDemandAdapter.LinearViewHolder> {
    private PeopleDemand mContext;

    //以下用于手机存用户信息
    private SharedPreferences AllProjectData;
    private SharedPreferences.Editor AllProjectDataEditor;

    public PeopleDemandAdapter (PeopleDemand Context) {
        this.mContext = Context;
    }

    @NonNull
    @Override
    public PeopleDemandAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PeopleDemandAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_people_demand_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleDemandAdapter.LinearViewHolder holder, final int position) {
        AllProjectData = mContext.getSharedPreferences("all_project_data",MODE_PRIVATE);
        String member_title = AllProjectData.getString("member_title","");
        String member_tag = AllProjectData.getString("member_tag","");
        String remark = AllProjectData.getString("remark","");

        final String[] member_titlestrarr = member_title.split("~");
        String[] remarkstrarr = remark.split("~");
        String[] member_tagstrarr = member_tag.split("~");

        //将各个项目名字写上
        for(int m=0;m<member_titlestrarr.length;m++) {
            if(position==m) {
                holder.textName.setText(member_titlestrarr[m]);
                holder.textContent.setText(remarkstrarr[m]);
                String[] membertag=member_tagstrarr[m].split(",");
                String tag="";
                for(int yy=0;yy<membertag.length;yy++){
                    if(!membertag[yy].equals("C")){
                    if(yy==0) tag=membertag[yy];
                    else tag=tag+"; "+membertag[yy];
                    }else{
                        if(yy==0) tag="C++";
                        else tag=tag+"; "+"C++";
                    }
                }
                holder.textTag.setText(tag);
            }
        }

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String job="";
                job="是否确定申请 “"+member_titlestrarr[position]+"” 职位？";
                ColorDialog dialog = new ColorDialog(mContext);
                dialog.setTitle("提示");
                dialog.setColor("#ffffff");//颜色
                dialog.setContentTextColor("#656565");
                dialog.setTitleTextColor("#656565");
                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
                dialog.setContentText(job);
                dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        //提交申请操作
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
        });

    }

    @Override
    public int getItemCount() {
        SharedPreferences shp = mContext.getSharedPreferences("all_project_data", Context.MODE_PRIVATE);
        int m=shp.getInt("curr_pj",-1);
        String member_count=shp.getString("count_member","");
        String[] count_memberstrarr = member_count.split("~");
        return Integer.parseInt(count_memberstrarr[m]);
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private Button button;
        TextView textName,textTag,textContent;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.pd_item_apply);
            textName = itemView.findViewById(R.id.epd_item_name);
            textTag = itemView.findViewById(R.id.epd_item_tag);
            textContent = itemView.findViewById(R.id.epd_item_content);
        }
    }
}
