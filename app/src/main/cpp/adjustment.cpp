//
// Created by 赵帆 on 2019-11-29.
//

#include <jni.h>
#include <iostream>
#include <opencv2/opencv.hpp>
#include "utils.h"

using namespace std;
using namespace cv;

extern "C" JNIEXPORT jobject JNICALL
Java_cn_vansz_opencv_OpenCVHelper_adjustBrightness(JNIEnv *env, jobject thiz, jobject bitmap,
                                                   jdouble level, jobject argb8888) {
    Mat *src = new Mat;
    Mat *dst = new Mat;

    Scalar scalar(level, level, level);
    bitmap2Mat(env, bitmap, src);
    add(*src, scalar, *dst);
    jobject result = createBitmap(env, *dst, argb8888);

    delete dst;
    delete src;

    return result;
}

extern "C" JNIEXPORT jobject JNICALL
Java_cn_vansz_opencv_OpenCVHelper_adjustCombine(JNIEnv *env, jobject thiz, jobject bitmap,
                                                jdouble brightness, jdouble contrast,
                                                jobject config) {
    Mat *src = new Mat;
    Mat *dst = new Mat;

    bitmap2Mat(env, bitmap, src);
    Mat black = Mat((*src).size(), (*src).type());
    addWeighted(*src, contrast, black, 1 - contrast, brightness, *dst);
    jobject result = createBitmap(env, *dst, config);

    delete dst;
    delete src;

    return result;
}
