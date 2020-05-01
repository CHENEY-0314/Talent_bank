package com.example.talent_bank.home_page;

import androidx.lifecycle.ViewModelProviders;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.IBinder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import android.view.Window;

import com.example.talent_bank.Adapter.FindingAdapter;
import com.example.talent_bank.Adapter.MyPublishAdapter;
import com.example.talent_bank.MainActivity;
import com.example.talent_bank.R;
import com.example.talent_bank.user_fragment.MyPublishActivity;
import com.example.talent_bank.viewmodel.TalkingViewModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import q.rorbin.verticaltablayout.util.TabFragmentManager;

public class FindFragment extends Fragment {

    private TalkingViewModel mViewModel;
    private RecyclerView mRvMain;
    private View mView;
    private MainActivity mContext;
    private ImageView imgSearch;
    private LinearLayout runWebView;
    private EditText editText;

    //以下用于手机存用户信息
    private SharedPreferences AllProjectData;
    private SharedPreferences.Editor AllProjectDataEditor;

    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {


        mView = inflater.inflate(R.layout.talking_fragment, container, false);
        mContext = (MainActivity)getActivity();
        mRvMain = mView.findViewById(R.id.rv_finding);
        imgSearch = mView.findViewById(R.id.finding_search);
        runWebView = mView.findViewById(R.id.finding_loading);
        editText = mView.findViewById(R.id.finding_edit);

        AllProjectData = mContext.getSharedPreferences("all_project_data",mContext.MODE_PRIVATE);
        AllProjectDataEditor = AllProjectData.edit();
        AllProjectDataEditor.clear();
        AllProjectDataEditor.apply();
        if(String.valueOf(editText.getText())=="") {
            showProgress(true);
            loadingProject();
        } else {
            showProgress(true);
            searchingProject(String.valueOf(editText.getText()));
        }

        imgSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { //检索操作
                AllProjectDataEditor.clear();
                AllProjectDataEditor.apply();
                String target = String.valueOf(editText.getText());
                showProgress(true);
                searchingProject(target);
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if(actionId== EditorInfo.IME_ACTION_SEARCH) {
                    InputMethodManager im = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
                    im.hideSoftInputFromWindow(mView.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    AllProjectDataEditor.clear();
                    AllProjectDataEditor.apply();
                    String target = String.valueOf(editText.getText());
                    showProgress(true);
                    searchingProject(target);
                }
                return false;
            }
        });
        return mView;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TalkingViewModel.class);
        // TODO: Use the ViewModel
    }

    /**
     * Shows the progress UI and hides the login form.
     * 显示进度UI并隐藏登录表单。
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        //在Honeycomb MR2上，我们有ViewPropertyAnimator API，可以实现非常简单的动画。如果可用，请使用这些API淡入进度微调器。
//        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);
        mRvMain.setVisibility(!show ? View.VISIBLE : View.GONE);
        runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
//        runWebView.animate().setDuration(shortAnimTime).alpha(
//                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
//            }
//        });
//        mRvMain.animate().setDuration(shortAnimTime).alpha(
//                !show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
//            @Override
//            public void onAnimationEnd(Animator animation) {
//                mRvMain.setVisibility(!show ? View.VISIBLE : View.GONE);
//            }
//        });
    }

    //加载项目基本信息到手机暂存
    public void loadingProject(){
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/GetProjectAll";
        String tag = "GetProject";
        //取得请求队列
        RequestQueue GetProject = Volley.newRequestQueue(mContext);
        //防止重复请求，所以先取消tag标识的请求队列
        GetProject.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest GetProjectrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            AllProjectDataEditor.putString("pj_id",jsonObject.getString("pj_id"));
                            AllProjectDataEditor.putString("pj_name",jsonObject.getString("pj_name"));
                            AllProjectDataEditor.putString("pj_introduce",jsonObject.getString("pj_introduce"));
                            AllProjectDataEditor.putString("count_member",jsonObject.getString("count_member"));
                            AllProjectDataEditor.apply();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showProgress(false);
                                    mRvMain.setLayoutManager(new LinearLayoutManager(mContext));
                                    mRvMain.setAdapter(new FindingAdapter(FindFragment.this));
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
                Toast.makeText(mContext,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                return params;
            }
        };
        //设置Tag标签
        GetProjectrequest.setTag(tag);
        //将请求添加到队列中
        GetProject.add(GetProjectrequest);
    }

    //加载符合搜索匹配的项目基本信息到手机暂存
    public void searchingProject(final String target){
        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/SearchProject?target="+target;
        String tag = "GetProject";
        //取得请求队列
        RequestQueue GetProject = Volley.newRequestQueue(mContext);
        //防止重复请求，所以先取消tag标识的请求队列
        GetProject.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest GetProjectrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            AllProjectDataEditor.putString("pj_id",jsonObject.getString("pj_id"));
                            AllProjectDataEditor.putString("pj_name",jsonObject.getString("pj_name"));
                            AllProjectDataEditor.putString("pj_introduce",jsonObject.getString("pj_introduce"));
                            AllProjectDataEditor.putString("count_member",jsonObject.getString("count_member"));
                            AllProjectDataEditor.apply();
                            Handler mHandler = new Handler();
                            mHandler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    showProgress(false);
                                    mRvMain.setLayoutManager(new LinearLayoutManager(mContext));
                                    mRvMain.setAdapter(new FindingAdapter(FindFragment.this));
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
                Toast.makeText(mContext,"请稍后重试！",Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("target", target);
                return params;
            }
        };
        //设置Tag标签
        GetProjectrequest.setTag(tag);
        //将请求添加到队列中
        GetProject.add(GetProjectrequest);

    }

}
