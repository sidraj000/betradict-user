package com.vinciis.beTraDict;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private MutableLiveData<String> text=new MutableLiveData<>();
    public void setText(String s)
    {
        text.setValue(s);
    }
    public LiveData<String> getText()
    {
        return text;
    }
}
