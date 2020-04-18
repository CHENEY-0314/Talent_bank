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
    private String key = "fristload_key";
    private String key2 = "editNum_key";
    private String key3 = "editDemand_key";
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
    }
}
