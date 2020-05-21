package com.example.a44261.bysj102.TT;

import android.annotation.TargetApi;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.db.AppSQLiteData;


public class AlarmService extends Service {
    private AlarmManager am;
    private PendingIntent pi;
    private Long time;
    private String title;
    private String content;
    private String org_name;
    private AppSQLiteData mRemindSQL;
    private static final String TAG="MyService";
    private static final String ID="channel_1";
    private static final String NAME="前台服务";
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d (TAG,"onCreate");
        if(Build.VERSION.SDK_INT>=26){
            setForeground();
        }else{

        }
    }
    @TargetApi(26)
    private void setForeground(){
        NotificationManager manager=(NotificationManager)getSystemService (NOTIFICATION_SERVICE);
        NotificationChannel channel=new NotificationChannel(ID,NAME, NotificationManager.IMPORTANCE_HIGH);
        manager.createNotificationChannel (channel);
        Notification notification=new Notification.Builder (this,ID)
                .setContentTitle ("系统正在为您服务")
                .setSmallIcon (R.drawable.icon)
               // .setLargeIcon (BitmapFactory.decodeResource (getResources (),R.drawable.icon))
                .build ();
        startForeground (1,notification);
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                getAlarmTime();
            }
        }).start();
        return START_REDELIVER_INTENT;
    } //这里为了提高优先级，选择START_REDELIVER_INTENT 没那么容易被内存清理时杀死

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void getAlarmTime() {
        mRemindSQL= new AppSQLiteData(this,"Remind.db", null, 1);
        SQLiteDatabase db = mRemindSQL.getWritableDatabase();
        Cursor cursor = db.query("Remind_data", null, null, null, null, null, null);
        if (cursor.moveToFirst()) { //遍历数据库的表，拿出一条，选择最近的时间赋值，作为第一条提醒数据。
            time = cursor.getLong(cursor.getColumnIndex("remindTime"));
            org_name = cursor.getString(cursor.getColumnIndex("orgname"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            content = cursor.getString(cursor.getColumnIndex("content"));
            do {   if (time > cursor.getLong(cursor.getColumnIndex("remindTime"))) {
                time = cursor.getLong(cursor.getColumnIndex("remindTime"));
                org_name = cursor.getString(cursor.getColumnIndex("orgname"));
                title = cursor.getString(cursor.getColumnIndex("title"));
                content = cursor.getString(cursor.getColumnIndex("content"));
            }
            } while (cursor.moveToNext());
        } else {
            time = null;
        }
        db.delete("Remind_data", "remindTime=?", new String[]{String.valueOf(time)});      //删除已经发送提醒的时间
        cursor.close();

        Intent startNotification = new Intent(this, AlarmReceiver.class);   //这里启动的广播，下一步会教大家设置
        startNotification.putExtra("org_name", org_name);
        startNotification.putExtra("title", title);
        startNotification.putExtra("content", content);
        am = (AlarmManager) getSystemService(ALARM_SERVICE);   //这里是系统闹钟的对象
        pi = PendingIntent.getBroadcast(this, 0, startNotification, PendingIntent.FLAG_UPDATE_CURRENT);     //设置事件
        if (time != null) {
            am.set(AlarmManager.RTC_WAKEUP, time, pi);    //提交事件，发送给 广播接收器
        } else {
            //当提醒时间为空的时候，关闭服务，下次添加提醒时再开启
            stopService(new Intent(this, AlarmService.class));
        }
    }
}

