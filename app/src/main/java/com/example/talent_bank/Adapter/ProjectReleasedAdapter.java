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
import com.example.talent_bank.ProjectReleased2;
import com.example.talent_bank.R;

public class ProjectReleasedAdapter extends RecyclerView.Adapter<ProjectReleasedAdapter.LinearViewHolder> {
    private String shpName = "SHP_NAME";
    private ProjectReleased2 mContext;
    public ProjectReleasedAdapter (ProjectReleased2 context) {
        this.mContext = context;
    }
    @NonNull
    @Override
    public ProjectReleasedAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ProjectReleasedAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_pr_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ProjectReleasedAdapter.LinearViewHolder holder, int position) {
    }

    @Override
    public int getItemCount() {
        SharedPreferences shp = mContext.getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
        int x = shp.getInt("demandNum_key",0);
        return x;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
