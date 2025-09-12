cd "native"
#进入native目录
#针对四种不同的Android ABI架构（armeabi-v7a、arm64-v8a、x86、x86_64）分别执行：
#配置CMake构建环境
#编译生成对应架构的库文件
#清理CMake缓存文件
#实现Android NDK的多架构交叉编译。
rm -r  CMakeFiles/
cmake -DCMAKE_TOOLCHAIN_FILE=$NDK_ROOT/build/cmake/android.toolchain.cmake -DANDROID_ABI=armeabi-v7a -DCMAKE_BUILD_TYPE=Release
make
rm -r  CMakeFiles/

cmake -DCMAKE_TOOLCHAIN_FILE=$NDK_ROOT/build/cmake/android.toolchain.cmake -DANDROID_ABI=arm64-v8a -DCMAKE_BUILD_TYPE=Release
make
rm -r  CMakeFiles/

cmake -DCMAKE_TOOLCHAIN_FILE=$NDK_ROOT/build/cmake/android.toolchain.cmake -DANDROID_ABI=x86  -DCMAKE_BUILD_TYPE=Release
make
rm -r  CMakeFiles/

cmake -DCMAKE_TOOLCHAIN_FILE=$NDK_ROOT/build/cmake/android.toolchain.cmake -DANDROID_ABI=x86_64  -DCMAKE_BUILD_TYPE=Release
make
rm -r  CMakeFiles/
