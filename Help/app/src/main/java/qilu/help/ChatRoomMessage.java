package qilu.help;

/**
 * Created by Y481 on 2017/9/11.
 */

public class ChatRoomMessage {
    public static final int TYPE_RECEIVED = 0;//标志接收消息
    public static final int TYPE_SENT=1;//标志发送消息

    private String content;
    private int type;
    private String username;
    private String touxiang;

    public ChatRoomMessage(String content,int type/*,String username*/){
        this.content = content;
        this.type = type;
        /*this.username = username;*/
    }

    public void setContent(String content){
        this.content = content;
    }public String getContent(){
        return content;
    }

    public int getType(){
        return type;
    }

    public void setTouxiang(String touxiang){
        this.touxiang = touxiang;
    }public String gettouxiang(){return touxiang;}

    public void setUsername(String username){
        this.username = username;
    }public String getUsername(){return username;}



}
