package qilu.help;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Y481 on 2017/9/7.
 */

public class HttpUtil {
    //创建HttpUtil对象
    /*public static String HttpGetMethod (String url,String params)
            throws IOException {
        String result = "";
        BufferedReader in = null;
        try{
            URL url =
        }
    }
*/
    //自定义的POST工具
    public static String HttpPostMethod(String url,String params)
            throws IOException{

        PrintWriter out = null;
        BufferedReader in = null;
        //StringBuilder result = null;
        String result = "";
        try{
            URL realUrl = new URL(url);//设置连接
            HttpURLConnection conn = (HttpURLConnection)realUrl.openConnection();

            conn.setRequestMethod("POST");//设置方法
            conn.setDoOutput(true);//如果是Post方法，这两个必须设置
            conn.setDoInput(true);

            //conn.setUseCaches(false);//设置连接不缓存
            conn.setInstanceFollowRedirects(true);//一些通用的设置
            conn.setRequestProperty("Charset","UTF-8");

            out = new PrintWriter(conn.getOutputStream());//将数据写入流中
            out.print(params);
            out.flush();//发送流

            in = new BufferedReader(new InputStreamReader(conn.getInputStream()));//读取服务器返回的信息
            String line = null;
            while((line=in.readLine())!=null){
                result += line; //用result来存储从服务器返回的数据
                //result.append(line);
            }
            out.close();//关闭流和连接
            conn.disconnect();

        }catch(IOException e){
            System.out.println("连接POST失败");
            e.printStackTrace();
        }
        return result.toString();

    }

}