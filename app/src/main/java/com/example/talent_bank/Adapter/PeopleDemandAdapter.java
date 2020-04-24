package com.example.talent_bank.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.PeopleDemand;
import com.example.talent_bank.ProjectContents;
import com.example.talent_bank.R;

import cn.refactor.lib.colordialog.ColorDialog;

public class PeopleDemandAdapter extends RecyclerView.Adapter<PeopleDemandAdapter.LinearViewHolder> {
    private PeopleDemand mContext;

    public PeopleDemandAdapter (PeopleDemand Context) {
        this.mContext = Context;
    }

    @NonNull
    @Override
    public PeopleDemandAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new PeopleDemandAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_people_demand_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull PeopleDemandAdapter.LinearViewHolder holder, int position) {
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDialog dialog = new ColorDialog(mContext);
                dialog.setTitle("提示");
                dialog.setColor("#ffffff");//颜色
                dialog.setContentTextColor("#656565");
                dialog.setTitleTextColor("#656565");
                dialog.setContentText("是否确定申请“软件开发”职位？");
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
        return 3;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private Button button;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            button = itemView.findViewById(R.id.pd_item_apply);
        }
    }
}
