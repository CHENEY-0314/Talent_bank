package com.example.talent_bank.Adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.ProjectContentsApply;
import com.example.talent_bank.R;
import com.example.talent_bank.home_page.FindFragment;

public class FindingAdapter extends RecyclerView.Adapter<FindingAdapter.LinearViewHolder> {
    private FindFragment mFragment;

    public FindingAdapter (FindFragment mFragment) {
        this.mFragment = mFragment;
    }
    @NonNull
    @Override
    public FindingAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FindingAdapter.LinearViewHolder(LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.rv_finding_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull FindingAdapter.LinearViewHolder holder, int position) {
        holder.detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mFragment.getActivity(), ProjectContentsApply.class);
                mFragment.getActivity().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return 5;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView detail;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            detail = itemView.findViewById(R.id.finding_detail);
        }
    }
}
