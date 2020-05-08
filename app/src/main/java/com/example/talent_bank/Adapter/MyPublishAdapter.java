package com.example.talent_bank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.ProjectContents;
import com.example.talent_bank.R;
import com.example.talent_bank.user_fragment.MyPublishActivity;

import static android.content.Context.MODE_PRIVATE;

public class MyPublishAdapter extends RecyclerView.Adapter<MyPublishAdapter.LinearViewHolder> {
    private Context mContext;
    private AdapterView.OnItemClickListener mListener; //点击事件

    //以下用于手机存用户信息
    private SharedPreferences UseforProjectData;
    private SharedPreferences.Editor ProjectDataEditor;

    public MyPublishAdapter(Context context, AdapterView.OnItemClickListener listener) {
        this.mContext = context;
        this.mListener = listener;

    }

    public MyPublishAdapter(Context context) {
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
    //设置不同item类型的样式
    @NonNull
    @Override
    public MyPublishAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        if (i==0) {
            return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_mypush_item2, parent, false));
        } else if(i==1){
            return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_mypush_item, parent, false));
        }else  return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_mypush_item1, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull MyPublishAdapter.LinearViewHolder holder, final int position) {
        UseforProjectData=mContext.getSharedPreferences("projectdata",MODE_PRIVATE);
        String pj_id=UseforProjectData.getString("pj_id","");
        String pj_name=UseforProjectData.getString("pj_name","");
        String[] IDstrarr = pj_id.split("~");
        String[] Namestrarr = pj_name.split("~");

        //将各个项目名字写上
        for(int m=0;m<IDstrarr.length;m++){
            if(position==m) holder.pj_name.setText(Namestrarr[m]);
        }

        ProjectDataEditor=UseforProjectData.edit();

        //设置item的点击事件
        holder.cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ProjectDataEditor.putInt("curr_pj",position);
                        ProjectDataEditor.apply();
                        Intent intent = new Intent(mContext, ProjectContents.class);
                        mContext.startActivity(intent);
                    }
                });
    }

    @Override
    public int getItemCount() {
        UseforProjectData = mContext.getSharedPreferences("projectdata",MODE_PRIVATE);
        String pj_id=UseforProjectData.getString("pj_id","");
        String[] strarr = pj_id.split("~");
        return strarr.length;
    }  //项目数量

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

    //点击事件
    public interface OnItemClickListener {
        void onClick(int pos);
    }
}