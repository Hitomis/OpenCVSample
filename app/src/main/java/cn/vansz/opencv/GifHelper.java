package cn.vansz.opencv;

import android.graphics.Bitmap;

/**
 * Created by Vans Z on 2019-12-04.
 */
public class GifHelper {
    static {
        System.loadLibrary("OpenCV");
    }

    private volatile long gifInfo; // gif 信息地址

    public GifHelper(String path) {
        gifInfo = openFile(path);
    }

    public synchronized int getWidth() {
        return getWidthN(gifInfo);
    }

    public synchronized int getHeight() {
        return getHeightN(gifInfo);
    }

    public synchronized int getLength() {
        return getLengthN(gifInfo);
    }

    public long renderFrame(Bitmap bitmap, int index) {
        return renderFrame(bitmap, index, gifInfo);
    }

    public native long openFile(String path);
    private native int getWidthN(long gifInfo);
    private native int getHeightN(long gifInfo);
    private native int getLengthN(long gifInfo);
    private native long renderFrame(Bitmap bitmap, int index, long gifInfo);
}
