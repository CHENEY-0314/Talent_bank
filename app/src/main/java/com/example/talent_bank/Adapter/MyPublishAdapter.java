package com.example.talent_bank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.ProjectContents;
import com.example.talent_bank.R;

public class MyPublishAdapter extends RecyclerView.Adapter<MyPublishAdapter.LinearViewHolder> {
    private Context mContext;
    private AdapterView.OnItemClickListener mListener; //点击事件

    public MyPublishAdapter(Context context, AdapterView.OnItemClickListener listener) {
        this.mContext = context;
        this.mListener = listener;
    }

    public MyPublishAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public MyPublishAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (i==0) {
            return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_mypush_item, parent, false));
        } else {
            return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_mypush_item2, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull MyPublishAdapter.LinearViewHolder holder, final int position) {
        if(position % 2 !=0){  //position为奇数
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ProjectContents.class);
                    mContext.startActivity(intent);
                }
            });

        } else {   //偶数
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,ProjectContents.class);
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {  //判断Item的Type
        if(position % 2 !=0){  //position为奇数
            return 0;
        } else {   //偶数
            return 1;
        }
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.mypush_card);
        }
    }
    //点击事件
    public interface OnItemClickListener {
        void onClick(int pos);
    }
}