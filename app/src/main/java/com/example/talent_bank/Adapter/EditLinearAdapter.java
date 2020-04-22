package com.example.talent_bank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.EditDemand;
import com.example.talent_bank.EditPeopleDemand;
import com.example.talent_bank.R;

public class EditLinearAdapter extends RecyclerView.Adapter<EditLinearAdapter.LinearViewHolder> {
    private String shpName = "SHP_NAME";
    private EditPeopleDemand mContext;

    public EditLinearAdapter (EditPeopleDemand context) {
        this.mContext = context;
    }
    @NonNull
    @Override
    public EditLinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_epd_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull EditLinearAdapter.LinearViewHolder holder, int position) {
        holder.textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);  //给textView设置下划线
        holder.textView.setOnClickListener(new View.OnClickListener() {  //点击返回按钮返回上一页面
            @Override
            public void onClick(View v) {  //点击编辑按钮
                Intent intent = new Intent(mContext, EditDemand.class);
                mContext.startActivity(intent);
            }
        });
        SharedPreferences shp = mContext.getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        String x =shp.getString("editDemand_key","");
        holder.textContent.setText(x);
    }

    @Override
    public int getItemCount() {
        SharedPreferences shp = mContext.getSharedPreferences(shpName, Context.MODE_PRIVATE);
        int x = shp.getInt("editNum_key",0);
        return x;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView textView,textContent;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.epd_item_textview);
            textContent = itemView.findViewById(R.id.epd_item_content);
        }
    }

}
