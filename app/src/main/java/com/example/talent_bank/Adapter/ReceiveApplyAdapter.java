package com.example.talent_bank.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.ReceiveApply;
import com.example.talent_bank.R;

import cn.refactor.lib.colordialog.ColorDialog;

import static android.content.Context.MODE_PRIVATE;

public class ReceiveApplyAdapter extends RecyclerView.Adapter<ReceiveApplyAdapter.LinearViewHolder> {
    private String shpName = "SHP_NAME";
    private ReceiveApply mContext;

    //以下用于手机存用户信息
    private SharedPreferences ReceiveApplyData;
    private SharedPreferences.Editor ReceiveApplyDataEditor;

    public ReceiveApplyAdapter (ReceiveApply context) {
        this.mContext = context;
    }
    @NonNull
    @Override
    public ReceiveApplyAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ReceiveApplyData = mContext.getSharedPreferences("receive_apply_data", MODE_PRIVATE);
        String apply_id = ReceiveApplyData.getString("apply_id", "");
        if (apply_id.equals("")) {
            return new ReceiveApplyAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.receive_null_apply,parent,false));
        }else {
            return new ReceiveApplyAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_receive_apply_item,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveApplyAdapter.LinearViewHolder holder, final int position) {
        ReceiveApplyData = mContext.getSharedPreferences("receive_apply_data", MODE_PRIVATE);
        String apply_id = ReceiveApplyData.getString("apply_id", "");
        String apply_job = ReceiveApplyData.getString("apply_job", "");
        String apply_send_name = ReceiveApplyData.getString("apply_send_name", "");
        String apply_send_number = ReceiveApplyData.getString("apply_send_number", "");
        String apply_project_title = ReceiveApplyData.getString("apply_project_title", "");

        final String[] applyIdStrarr = apply_id.split("~");
        final String[] applyJobStrarr = apply_job.split("~");
        String[] applySendNameStrarr = apply_send_name.split("~");
        final String[] applySendNumberStrarr = apply_send_number.split("~");
        final String[] applyProjectTitleStrarr = apply_project_title.split("~");
        //为每个item设置项目Text
        if (!apply_id.equals("")) {
            for (int m = 0; m < applyIdStrarr.length; m++) {
                if (position == m) {
                    holder.job.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  //给textView设置下划线
                    holder.job.setText(applyJobStrarr[m]);
                    holder.name.setText(applySendNameStrarr[m]);

                    final int finalM = m;
                    holder.imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ColorDialog dialog = new ColorDialog(mContext);
                            dialog.setTitle("提示");
                            dialog.setColor("#ffffff");//颜色
                            dialog.setContentTextColor("#656565");
                            dialog.setTitleTextColor("#656565");
                            dialog.setContentText("是否确定删除此人的申请?\n申请一但删除则无法恢复。");
                            dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    //删除申请的操作
                                    mContext.deleteApply(applyIdStrarr[finalM]);
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
                    holder.agree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ColorDialog dialog = new ColorDialog(mContext);
                            dialog.setTitle("提示");
                            dialog.setColor("#ffffff");//颜色
                            dialog.setContentTextColor("#656565");
                            dialog.setTitleTextColor("#656565");
                            dialog.setContentText("是否同意此人的申请?并发送消息通知对方。");
                            dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    //同意申请的操作
                                    mContext.SendNewsAgree(applySendNumberStrarr[finalM],applyJobStrarr[finalM],applyProjectTitleStrarr[finalM]);
                                    mContext.deleteApply(applyIdStrarr[finalM]);
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
                    holder.disagree.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ColorDialog dialog = new ColorDialog(mContext);
                            dialog.setTitle("提示");
                            dialog.setColor("#ffffff");//颜色
                            dialog.setContentTextColor("#656565");
                            dialog.setTitleTextColor("#656565");
                            dialog.setContentText("是否拒绝此人的申请?并发送消息通知对方。");
                            dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    //拒绝申请的操作
                                    mContext.SendNewsDisagree(applySendNumberStrarr[finalM],applyJobStrarr[finalM],applyProjectTitleStrarr[finalM]);
                                    mContext.deleteApply(applyIdStrarr[finalM]);
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
            }
        }

    }

    @Override
    public int getItemCount() {
        ReceiveApplyData = mContext.getSharedPreferences("receive_apply_data", MODE_PRIVATE);
        String apply_id = ReceiveApplyData.getString("apply_id", "");
        if (apply_id.equals("")) {
            return 1;
        } else {
            String[] strarr = apply_id.split("~");
            return strarr.length;
        }
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView job,name,jianli,agree,disagree;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            job = itemView.findViewById(R.id.RA_item_job);
            imageView = itemView.findViewById(R.id.rv_RA_item_delete);
            name = itemView.findViewById(R.id.send_apply_name);
            jianli = itemView.findViewById(R.id.receive_apply_jianli);
            agree = itemView.findViewById(R.id.receive_apply_agree);
            disagree = itemView.findViewById(R.id.receive_apply_disagree);
        }
    }
}