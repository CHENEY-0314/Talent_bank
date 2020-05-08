package com.example.talent_bank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.ProjectContents;
import com.example.talent_bank.ProjectContentsApply;
import com.example.talent_bank.user_fragment.MyApplyActivity;
import com.example.talent_bank.R;

import cn.refactor.lib.colordialog.ColorDialog;

import static android.content.Context.MODE_PRIVATE;

public class MyApplyAdapter extends RecyclerView.Adapter<MyApplyAdapter.LinearViewHolder> {
    private MyApplyActivity mContext;
    //以下用于手机存用户信息
    private SharedPreferences AllApplyData;
    private SharedPreferences.Editor AllApplyDataEditor;

    private SharedPreferences AllProjectData;
    private SharedPreferences.Editor AllProjectDataEditor;

    private SharedPreferences Userdata;

    public MyApplyAdapter (MyApplyActivity mContext) {
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public MyApplyAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllApplyData = mContext.getSharedPreferences("all_apply_data", MODE_PRIVATE);
        String apply_id = AllApplyData.getString("apply_id", "");
        if (apply_id.equals("")) {
            return new MyApplyAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.released_null_apply,parent,false));
        } else {
            return new MyApplyAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_my_apply_item,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyApplyAdapter.LinearViewHolder holder, final int position) {
        AllProjectData = mContext.getSharedPreferences("all_project_data",MODE_PRIVATE);
        AllProjectDataEditor = AllProjectData.edit();
        AllApplyData = mContext.getSharedPreferences("all_apply_data", MODE_PRIVATE);
        String apply_id = AllApplyData.getString("apply_id", "");
        final String apply_project_id = AllApplyData.getString("apply_project_id", "");
        String apply_project_title = AllApplyData.getString("apply_project_title", "");
        String apply_project_content = AllApplyData.getString("apply_project_content", "");

        final String[] applyIdStrarr = apply_id.split("~");
        String[] applyProjectIdStrarr = apply_project_id.split("~");
        String[] applyProjectTitleStrarr = apply_project_title.split("~");
        String[] applyProjectContentStrarr = apply_project_content.split("~");
        //为每个item设置项目Text
        if (!apply_id.equals("")) {
            for (int m = 0; m < applyIdStrarr.length; m++) {
                if (position == m) {
                    final int finalM = m;
                    holder.title.setText(applyProjectTitleStrarr[m]);
                    holder.content.setText(applyProjectContentStrarr[m]);
                    holder.detail.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            AllProjectDataEditor.putInt("curr_pj",position);
                            AllProjectDataEditor.apply();
                            Intent intent = new Intent (mContext, ProjectContentsApply.class);
                            mContext.startActivity(intent);
                        }
                    });
                    holder.delete.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ColorDialog dialog = new ColorDialog(mContext);
                            dialog.setTitle("提示");
                            dialog.setColor("#ffffff");//颜色
                            dialog.setContentTextColor("#656565");
                            dialog.setTitleTextColor("#656565");
                            dialog.setContentText("是否撤销对此项目的申请？\n申请一但删除则无法恢复。");
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
                }
            }
        }
    }

    @Override
    public int getItemCount() {
        AllApplyData = mContext.getSharedPreferences("all_apply_data",MODE_PRIVATE);
        String apply_id = AllApplyData.getString("apply_id","");
        if (apply_id.equals("")) {
            return 1;
        } else {
            String[] strarr = apply_id.split("~");
            return strarr.length;
        }
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView delete,detail,title,content;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            title= itemView.findViewById(R.id.my_apply_title);
            content= itemView.findViewById(R.id.my_apply_content);
            delete= itemView.findViewById(R.id.my_apply_delete);
            detail= itemView.findViewById(R.id.my_apply_detail);
        }
    }
}
