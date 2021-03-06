package qilu.help;

/**
 * Created by Y481 on 2017/9/11.
 */

public class MessageItem {
    private String Message_item_name;      //列表中的名字
    private String Message_item_content;   //列表中的内容
    private String Message_item_time;      //列表中的时间
    private String Message_item_touxiang;  //列表中的头像
    private String Message_item_heId;      //列表中的Id

    private boolean ifFirstClick = true;//标记是不是第一次点击
    private boolean ifIhelpOther = false;//标记是不是我帮助了别人

    public void setMessage_item_name(String Message_item_name){
        this.Message_item_name = Message_item_name;
    }public String getMessage_item_name(){
        return Message_item_name;
    }

    public void setMessage_item_content(String Message_item_content){
        this.Message_item_content = Message_item_content;
    }public String getMessage_item_content(){
        return Message_item_content;
    }

    public void setMessage_item_time(String Message_item_time){
        this.Message_item_time = Message_item_time;
    }public String getMessage_item_time(){
        return Message_item_time;
    }

    public void setMessage_item_touxiang(String Message_item_touxiang){
        this.Message_item_touxiang = Message_item_touxiang;
    }public String getMessage_item_touxiang(){return Message_item_touxiang;}

    public void setIfFirstClick(boolean ifFirstClick){
        this.ifFirstClick = ifFirstClick;
    }public boolean getIfFirstClick(){
        return ifFirstClick;
    }

    public void setIfIhelpOther(boolean ifIhelpOther){
        this.ifIhelpOther = ifIhelpOther;
    }public boolean getIfIhelpOther(){return ifIhelpOther;}

    public void setMessage_item_heId(String Message_item_heId){
        this.Message_item_heId = Message_item_heId;
    }public String getMessage_item_heId(){ return Message_item_heId; }
}
