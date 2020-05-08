package com.example.talent_bank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.ProjectContentsApply;
import com.example.talent_bank.R;

import static android.content.Context.MODE_PRIVATE;

public class MyCollectionAdapter extends RecyclerView.Adapter<MyCollectionAdapter.LinearViewHolder> {
    private Context mContext;

    //以下用于手机存用户信息
    private SharedPreferences AllProjectData;
    private SharedPreferences.Editor AllProjectDataEditor;

    private SharedPreferences ShareUserData;

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
        if (i==0) {
            return new MyCollectionAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_mypush_item2, parent, false));
        } else if(i==1){
            return new MyCollectionAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_mypush_item, parent, false));
        }else  return new MyCollectionAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_mypush_item1, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyCollectionAdapter.LinearViewHolder holder, final int position) {

        AllProjectData =mContext.getSharedPreferences("all_project_data",MODE_PRIVATE);
        AllProjectDataEditor = AllProjectData.edit();
        String pj_id = AllProjectData.getString("pj_id","");
        String pj_name = AllProjectData.getString("pj_name","");

        String[] IDstrarr = pj_id.split("~");
        String[] Namestrarr = pj_name.split("~");

        //为每个item设置项目Text
        if (!pj_id.equals("")) {
            for(int m=0;m<IDstrarr.length;m++){
                if(position==m) {
                    holder.pj_name.setText(Namestrarr[m]);
                }
            }

            //点击详情的点击事件
            holder.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllProjectDataEditor.putInt("curr_pj",position);
                    AllProjectDataEditor.apply();
                    Intent intent = new Intent(mContext, ProjectContentsApply.class);
                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        AllProjectData = mContext.getSharedPreferences("all_project_data",MODE_PRIVATE);
        String pj_id = AllProjectData.getString("pj_id","");
        if (pj_id.equals("null")) {
            return 0;
        } else {
            String[] strarr = pj_id.split("~");
            return strarr.length;
        }

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
