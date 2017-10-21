package qilu.help;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import java.io.*;
import java.net.Socket;

public class socketService extends Service {
    //服务一启动就一直连接着
    private static Socket socket;
    private static BufferedWriter bWriter;//����������͡�д����Ϣ
    private static BufferedReader bReader;//�����������ܡ���ȡ��Ϣ

    public socketService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("==================="+"启动服务");
        new Thread(new Runnable() {
            public void run() {
                try {
                    socket = new Socket("192.168.43.49",8081);
                    System.out.print("服务器连接成功！");
                    //写入
                    bWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(),"UTF-8"));
                    //读出
                    bReader = new BufferedReader(new InputStreamReader(socket.getInputStream(),"UTF-8"));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public static void send(String jsonString)
    {
        jsonString += "\n";
        try {
            bWriter.write(jsonString);
            bWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void get(){
        String s;
        try{
            while ((s = bReader.readLine()) != null) {//读取
                if(s.equals("我可以帮助你")){

                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
