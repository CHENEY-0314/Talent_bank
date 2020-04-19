package com.example.talent_bank;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReceiveApplyAdapter extends RecyclerView.Adapter<ReceiveApplyAdapter.LinearViewHolder> {
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
    public void onBindViewHolder(@NonNull ReceiveApplyAdapter.LinearViewHolder holder, int position) {
        holder.textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  //给textView设置下划线
    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.RA_item_job);
        }
    }
}