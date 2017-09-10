package Controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "LoginingServlet")
public class LoginingServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request,response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //从流中读出数据
        System.out.println("连接登录成功");

        //设置类型
        response.setContentType("text/html;charset=utf-8");
        //设置字符编码集
        response.setCharacterEncoding("utf-8");

        //注意。write啊，Output啊，都是把数据写入流中
        PrintWriter out=response.getWriter();
        String msg = null;
        msg = "登录成功";
        out.print(msg);

        out.close();
    }
}
