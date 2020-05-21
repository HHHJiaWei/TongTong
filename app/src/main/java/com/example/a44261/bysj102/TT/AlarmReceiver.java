package com.example.a44261.bysj102.TT;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.example.a44261.bysj102.R;

public class AlarmReceiver extends BroadcastReceiver {

    private NotificationManager manager;
    private static final int NOTIFICATION_ID_1 = 0x00113;
    private String title,org_name;
    private String content = "提醒的时间到啦，快看看你要做的事...";
    @Override
    public void onReceive(Context context, Intent intent) {
        org_name = intent.getStringExtra("org_name");
        title = intent.getStringExtra("title");
        content = intent.getStringExtra("content");
        showNormal(context);
        if(title!=null&&content!=null) {
            Log.e("org_name:", org_name);
            Log.e("Title:", title);
            Log.e("Content:", content);
        }
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setClass(context, AlarmService.class);
        context.startService(intent);
        //回调Service,同一个Service只会启动一个，所以直接再次启动Service，会重置开启新的提醒，
    }    /**     * 发送通知     */
    private void showNormal(Context context) {
        Intent intent = new Intent(context, HomePage.class);//这里是点击Notification 跳转的界面，可以自己选择
        PendingIntent pi = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            //只在Android O之上需要渠道
            NotificationChannel notificationChannel = new NotificationChannel(title+content,"channelname",NotificationManager.IMPORTANCE_HIGH);
            //如果这里用IMPORTANCE_NOENE就需要在系统的设置里面开启渠道，通知才能正常弹出
            manager.createNotificationChannel(notificationChannel);
        }
        Notification notification = new NotificationCompat.Builder(context,title+content)
                .setSmallIcon(R.drawable.icon)
                .setTicker(content)        //通知时在状态栏显示的通知内容
                .setContentInfo("便签提醒")        //内容信息
                .setContentTitle(org_name+":"+title)        //设置通知标题。
                .setStyle(new NotificationCompat.BigTextStyle().bigText(content))        //设置通知内容。
                .setAutoCancel(true)                //点击通知后通知消失
                .setDefaults(Notification.DEFAULT_ALL)        //设置系统默认的通知音乐、振动、LED等。
                .setContentIntent(pi)
                .build();
        manager.notify(NOTIFICATION_ID_1, notification);
    }
}
