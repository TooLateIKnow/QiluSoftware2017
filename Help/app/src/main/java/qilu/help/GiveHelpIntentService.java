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

public class GiveHelpIntentService extends IntentService {
    //TODO:该服务在点击“给予帮助”的时候启动
    //TODO:该服务的作用是：启用一个线程实时的连接服务器，将“我的用户名（如果可以的话加上头像）
    //TODO:以及我要施救的人的用户名”上传到服务器。服务器完成的操作是给那个人发送一个通知：
    //TODO:XXX（我的用户名）可以给你提供帮助。当对方接受或者拒绝这个帮助的时候，也会发送一个信号给服务器。
    //TODO:然后这个服务器会根据对方的不同的回应给我发送不同的消息。
    //TODO:如果对方接受，返回accept，这份服务将z会在消息大厅中添加一行记录,并且也会有一个通知
    //TODO:如果对方拒绝，返回reject，这个服务停止，

    //现在为了测试，这个服务的功能就是在启动二十秒之后会给手机发送一个通知。并且在“消息大厅”中添加一条记录

    public GiveHelpIntentService() {
        super("GiveHelpIntentService");
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
            MessageActivity.addRecord("木土土的","我在南教北门 ，下雨了没带伞，希望有人能捎我一程",currentDate,true,true);
            //给手机发送一个通知
            Intent Notificationintent = new Intent(GiveHelpIntentService.this,MessageActivity.class);
            PendingIntent pi = PendingIntent.getActivity(GiveHelpIntentService.this,0,Notificationintent,0);
            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            Notification notification = new android.support.v7.app.NotificationCompat.Builder(GiveHelpIntentService.this)
                    .setContentTitle("提示")
                    .setContentText("木土土的：我在南教北门 ，下雨了没带伞，希望有人能捎我一程")
                    .setWhen(System.currentTimeMillis())
                    .setSmallIcon(R.mipmap.ic_launcher)
                    .setLargeIcon(BitmapFactory.decodeResource(getResources(),R.mipmap.ic_launcher))
                    .setContentIntent(pi)
                    .setAutoCancel(true)
                    .setVibrate(new long[]{0,1000,1000,1000})
                    .setLights(Color.GREEN,1000,1000)
                    .setColor(Color.RED)
                    .setDefaults(android.support.v7.app.NotificationCompat.DEFAULT_ALL)
                    .build();
            manager.notify(1,notification);

            //TODO:这个服务我没有关闭，应该在聊天窗口中确定后关闭
        }
    }

}
