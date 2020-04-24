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
import com.example.talent_bank.Adapter.FindingAdapter;
import com.example.talent_bank.MainActivity;
import com.example.talent_bank.R;
import com.example.talent_bank.viewmodel.TalkingViewModel;

public class FindFragment extends Fragment {

    private TalkingViewModel mViewModel;
    private RecyclerView mRvMain;
    private View mView;
    private MainActivity mContext;

    public static FindFragment newInstance() {
        return new FindFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.talking_fragment, container, false);
        mContext = (MainActivity)getActivity();
        mRvMain = mView.findViewById(R.id.rv_finding);
        mRvMain.setLayoutManager(new LinearLayoutManager(mContext));
        mRvMain.setAdapter(new FindingAdapter(FindFragment.this));
        return mView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(TalkingViewModel.class);
        // TODO: Use the ViewModel
    }
}
