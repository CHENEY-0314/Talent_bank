package com.example.talent_bank.home_page;

import androidx.lifecycle.ViewModelProviders;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.talent_bank.Adapter.FindingAdapter;
import com.example.talent_bank.Adapter.NewsAdapter;
import com.example.talent_bank.MainActivity;
import com.example.talent_bank.R;
import com.example.talent_bank.viewmodel.NewsViewModel;

public class NewsFragment extends Fragment {

    private RecyclerView mRvMain;
    private View mView;
    private MainActivity mContext;
    private LinearLayout runWebView;

    private NewsViewModel mViewModel;

    public static NewsFragment newInstance() {
        return new NewsFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.news_fragment, container, false);
        mContext = (MainActivity)getActivity();
        mRvMain = mView.findViewById(R.id.rv_news);
        runWebView = mView.findViewById(R.id.news_loading);
        mRvMain.setLayoutManager(new LinearLayoutManager(mContext));
        mRvMain.setAdapter(new NewsAdapter(NewsFragment.this));

       return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(NewsViewModel.class);
        // TODO: Use the ViewModel
    }

}
