package com.example.talent_bank.Adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.talent_bank.ProjectContentsApply;
import com.example.talent_bank.R;
import com.example.talent_bank.home_page.FindFragment;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static android.content.Context.MODE_PRIVATE;

public class FindingAdapter extends RecyclerView.Adapter<FindingAdapter.LinearViewHolder> {
    private FindFragment mFragment;

    //以下用于手机存用户信息
    private SharedPreferences AllProjectData;
    private SharedPreferences.Editor AllProjectDataEditor;

    private SharedPreferences ShareUserData;


    public FindingAdapter (FindFragment mFragment) {
        this.mFragment = mFragment;
    }
    @NonNull
    @Override
    public FindingAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AllProjectData = mFragment.getActivity().getSharedPreferences("all_project_data",MODE_PRIVATE);
        String pj_id = AllProjectData.getString("pj_id","");

        ShareUserData=mFragment.getActivity().getSharedPreferences("userdata",MODE_PRIVATE);

        if (pj_id.equals("")) {
            return new FindingAdapter.LinearViewHolder(LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.rv_null_finding,parent,false));
        } else {
            return new FindingAdapter.LinearViewHolder(LayoutInflater.from(mFragment.getActivity()).inflate(R.layout.rv_finding_item,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull final FindingAdapter.LinearViewHolder holder, final int position) {
        AllProjectData = mFragment.getActivity().getSharedPreferences("all_project_data",MODE_PRIVATE);
        AllProjectDataEditor = AllProjectData.edit();
        String pj_id = AllProjectData.getString("pj_id","");
        String pj_name = AllProjectData.getString("pj_name","");
        String pj_introduce = AllProjectData.getString("pj_introduce","");

        String[] IDstrarr = pj_id.split("~");
        String[] Namestrarr = pj_name.split("~");
        String[] Introducestrarr = pj_introduce.split("~");

        //为每个item设置项目Text
        if (!pj_id.equals("")) {
            for(int m=0;m<IDstrarr.length;m++){
                if(position==m) {
                    holder.name.setText(Namestrarr[m]);
                    holder.content.setText(Introducestrarr[m]);
                }
            }

            //点击详情的点击事件
            holder.detail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AllProjectDataEditor.putInt("curr_pj",position);
                    AllProjectDataEditor.apply();
                    Intent intent = new Intent(mFragment.getActivity(), ProjectContentsApply.class);
                    mFragment.getActivity().startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        AllProjectData = mFragment.getActivity().getSharedPreferences("all_project_data",MODE_PRIVATE);
        String pj_id = AllProjectData.getString("pj_id","");
        if (pj_id.equals("")) {
            return 1;
        } else {
            String[] strarr = pj_id.split("~");
            return strarr.length;
        }
    }

    class LinearViewHolder extends RecyclerView.ViewHolder {
        private TextView name,content;
        private LinearLayout detail;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            detail = itemView.findViewById(R.id.finding_detail);
            name = itemView.findViewById(R.id.finding_item_name);
            content = itemView.findViewById(R.id.finding_item_content);

        }
    }
}
