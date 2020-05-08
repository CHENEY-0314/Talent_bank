package com.example.talent_bank.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.R;

public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.LinearViewHolder> {
    private Context mContext;

    public MyCollectionAdapter (Context context) {
        this.mContext = context;
    }

    //设置不同item标上序号以区别不同类型显示
    @Override
    public int getItemViewType(int position) {  //判断Item的Type
        if(position % 3 ==0){
            return 0;
        } else if(position % 3 ==1){
            return 1;
        }else return 2;
    }
    @NonNull
    @Override
    public MyCollectionAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
//        if (pj_id.equals("")) {   //做判断当为空的时候显示此
//            return new MyCollectionAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.my_collection_null, parent, false));
//        }
        if (i==0) {
            return new MyCollectionAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_mypush_item2, parent, false));
        } else if(i==1){
            return new MyCollectionAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_mypush_item, parent, false));
        }else  return new MyCollectionAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_mypush_item1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCollectionAdapter.LinearViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 2;
    }

    //声明控件
    class LinearViewHolder extends RecyclerView.ViewHolder {
        private CardView cardView;
        private TextView pj_name;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.mypush_card);
            pj_name=itemView.findViewById(R.id.mypush_text);
        }
    }
}
