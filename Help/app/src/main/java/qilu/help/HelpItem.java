package qilu.help;

/**
 * Created by Y481 on 2017/9/9.
 */

public class HelpItem {
    private String help_item_username;//求助信息中的用户名
    private String help_item_time;    //求助信息中的时间
    private String help_item_location;//求助信息中的地点
    private String help_item_content; //求助信息中的求助内容
    private String touxiang;          //求助信息中的头像
    private String userId;            //用户的ID
    private String reqId;             //列表中的编号
    private boolean ifMine = false;   //标记是否是用户自己发送的

    public void setHelp_item_username(String help_item_username){
        this.help_item_username = help_item_username;
    }public String getHelp_item_username(){
        return help_item_username;
    }

    public void setHelp_item_time(String help_item_time){
        this.help_item_time = help_item_time;
    }public String getHelp_item_time(){return help_item_time;}

    public void setHelp_item_location(String help_item_location){
        this.help_item_location = help_item_location;
    }public String getHelp_item_location(){return help_item_location;}

    public void setHelp_item_content(String help_item_content){
        this.help_item_content = help_item_content;
    }public String getHelp_item_content(){
        return help_item_content;
    }

    public void setTouxiang(String touxiang){
        this.touxiang = touxiang;
    }public String getTouxiang(){
        return touxiang;
    }

    public void setIfMine(boolean ifMine){
        this.ifMine = ifMine;
    }public boolean getIfMine(){
        return ifMine;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }public String getUserId(){ return userId; }

    public void setReqId(String reqId){
        this.reqId = reqId;
    }public String getReqId(){ return reqId; }

}

