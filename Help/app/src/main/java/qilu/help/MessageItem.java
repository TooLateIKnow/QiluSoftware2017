package qilu.help;

/**
 * Created by Y481 on 2017/9/11.
 */

public class MessageItem {
    private String Message_item_name;
    private String Message_item_content;
    /*private String Message_item_incident;*/

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

    /*public void setRecord_item_incident(String Record_item_incident){
        this.Record_item_incident = Record_item_incident;
    }public String getRecord_item_incident(){
        return Record_item_incident;
    }*/
}
