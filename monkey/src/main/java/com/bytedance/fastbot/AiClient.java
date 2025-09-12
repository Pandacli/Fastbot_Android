/*
 * This code is licensed under the Fastbot license. You may obtain a copy of this license in the LICENSE.txt file in the root directory of this source tree.
 */

package com.bytedance.fastbot;

import android.graphics.PointF;
import android.os.Build;
import android.os.SystemClock;

import com.android.commands.monkey.fastbot.client.Operate;
import com.android.commands.monkey.utils.Logger;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * @author Jianqiang Guo, Zhao Zhang
 * 这段代码实现了一个Android平台的AI客户端（AiClient），用于加载本地库并调用native方法执行智能操作。主要功能包括：
 * 静态初始化时尝试加载native库（.so文件），失败则重试；
 * 提供算法类型枚举和初始化接口；
 * 封装native方法调用，如获取操作、检查屏蔽点等；
 * 使用单例模式确保唯一实例。
 * 核心是通过JNI调用底层C++逻辑实现智能决策。
 */

public class AiClient {
    //单例模式
    private static final AiClient singleton;

    //静态初始化代码
    static {
        boolean success;
        //调用 SystemClock.elapsedRealtimeNanos() ， 获取加载开始时间begin
        long begin = SystemClock.elapsedRealtimeNanos();
        //默认加载本地nativeLib
        success = tryToLoadNativeLib(false);
//        if (!success){
//            //如果首次加载失败，再次调用tryToLoadNativeLib(true)重试
//            Logger.infoFormat("从apk中 加载so 文件");
//            success = tryToLoadNativeLib(true);
//        }
        //调用 SystemClock.elapsedRealtimeNanos() ， 获取加载结束时间end
        long end = SystemClock.elapsedRealtimeNanos();
        Logger.infoFormat("load fastbot_native takes %d ms.", TimeUnit.NANOSECONDS.toMillis(end - begin));
        singleton = new AiClient(success);
    }

    public enum AlgorithmType {
        Random(0),
        SataRL(1),
        SataNStep(2),
        NStepQ(3),
        Reuse(4);

        private final int _value;

        AlgorithmType(int value) {
            this._value = value;
        }

        public int value() {
            return this._value;
        }
    }

    /**
     * 初始化Agent
     * @param agentType
     * @param packagename
     */
    public static void InitAgent(AlgorithmType agentType, String packagename) {
        singleton.fgdsaf5d(agentType.value(), packagename, 0);
    }

    private boolean loaded = false;

    /**
     * 构造函数
     * @param success
     */
    protected AiClient(boolean success) {
        loaded = success;
    }

    private static boolean tryToLoadNativeLib(boolean fromAPK){
        String path = "";
        try {
            path = getAiPathLocally();
            System.load(path);
            Logger.println("fastbot native : library load!加载 +"+path+" 开始启动：");
        } catch (UnsatisfiedLinkError e) {
            Logger.errorPrintln("Error: Could not load library!");
            Logger.errorPrintln(path);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 从APK中获取AI路径
     *
     * 该函数根据设备支持的ABI架构类型，返回对应的native库在APK中的路径。
     * 主要用于定位不同架构下的libfastbot_native.so文件。
     *
     * @return 返回AI native库在APK中的完整路径字符串
     */
    private static String getAiPathFromAPK(){
        String[] abis = Build.SUPPORTED_ABIS;
        List<String> abisList = Arrays.asList(abis);
        if (abisList.contains("x86_64")) {
            return "/data/local/tmp/monkey.apk!/lib/x86_64/libfastbot_native.so";
        } else if (abisList.contains("x86")) {
            return "/data/local/tmp/monkey.apk!/lib/x86/libfastbot_native.so";
        } else if (abisList.contains("arm64-v8a")) {
            return "/data/local/tmp/monkey.apk!/lib/arm64-v8a/libfastbot_native.so";
        } else {
            return "/data/local/tmp/monkey.apk!/lib/armeabi-v7a/libfastbot_native.so";
        }
    }

    /**
     * 获取AI库文件的本地路径
     * <p>
     * 该函数根据当前设备支持的CPU架构类型，返回对应的AI库文件路径。
     * 支持的架构包括x86_64、x86、arm64-v8a和armeabi-v7a。
     * Android 10 及以上版本为了安全，限制了原生库的加载路径，仅允许从应用私有目录（如 /data/data/包名/）、
     * 系统目录（如 /system/lib64/）或 /data/local/tmp/ 加载动态库，/sdcard/（外部存储）属于禁止路径，因此必须迁移库文件。
     * @return 返回AI库文件的完整路径字符串
     */
    private static String getAiPathLocally(){
        String[] abis = Build.SUPPORTED_ABIS;
        List<String> abisList = Arrays.asList(abis);
        if (abisList.contains("x86_64")) {
            return "/data/local/tmp/x86_64/libfastbot_native.so";
        } else if (abisList.contains("x86")) {
            return "/data/local/tmp/x86/libfastbot_native.so";
        } else if (abisList.contains("arm64-v8a")) {
            return "/data/local/tmp/arm64-v8a/libfastbot_native.so";
        } else {
            return "/data/local/tmp/armeabi-v7a/libfastbot_native.so";
        }
    }

    private static String getAiPath(boolean fromAPK) {
//        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.LOLLIPOP_MR1) {
//            return getAiPathLocally();
//        } else {
//            return fromAPK ? getAiPathFromAPK() : getAiPathLocally();
//        }
        return getAiPathLocally();
    }

    public static void loadResMapping(String resmapping) {
        if (null == singleton) {
            Logger.println("// Error: AiCore not initted!");
            return;
        }
        if (!singleton.loaded) {
            Logger.println("// Error: Could not load native library!");
            Logger.println("Please report this bug issue to github");
            System.exit(1);
        }
        singleton.jdasdbil(resmapping);
    }

    public static Operate getAction(String acvitty, String pageDesc) {
        return singleton.b1bhkadf(acvitty, pageDesc);
    }

    private native void jdasdbil(String b9);

    private native String b0bhkadf(String a0, String a1);
    private native void fgdsaf5d(int b7, String b2, int t);
    private native boolean nkksdhdk(String a0, float p1, float p2);

    public static native String getNativeVersion();

    public static boolean checkPointIsShield(String activity, PointF point)
    {
        return singleton.nkksdhdk(activity, point.x, point.y);
    }

    public Operate b1bhkadf(String activity, String pageDesc) {
        if (!loaded) {
            Logger.println("// Error: Could not load native library!");
            Logger.println("Please report this bug issue to github");
            System.exit(1);
        }
        String operateStr = b0bhkadf(activity, pageDesc);

        if (operateStr.length() < 1) {
            Logger.errorPrintln("native get operate failed " + operateStr);
            return null;
        }
        return Operate.fromJson(operateStr);
    }

}
