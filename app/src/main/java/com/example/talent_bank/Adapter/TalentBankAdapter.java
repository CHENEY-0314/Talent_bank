package com.example.talent_bank.Adapter;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.R;
import com.example.talent_bank.TalentBank.TalentBank;

public class TalentBankAdapter extends RecyclerView.Adapter<TalentBankAdapter.LinearViewHolder> {
    private TalentBank mContext;

    public TalentBankAdapter (TalentBank mContext) {
        this.mContext = mContext;
    }
    @NonNull
    @Override
    public TalentBankAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TalentBankAdapter.LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.rv_talent_bank_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull TalentBankAdapter.LinearViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
