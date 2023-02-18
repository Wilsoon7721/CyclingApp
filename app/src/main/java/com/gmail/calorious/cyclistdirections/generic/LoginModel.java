package com.gmail.calorious.cyclistdirections.generic;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class LoginModel extends ViewModel {
    private MutableLiveData<LoginUIState> uiState;

    public LiveData<LoginUIState> getUIState() {
        return uiState;
    }

    public void setUIState(LoginUIState uiState) {
        this.uiState = new MutableLiveData<>(uiState);
    }
}
