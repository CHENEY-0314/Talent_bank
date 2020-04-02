package com.example.talent_bank;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

public class GuideViewModel extends AndroidViewModel {
    private MutableLiveData<Boolean> fristload;
    private String key = "fristload_key";
    private String shpName = "SHP_NAME";

    public GuideViewModel(@NonNull Application application) {
        super(application);
        if(fristload == null) {
            fristload = new MutableLiveData<>();
            SharedPreferences shp = getApplication().getSharedPreferences(shpName, Context.MODE_PRIVATE);
            fristload.setValue(shp.getBoolean(key,true));
        }
    }
}
