package com.example.cugerhuo.tools.async;

/**
 * 下载图片接口异步任务
 * @author 施立豪
 * @time 2023/4/13
 */
public interface DownLoadImageInterface {
    Boolean doInBackground(String... inputs);

    void onProgressUpdate(Integer... progress);


}
