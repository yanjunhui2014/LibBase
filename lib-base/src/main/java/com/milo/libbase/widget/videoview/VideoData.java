package com.milo.libbase.widget.videoview;

/**
 * 标题：视频信息
 * 功能：
 * 备注：
 * <p>
 * Created by Milo
 * E-Mail : 303767416@qq.com
 * 2018/12/4 18:30
 */
public class VideoData {

    /**
     * 视频URL
     */
    public String mVideoUrl;
    /**
     * 视频大小（可以直接用于显示的字符串）
     */
    public String mVideoSize;

    /**
     * 视频清晰度
     */
    public VideoDefinition mVideoDefinition;

    /**
     * 构造，默认标清视频
     *
     * @param videoUrl  - 地址
     * @param videoSize - 大小
     */
    public VideoData(String videoUrl, String videoSize) {
        this.mVideoUrl = videoUrl;
        this.mVideoSize = videoSize;
        this.mVideoDefinition = VideoDefinition.STANDARD;
    }


    /**
     * @param videoUrl        - 地址
     * @param videoSize       - 大小
     * @param videoDefinition - 清晰度
     */
    public VideoData(String videoUrl, String videoSize, VideoDefinition videoDefinition) {
        this.mVideoUrl = videoUrl;
        this.mVideoSize = videoSize;
        this.mVideoDefinition = videoDefinition;
    }

}
