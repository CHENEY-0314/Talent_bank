package com.example.talent_bank;

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

import cn.refactor.lib.colordialog.ColorDialog;

public class ReceiveApplyAdapter extends RecyclerView.Adapter<ReceiveApplyAdapter.LinearViewHolder> {
    private String shpName = "SHP_NAME";
    private ReceiveApply mContext;

    public ReceiveApplyAdapter (ReceiveApply context) {
        this.mContext = context;
    }
    @NonNull
    @Override
    public ReceiveApplyAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ReceiveApplyAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_receive_apply_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ReceiveApplyAdapter.LinearViewHolder holder, final int position) {
        holder.textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  //给textView设置下划线
//        为了测试app而做的初始化动作，需要重新初始化界面numb可以使用以下代码
//        SharedPreferences shp = mContext.getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = shp.edit();
//        editor.putInt("receiveApplyNum_key",3);
//        editor.apply();
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ColorDialog dialog = new ColorDialog(mContext);
                dialog.setTitle("提示");
                dialog.setColor("#ffffff");//颜色
                dialog.setContentTextColor("#656565");
                dialog.setTitleTextColor("#656565");
                dialog.setContentText("是否确定拒绝并删除此人的申请?\n申请一但删除则无法恢复。");
                dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                    @Override
                    public void onClick(ColorDialog dialog) {
                        //删除申请的操作
                        SharedPreferences shp = mContext.getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shp.edit();
                        dialog.dismiss();
                        int x = shp.getInt("receiveApplyNum_key",3);
                        x = x-1;
                        editor.putInt("receiveApplyNum_key",x);
                        editor.apply();
                        notifyItemRemoved(position);
                        int itemCount = x-1-position;
                        notifyItemRangeChanged(position,itemCount);
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
        SharedPreferences shp = mContext.getSharedPreferences(shpName, Context.MODE_PRIVATE);
        int x = shp.getInt("receiveApplyNum_key",3);
        return x;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private ImageView imageView;
        private TextView textView;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.RA_item_job);
            imageView = itemView.findViewById(R.id.rv_RA_item_delete);
        }
    }
}