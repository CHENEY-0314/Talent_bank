package com.example.talent_bank.home_page;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.talent_bank.LoginActivity;
import com.example.talent_bank.MainActivity;
import com.example.talent_bank.ProjectReleased;
import com.example.talent_bank.viewmodel.MainViewModel;
import com.example.talent_bank.R;

import java.util.Objects;

import cn.refactor.lib.colordialog.ColorDialog;

import static android.content.Context.MODE_PRIVATE;

public class MainFragment extends Fragment {

    private ImageView mimg_back;

    private View mView;
    private CardView enterTable;
    private CardView publishProject;
    private MainActivity mContext;

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
        enterTable = mView.findViewById(R.id.main_btn_entertable);
        enterTable.setOnClickListener(new ButtonListener());
        publishProject = mView.findViewById(R.id.main_btn_publish);
        publishProject.setOnClickListener(new ButtonListener());
        mimg_back = mView.findViewById(R.id.main_user_img);
        mimg_back.setOnClickListener(new ButtonListener());

        mSharedPreferences = Objects.requireNonNull(getActivity()).getSharedPreferences("userdata", MODE_PRIVATE);
        mEditor = mSharedPreferences.edit();
    }

    public class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.main_btn_entertable:
                    break;
                case R.id.main_btn_publish:
                    Intent intent1 = new Intent(getActivity(), ProjectReleased.class);
                    startActivity(intent1);
                    break;
                case R.id.main_user_img:
                    ColorDialog dialog = new ColorDialog(mContext);
                    dialog.setTitle("提示");
                    dialog.setColor("#ffffff");//颜色
                    dialog.setContentTextColor("#656565");
                    dialog.setTitleTextColor("#656565");
                    dialog.setContentText("是否注销账号？");
                    dialog.setPositiveListener("确定", new ColorDialog.OnPositiveListener() {
                        @Override
                        public void onClick(ColorDialog dialog) {
                            //删除申请的操作
                            mEditor.putString("auto", "false");
                            mEditor.apply();
                            Intent intent2 = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent2.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK));
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
}


