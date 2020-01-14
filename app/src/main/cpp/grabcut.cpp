//
// Created by 赵帆 on 2019-12-02.
//
#include <jni.h>
#include <iostream>
#include <opencv2/opencv.hpp>
#include <string>
#include <vector>
#include "utils.h"

using namespace std;
using namespace cv;

extern "C" JNIEXPORT jobject JNICALL
Java_cn_vansz_opencv_OpenCVHelper_imageKmans(JNIEnv *env, jobject thiz, jstring imgPath,
                                             jobject config) {

    Vec3b color_tab[] = {
            Vec3b(0, 0, 255),
            Vec3b(0, 255, 0),
            Vec3b(255, 100, 100),
            Vec3b(255, 0, 255),
            Vec3b(0, 255, 255)
    };
    string path = jstring2str(env, imgPath);
    Mat src = imread(path);
    Mat data;
    Mat labels;
    for (int i = 0; i < src.rows; i++) {
        for (int j = 0; j < src.cols; j++) {
            Vec3b point = src.at<Vec3b>(i, j);
            Mat tmp = (Mat_<float>(1, 3) << point[0], point[1], point[2]);
            data.push_back(tmp);
        }
    }

    kmeans(data, 3, labels, TermCriteria(TermCriteria::EPS + TermCriteria::COUNT, 10, 1.0), 3,
           KMEANS_RANDOM_CENTERS);

    int n = 0;
    for (int i = 0; i < src.rows; i++) {
        for (int j = 0; j < src.cols; j++) {
            int clusterIdx = labels.at<int>(n);
            src.at<Vec3b>(i, j) = color_tab[clusterIdx];
            n++;
        }
    }

    Mat gradx;
    Mat grady;
    Mat edges;
    Mat dst;
    Sobel(src, gradx, CV_16S, 1, 0);
    Sobel(src, grady, CV_16S, 0, 1);
    Canny(gradx, grady, edges, 50, 150);
    bitwise_and(src, src, dst, edges);


    jobject result = createBitmap(env, dst, config);

    dst.release();
    edges.release();
    grady.release();
    gradx.release();
    labels.release();
    data.release();
    src.release();
    return result;
}

void getBinMask(const Mat& comMask, Mat& binMask)
{
    if (comMask.empty() || comMask.type() != CV_8UC1)
        CV_Error(Error::StsBadArg, "comMask is empty or has incorrect type (not CV_8UC1)");
    if (binMask.empty() || binMask.rows != comMask.rows || binMask.cols != comMask.cols)
        binMask.create(comMask.size(), CV_8UC1);
    binMask = comMask & 1;
}

extern "C" JNIEXPORT jobject JNICALL
Java_cn_vansz_opencv_OpenCVHelper_grabCut(JNIEnv *env, jobject thiz, jstring imgPath,
                                          jobject config) {
//    string path = jstring2str(env, imgPath);
//    Mat src = imread(path);
////    Mat dst;
//
//    // canny 边缘提取
//    Mat gradx;
//    Mat grady;
//    Mat edges;
//    Mat canny_dst;
//    Sobel(src, gradx, CV_16S, 1, 0);
//    Sobel(src, grady, CV_16S, 0, 1);
//    Canny(gradx, grady, edges, 50, 150);
//    bitwise_and(src, src, canny_dst, edges);
//
//    vector<Mat> contours;
//    OutputArrayOfArrays outputArrays(contours);
//    Mat hierarchy;
//    Point point(0, 0);
//    findContours(canny_dst, outputArrays, hierarchy, RETR_EXTERNAL, CHAIN_APPROX_SIMPLE, point);
//
////    dst.create(src.size(), src.type());
//    Scalar scalar(255, 0, 0);
//    for (int i = 0; i < contours.size(); i++) {
//        Rect rect = boundingRect(contours[i]);
//        rectangle(src, rect.tl(), rect.br(), scalar, 2, 8, 0);
//    }
//
//    jobject result = createBitmap(env, src, config);
//    hierarchy.release();
//    edges.release();
//    grady.release();
//    gradx.release();
//    src.release();
//    return result;

    string path = jstring2str(env, imgPath);
    Mat src = imread(path);

    Mat mask;
    Mat bgmodel;
    Mat fgmodel;

    mask.create(src.size(), CV_8UC1);
    mask.setTo(Scalar::all(GC_BGD));//背景为黑色
    Rect rect(0, 0, 100, 100);
//    mask(rect).setTo(Scalar(GC_PR_FGD));
    grabCut(src, mask, rect, bgmodel, fgmodel, 1, GC_INIT_WITH_RECT);//分割，抠图

    jobject result = createBitmap(env, src, config);

    fgmodel.release();
    bgmodel.release();
    mask.release();
    src.release();

//
//    string path = jstring2str(env, imgPath);
//    Mat src = imread(path);
//    Mat mask;
//    Mat bgModel;
//    Mat fgModel;
//    Rect rect(Point(66, 100), Point(330, 300));
//    grabCut(src, mask, rect, bgModel, fgModel, 10, GC_INIT_WITH_RECT);
//    rectangle(src, rect, Scalar(255), 2, 8);
//
//    Mat binMask;
//    getBinMask(mask, binMask);
//
//    jobject result = createBitmap(env, binMask, config);

    return result;
}