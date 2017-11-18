package com.berber.orange.memories;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.berber.orange.memories.activity.additem.AddItemActivity;
import com.berber.orange.memories.activity.main.CoordinatorActivity;

public class NotificationService extends Service {
    public static final String INTENT_ALARM_LOG = "intent_alarm_log";

    public NotificationService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.e("TAG", "Notification Service on Create");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Notification.Builder builder = new Notification.Builder(this.getApplicationContext()); //获取一个Notification构造器
        Intent nfIntent = new Intent(this, CoordinatorActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(this, 0, nfIntent, 0)).setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                R.drawable.logo))// 设置下拉列表中的图标(大图标)
                .setContentTitle("下拉列表中的Title")// 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher)// 设置状态栏内的小图标
                .setContentText("要显示的内容")// 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音
        startForeground(110, notification);

        AlarmManager manager = (AlarmManager) getSystemService(ALARM_SERVICE);

        int anHour = 5 * 1000; // 这是一小时的毫秒数

        long triggerAtTime = SystemClock.elapsedRealtime() + anHour;

        Intent i = new Intent(this, MyReceiver.class);
        i.setAction(INTENT_ALARM_LOG);

        PendingIntent pi = PendingIntent.getBroadcast(this, 0, i, 0);

        manager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerAtTime, pi);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);// 停止前台服务--参数：表示是否移除之前的通知
        super.onDestroy();
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.e("TAG", "NotificationService onBind()");
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
