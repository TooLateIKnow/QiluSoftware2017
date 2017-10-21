package qilu.help;

/**
 * Created by Y481 on 2017/9/9.
 */

public class RecordItem {
    private String Record_left_name;      //左边的用户名
    private String Record_item_AFHuserId; //施救ID(左边)
    private String Record_left_touxiang;  //左边的头像

    private String Record_right_name;     //右边的用户名
    private String Record_right_touxiang; //右边的头像
    private String Record_item_PHuserId;  //被救ID(右边)

    private String Record_item_date;      //记录的时间
    private String Record_item_location;  //记录的地点
    private String Record_item_incident;  //记录的事件

    private Boolean Record_item_ifIhelpOther;  //标记是否帮助了对方


    public void setRecord_item_date(String Record_item_date){
        this.Record_item_date = Record_item_date;
    }public String getRecord_item_date(){
        return Record_item_date;
    }

    public void setRecord_item_location(String Record_item_location){
        this.Record_item_location = Record_item_location;
    }public String getRecord_item_location(){
        return Record_item_location;
    }

    public void setRecord_item_incident(String Record_item_incident){
        this.Record_item_incident = Record_item_incident;
    }public String getRecord_item_incident(){
        return Record_item_incident;
    }

    public void setRecord_left_name(String Record_left_name){
        this.Record_left_name = Record_left_name;
    }public String getRecord_left_name(){return Record_left_name;}

    public void setRecord_right_name(String Record_right_name){
        this.Record_right_name = Record_right_name;
    }public String getRecord_right_name(){return Record_right_name;}

    public void setRecord_left_touxiang(String Record_left_touxiang){
        this.Record_left_touxiang = Record_left_touxiang;
    }public String getRecord_left_touxiang(){return Record_left_touxiang;}

    public void setRecord_right_touxiang(String Record_right_touxiang){
        this.Record_right_touxiang = Record_right_touxiang;
    }public String getRecord_right_touxiang(){return Record_right_touxiang;}

    public void setRecord_item_ifIhelpOther(boolean Record_item_ifIhelpOther){
        this.Record_item_ifIhelpOther = Record_item_ifIhelpOther;
    }public boolean getRecord_item_ifIhelpOther(){return Record_item_ifIhelpOther;}

    public void setRecord_item_AFHuserId(String Record_item_AFHuserId){
        this.Record_item_AFHuserId = Record_item_AFHuserId;
    }public String getRecord_item_AFHuserId(){return Record_item_AFHuserId;}

    public void setRecord_item_PHuserId(String Record_item_PHuserId){
        this.Record_item_PHuserId = Record_item_PHuserId;
    }public String getRecord_item_PHuserId(){
        return Record_item_PHuserId;
    }
}
