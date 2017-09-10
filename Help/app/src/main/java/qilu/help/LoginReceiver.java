package qilu.help;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;


public class LoginReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        MainActivity.ifLogin = true;
        Toast.makeText(context,"已经成功登录",Toast.LENGTH_SHORT).show();
    }
}
