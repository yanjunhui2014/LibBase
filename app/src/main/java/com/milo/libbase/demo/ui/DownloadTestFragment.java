package com.milo.libbase.demo.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.milo.libbase.demo.R;
import com.milo.libbase.demo.ui.contract.DownloadTestContract;
import com.milo.libbase.framework.mvp.FZBaseFragment;
import com.milo.libbase.utils.ToastUtils;
import com.milo.libbase.utils.Utils;
import com.milo.libbase.utils.download.FileDownloader;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/12/1
 */
public class DownloadTestFragment extends FZBaseFragment<DownloadTestContract.Presenter> implements View.OnClickListener, DownloadTestContract.View {

    private TextView mTvCount;
    private TextView mTvStatus;
    private Button   mBtnDelete;
    private Button   mBtnStart;
    private Button   mBtnFileDownload;

    public static DownloadTestFragment newInstance() {
        return new DownloadTestFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download_test, container, false);
        initView(view);
        return view;
    }

    @Override
    public void showDownloadOk(long timeMillis) {
        mTvStatus.setText("下载完成，耗时:" + timeMillis);
    }

    @Override
    public void showCount(int cur, int total) {
        mActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTvCount.setText("已下载 " + cur + "/"+total);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if (v == mBtnStart) {
            mPresenter.startDownload();
//            final String url = "https://peiyinimg.qupeiyin.cn/2020-10-12/2cd90d68554a5dbd08f9312c130efb93.jpg";
//            Utils.delete(new File(FileFactory.COMMON_STORAGE.getFilePath(url)));
//            FZFileDownloader.getInstance().getFileDownloadTaskBuilder()
//                    .withDownloadUrl(url)
//                    .withSavePath(FileFactory.COMMON_STORAGE.getFilePath(url))
//                    .withListener(new DownloadListener() {
//                        @Override
//                        public void onConnectIng() {
//
//                        }
//
//                        @Override
//                        public void onGetSize(long totalSize) {
//
//                        }
//
//                        @Override
//                        public void onDownloadIng(float progressF, int progress) {
//
//                        }
//
//                        @Override
//                        public void onCancel() {
//
//                        }
//
//                        @Override
//                        public void onError(String error) {
//                            mTvStatus.setText("download error");
//                        }
//
//                        @Override
//                        public void onRepeatTask() {
//
//                        }
//
//                        @Override
//                        public void onDone(String localPath) {
//                            mTvStatus.setText("download ok, path == " + localPath);
//                        }
//                    })
//                    .build()
//                    .start();
        } else if (v == mBtnDelete) {
            Utils.delFileOrDir(mPresenter.getRootPath());
            ToastUtils.show(mActivity, "文件已删除");
        } else if(v == mBtnFileDownload){
//            FileDownloader.getImpl().create("abc")
//                    .setPath("")
//                    .setListener(new FileDownloadListener() {
//                        @Override
//                        protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//
//                        }
//
//                        @Override
//                        protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//
//                        }
//
//                        @Override
//                        protected void completed(BaseDownloadTask task) {
//
//                        }
//
//                        @Override
//                        protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
//
//                        }
//
//                        @Override
//                        protected void error(BaseDownloadTask task, Throwable e) {
//                            mBtnFileDownload.setText("Error");
//                        }
//
//                        @Override
//                        protected void warn(BaseDownloadTask task) {
//
//                        }
//                    }).start();
        }
    }

    private void initView(View v) {
        mTvCount = v.findViewById(R.id.mTvCount);
        mTvStatus = v.findViewById(R.id.mTvStatus);
        mBtnDelete = v.findViewById(R.id.mBtnDelete);
        mBtnStart = v.findViewById(R.id.mBtnStart);
        mBtnFileDownload = v.findViewById(R.id.mBtnFileDownload);

        mBtnDelete.setOnClickListener(this);
        mBtnStart.setOnClickListener(this);
        mBtnFileDownload.setOnClickListener(this);
    }

}
