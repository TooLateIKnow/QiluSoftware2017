package qilu.help;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import net.sf.json.JSON;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ChatRoom extends AppCompatActivity implements View.OnClickListener{

    private Button ChatRoom_back;//返回键
    private Button ChatRoom_sure;//确定键

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
    static public String chatHeuserId;

    //获得对话框中的控件
    private TextView chatrooom_Dialog_heusername;
    private TextView chatrooom_Dialog_location;
    private TextView chatrooom_Dialog_date;
    private TextView chatrooom_Dialog_incident;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_activity);

        Intent intent = getIntent();
        chatHeusername = intent.getStringExtra("his Name");        //获取对方用户名
        String ChatContent = intent.getStringExtra("his Content") ;//获取聊天内容
        chatHeTouxiang = MessageActivity.ClickPictureName;         //获取对方头像
        chatIfIhelpOther = MessageActivity.ifIHelpOther;           //获取是否我帮助了他
        chatHeuserId = intent.getStringExtra("his UserId");        //获取对方Id

        //显示中间的名字
        TextView chatroonusername = (TextView)findViewById(R.id.ChatRoom_username);
        chatroonusername.setText(chatHeusername);

        init();

        //首先初始化一些消息数据用于测试
        initMsgs(ChatContent);
        sendOrReciveMessage();
    }
    public void init(){
        ChatRoom_back = (Button)findViewById(R.id.ChatRoom_back);
        ChatRoom_sure = (Button)findViewById(R.id.ChatRoom_sure);
        ChatRoom_back.setOnClickListener(this);
        ChatRoom_sure.setOnClickListener(this);
    }
    @Override
    public void onClick(final View v){
        switch(v.getId()){
            case R.id.ChatRoom_back:
                Intent intent = new Intent(ChatRoom.this,MessageActivity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.ChatRoom_sure:
                //弹出对话框
                final TableLayout putHelpDialog = (TableLayout) getLayoutInflater().inflate(R.layout.put_help_dialog, null);
                //对话框的控件添加信息
                chatrooom_Dialog_heusername = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_username);
                chatrooom_Dialog_location = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_location);
                chatrooom_Dialog_date = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_date);
                chatrooom_Dialog_incident = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_incident);
                chatrooom_Dialog_heusername.setText(chatHeusername);
                chatrooom_Dialog_location.setText(MainActivity.sendingCurrentposition);
                chatrooom_Dialog_date.setText(HelpCommunity.helpDate);

                AlertDialog.Builder putIntoRecordDialog = new AlertDialog.Builder(ChatRoom.this);
                putIntoRecordDialog.setTitle("确定这次交易！")
                        .setView(putHelpDialog)
                        .setCancelable(true)
                        .setIcon(android.R.drawable.ic_menu_agenda)
                        .setPositiveButton("确定",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //把数据写成json
                                try{
                                    if(chatIfIhelpOther){
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("userAFHId",DataBaseTools.queryData("userId"));
                                        jsonObject.put("userPHId",chatHeuserId);
                                        jsonObject.put("time",chatrooom_Dialog_date.getText().toString());
                                        jsonObject.put("location",chatrooom_Dialog_location.getText().toString());
                                        jsonObject.put("helpWhat",chatrooom_Dialog_incident.getText().toString());
                                        //应该是把数据上传到服务器
                                        connectToServer(jsonObject.toString(),v);
                                    }else{
                                        JSONObject jsonObject = new JSONObject();
                                        jsonObject.put("userAFHId",chatHeuserId);
                                        jsonObject.put("userPHId",DataBaseTools.queryData("userId"));
                                        jsonObject.put("time",chatrooom_Dialog_date.getText().toString());
                                        jsonObject.put("location",chatrooom_Dialog_location.getText().toString());
                                        jsonObject.put("helpWhat",chatrooom_Dialog_incident.getText().toString());
                                        //应该是把数据上传到服务器
                                        connectToServer(jsonObject.toString(),v);
                                    }
                                }catch(JSONException e){
                                    e.printStackTrace();
                                }
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
    //将历史纪录上传到服务器
    public void connectToServer(final String data,final View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String urlStr="http://192.168.43.49:8080/HistoryServlet";//设置路径
                    //下面是写入服务器中的数据
                    String params = "addRecord=" + data;//给服务器发送一个消息，内容待定
                    String resultData=HttpUtil.HttpPostMethod(urlStr,params);
                    //不管是成功还是失败，都只是做提示
                    Snackbar.make(view,resultData,Snackbar.LENGTH_SHORT)
                            .show();
                }catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //初始化一些聊天信息的数据，用于测试
    public void initMsgs(String Chatcontent){
        ChatRoomMessage msg1 = new ChatRoomMessage(Chatcontent,ChatRoomMessage.TYPE_RECEIVED);
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
