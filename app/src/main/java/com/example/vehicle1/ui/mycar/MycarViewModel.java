package com.example.vehicle1.ui.mycar;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MycarViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MycarViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is Mycar fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}