//
// Created by 赵帆 on 2019-12-04.
//
#include <jni.h>
#include <iostream>
#include <opencv2/opencv.hpp>
#include "utils.h"
#include "giflib/gif_lib.h"

using namespace std;
using namespace cv;

extern "C" JNIEXPORT jlong JNICALL
Java_cn_vansz_opencv_GifHelper_openFile(JNIEnv *env, jobject thiz, jstring path) {
    const char *_path = env->GetStringUTFChars(path, 0);
    int error;
    GifFileType *gif_file = DGifOpenFileName(_path, &error); // 读取gif图片
    env->ReleaseStringUTFChars(path, _path); // 释放字符串拷贝的内存
    return reinterpret_cast<jlong>(gif_file); // 把 GifFileType 类型强转为地址 long
}

extern "C" JNIEXPORT jint JNICALL
Java_cn_vansz_opencv_GifHelper_getWidthN(JNIEnv *env, jobject thiz, jlong gif_info) {
    auto *gif_file = reinterpret_cast<GifFileType *>(gif_info); //  把地址 long 强转为 GifFileType 类型
    return gif_file->SWidth; // 获取 gif 图片宽度
}

extern "C" JNIEXPORT jint JNICALL
Java_cn_vansz_opencv_GifHelper_getHeightN(JNIEnv *env, jobject thiz, jlong gif_info) {
    auto *gif_file = reinterpret_cast<GifFileType *>(gif_info);
    return gif_file->SHeight; // 获取 gif 图片高度
}

extern "C" JNIEXPORT jint JNICALL
Java_cn_vansz_opencv_GifHelper_getLengthN(JNIEnv *env, jobject thiz, jlong gif_info) {
    auto *gif_file = reinterpret_cast<GifFileType *>(gif_info);
    return gif_file->ImageCount; // 获取 gif 图片帧数
}

int drawFrame() {
    return 1;
}

extern "C" JNIEXPORT jlong JNICALL
Java_cn_vansz_opencv_GifHelper_renderFrame(JNIEnv *env, jobject thiz, jobject bitmap, jint index,
                                           jlong gif_info) {
    auto *gif_file = reinterpret_cast<GifFileType *>(gif_info);
    Mat src;
    // bitmap 转成颜色矩形阵
    void *pixels;
    AndroidBitmapInfo info;
    int ret;
    if ((ret = AndroidBitmap_getInfo(env, bitmap, &info)) < 0) {
        return -1;
    }

    if (info.format != ANDROID_BITMAP_FORMAT_RGBA_8888) {
        return -2;
    }

    if ((ret = AndroidBitmap_lockPixels(env, bitmap, &pixels)) < 0) {
        return -3;
    }
    // 开始渲染

    long delay_time = drawFrame();
    AndroidBitmap_unlockPixels(env, bitmap);
    return delay_time;
}