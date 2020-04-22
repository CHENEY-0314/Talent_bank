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
import com.example.talent_bank.MyApplyActivity;
import com.example.talent_bank.R;

import cn.refactor.lib.colordialog.ColorDialog;

public class MyApplyAdapter extends RecyclerView.Adapter<MyApplyAdapter.LinearViewHolder> {
    private MyApplyActivity mContext;
    private String shpName = "SHP_NAME";

    public MyApplyAdapter (MyApplyActivity mContext) {
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public MyApplyAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyApplyAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_my_apply_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyApplyAdapter.LinearViewHolder holder, final int position) {
        holder.textView.setOnClickListener(new View.OnClickListener() {
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
                        SharedPreferences shp = mContext.getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = shp.edit();
                        dialog.dismiss();
                        int x = shp.getInt("myApplyNum_key",3);
                        x = x-1;
                        editor.putInt("myApplyNum_key",x);
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
        int x = shp.getInt("myApplyNum_key",3);
        return x;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.my_apply_delete);
        }
    }
}
