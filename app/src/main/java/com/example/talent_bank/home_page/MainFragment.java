package com.example.talent_bank.home_page;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.EnterTalentBank.EnterTalentBank;
import com.example.talent_bank.EnterTalentBank.EnterTalentBankLast;
import com.example.talent_bank.HandlerActivity;
import com.example.talent_bank.LoginActivity;
import com.example.talent_bank.MainActivity;
import com.example.talent_bank.ProjectReleased;
import com.example.talent_bank.SignUPActivity;
import com.example.talent_bank.viewmodel.MainViewModel;
import com.example.talent_bank.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import cn.refactor.lib.colordialog.ColorDialog;
import pl.droidsonroids.gif.GifImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.talent_bank.user_fragment.ChangeImageActivity.convertStringToIcon;

public class MainFragment extends Fragment {

    private ImageView mimg_back;

    private View mView;
    private CardView enterTable;
    private CardView publishProject;
    private MainActivity mContext;
    private GifImageView runWebView;
    private ImageView EnterTBIMG;

    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor mEditor;

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.main_fragment, container, false);
        mContext = (MainActivity)getActivity();

        initView();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        // TODO: Use the ViewModel
    }

    private void initView() {
        EnterTBIMG=mView.findViewById(R.id.main_EnterTB);
        runWebView =mView.findViewById(R.id.main_loading);
        enterTable = mView.findViewById(R.id.main_btn_entertable);
        enterTable.setOnClickListener(new ButtonListener());
        publishProject = mView.findViewById(R.id.main_btn_publish);
        publishProject.setOnClickListener(new ButtonListener());
        mimg_back = mView.findViewById(R.id.main_user_img);
        mimg_back.setOnClickListener(new ButtonListener());

        mSharedPreferences = requireActivity().getSharedPreferences("userdata", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();

        String Suserimage=mSharedPreferences.getString("userimage","");  //获取现在的头像
        if(!Suserimage.equals("")){
            mimg_back.setImageBitmap(convertStringToIcon(Suserimage));
        }

    }

    public class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_btn_entertable:  //进入加入人才库页面
                    enterTable.setEnabled(false);
                    showProgress(true);
                    String isintalentbank =mSharedPreferences.getString("intalent_bank","");
                    if(isintalentbank.equals("")){
                        ifhaveEnter();
                    }else if(isintalentbank.equals("true")){
                        ColorDialog dialog = new ColorDialog(mContext);
                        dialog.setTitle("提示");
                        dialog.setColor("#ffffff");//颜色
                        dialog.setContentTextColor("#656565");
                        dialog.setTitleTextColor("#656565");
                        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
                        dialog.setContentText("您已加入人才库，是否退出");
                        dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                            @Override
                            public void onClick(ColorDialog dialog) {
                                ExitTalentBank();
                                dialog.dismiss();
                            }
                        })
                                .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                                    @Override
                                    public void onClick(ColorDialog dialog) {
                                        enterTable.setEnabled(true);
                                        dialog.dismiss();
                                        showProgress(false);
                                    }
                                }).show();
                    }else {
                        Intent intent3 = new Intent(getActivity(), EnterTalentBank.class);
                        startActivity(intent3);
                        enterTable.setEnabled(true);
                        showProgress(false);
                    }
                    Handler mHandler = new Handler();
                    mHandler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enterTable.setEnabled(true);
                            showProgress(false);
                        }
                    },1800);
                    break;
                case R.id.main_btn_publish:  //发布项目
                    publishProject.setEnabled(false);
                    Intent intent2 = new Intent(getActivity(), ProjectReleased.class);
                    startActivity(intent2);
                    break;
                case R.id.main_user_img:  //点击用户头像
                    ColorDialog dialog = new ColorDialog(mContext);
                    dialog.setTitle("提示");
                    dialog.setColor("#ffffff");//颜色
                    dialog.setContentTextColor("#656565");
                    dialog.setTitleTextColor("#656565");
                    dialog.setContentText("是否注销账号？");
                    Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
                    dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            mEditor.putString("auto", "false");
                            mEditor.putString("intalent_bank", "");
                            mEditor.putString("userimage", "");
                            mEditor.apply();
                            Intent intent3 = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent3.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
                            dialog.dismiss();
                        }
                    })
                            .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                                @Override
                                public void onClick(ColorDialog dialog) {
                                    dialog.dismiss();
                                }
                            }).show();
                    break;
            }
        }
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
        int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

        EnterTBIMG.setVisibility(show ? View.GONE : View.VISIBLE);
        EnterTBIMG.animate().setDuration(shortAnimTime).alpha(
                show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                EnterTBIMG.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        });

        runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
        runWebView.animate().setDuration(shortAnimTime).alpha(
                show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                runWebView.setVisibility(show ? View.VISIBLE : View.GONE);
            }
        });
    }

    //判断是否已经加入了人才库
    public void ifhaveEnter(){

        final String number=mSharedPreferences.getString("number","");

        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/IfInTalentBank?number="+number;
        String tag = "IFENTER";
        //取得请求队列
        RequestQueue IFENTER = Volley.newRequestQueue(getActivity());
        //防止重复请求，所以先取消tag标识的请求队列
        IFENTER.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest IFENTERrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = (JSONObject) new JSONObject(response).get("1");
                            if(jsonObject.getString("intalent_bank").equals("")){
                                Intent intent1 = new Intent(getActivity(), EnterTalentBank.class);
                                startActivity(intent1);
                                enterTable.setEnabled(true);
                                showProgress(false);

                            }else {
                                mEditor.putString("intalent_bank","true");
                                mEditor.apply();
                                ColorDialog dialog = new ColorDialog(mContext);
                                dialog.setTitle("提示");
                                dialog.setColor("#ffffff");//颜色
                                dialog.setContentTextColor("#656565");
                                dialog.setTitleTextColor("#656565");
                                Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawableResource(R.drawable.dialog_style);
                                dialog.setContentText("您已加入人才库，是否退出");
                                dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                                    @Override
                                    public void onClick(ColorDialog dialog) {
                                        ExitTalentBank();
                                        dialog.dismiss();
                                    }
                                })
                                        .setNegativeListener("取消", new ColorDialog.OnNegativeListener() {
                                            @Override
                                            public void onClick(ColorDialog dialog) {
                                                enterTable.setEnabled(true);
                                                dialog.dismiss();
                                                showProgress(false);
                                            }
                                        }).show();
                            }
                        } catch (JSONException e) {
                            //做自己的请求异常操作，如Toast提示（“无网络连接”等）
                            Toast.makeText(getActivity(),"无网络连接！",Toast.LENGTH_SHORT).show();
                            enterTable.setEnabled(true);
                            showProgress(false);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getActivity(),"无网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
                enterTable.setEnabled(true);
                showProgress(false);
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
        IFENTERrequest.setTag(tag);
        //将请求添加到队列中
        IFENTER.add(IFENTERrequest);

    }

    public void ExitTalentBank(){

        final String number=mSharedPreferences.getString("number","");
        final String password=mSharedPreferences.getString("password","");

        //请求地址
        String url = "http://47.107.125.44:8080/Talent_bank/servlet/ExitTalentBankServlet?number="+number+"&password="+password;
        String tag = "ExitTalent";
        //取得请求队列
        RequestQueue ExitTalent = Volley.newRequestQueue(getActivity());
        //防止重复请求，所以先取消tag标识的请求队列
        ExitTalent.cancelAll(tag);
        //创建StringRequest，定义字符串请求的请求方式为POST(省略第一个参数会默认为GET方式)
        final StringRequest ExitTalentrequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast toast=Toast.makeText(getActivity(),null,Toast.LENGTH_SHORT);
                        toast.setText("成功退出");
                        toast.show();
                        mEditor.putString("intalent_bank","false");
                        mEditor.apply();
                        enterTable.setEnabled(true);
                        showProgress(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //做自己的响应错误操作，如Toast提示（“请稍后重试”等）
                Toast.makeText(getActivity(),"无网络连接，请稍后重试！",Toast.LENGTH_SHORT).show();
                enterTable.setEnabled(true);
                showProgress(false);

            }
        }) {
            @Override
            protected Map<String, String> getParams()  {
                Map<String, String> params = new HashMap<>();
                params.put("number", number);
                params.put("password", password);
                return params;
            }
        };
        //设置Tag标签
        ExitTalentrequest.setTag(tag);
        //将请求添加到队列中
        ExitTalent.add(ExitTalentrequest);
    }

    @Override
    public void onStart() {
        super.onStart();
        publishProject.setEnabled(true);
    }
}


