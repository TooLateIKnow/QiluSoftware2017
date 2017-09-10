package Controller;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

//导入JSON需要的包
import net.sf.json.JSONObject;
import com.alibaba.fastjson.JSON;

public class LoginServlet extends HttpServlet {

    private static String username;
    private static String mobilephone;
    private static String usermail;
    private static String password;

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String mobilephone = request.getParameter("mobilephone");
        String usermail = request.getParameter("usermail");
        String usersex = request.getParameter("usersex");
        String password = request.getParameter("password");
        //从流中读出数据
        System.out.println("连接成功");

        //设置类型
        response.setContentType("text/html;charset=utf-8");
        //设置字符编码集
        response.setCharacterEncoding("utf-8");

        //注意。write啊，Output啊，都是把数据写入流中
        PrintWriter out=response.getWriter();
        String msg = null;
        msg = "注册成功";
        out.print(msg);

        out.close();
    }
}
