package com.example.talent_bank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.talent_bank.ProjectContentsApply;
import com.example.talent_bank.R;
import com.example.talent_bank.home_page.NewsFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import cn.refactor.lib.colordialog.ColorDialog;

import static android.content.Context.MODE_PRIVATE;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.LinearViewHolder> {
    private Context mContext;
    private NewsFragment mFragment;
    //以下用于手机存用户信息
    private SharedPreferences AllNewsData;
    private SharedPreferences.Editor AllNewsDataEditor;

    public NewsAdapter (NewsFragment mFragment) {
        this.mFragment = mFragment;
        mContext = mFragment.getActivity();
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
        String news_id = AllNewsData.getString("news_id","");
        String news_send_name = AllNewsData.getString("news_send_name","");
        String news_send_number = AllNewsData.getString("news_send_number","");
        String news_in_checked = AllNewsData.getString("news_in_checked","");
        String news_time = AllNewsData.getString("news_time","");
        String news_send_content = AllNewsData.getString("news_send_content","");
        final String[] idStrarr = news_id.split("~");
        final String[] numberStrarr = news_send_number.split("~");
        String[] nameStrarr = news_send_name.split("~");
        String[] checkedStrarr = news_in_checked.split("~");
        String[] timeStrarr = news_time.split("~");
        final String[] contentStrarr = news_send_content.split("~");

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
                    final int finalM = m;
                    holder.item.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) { //点击item的监听
                            ColorDialog dialog = new ColorDialog(mContext);
                            dialog.setTitle("提示");
                            dialog.setColor("#ffffff");//颜色
                            dialog.setContentTextColor("#656565");
                            dialog.setTitleTextColor("#656565");
                            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
                            dialog.setContentText(contentStrarr[finalM]+"联系方式："+numberStrarr[finalM]);
                            dialog.setPositiveListener("确认收到", new ColorDialog.OnPositiveListener() {
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    //确定操作
                                    mFragment.CheckedNews(idStrarr[finalM]);
                                    dialog.dismiss();
                                }
                            })
                                    .setNegativeListener("删除消息", new ColorDialog.OnNegativeListener() {
                                        @Override
                                        public void onClick(ColorDialog dialog) {
                                            mFragment.deleteNews(idStrarr[finalM]);
                                            dialog.dismiss();
                                        }
                                    }).show();
                        }
                    });
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
        LinearLayout item;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.news_item_name);
            content = itemView.findViewById(R.id.news_item_content);
            time = itemView.findViewById(R.id.news_item_time);
            checked = itemView.findViewById(R.id.news_item_tag);
            item = itemView.findViewById(R.id.news_item);
        }
    }
}
