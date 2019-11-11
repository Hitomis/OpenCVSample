#版本信息
cmake_minimum_required(VERSION 3.4.1)

#1. OpenCV 头文件配置
include_directories(include)

#2. 添加 OpenCV so库信息,也就是本地库
file(GLOB_RECURSE openCVLibs ${CMAKE_SOURCE_DIR}/*.cpp ${CMAKE_SOURCE_DIR}/*.c)
add_library(OpenCV SHARED ${openCVLibs})

#3. 添加 libopencv_java4 so库 IMPORTED表示只需要导入，不需要构建so库，因为已经构建好了
add_library(lib_opencv SHARED IMPORTED)
#设置lib_opencv动态库的真实地址
set_target_properties(lib_opencv PROPERTIES IMPORTED_LOCATION ${CMAKE_SOURCE_DIR}/../jniLibs/${ANDROID_ABI}/libopencv_java4.so)

#4. 添加 gif so库信息
file(GLOB_RECURSE gifLibs ${CMAKE_SOURCE_DIR}/giflib/*.*)
add_library(GifLib SHARED ${gifLibs})

# 使用 NDK 中定义好的库，参数一是库的别名
find_library(log-lib log)
find_library(jnigraphics-lib jnigraphics)

# 将 NDK 中需要用到的库和第三方库链接到本地库中, 这样在编译的时候可以用到，参数一是本地库别名
target_link_libraries(
        OpenCV
        lib_opencv
        ${log-lib}
        ${jnigraphics-lib}
)