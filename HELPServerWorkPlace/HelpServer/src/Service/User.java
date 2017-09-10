package Service;

public class User {//存储一个用户的JavaBean

    private String username;//定义用户名
    private String mobilephone;//定义手机
    private String usermail;//定义邮箱
    private String usersex;//定义性别
    private String password;//定义密码

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

    public void serPassword(String password){//password的get和set方法
        this.password = password;
    }
    public String getPassword(){
        return password;
    }
}
