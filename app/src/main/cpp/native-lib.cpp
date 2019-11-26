#include <jni.h>
#include <string>
#include "utils.h"
#include <opencv2/opencv.hpp>

#define DEFAULT_CARD_WIDTH 640
#define DEFAULT_CARD_HEIGHT 400
#define FIX_IDCARD_SIZE Size(DEFAULT_CARD_WIDTH,DEFAULT_CARD_HEIGHT)

using namespace std;
using namespace cv;

extern "C" JNIEXPORT jstring JNICALL
Java_cn_vansz_opencv_JniHelper_stringFromJNI(
        JNIEnv *env,
        jobject /* this */) {
    string hello = "Hello from C++";
    return env->NewStringUTF(hello.c_str());
}

extern "C" JNIEXPORT jobject JNICALL
Java_cn_vansz_opencv_JniHelper_findIdNumber(JNIEnv *env, jobject instance,
                                            jobject bitmap, jobject argb8888) {

    //原图
    Mat src_img;
    Mat dst;
    Mat dst_img;
    //c 没有Bitmap 要将Bitmap转成Mat
    //1 先将Bitmap转成Mat
    bitmap2Mat(env, bitmap, &src_img);
    //3 归一化
    resize(src_img, src_img, FIX_IDCARD_SIZE);
    //4 灰度化
    cvtColor(src_img, dst, COLOR_RGB2GRAY);
    //5 二值化
    threshold(dst, dst, 100, 255, THRESH_BINARY);
    //6 腐蚀
    Mat erodeElement = getStructuringElement(MORPH_RECT, Size(20, 10));
    erode(dst, dst, erodeElement);
    //7 轮廓检测
    vector<vector<Point>> contours;
    vector<Rect> rects;
    findContours(dst, contours, RETR_TREE, CHAIN_APPROX_SIMPLE, Point(0, 0));
    for (int i = 0; i < contours.size(); i++) {
        //获取到矩形区域
        Rect rect = boundingRect(contours.at(i));
        //绘制
//        rectangle(dst,rect,Scalar(0,0,255));
        //8 逻辑处理，找到号码所在区域
        //身份证号码有固定宽高比>1:8&&<1:16
        if (rect.width > rect.height * 8 && rect.width < rect.height * 16) {

            rects.push_back(rect);
        }
    }
    //9 继续查找坐标最低的矩形区域
    int lowPoint = 0;
    Rect finalRect;
    for (int i = 0; i < rects.size(); i++) {
        Rect rect = rects.at(i);
        Point point = rect.tl();
        if (point.y > lowPoint) {
            lowPoint = point.y;
            finalRect = rect;
        }
    }
    //10 图像切割
//    rectangle(dst,finalRect,Scalar(0,0,255));
    dst_img = src_img(finalRect);
    //2 将Mat转成Bitmap
    return createBitmap(env, dst_img, argb8888);

}

extern "C" JNIEXPORT void JNICALL
Java_cn_vansz_opencv_JniHelper_getEdge(JNIEnv *env, jobject instance, jobject bitmap) {
    AndroidBitmapInfo info;
    void *pixels;

    CV_Assert(AndroidBitmap_getInfo(env, bitmap, &info) >= 0);
    CV_Assert(info.format == ANDROID_BITMAP_FORMAT_RGBA_8888 ||
              info.format == ANDROID_BITMAP_FORMAT_RGB_565);
    CV_Assert(AndroidBitmap_lockPixels(env, bitmap, &pixels) >= 0);
    CV_Assert(pixels);
    if (info.format == ANDROID_BITMAP_FORMAT_RGBA_8888) {
        Mat temp(info.height, info.width, CV_8UC4, pixels);
        Mat gray;
        cvtColor(temp, gray, COLOR_RGBA2GRAY);
        Canny(gray, gray, 125, 225);
        cvtColor(gray, temp, COLOR_GRAY2RGBA);
    } else {
        Mat temp(info.height, info.width, CV_8UC2, pixels);
        Mat gray;
        cvtColor(temp, gray, COLOR_RGB2GRAY);
        Canny(gray, gray, 125, 225);
        cvtColor(gray, temp, COLOR_GRAY2RGB);
    }
    AndroidBitmap_unlockPixels(env, bitmap);
}