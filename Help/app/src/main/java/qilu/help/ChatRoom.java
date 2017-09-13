package qilu.help;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

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

    //获得聊天对象的用户名,头像地址和施救/被救
    static public String chatHeusername;
    static public String chatHeTouxiang;
    static public boolean chatIfIhelpOther;

    //获得对话框中的控件
    private TextView chatrooom_Dialog_heusername;
    private TextView chatrooom_Dialog_location;
    private TextView chatrooom_Dialog_date;
    private TextView chatrooom_Dialog_incident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_activity);

        //显示中间的名字
        TextView chatroonusername = (TextView)findViewById(R.id.ChatRoom_username);
        Intent intent = getIntent();
        chatHeusername = intent.getStringExtra("his Name");//获得上面传递过来的数据
        chatHeTouxiang = intent.getStringExtra("his Touxiang");
        chatIfIhelpOther = intent.getBooleanExtra("if i help Other or not",false);
        chatroonusername.setText(chatHeusername);

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
                //弹出对话框
                final TableLayout putHelpDialog = (TableLayout) getLayoutInflater().inflate(R.layout.put_help_dialog, null);
                //对话框的控件添加信息
                chatrooom_Dialog_heusername = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_username);
                chatrooom_Dialog_location = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_location);
                chatrooom_Dialog_date = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_date);
                chatrooom_Dialog_incident = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_incident);
                AlertDialog.Builder putIntoRecordDialog = new AlertDialog.Builder(ChatRoom.this);
                putIntoRecordDialog.setTitle("确定这次交易！")
                        .setView(putHelpDialog)
                        .setCancelable(true)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //往记录中添加纪录
                                RecordActivity.addRecord(chatHeusername, chatHeTouxiang, chatIfIhelpOther
                                        ,chatrooom_Dialog_date.getText().toString()
                                        ,chatrooom_Dialog_location.getText().toString()
                                        ,chatrooom_Dialog_incident.getText().toString());
                            }})
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                putIntoRecordDialog.show();
                break;
        }
    }

    //初始化一些聊天信息的数据，用于测试
    public void initMsgs(){
        ChatRoomMessage msg1 = new ChatRoomMessage("你好",ChatRoomMessage.TYPE_RECEIVED);
        msgList.add(msg1);
    }

    //点击发送之后就可以发送一条消息
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
