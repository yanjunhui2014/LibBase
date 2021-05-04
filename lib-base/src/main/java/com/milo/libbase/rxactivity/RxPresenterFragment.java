package com.milo.libbase.rxactivity;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.HashMap;
import java.util.Map;

/**
 * 标题：
 * 功能：
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/9/14 09:31
 */
public class RxPresenterFragment extends Fragment {

    private Map<Integer, ActivityOnResult.Callback> mCallbckMap = new HashMap<>();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ActivityOnResult.Callback callbck = mCallbckMap.remove(requestCode);
        if (callbck != null) {
            callbck.onActivityResult(requestCode, resultCode, data);
        }
    }

    public void startForResult(Intent intent, int requestCode, ActivityOnResult.Callback callbck) {
        mCallbckMap.put(requestCode, callbck);
        startActivityForResult(intent, requestCode);
    }

}
