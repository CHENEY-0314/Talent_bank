package com.example.talent_bank.Adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.ProjectContentsApply;
import com.example.talent_bank.R;
import com.example.talent_bank.home_page.NewsFragment;

import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Context.MODE_PRIVATE;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.LinearViewHolder> {
    private NewsFragment mFragment;
    //以下用于手机存用户信息
    private SharedPreferences AllNewsData;
    private SharedPreferences.Editor AllNewsDataEditor;

    public NewsAdapter (NewsFragment mFragment) {
        this.mFragment = mFragment;
    }
    @NonNull
    @Override
    public NewsAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllNewsData = mFragment.getActivity().getSharedPreferences("all_news_data",MODE_PRIVATE);
        String news_number = AllNewsData.getString("news_number","");
        if(news_number.equals("")) {
            return new NewsAdapter.LinearViewHolder(LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.rv_null_news,parent,false));
        } else {
            return new NewsAdapter.LinearViewHolder(LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.rv_news_item,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull NewsAdapter.LinearViewHolder holder, int position) {
        AllNewsData = mFragment.getActivity().getSharedPreferences("all_news_data",MODE_PRIVATE);
        AllNewsDataEditor = AllNewsData.edit();
        String news_send_name = AllNewsData.getString("news_send_name","");
        String news_in_checked = AllNewsData.getString("news_in_checked","");
        String news_time = AllNewsData.getString("news_time","");
        String news_send_content = AllNewsData.getString("news_send_content","");
        String[] nameStrarr = news_send_name.split("~");
        String[] checkedStrarr = news_in_checked.split("~");
        String[] timeStrarr = news_time.split("~");
        String[] contentStrarr = news_send_content.split("~");

        //为每个item设置项目Text
        if (!news_send_name.equals("")) {
            for(int m=0;m<nameStrarr.length;m++){
                if(position==m) {
                    holder.name.setText(nameStrarr[m]);
                    holder.content.setText(contentStrarr[m]);
                    holder.time.setText(timeStrarr[m]);
                    if(checkedStrarr[m].equals("true")) {
                        holder.checked.setVisibility(View.GONE);
                    }
                }
            }

        }

    }

    @Override
    public int getItemCount() {
        AllNewsData = mFragment.getActivity().getSharedPreferences("all_news_data",MODE_PRIVATE);
        String news_number = AllNewsData.getString("news_number","");
        if (news_number.equals("")) {
            return 1;
        } else {
            String[] strarr = news_number.split("~");
            return strarr.length;
        }
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView content;
        TextView time;
        TextView checked;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.news_item_name);
            content = itemView.findViewById(R.id.news_item_content);
            time = itemView.findViewById(R.id.news_item_time);
            checked = itemView.findViewById(R.id.news_item_tag);
        }
    }
}
