package com.application.a4_school.ui.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel{

        private MutableLiveData<String> mText;

        public ProfileViewModel() {
            mText = new MutableLiveData<>();
            mText.setValue("");
        }

        public LiveData<String> getText() {
            return mText;
        }
}
