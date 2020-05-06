package com.example.talent_bank.home_page;

import androidx.lifecycle.ViewModelProviders;

import android.annotation.TargetApi;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.Adapter.FindingAdapter;
import com.example.talent_bank.Adapter.NewsAdapter;
import com.example.talent_bank.MainActivity;
import com.example.talent_bank.R;
import com.example.talent_bank.viewmodel.NewsViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class NewsFragment extends Fragment {

    private RecyclerView mRvMain;
    private View mView;
    private MainActivity mContext;
    private LinearLayout runWebView;

    private NewsViewModel mViewModel;

    //以下用于手机存用户信息
    private SharedPreferences AllNewsData;
    private SharedPreferences.Editor AllNewsDataEditor;

    private SharedPreferences UserData;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.news_fragment, container, false);
        init();

        showProgress(true);
        loadingNews();

        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        // TODO: Use the ViewModel
    }
    public void init() {
        mContext = (MainActivity)getActivity();
        mRvMain = mView.findViewById(R.id.rv_news);
        runWebView = mView.findViewById(R.id.news_loading);
        UserData = mContext.getSharedPreferences("userdata",mContext.MODE_PRIVATE);
        AllNewsData = mContext.getSharedPreferences("all_news_data",mContext.MODE_PRIVATE);
        AllNewsDataEditor = AllNewsData.edit();
        AllNewsDataEditor.clear();
        AllNewsDataEditor.apply();
    }

    /**
     * Shows the progress UI and hides the login form.
     * 显示进度UI并隐藏登录表单。
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show) {
        mRvMain.setVisibility(!show ? View.VISIBLE : View.GONE);
        runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    //加载消息基本信息到手机暂存
    public void loadingNews(){
        final String number = UserData.getString("number","");
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/GetNewsByNumberServlet?number="+number;
        String tag = "GetNews";
        //取得请求队列
        RequestQueue GetNews = Volley.newRequestQueue(mContext);
        //防止重复请求，所以先取消tag标识的请求队列
        GetNews.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest GetNewsrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            AllNewsDataEditor.putString("news_id",jsonObject.getString("news_id"));
                            AllNewsDataEditor.putString("news_number",jsonObject.getString("news_number"));
                            AllNewsDataEditor.putString("news_send_number",jsonObject.getString("news_send_number"));
                            AllNewsDataEditor.putString("news_send_content",jsonObject.getString("news_send_content"));
                            AllNewsDataEditor.putString("news_send_name",jsonObject.getString("news_send_name"));
                            AllNewsDataEditor.putString("news_in_checked",jsonObject.getString("news_in_checked"));
                            AllNewsDataEditor.putString("news_time",jsonObject.getString("news_time"));
                            AllNewsDataEditor.apply();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showProgress(false);
                                    mRvMain.setLayoutManager(new LinearLayoutManager(mContext));
                                    mRvMain.setAdapter(new NewsAdapter(NewsFragment.this));
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(mContext,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(mContext,"无网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("number", number);
                return params;
            }
        };
        //设置Tag标签
        GetNewsrequest.setTag(tag);
        //将请求添加到队列中
        GetNews.add(GetNewsrequest);
    }

    //删除指定的消息
    public void deleteNews(final String news_id){
        AllNewsData = mContext.getSharedPreferences("all_news_data",mContext.MODE_PRIVATE);
        AllNewsDataEditor = AllNewsData.edit();
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/DeleteNewsServlet?news_id="+news_id;
        String tag = "DeleteNews";
        //取得请求队列
        RequestQueue GetNews = Volley.newRequestQueue(mContext);
        //防止重复请求，所以先取消tag标识的请求队列
        GetNews.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest DeleteNewsrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response);
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AllNewsDataEditor.clear();
                                    AllNewsDataEditor.apply();
                                    loadingNews();
                                    Toast.makeText(mContext,"删除消息成功！",Toast.LENGTH_SHORT).show();
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(mContext,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(mContext,"无网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("news_id", news_id);
                return params;
            }
        };
        //设置Tag标签
        DeleteNewsrequest.setTag(tag);
        //将请求添加到队列中
        GetNews.add(DeleteNewsrequest);
    }

    //确认收到消息
    public void CheckedNews(final String news_id){
        AllNewsData = mContext.getSharedPreferences("all_news_data",mContext.MODE_PRIVATE);
        AllNewsDataEditor = AllNewsData.edit();
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/CheckedNewsServlet?news_id="+news_id;
        String tag = "CheckedNews";
        //取得请求队列
        RequestQueue CheckedNews = Volley.newRequestQueue(mContext);
        //防止重复请求，所以先取消tag标识的请求队列
        CheckedNews.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest CheckedNewsrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response);
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    AllNewsDataEditor.clear();
                                    AllNewsDataEditor.apply();
                                    loadingNews();
                                }
                            },500);
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(mContext,"无网络连接！",Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(mContext,"无网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("news_id", news_id);
                return params;
            }
        };
        //设置Tag标签
        CheckedNewsrequest.setTag(tag);
        //将请求添加到队列中
        CheckedNews.add(CheckedNewsrequest);
    }



}
