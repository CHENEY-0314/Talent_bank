package com.example.talent_bank.viewmodel;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class GuideViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> fristload;
    private MutableLiveData<Integer> editnum;
    private MutableLiveData<String> editDemand;
    private MutableLiveData<Integer> receiveApplyNum;
    private MutableLiveData<Integer> demandNum;
    private MutableLiveData<Integer> myApplyNum; //我发出的申请的num
    private MutableLiveData<Boolean> ifOpenTable; //判断我是否打开了TalentBank中的Table；
    private String key = "fristload_key";
    private String key2 = "editNum_key";
    private String key3 = "editDemand_key";
    private String key4 = "receiveApplyNum_key";
    private String key5 = "demandNum_key";
    private String key6 = "myApplyNum_key";
    private String key7 = "ifOpenTable_key";
    private String shpName = "SHP_NAME";

    public GuideViewModel(@NonNull Application application) {
        super(application);
        if(fristload == null) {
            fristload = new MutableLiveData<>();
            SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
            fristload.setValue(shp.getBoolean(key,true));
        }
        if(editnum == null) {
            editnum = new MutableLiveData<>();
            SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
            editnum.setValue(shp.getInt(key2,0));
        }
        if(editDemand == null) {
            editDemand = new MutableLiveData<>();
            SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
            editDemand.setValue(shp.getString(key3,""));
        }
        if(receiveApplyNum == null) {
            receiveApplyNum = new MutableLiveData<>();
            SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
            receiveApplyNum.setValue(shp.getInt(key4,3));
        }
        if(demandNum == null) {
            demandNum = new MutableLiveData<>();
            SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
            demandNum.setValue(shp.getInt(key5,0));
        }
        if(myApplyNum == null) {
            myApplyNum = new MutableLiveData<>();
            SharedPreferences shp = getApplication().getSharedPreferences(shpName,Context.MODE_PRIVATE);
            myApplyNum.setValue(shp.getInt(key6,0));
        }
        if(ifOpenTable == null) {
            ifOpenTable = new MutableLiveData<>();
            SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
            ifOpenTable.setValue(shp.getBoolean(key7,false));
        }
    }
}
