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

    public ChatRoomMessage(String content,int type/*,String username*/){
        this.content = content;
        this.type = type;
        /*this.username = username;*/
    }

    public String getContent(){
        return content;
    }

    public int getType(){
        return type;
    }

    public String getUsername(){return username;}
}
