package qilu.help;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import java.text.SimpleDateFormat;
import java.util.Date;


//TODO:这是一个在发布求助信息后启动的一个服务
public class PutHelpIntentService extends IntentService {

    public PutHelpIntentService() {
        super("PutHelpIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            try {
                Thread.currentThread().sleep(2000);//阻断2秒
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //读取当前系统时间
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
            Date curDate = new Date(System.currentTimeMillis());//获取当前时间
            String currentDate = formatter.format(curDate);
            //在“消息大厅”中添加记录
            MessageActivity.addRecord("余成","我在石油大学，我可以帮助你",currentDate,true,false,"helpi");
            //给手机发送一个通知
            Intent Notificationintent = new Intent(PutHelpIntentService.this,MessageActivity.class);
            PendingIntent pi = PendingIntent.getActivity(PutHelpIntentService.this,0,Notificationintent,0);
            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new android.support.v7.app.NotificationCompat.Builder(PutHelpIntentService.this)
                    .setContentTitle("提示")
                    .setContentText("余成:我在石油大学，可以帮你买黄焖鸡")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.logo_new)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.logo_new))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0,1000,1000,1000})
                    .setLights(Color.GREEN,1000,1000)
                    .setColor(Color.RED)
                    .setDefaults(android.support.v7.app.NotificationCompat.DEFAULT_ALL)
                    .build();
            manager.notify(1,notification);
        }
    }
}
