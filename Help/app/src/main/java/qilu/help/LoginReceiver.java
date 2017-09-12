package qilu.help;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class LoginReceiver extends BroadcastReceiver {

    //当注册成功后激活的一个广播，为整个程序发送“已经登录”的消息
    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.ifLogin = true;
        Toast.makeText(context,"已经成功登录",Toast.LENGTH_SHORT).show();

        HelpCommunity.initHelpItem();//提前在“帮助社区”中添加一些数据，以便用来测试
        MessageActivity.initMessageItem();//提前在“消息大厅”中添加一些数据，以便用来测试
    }
}
