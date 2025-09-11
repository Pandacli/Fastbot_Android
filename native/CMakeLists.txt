# @author Jianqiang Guo, Yuhui Su
cmake_minimum_required(VERSION 3.10)
project(fastbot_native)

# 设置默认的库路径为 mac 平台
set( LIBPATH mac)

# 输出当前系统名称，用于调试
message(STATUS  ${CMAKE_SYSTEM_NAME})

# 根据不同的操作系统平台设置 OpenCV 路径和编译宏定义
IF (CMAKE_SYSTEM_NAME MATCHES "Android")
  # Android 平台配置
  message(STATUS "${ANDROID_NDK}/sources/third_party/opencv/jni")
  set( OpenCV_DIR "${ANDROID_NDK}/sources/third_party/opencv/jni" )
  add_definitions(-D__ANDROID__ )
  set( LIBPATH android/libs/${ANDROID_ABI}/)
ELSEIF (CMAKE_SYSTEM_NAME MATCHES "Darwin")
   # macOS 平台配置
   set( LIBPATH mac)
ELSEIF (CMAKE_SYSTEM_NAME MATCHES "Linux")
  # Linux 平台配置
  set( LIBPATH linux)
ENDIF (CMAKE_SYSTEM_NAME MATCHES "Android")

# 根据构建类型设置编译选项
IF (CMAKE_BUILD_TYPE MATCHES "Release")
 # 发布版本输出提示信息
 message(STATUES "compile release")
ELSE (CMAKE_BUILD_TYPE MATCHES "Release")
# 非发布版本添加调试宏定义
add_definitions(-D_DEBUG_)
ENDIF (CMAKE_BUILD_TYPE MATCHES "Release")

# 设置 Android STL 类型
set(ANDROID_STL "c++_static")

# 设置 C++ 标准为 C++14，并要求必须支持
set(CMAKE_CXX_STANDARD 14)
set(CMAKE_CXX_STANDARD_REQUIRED on)

# 设置 C++ 编译标志
set(LOCAL_CXX_FLAGS "-std=c++14 -fPIC  -fvisibility=hidden -std=c++11 -frtti -Wno-switch-enum -Wno-switch ${LOCAL_CPPFLAGS}")

# 设置可执行文件和库文件的输出目录
set(CMAKE_RUNTIME_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/../libs/${ANDROID_ABI}")
set(CMAKE_LIBRARY_OUTPUT_DIRECTORY "${CMAKE_CURRENT_SOURCE_DIR}/../libs/${ANDROID_ABI}")

# 启用 ARM NEON 指令集优化
set(ANDROID_ARM_NEON ON)

# 设置发布版本的 C/C++ 编译标志
set(CMAKE_C_FLAGS_RELEASE "${CMAKE_C_FLAGS_RELEASE}  -s -Wall ")
set(CMAKE_CXX_FLAGS_RELEASE "${CMAKE_CXX_FLAGS_RELEASE}  -s -Wall ")

# 查找 Android 日志库
find_library( # Sets the name of the path variable.
              log-lib

              # Specifies the name of the NDK library that
              # you want CMake to locate.
              log )

# 添加项目包含目录
include_directories(
  "./"
  "agent/"
  "desc/"
  "desc/reuse"
  "events/"
  "model/"
  "monkey/"
  "project/jni/"
  "storage"
  "thirdpart/"
  "thirdpart/flatbuffers/"
  "thirdpart/tinyxml2/"
  "thirdpart/json/"
)

# 收集所有源文件
file( GLOB SRC_LIST
      "*.hpp"
      "*.cpp"
      "desc/*.cpp"
      "desc/reuse/*.cpp"
      "model/*.cpp"
      "agent/*.cpp"
      "events/*.cpp"
      "monkey/*.cpp"
      "thirdpart/tinyxml2/*.cpp"
      "thirdpart/flatbuffers/*.cpp"
      "thirdpart/json/*.hpp")

# 输出源文件列表，用于调试
message(STATUS ${SRC_LIST})

# 创建共享库 fastbot_native
add_library( # Sets the name of the library.
             fastbot_native
             # Sets the library as a shared library.
             SHARED
             # Provides a relative path to your source file(s).
             ${SRC_LIST}
             "project/jni/fastbot_native.cpp"
        )

# 查找线程库
find_package(Threads)

# 检查并使用 ccache 加速编译
find_program(CCACHE_FOUND ccache)
if(CCACHE_FOUND)
  message("Found ccache ${CCACHE_FOUND}")
  message("Using ccache to speed up compilation")
  set(ENV{CCACHE_CPP2} "yes")
  set_property(GLOBAL PROPERTY RULE_LAUNCH_COMPILE ccache)
  set_property(GLOBAL PROPERTY RULE_LAUNCH_LINK ccache)
endif(CCACHE_FOUND)

# 如果未指定构建类型，默认设置为 Release
if (NOT CMAKE_BUILD_TYPE)
  message(STATUS "No build type selected, default to Release")
  set(CMAKE_BUILD_TYPE "Release")
endif()

# 链接所需的系统库
target_link_libraries(
               fastbot_native
               log
               android
               atomic
            )
