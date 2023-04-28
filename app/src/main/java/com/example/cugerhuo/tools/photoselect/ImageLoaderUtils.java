package com.example.cugerhuo.tools.photoselect;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;

/**
 *
 * 加载照片
 * @author 施立豪
 * @date：2023/3/21
 */
public class ImageLoaderUtils {

    public static boolean assertValidRequest(Context context) {
        if (context instanceof Activity) {
            Activity activity = (Activity) context;
            return !isDestroy(activity);
        } else if (context instanceof ContextWrapper){
            ContextWrapper contextWrapper = (ContextWrapper) context;
            if (contextWrapper.getBaseContext() instanceof Activity){
                Activity activity = (Activity) contextWrapper.getBaseContext();
                return !isDestroy(activity);
            }
        }
        return true;
    }

    private static boolean isDestroy(Activity activity) {
        if (activity == null) {
            return true;
        }
        return activity.isFinishing() || activity.isDestroyed();
    }


}
