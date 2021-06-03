package com.milo.libbase.demo.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.Manifest;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.milo.libbase.demo.data.bean.MainItemData;
import com.milo.libbase.demo.data.factory.CommonFactory;
import com.milo.libbase.demo.databinding.ActivityMainBinding;
import com.milo.libbase.demo.ui.videopaly.BaseVideoViewDemoActivity;
import com.milo.libbase.demo.vh.MainItemVH;
import com.tbruyelle.rxpermissions3.RxPermissions;
import com.zhl.commonadapter.BaseViewHolder;
import com.zhl.commonadapter.CommonRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding mBinding;

    private List<MainItemData>                  mDataList = new ArrayList<>();
    private CommonRecyclerAdapter<MainItemData> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(mBinding.getRoot());

        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(granted -> {
                    if (granted) {

                    } else {
                        finish();
                    }
                });
        initView();
    }

    private void initView() {
        mDataList.add(CommonFactory.createMainItemData(MainItemData.TYPE_DOWNLOAD, "下载"));
        mDataList.add(CommonFactory.createMainItemData(MainItemData.TYPE_VIDEO_PLAY, "视频播放"));
        mDataList.add(CommonFactory.createMainItemData(MainItemData.TYPE_SCREEN_RECORD, "屏幕录制"));

        mAdapter = new CommonRecyclerAdapter<MainItemData>(mDataList) {
            @Override
            public BaseViewHolder createViewHolder(int type) {
                return new MainItemVH();
            }
        };
        mAdapter.setOnItemClickListener(new CommonRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                int type = mDataList.get(position).getType();
                switch (type) {
                    case MainItemData.TYPE_DOWNLOAD:
                        DownloadTestActivity.createJump(MainActivity.this)
                                .navigation();
                        break;
                    case MainItemData.TYPE_VIDEO_PLAY:
                        BaseVideoViewDemoActivity.createJump(MainActivity.this)
                                .navigation();
                        break;
                    case MainItemData.TYPE_SCREEN_RECORD:
                        break;
                }
            }
        });
        mBinding.recyclerview.setAdapter(mAdapter);
        mBinding.recyclerview.setLayoutManager(new LinearLayoutManager(this));
        mBinding.recyclerview.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

}