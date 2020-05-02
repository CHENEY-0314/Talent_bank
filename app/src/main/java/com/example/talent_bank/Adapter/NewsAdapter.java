package com.example.talent_bank.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.talent_bank.R;
import com.example.talent_bank.home_page.NewsFragment;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.LinearViewHolder> {
    private NewsFragment mFragment;


    public NewsAdapter (NewsFragment mFragment) {
        this.mFragment = mFragment;
    }
    @NonNull
    @Override
    public NewsAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NewsAdapter.LinearViewHolder(LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.rv_news_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.LinearViewHolder holder, int position) {

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
