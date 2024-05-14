package com.example.intento.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private final MutableLiveData<String> mText;
    private final MutableLiveData<Boolean> shouldRedirectToShowProduct;

    public HomeViewModel() {
        mText = new MutableLiveData<>();


        shouldRedirectToShowProduct = new MutableLiveData<>(false);

    }

    public LiveData<String> getText() {
        return mText;
    }

    public LiveData<Boolean> shouldRedirect() {
        return shouldRedirectToShowProduct;
    }

    public void triggerRedirect() {
        shouldRedirectToShowProduct.setValue(true);
    }
}
