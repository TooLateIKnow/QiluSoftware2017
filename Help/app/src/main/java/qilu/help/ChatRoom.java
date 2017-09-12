package qilu.help;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;


public class ChatRoom extends AppCompatActivity implements View.OnClickListener{

    private Button ChatRoom_back;
    private Button ChatRoom_sure;

    //用来显示聊天列表,聊天子布局中的控件
    private List<ChatRoomMessage> msgList = new ArrayList<>();
    private EditText inputText;
    private Button send;
    private RecyclerView msgRecyclerView;
    private ChatRoomMessageAdapter adapter;

    //获得聊天对象的用户名
    static public String chatusername;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_activity);

        //显示中间的名字
        TextView chatroonusername = (TextView)findViewById(R.id.ChatRoom_username);
        Intent intent = getIntent();
        chatusername = intent.getStringExtra("extra_data");//获得上面传递过来的数据
        chatroonusername.setText(chatusername);

        init();

        //首先初始化一些消息数据用于测试
        initMsgs();
        sendOrReciveMessage();

    }
    public void init(){
        ChatRoom_back = (Button)findViewById(R.id.ChatRoom_back);
        ChatRoom_sure = (Button)findViewById(R.id.ChatRoom_sure);
        ChatRoom_back.setOnClickListener(this);
        ChatRoom_sure.setOnClickListener(this);

        //显示气泡上的用户名
       /* heName = (TextView)findViewById(R.id.ChatRoom_heName);
        myName = (TextView)findViewById(R.id.ChatRoom_myName);
        heName.setText(chatusername);
        myName.setText("我");*/
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.ChatRoom_back:
                finish();
                break;
            case R.id.ChatRoom_sure:
                //弹出对话框
                new AlertDialog.Builder(this).setTitle("确定这次交易！")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //为了测试，自定义一些记录
                                RecordActivity.addRecord("2010-12-1","青岛市黄岛区石油大学","车胎爆了");
                                //关闭服务，还未关闭HelpCommunity中的GiveHelpIntentService类服务
                            }}).show();
                break;
        }
    }

    //初始化一些聊天信息的数据，用于测试
    public void initMsgs(){
        ChatRoomMessage msg1 = new ChatRoomMessage("你好",ChatRoomMessage.TYPE_RECEIVED);
        msgList.add(msg1);
    }
    //
    public void sendOrReciveMessage(){
        inputText = (EditText)findViewById(R.id.ChatRoom_inputText);
        send = (Button)findViewById(R.id.ChatRoom_send);
        msgRecyclerView = (RecyclerView)findViewById(R.id.ChatRoom_recycle_view);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        msgRecyclerView.setLayoutManager(layoutManager);

        adapter = new ChatRoomMessageAdapter(msgList);
        msgRecyclerView.setAdapter(adapter);


        send.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String content = inputText.getText().toString();
                if(!"".equals(content)){
                    ChatRoomMessage msg = new ChatRoomMessage(content,ChatRoomMessage.TYPE_SENT);
                    msgList.add(msg);
                    adapter.notifyItemInserted(msgList.size()-1);

                    msgRecyclerView.scrollToPosition(msgList.size()-1);
                    inputText.setText("");
                }
            }
        });
    }
}
