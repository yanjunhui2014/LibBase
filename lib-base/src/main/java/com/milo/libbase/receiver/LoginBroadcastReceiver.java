package com.milo.libbase.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.milo.libbase.data.AppConstants;

public class LoginBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = LoginBroadcastReceiver.class.getSimpleName();

    private LoginListener mLoginListener;

    public interface LoginListener {

        void onLoginSuccess();

        void onLogOutSuccess();
    }

    public LoginBroadcastReceiver(){}

    public LoginBroadcastReceiver(LoginListener loginListener) {
        mLoginListener = loginListener;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (AppConstants.ACTION_LOGIN_SUCCESS.equals(action)) {
            if (mLoginListener != null) {
                mLoginListener.onLoginSuccess();
            }
        } else if (AppConstants.ACTION_LOGOUT_SUCCESS.equals(action)) {
            if (mLoginListener != null) {
                mLoginListener.onLogOutSuccess();
            }
        }
    }
}
