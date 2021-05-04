package com.milo.libbase.demo.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.milo.libbase.demo.R;
import com.milo.libbase.demo.databinding.ActivityMainBinding;
import com.tbruyelle.rxpermissions3.RxPermissions;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(mainBinding.getRoot());
        mainBinding.btnDownload.setOnClickListener(this);
        mainBinding.btnVideoPlay.setOnClickListener(this);

        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {

                    } else {

                    }
                });
    }

    @Override
    public void onClick(View v) {
        if (v == mainBinding.btnDownload) {

        } else if (v == mainBinding.btnVideoPlay) {

        }
    }

}