package com.example.talent_bank.Adapter;


import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.R;
import com.example.talent_bank.TalentBank.TalentBank;
import com.google.android.flexbox.FlexboxLayout;

import static android.content.Context.MODE_PRIVATE;

public class TalentBankAdapter extends RecyclerView.Adapter<TalentBankAdapter.LinearViewHolder> {
    private TalentBank mContext;

    //以下用于手机存用户信息
    private SharedPreferences AllUsersData;
    private SharedPreferences.Editor AllUsersDataEditor;

    public TalentBankAdapter (TalentBank mContext) {
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public TalentBankAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TalentBankAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_talent_bank_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TalentBankAdapter.LinearViewHolder holder, int position) {
        AllUsersData = mContext.getSharedPreferences("all_users_data",MODE_PRIVATE);
        AllUsersDataEditor = AllUsersData.edit();
        String user_name = AllUsersData.getString("user_name","");
        String user_grade = AllUsersData.getString("user_grade","");
        String user_tag = AllUsersData.getString("user_tag","");
        String[] Namestrarr = user_name.split("~");
        String[] Gradestrarr = user_grade.split("~");
        String[] Tagstrarr = user_tag.split("~");
        //为每个item设置项目Text
        for(int m=0;m<Namestrarr.length;m++){
            if(position==m) {
                holder.userName.setText(Namestrarr[m]);
                holder.userGrade.setText(Gradestrarr[m]);
                String[] Tag = Tagstrarr[m].split(",");
                for(int i=0;i<Tag.length;i++) {
                    if(Tag[i].equals("包装设计")) {
                        holder.mC1.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("平面设计")) {
                        holder.mC2.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("UI设计")) {
                        holder.mC3.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("产品设计")) {
                        holder.mC4.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("英语")) {
                        holder.mC5.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("其他外语")) {
                        holder.mC6.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("视频剪辑")) {
                        holder.mC7.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("演讲能力")) {
                        holder.mC8.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("Photoshop")) {
                        holder.mC9.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("PPT制作")) {
                        holder.mC10.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("C")) {
                        holder.mC11.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("JAVA")) {
                        holder.mC12.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("微信小程序开发")) {
                        holder.mC13.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("Android开发")) {
                        holder.mC14.setVisibility(View.VISIBLE);
                    } else if (Tag[i].equals("IOS开发")) {
                        holder.mC15.setVisibility(View.VISIBLE);
                    }
                }
            }
        }
    }



    @Override
    public int getItemCount() {
        AllUsersData = mContext.getSharedPreferences("all_users_data",MODE_PRIVATE);
        String user_name = AllUsersData.getString("user_name","");
        String[] strarr = user_name.split("~");
        return strarr.length;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        TextView userName,userGrade;
        FlexboxLayout tagBox;
        TextView mC1,mC2,mC3,mC4,mC5,mC6,mC7,mC8,mC9,mC10,mC11,mC12,mC13,mC14,mC15;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            userName = itemView.findViewById(R.id.item_user_name);
            userGrade = itemView.findViewById(R.id.item_user_grade);
            tagBox = itemView.findViewById(R.id.talent_bank_tag_box);
            mC1 = itemView.findViewById(R.id.talent_bank_tag1);
            mC2 = itemView.findViewById(R.id.talent_bank_tag2);
            mC3 = itemView.findViewById(R.id.talent_bank_tag3);
            mC4 = itemView.findViewById(R.id.talent_bank_tag4);
            mC5 = itemView.findViewById(R.id.talent_bank_tag5);
            mC6 = itemView.findViewById(R.id.talent_bank_tag6);
            mC7 = itemView.findViewById(R.id.talent_bank_tag7);
            mC8 = itemView.findViewById(R.id.talent_bank_tag8);
            mC9 = itemView.findViewById(R.id.talent_bank_tag9);
            mC10 = itemView.findViewById(R.id.talent_bank_tag10);
            mC11 = itemView.findViewById(R.id.talent_bank_tag11);
            mC12 = itemView.findViewById(R.id.talent_bank_tag12);
            mC13 = itemView.findViewById(R.id.talent_bank_tag13);
            mC14 = itemView.findViewById(R.id.talent_bank_tag14);
            mC15 = itemView.findViewById(R.id.talent_bank_tag15);
        }
    }
}
