//package com.xinghe.syncore;
//
//import android.app.Application;
//import android.content.Context;
//import android.content.pm.PackageInfo;
//import android.content.pm.PackageManager;
//import android.util.Log;
//
//import androidx.test.platform.app.InstrumentationRegistry;
//
//import com.dianping.logan.Logan;
//import com.dianping.logan.SendLogCallback;
//import com.xinghe.syncore.logan.RealSendLogRunnable;
//
//import org.junit.Test;
//import org.mockito.Mock;
//
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
//public class LoganUtilsTest extends Application {
//    private static final String TAG = LoganUtilsTest.class.getName();
//    @Mock
//    public RealSendLogRunnable mSendLogRunnable;
//
//
//    @Test
//    public void loganTest() {
//        new Thread() {
//            @Override
//            public void run() {
//                super.run();
//                try {
//                    for (int i = 0; i < 9; i++) {
//                        Log.d(TAG, "times : " + i);
//                        Logan.w(String.valueOf(i), 1);
//                        Thread.sleep(5);
//                    }
//                    Log.d(TAG, "write log end");
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }.start();
//    }
//    @Test
//    public void loganSend() {
//        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
//        String d = dataFormat.format(new Date(System.currentTimeMillis()));
//        String[] temp = new String[1];
//        temp[0] = d;
//        Logan.s(temp, mSendLogRunnable);
//    }
//
//
//    @Test
//    public void loganSendByDefault() {
//        String buildVersion = "";
//        String appVersion = "";
//        try {
//            Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
//            PackageManager packageManager = appContext.getPackageManager();
//            PackageInfo pInfo = packageManager.getPackageInfo(getPackageName(), 0);
//            appVersion = pInfo.versionName;
//            buildVersion = String.valueOf(pInfo.versionCode);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        //final String url = "https://openlogan.inf.test.sankuai.com/logan/upload.json";
//        final String url = "https://192.168.1.89:8080/logan/upload.json";
//        SimpleDateFormat dataFormat = new SimpleDateFormat("yyyy-MM-dd");
//        final String date = dataFormat.format(new Date(System.currentTimeMillis()));
//        Logan.s(url, date, "1", "logan-test-unionid", "deviceId", buildVersion, appVersion, new SendLogCallback() {
//            @Override
//            public void onLogSendCompleted(int statusCode, byte[] data) {
//                final String resultData = data != null ? new String(data) : "";
//                Log.d(TAG, "日志上传结果, http状态码: " + statusCode + ", 详细: " + resultData);
//            }
//        });
//    }
//
//}