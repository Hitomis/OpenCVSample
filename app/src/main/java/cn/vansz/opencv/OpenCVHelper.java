package cn.vansz.opencv;

import android.graphics.Bitmap;

/**
 * Created by Vans Z on 2019-11-29.
 */
public class OpenCVHelper {
    static {
        System.loadLibrary("OpenCV");
    }

    private OpenCVHelper() {
    }

    private static class SingletonHolder {

        private final static OpenCVHelper instance = new OpenCVHelper();
    }


    public static OpenCVHelper getInstance() {
        return SingletonHolder.instance;
    }

    public native Bitmap adjustBrightness(Object bitmap, double level, Bitmap.Config config);

    public native Bitmap adjustCombine(Object bitmap, double brightness, double contrast, Bitmap.Config config);
}
