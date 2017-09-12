package qilu.help;

/**
 * Created by Y481 on 2017/9/9.
 */

public class HelpItem {
    private String help_item_username;
    private String help_item_time;
    private String help_item_location;
    private String help_item_content;

    private boolean ifMine = false;

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

    public void setIfMine(boolean ifMine){
        this.ifMine = ifMine;
    }public boolean getIfMine(){
        return ifMine;
    }
}

