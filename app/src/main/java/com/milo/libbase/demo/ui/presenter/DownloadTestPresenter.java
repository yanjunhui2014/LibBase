package com.milo.libbase.demo.ui.presenter;

import android.os.Environment;

import androidx.annotation.Nullable;
import com.milo.libbase.business.NormalResPrepareFactory;
import com.milo.libbase.business.ResPreparePresenter;
import com.milo.libbase.data.impl.PrepareRes;
import com.milo.libbase.demo.ui.contract.DownloadTestContract;
import com.milo.libbase.utils.LogUtils;
import com.milo.libbase.utils.Utils;

import java.util.ArrayList;
import java.util.List;

/**
 * Title：
 * Describe：
 * Remark：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2020/12/1
 */
public class DownloadTestPresenter extends ResPreparePresenter implements DownloadTestContract.Presenter {

    private final String mRootPath;

    private DownloadTestContract.View mView;

    private List<String> downloadList = new ArrayList<>();

    private NormalResPrepareFactory factory = new NormalResPrepareFactory();

    private long mStartTime;

    private int mPreparedCount;

    public DownloadTestPresenter(DownloadTestContract.View view) {
        this.mView = view;
        this.mView.setPresenter(this);
        this.mRootPath = Environment.getExternalStorageDirectory() + "/1test";
        init();
    }

    @Override
    public String getRootPath() {
        return mRootPath;
    }

    @Override
    public void startDownload() {
        mStartTime = System.currentTimeMillis();
        startResPrepare();
    }

    @Override
    protected List<PrepareRes> getPrepareResList() {
        List<PrepareRes> resList = new ArrayList<>();
        for (String item : downloadList) {
            resList.add(factory.createRes(item, getSavePath(item)));
        }
        return resList;
    }

    @Override
    protected void onItemResPrepared(PrepareRes prepareRes, String channel) {
        mPreparedCount++;
//        FZLogger.d(TAG, String.format("%s准备完成::%s", prepareRes.getDownloadUrl(), channel));
        mView.showCount(mPreparedCount, downloadList.size());
    }

    @Override
    protected void onResPrepareError(@Nullable PrepareRes prepareRes, Throwable throwable) {
        String message = String.format("%s下载时发生错误%s", prepareRes == null ? "" : prepareRes.getDownloadUrl(), throwable.getMessage());
        LogUtils.e(TAG, message);
        Utils.writeCommonLog(TAG, message);
    }

    @Override
    protected void onResPreparedCompleted() {
        LogUtils.d(TAG, "onResPreparedCompleted,所有资源下载完成");
        mView.showDownloadOk(System.currentTimeMillis() - mStartTime);
    }

    private String getSavePath(String url) {
        String savePath = mRootPath + "/" + Utils.getFileName(url);
        return savePath;
    }

