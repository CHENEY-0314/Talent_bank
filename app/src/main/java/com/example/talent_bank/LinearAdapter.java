package com.example.talent_bank;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class LinearAdapter extends RecyclerView.Adapter<LinearAdapter.LinearViewHolder> {
    private Context mContext;
    //构造方法为private变量赋值
    public LinearAdapter(Context context) {
        this.mContext = context;
    }

    @NonNull
    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) { //创建一个Holder连接到对应的layout
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_mypush_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder holder, int position) {  //对position的item的控件赋值或进行点击事件控制
        //for example
//        holder.textView.setText("Hello World!");
//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(mContext,"click..."+position,Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public int getItemCount() {  //决定item的数量
        return 8;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {  //对控件进行绑定id
        //for example
//        private TextView textView;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
//            textView = itemView.findViewById(R.id.tv_title);
        }
    }


}
