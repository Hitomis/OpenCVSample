package cn.vansz.opencv;

import android.graphics.Bitmap;

/**
 * Created by Vans Z on 2019-11-08.
 */
public class JniHelper {
    static {
        System.loadLibrary("OpenCV");
    }


    private JniHelper() {
    }

    private static class SingletonHolder {

        private final static JniHelper instance = new JniHelper();
    }

    public static JniHelper getInstance() {
        return SingletonHolder.instance;
    }

    public native void getEdge(Object bitmap);

    public native Bitmap findIdNumber(Bitmap bitmap, Bitmap.Config argb8888);

}
