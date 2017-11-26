package com.berber.orange.memories;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.audiofx.LoudnessEnhancer;
import android.util.Log;

import com.berber.orange.memories.activity.main.CoordinatorActivity;
import com.berber.orange.memories.model.db.NotificationSending;
import com.berber.orange.memories.model.db.NotificationSendingDao;

import java.util.Date;
import java.util.List;

public class MyReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (NotificationService.INTENT_ALARM_LOG.equals(action)) {
            Log.e("AlarmReceiver", "启动扫描服务");
            startScanTask(context);
            Intent i = new Intent(context, NotificationService.class);
            context.startService(i);
        }

    }

    private void startScanTask(Context context) {
        APP mApplication = ((APP) context.getApplicationContext());
        NotificationSendingDao notificationSendingDao = mApplication.getDaoSession().getNotificationSendingDao();
        Date currentDate = new Date();

        //NotificationSendingDao.Properties.SentDate.le(currentDate)
        List<NotificationSending> list = notificationSendingDao.queryBuilder().where(NotificationSendingDao.Properties.SentDate.isNull(), NotificationSendingDao.Properties.SendingDate.le(currentDate)).list();
        if (list.isEmpty()) {
            Log.e("TAG", "sending list is empty");
        }
        for (NotificationSending notificationSending : list) {
            Log.e("TAG", "sending data: " + notificationSending.getSendingDate().toString());
            sendingNotification(context, notificationSending);
            notificationSending.setSentDate(currentDate);
        }

        notificationSendingDao.updateInTx(list);

    }

    private void sendingNotification(Context context, NotificationSending notificationSending) {
        // TODO: 2017/11/18 notification function
        Log.e("TAG", "已经发送了通知: " + notificationSending.getAnniversary().getTitle());
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification.Builder builder = new Notification.Builder(context); //获取一个Notification构造器
        Intent nfIntent = new Intent(context, CoordinatorActivity.class);
        builder.setContentIntent(PendingIntent.getActivity(context, 0, nfIntent, 0))
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.drawable.ic_mr_button_connected_13_light))// 设置下拉列表中的图标(大图标)
                .setContentTitle("你有一个新的通知提醒,点击查看")// 设置下拉列表里的标题
                .setSmallIcon(R.mipmap.ic_launcher)// 设置状态栏内的小图标
                .setContentText(notificationSending.getAnniversary().getTitle())// 设置上下文内容
                .setWhen(System.currentTimeMillis()); // 设置该通知发生的时间

        Notification notification = builder.build(); // 获取构建好的Notification
        notification.defaults = Notification.DEFAULT_SOUND; //设置为默认的声音

        // Issue the notification.
        mNotificationManager.notify((int) (Math.random() * 100 + 1), notification);
    }
}
