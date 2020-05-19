package com.example.talent_bank.Adapter;


import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.ProjectContents;
import com.example.talent_bank.R;
import com.example.talent_bank.TalentBank.OthersBiographical;
import com.example.talent_bank.TalentBank.TalentBank;
import com.example.talent_bank.user_fragment.MyApplyActivity;
import com.google.android.flexbox.FlexboxLayout;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

import cn.refactor.lib.colordialog.ColorDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

import static android.content.Context.MODE_PRIVATE;
import static com.example.talent_bank.user_fragment.ChangeImageActivity.convertIconToString;

public class TalentBankAdapter extends RecyclerView.Adapter<TalentBankAdapter.LinearViewHolder> {
    private TalentBank mContext;

    private Bitmap userimage;
    //以下用于手机存用户信息
    private SharedPreferences AllUsersData;
    private SharedPreferences.Editor AllUsersDataEditor;

    private SharedPreferences SHP;
    private SharedPreferences.Editor SHPEditor;

    public TalentBankAdapter (TalentBank mContext) {
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public TalentBankAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllUsersData = mContext.getSharedPreferences("all_users_data",MODE_PRIVATE);
        String user_name = AllUsersData.getString("user_name","");
        if(user_name.equals("")) {
            return new TalentBankAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_null_finding,parent,false));
        } else {
            return new TalentBankAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_talent_bank_item,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final TalentBankAdapter.LinearViewHolder holder, int position) {
        SHP = mContext.getSharedPreferences("SHP_NAME",MODE_PRIVATE);
        AllUsersData = mContext.getSharedPreferences("all_users_data",MODE_PRIVATE);
        AllUsersDataEditor = AllUsersData.edit();
        String user_name = AllUsersData.getString("user_name","");
        String user_grade = AllUsersData.getString("user_grade","");
        String user_tag = AllUsersData.getString("user_tag","");
        String user_number = AllUsersData.getString("user_number","");

        String[] Namestrarr = user_name.split("~");
        String[] Gradestrarr = user_grade.split("~");
        String[] Tagstrarr = user_tag.split("~");
        final String[] Userstrarr = user_number.split("~");

        if(!user_name.equals("")) {
            //为每个item设置项目Text
            for (int m = 0; m < Namestrarr.length; m++) {
                if (position == m) {
                    final int finalM1 = m;
                    holder.sendNews.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //点击”沟通“的监听
                            ColorDialog dialog = new ColorDialog(mContext);
                            dialog.setTitle("提示");
                            dialog.setColor("#ffffff");//颜色
                            dialog.setContentTextColor("#656565");
                            dialog.setTitleTextColor("#656565");
                            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
                            dialog.setContentText("是否向此同学发出沟通邀请");
                            dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    //确定操作
                                    mContext.SendNews(Userstrarr[finalM1]);
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

                    //点击简历
                    holder.mMore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AllUsersDataEditor.putString("curr_user",Userstrarr[finalM1]);
                            AllUsersDataEditor.apply();
                            Intent intent2= new Intent(mContext, OthersBiographical.class);
                            mContext.startActivity(intent2);
                        }
                    });

                    downloadPic(Userstrarr[m]);
                    while (true) {
                        String done = SHP.getString("Done","");
                        if(done.equals("true")) {
                            if(userimage!=null) {
                                holder.mImage.setImageBitmap(userimage);
                            }
                            break;
                        }
                    }

                    holder.userName.setText(Namestrarr[m]);
                    holder.userGrade.setText(Gradestrarr[m]);
                    String[] Tag = Tagstrarr[m].split(",");
                    for (int i = 0; i < Tag.length; i++) {
                        if (Tag[i].equals("包装设计")) {
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
    }

    @Override
    public int getItemCount() {
        AllUsersData = mContext.getSharedPreferences("all_users_data",MODE_PRIVATE);
        String user_name = AllUsersData.getString("user_name","");
        if(user_name.equals("")) {
            return 1;
        } else {
            String[] strarr = user_name.split("~");
            return strarr.length;
        }
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        TextView userName,userGrade,sendNews,mMore;
        FlexboxLayout tagBox;
        CircleImageView mImage;
        TextView mC1,mC2,mC3,mC4,mC5,mC6,mC7,mC8,mC9,mC10,mC11,mC12,mC13,mC14,mC15;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mMore=itemView.findViewById(R.id.rv_talent_bank_more);
            mImage=itemView.findViewById(R.id.talent_bank_userimage);
            userName = itemView.findViewById(R.id.item_user_name);
            userGrade = itemView.findViewById(R.id.item_user_grade);
            tagBox = itemView.findViewById(R.id.talent_bank_tag_box);
            sendNews = itemView.findViewById(R.id.rv_talent_bank_touch);
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


    //获取用户头像
    private Bitmap downloadPic(String number) {
        SHP = mContext.getSharedPreferences("SHP_NAME",MODE_PRIVATE);
        SHPEditor = SHP.edit();
        SHPEditor.putString("Done","false");
        SHPEditor.apply();

        String url="http://47.107.125.44:8080/Talent_bank/userimagefiles/"+number+".png";

        OkHttpClient okHttpClient = new OkHttpClient();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                userimage=null;
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();//得到图片的流
                userimage = BitmapFactory.decodeStream(inputStream);
            }
        });
        SHPEditor.putString("Done","true");
        SHPEditor.apply();
        return userimage;
    }


}
