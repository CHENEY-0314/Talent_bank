package com.example.talent_bank.home_page;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProviders;

import android.Manifest;
import android.app.Activity;
import android.content.ClipData;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;

import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.talent_bank.ImageUtils;
import com.example.talent_bank.MyApplyNull;
import com.example.talent_bank.user_fragment.MyPublishActivity;
import com.example.talent_bank.user_fragment.AdviceActivity;
import com.example.talent_bank.user_fragment.MyApplyActivity;
import com.example.talent_bank.user_fragment.MyBiographicalActivity;
import com.example.talent_bank.user_fragment.MyCollectionActivity;
import com.example.talent_bank.user_fragment.SetUpActivity;
import com.example.talent_bank.viewmodel.HomeViewModel;
import com.example.talent_bank.R;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.content.Context.MODE_PRIVATE;
import static androidx.core.provider.FontsContractCompat.FontRequestCallback.RESULT_OK;

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

    protected static final int CHOOSE_PICTURE = 2;
    protected static final int TAKE_PICTURE = 1;
    private static final int CROP_SMALL_PICTURE = 0;
    protected static Uri tempUri;
    protected static Uri cutoutUri;


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
                case R.id.home_img_user:
                    //更换用户头像
                    if (ContextCompat.checkSelfPermission(Objects.requireNonNull(getActivity()), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                    } else {  //已经获取了权限
                        Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                        openAlbumIntent.setType("image/*");
                        startActivityForResult(openAlbumIntent, CROP_SMALL_PICTURE);
                    }
                    break;
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(), ""+resultCode, Toast.LENGTH_SHORT).show();
        if (resultCode == getActivity().RESULT_OK) { // 如果返回码是可以用的
            switch (requestCode) {
                case CROP_SMALL_PICTURE:
                    Toast.makeText(getActivity(), "进来了3", Toast.LENGTH_SHORT).show();
                    if (data != null) {

                        setImageToView(data); // 让刚才选择裁剪得到的图片显示在界面上
                    }
                    break;
            }
        }
    }

    /**
     * 保存裁剪之后的图片数据
     *
     */
    private void setImageToView(Intent data) {
        Bundle extras = data.getExtras();
        if (extras != null) {
            Toast.makeText(getActivity(), "进来了n", Toast.LENGTH_SHORT).show();
            Bitmap photo = extras.getParcelable("data");
            photo = ImageUtils.toRoundBitmap(photo, tempUri); // 这个时候的图片已经被处理成圆形的了
            circleImageView.setImageBitmap(photo);
            uploadPic(photo);
        }
    }

        @Override
    // 申请用户调用相册权限
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 用户允许授权
                    Intent openAlbumIntent = new Intent(Intent.ACTION_GET_CONTENT);
                    openAlbumIntent.setType("image/*");
                    startActivityForResult(openAlbumIntent, CROP_SMALL_PICTURE);
                } else {
                    // 用户拒绝授权
                    Toast.makeText(getActivity(), "请授权以继续", Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }

    private void uploadPic(Bitmap bitmap) {
        // 上传至服务器
        // ... 可以在这里把Bitmap转换成file，然后得到file的url，做文件上传操作
        // 注意这里得到的图片已经是圆形图片了
        // bitmap是没有做个圆形处理的，但已经被裁剪了

        String imagePath = ImageUtils.savePhoto(bitmap, Environment
                .getExternalStorageDirectory().getAbsolutePath(), String
                .valueOf(System.currentTimeMillis()));
        Log.e("imagePath", imagePath+"");
        if(imagePath != null){
            // 拿着imagePath上传了
            // ...
        }
    }

}
