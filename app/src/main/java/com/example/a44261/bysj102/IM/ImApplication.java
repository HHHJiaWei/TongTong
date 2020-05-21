package com.example.a44261.bysj102.IM;

import android.app.Application;

import com.orhanobut.logger.Logger;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

import cn.bmob.newim.BmobIM;
import cn.bmob.v3.Bmob;

public class ImApplication extends Application {

    private static ImApplication INSTANCE;
    public static ImApplication INSTANCE(){
        return INSTANCE;
    }
    private void setInstance(ImApplication app) {
        setBmobIMApplication(app);
    }
    private static void setBmobIMApplication(ImApplication a) {
        ImApplication.INSTANCE = a;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        setInstance(this);
        Bmob.initialize(this, "7ee361ae2183671d9c529b40ebb1ea56");
        //初始化
        Logger.init("smile");
        //只有主进程运行的时候才需要初始化
            BmobIM.init(this);
            BmobIM.registerDefaultMessageHandler(new ImMessageHandler(this));

    }
    /**
     * 获取当前运行的进程名
     * @return
     */
    public static String getMyProcessName() {
        try {
            File file = new File("/proc/" + android.os.Process.myPid() + "/" + "cmdline");
            BufferedReader mBufferedReader = new BufferedReader(new FileReader(file));
            String processName = mBufferedReader.readLine().trim();
            mBufferedReader.close();
            return processName;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
