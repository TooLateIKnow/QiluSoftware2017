package qilu.help;

import android.util.Log;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Created by tang on 2017/10/15. 操作数据库
 */
public class DataBaseTools {
    static void createTable(){//创建表格
        LitePal.getDatabase();
    }

    static void insertData(String username,String usermail,String usersex,
                           String password,String moiblephone,String userId){//往数据库中插入记录
        User user = new User();
        user.setUsername(username);
        user.setUsermail(usermail);
        user.setUsersex(usersex);
        user.setPassword(password);
        user.setMobilephone(moiblephone);
        user.setUserId(userId);
        user.save();
    }
    static void deleteData(){//删除记录
        DataSupport.deleteAll(User.class);//删除所有数据
    }
    static void upData(int itemId,String itemContent){//更新记录
        User user = new User();
        if(itemId==R.id.nav_setTel){
            user.setMobilephone(itemContent);
        }else if(itemId == R.id.nav_setMail){
            user.setUsermail(itemContent);
        }else if(itemId == R.id.nav_setPersonal){
            user.setUsername(itemContent);
        }
        user.updateAll();
    }
    static String queryData(String ItemID){//查询数据
        List<User> users = DataSupport.select("username","mobilephone","usermail","userId").find(User.class);
        for(User user:users){
            if(ItemID.equals("tel")){
                return user.getMobilephone();
            }else if(ItemID.equals("name")){
                return user.getUsername();
            }else if(ItemID.equals("mail")){
                return user.getUsermail();
            }else if(ItemID.equals("userId")){
                return user.getUserId();
            }
        }
        return null;
    }
}
