package com.example.talent_bank.home_page;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import com.example.talent_bank.MyApplyNull;
import com.example.talent_bank.user_fragment.ChangeImageActivity;
import com.example.talent_bank.user_fragment.MyPublishActivity;
import com.example.talent_bank.user_fragment.AdviceActivity;
import com.example.talent_bank.user_fragment.MyApplyActivity;
import com.example.talent_bank.user_fragment.MyBiographicalActivity;
import com.example.talent_bank.user_fragment.MyCollectionActivity;
import com.example.talent_bank.user_fragment.SetUpActivity;
import com.example.talent_bank.viewmodel.HomeViewModel;
import com.example.talent_bank.R;
import java.util.Objects;


import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static com.example.talent_bank.user_fragment.ChangeImageActivity.convertStringToIcon;

public class UserFragment extends Fragment {

    private HomeViewModel mViewModel;
    private View mView;
    private LinearLayout mSetUp;
    private LinearLayout mMyPublish;
    private LinearLayout mMyApply;
    private LinearLayout mMyCollection;
    private LinearLayout mMyBiographical;
    private LinearLayout mAdvice;
    private CircleImageView circleImageView;

    private TextView username;
    private TextView grade;


    private String shpName = "SHP_NAME";

    private SharedPreferences mSharedPreferences;     //用于读取手机暂存用户信息

    public static UserFragment newInstance() {
        return new UserFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView=inflater.inflate(R.layout.home_fragment, container, false);
        initView();
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(HomeViewModel.class);
        // TODO: Use the ViewModel
    }

    private void initView() {
        circleImageView= mView.findViewById(R.id.home_img_user);
        circleImageView.setOnClickListener(new ButtonListener());
        mMyPublish=mView.findViewById(R.id.home_btn_MyPublish);
        mMyPublish.setOnClickListener(new ButtonListener());
        mMyApply=mView.findViewById(R.id.home_btn_MyApply);
        mMyApply.setOnClickListener(new ButtonListener());
        mMyCollection=mView.findViewById(R.id.home_btn_MyCollection);
        mMyCollection.setOnClickListener(new ButtonListener());
        mMyBiographical=mView.findViewById(R.id.home_btn_MyBiographical);
        mMyBiographical.setOnClickListener(new ButtonListener());
        mSetUp=mView.findViewById(R.id.home_btn_setup);
        mSetUp.setOnClickListener(new ButtonListener());
        mAdvice=mView.findViewById(R.id.home_btn_advice);
        mAdvice.setOnClickListener(new ButtonListener());

        mSharedPreferences= Objects.requireNonNull(getActivity()).getSharedPreferences("userdata",MODE_PRIVATE);

        username=mView.findViewById(R.id.home_txt_username);
        grade=mView.findViewById(R.id.home_txt_grade);

        //初始化用户名称和年级信息
        username.setText(mSharedPreferences.getString("name",""));
        grade.setText(mSharedPreferences.getString("grade",""));

        String Suserimage=mSharedPreferences.getString("userimage","");  //获取现在的头像
        if(!Suserimage.equals("")){
            circleImageView.setImageBitmap(convertStringToIcon(Suserimage));
        }
    }

    private class ButtonListener implements View.OnClickListener {
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.home_btn_MyPublish:
                    Intent intent= new Intent(getActivity(), MyPublishActivity.class);
                    startActivity(intent);
                    break;
                case R.id.home_btn_MyApply:
                    //如果我的申请的Num为0则跳转到另一页面
                    SharedPreferences shp = Objects.requireNonNull(getActivity()).getSharedPreferences(shpName, Context.MODE_PRIVATE);
                    int x = shp.getInt("myApplyNum_key",0);
                    if (x ==0) {
                        Intent intent1 = new Intent(getActivity(), MyApplyNull.class);
                        startActivity(intent1);
                    } else {
                        Intent intent2= new Intent(getActivity(), MyApplyActivity.class);
                        startActivity(intent2);
                    }
                    break;
                case R.id.home_btn_MyCollection:
                    Intent intent2= new Intent(getActivity(), MyCollectionActivity.class);
                    startActivity(intent2);
                    break;
                case R.id.home_btn_MyBiographical:
                    Intent intent3= new Intent(getActivity(), MyBiographicalActivity.class);
                    startActivity(intent3);
                    break;
                case R.id.home_btn_setup:
                    Intent intent4= new Intent(getActivity(), SetUpActivity.class);
                    startActivity(intent4);
                    break;
                case R.id.home_btn_advice:
                    Intent intent5= new Intent(getActivity(), AdviceActivity.class);
                    startActivity(intent5);
                    break;
                case R.id.home_img_user:  //跳转到换头像的页面
                    Intent intent6= new Intent(getActivity(), ChangeImageActivity.class);
                    startActivity(intent6);
                    break;
            }
        }
    }

}
