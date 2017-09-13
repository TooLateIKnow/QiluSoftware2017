package qilu.help;

/**
 * Created by Y481 on 2017/9/9.
 */

public class RecordItem {
    private String Record_left_name;
    private String Record_right_name;
    private String Record_left_touxiang;
    private String Record_right_touxiang;

    private String Record_item_date;
    private String Record_item_location;
    private String Record_item_incident;

    private boolean Record_item_ifIhelpOther;

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
}
