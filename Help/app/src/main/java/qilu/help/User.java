package qilu.help;

import org.litepal.crud.DataSupport;

/**
 * Created by Y481 on 2017/9/7.
 */

public class User extends DataSupport {
    private String username;//定义用户名
    private String mobilephone;//定义手机
    private String usermail;//定义邮箱
    private String usersex;//定义性别
    private String password;//定义密码
    private String userId;//用户的ID

    public User(){

    }
    public User(String username,String password,String usermail,String usersex,String mobilephone){
        this.username = username;
        this.password = password;
        this.usermail = usermail;
        this.usersex = usersex;
        this.mobilephone = mobilephone;
    }

    public void setUsername(String username){//username的get和set方法
        this.username = username;
    }
    public String getUsername(){
        return username;
    }

    public void setMobilephone(String mobilephone){//mobliephone的get和set方法
        this.mobilephone = mobilephone;
    }
    public String getMobilephone(){
        return mobilephone;
    }

    public void setUsermail(String usermail){//usermail的get和set方法
        this.usermail = usermail;
    }
    public String getUsermail(){
        return usermail;
    }

    public void setUsersex(String usersex){//usersex的get和set方法
        this.usersex = usersex;
    }
    public String getUsersex(){
        return usersex;
    }

    public void setPassword(String password){//password的get和set方法
        this.password = password;
    }
    public String getPassword(){
        return password;
    }

    public void setUserId(String userId){ this.userId = userId;}
    public String getUserId(){ return userId; }
}
