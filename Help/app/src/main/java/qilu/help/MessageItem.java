package qilu.help;

/**
 * Created by Y481 on 2017/9/11.
 */

public class MessageItem {
    private String Message_item_name;
    private String Message_item_content;
    private String Message_item_time;
    private boolean ifFirstClick = false;

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

    public void setIfFirstClick(boolean ifFirstClick){
        this.ifFirstClick = ifFirstClick;
    }public boolean getIfFirstClick(){
        return ifFirstClick;
    }
}