    private void init() {
        //        downloadList.add("http://cdn.qupeiyin.cn/2021-01-13/lh048xbSL6iRwDfcqz78E7U6fOmv.mp4");//170MB的视频
        //        downloadList.add("http://cdn.qupeiyin.cn/2020-10-13/90f1164d59ce8596cc8dc0cd757e285f.mp4"); // 170MB的视频

        downloadList.add("https://cdn.qupeiyin.cn/2021-01-13/luC-rCl-RbIyODry_C2_HsMyzS3k.mp4");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/d48a879c15d82bfc839ca2279f84780d.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/e7891306f4380a1a4c3aab53c5878740.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/6fe35658ed34dd986941ba475217dc40.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/57274d6a1ae2c7bb9dc22d25b2158b36.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/f07d0382e6f1b4fee72ec3e023995c71.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/d48a879c15d82bfc839ca2279f84780d.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/6fe35658ed34dd986941ba475217dc40.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/e7891306f4380a1a4c3aab53c5878740.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/86305da79fe0f66a29159c51eed87433.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/dd624a61d39e53698ca41384b7cb41e4.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/4d628beb7825a10c2511f56a08104273.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/09453c226f03d0885370086ee227d9d1.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/2cd90d68554a5dbd08f9312c130efb93.jpg");
        downloadList.add("https://cdn.qupeiyin.cn/2020-09-28/711432b34fc76052fcc5e4a20dea1acf.mp3");
        downloadList.add("https://cdn.qupeiyin.cn/2020-10-13/ee65fcc3b0ab9b6a5bf80d314ec35eca.mp3");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/dd624a61d39e53698ca41384b7cb41e4.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/f07d0382e6f1b4fee72ec3e023995c71.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/6fe35658ed34dd986941ba475217dc40.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/d48a879c15d82bfc839ca2279f84780d.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/86305da79fe0f66a29159c51eed87433.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/e7891306f4380a1a4c3aab53c5878740.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/57274d6a1ae2c7bb9dc22d25b2158b36.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/6fe35658ed34dd986941ba475217dc40.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/86305da79fe0f66a29159c51eed87433.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/d48a879c15d82bfc839ca2279f84780d.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/dd624a61d39e53698ca41384b7cb41e4.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/e7891306f4380a1a4c3aab53c5878740.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/f07d0382e6f1b4fee72ec3e023995c71.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/4d628beb7825a10c2511f56a08104273.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/09453c226f03d0885370086ee227d9d1.jpg");
        downloadList.add("https://peiyinimg.qupeiyin.cn/2020-10-12/2cd90d68554a5dbd08f9312c130efb93.jpg");
        downloadList.add("https://cdn.qupeiyin.cn/2020-09-28/711432b34fc76052fcc5e4a20dea1acf.mp3");
        downloadList.add("https://cdn.qupeiyin.cn/2020-10-13/ee65fcc3b0ab9b6a5bf80d314ec35eca.mp3");
        downloadList.add("http://cdn.qupeiyin.cn/2018-11-27/1543308911214.mp4");
        downloadList.add("https://cdn.qupeiyin.cn/2018-03-26/1522038403960.mp4");
        downloadList.add("https://cdn.qupeiyin.cn/2020-09-10/8991599722397531.mp4");

        downloadList.add("http://cdn.qupeiyin.cn/2021-01-12/FjG7U5zhFtB4ETOb5RCLNNxShwtH.mp3");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-21/FlljoJblvgjFLYfaFldozXp2KRJf.jpg");
        downloadList.add("http://cdn.qupeiyin.cn/2021-01-13/6002016105057894791h720.mp4");
        downloadList.add("http://cdn.qupeiyin.cn/2021-01-05/liULPDA-xBWhRBOHZ8t2yTkxss93.mp4");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-12/FqzdKv1LlgveO1C0Yey6Yodx_-yR.jpg");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-12/FlljoJblvgjFLYfaFldozXp2KRJf.jpg");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-12/FoNXOrgjbtDnsG5KT8X3jD6w1g48.jpg");
        downloadList.add("http://cdn.qupeiyin.cn/2021appvideo/20210122hljc.mp4");
        downloadList.add("http://cdn.qupeiyin.cn/2021-01-12/luWvBBtGb8wfk-EpWrpuTDehcioS.mp4");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-12/Fl6GSaWH7KB4LUth1DAFYyIkW5KR.jpg");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-12/FklfSowTDP5GnmxRNvSptcwAY8fN.png");

        downloadList.add("http://cdn.qupeiyin.cn/2021appvideo/20210105sgcs.mp4");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-12/Fv2CEEljYc5E2XauRdquDDBhZhA_.jpg");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-08/FqCVoT3HHDIkUycTkECXBdxghgB2.jpg");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-21/FgpdWFq3zP3zxowrTcenMaWdwBqy.jpg");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-25/FvooBYmPUL2EeLAKqTHUY0JoZcc8.png");
        downloadList.add("http://cdn.qupeiyin.cn/2021appvideo/20210122yjwl.mp4");
        downloadList.add("http://cdn.qupeiyin.cn/2021appvideo/20210105qwtz.mp4");
        downloadList.add("http://cdn.qupeiyin.cn/2021-01-13/6002016105057883633h720.mp4");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-03/FhqKbdU7Y0ZE_Jf7ruG-pRGhLcdh.jpg");
        downloadList.add("http://cdn.qupeiyin.cn/2021appvideo/20210105xxjs.mp4");
        downloadList.add("http://cdn.qupeiyin.cn/2021appvideo/20210105ltbp.mp4");
        downloadList.add("http://cdn.qupeiyin.cn/2021-01-07/FtAdyqVu5Hbs94OCltjVJ_HwlZkW.mp3");
        downloadList.add("http://cdn.qupeiyin.cn/2021appvideo/20210105cgtb.mp4");
        downloadList.add("http://cdn.qupeiyin.cn/2021appvideo/20210105shlx.mp4");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-12/Fs1D02B-5eBb9z4wBTBSkkm-luXV.png");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-13/FlljoJblvgjFLYfaFldozXp2KRJf.jpg");
        downloadList.add("http://peiyinimg.qupeiyin.cn/2021-01-12/FgpdWFq3zP3zxowrTcenMaWdwBqy.jpg");
    }

}
