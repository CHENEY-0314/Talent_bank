package com.example.talent_bank.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
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

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import cn.refactor.lib.colordialog.ColorDialog;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.OkHttpClient;

import static android.content.Context.MODE_PRIVATE;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.LinearViewHolder> {
    private Context mContext;
    private NewsFragment mFragment;
    //以下用于手机存用户信息
    private SharedPreferences AllNewsData;
    private SharedPreferences.Editor AllNewsDataEditor;
    private SharedPreferences SHP;
    private SharedPreferences.Editor SHPEditor;
    private Bitmap userimage;

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
    public void onBindViewHolder(@NonNull final NewsAdapter.LinearViewHolder holder, int position) {
        SHP = mContext.getSharedPreferences("SHP_NAME",MODE_PRIVATE);
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
                    String show_time="";
                    //获得当前系统的时间方法
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
                    Date date = new Date(System.currentTimeMillis());
                    String now_time = simpleDateFormat.format(date);
                    //做时间判断
                    String target_time = timeStrarr[m];
                    String target_year = target_time.substring(0,4);
                    String target_month = target_time.substring(5,7);
                    String target_day = target_time.substring(8,10);
                    String target_day_time = target_time.substring(12,17);

                    String now_year = now_time.substring(0,4);
                    String now_month = now_time.substring(5,7);
                    String now_day = now_time.substring(8,10);
                    int dif = Integer.valueOf(now_day)-Integer.valueOf(target_day);
                    if(Integer.valueOf(now_year)>Integer.valueOf(target_year)){
                        show_time = target_year+"/"+target_month+"/"+target_day;
                    } else if(Integer.valueOf(now_month)>Integer.valueOf(target_month)) {
                        show_time = target_month+"/"+target_day;
                    } else if ((Integer.valueOf(now_month)==Integer.valueOf(target_month))&&(dif>2)) {
                        show_time = target_month+"/"+target_day;
                    } else if ((Integer.valueOf(now_month)==Integer.valueOf(target_month))&&(dif==2)) {
                        show_time = "前天";
                    } else if ((Integer.valueOf(now_month)==Integer.valueOf(target_month))&&(dif==1)){
                        show_time = "昨天";
                    } else if ((Integer.valueOf(now_month)==Integer.valueOf(target_month))&&(dif==0)) {
                        show_time = target_day_time;
                    }
                    holder.time.setText(show_time);
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

                    downloadPic(numberStrarr[m]);
                    while (true) {
                        String done = SHP.getString("Done","");
                        if(done.equals("true")) {
                            if(userimage!=null) {
                                holder.mImage.setImageBitmap(userimage);
                            }
                            break;
                        }
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
        CircleImageView mImage;

        LinearLayout item;
        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mImage=itemView.findViewById(R.id.news_item_userimage);
            name = itemView.findViewById(R.id.news_item_name);
            content = itemView.findViewById(R.id.news_item_content);
            time = itemView.findViewById(R.id.news_item_time);
            checked = itemView.findViewById(R.id.news_item_tag);
            item = itemView.findViewById(R.id.news_item);
        }
    }

    //获取用户头像
    private Bitmap downloadPic(String number) {
        SHP = mContext.getSharedPreferences("SHP_NAME",MODE_PRIVATE);
        SHPEditor = SHP.edit();
        SHPEditor.putString("Done","false");
        SHPEditor.apply();

        String url="http://47.107.125.44:8080/Talent_bank/userimagefiles/"+number+".png";

        OkHttpClient okHttpClient = new OkHttpClient();
        final okhttp3.Request request = new okhttp3.Request.Builder()
                .url(url)
                .build();
        okHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                userimage=null;
            }
            @Override
            public void onResponse(okhttp3.Call call, okhttp3.Response response) throws IOException {
                InputStream inputStream = response.body().byteStream();//得到图片的流
                userimage = BitmapFactory.decodeStream(inputStream);
            }
        });

        SHPEditor.putString("Done","true");
        SHPEditor.apply();
        return userimage;
    }


}
