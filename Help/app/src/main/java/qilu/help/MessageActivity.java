package qilu.help;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MessageActivity extends AppCompatActivity implements View.OnClickListener{
    //工具条上的后退按钮
    private Button Message_back;

    //界面跳转按钮
    private FloatingActionButton Message_To_Message;
    private FloatingActionButton Message_To_Community;
    private FloatingActionButton Message_To_Record;

    static private List<MessageItem> MessageItemList = new ArrayList<>();
    //定义适配器
    MessageAdapter adapter;

    //获取点击图片的名字
    static String ClickPictureName;//都是用来传递到下一级的

    static boolean ifIHelpOther;

    static public Context MessageContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

        init();
        addListToAdapter();
    }

    public void init(){
        Message_back = (Button)findViewById(R.id.Message_back);
        Message_back.setOnClickListener(this);

        Message_To_Message = (FloatingActionButton)findViewById(R.id.Message_To_Message);
        Message_To_Community = (FloatingActionButton)findViewById(R.id.Message_To_Community);
        Message_To_Record = (FloatingActionButton)findViewById(R.id.Message_To_Record);
        Message_To_Message.setOnClickListener(this);
        Message_To_Community.setOnClickListener(this);
        Message_To_Record.setOnClickListener(this);

        MessageContext = getApplicationContext();
    }

    //将list映射到适配器中，然后将适配器添加给ListView
    public void addListToAdapter(){
        //将ListView中的数据初始化适配器
        adapter = new MessageAdapter(MessageActivity.this, R.layout.message_item,MessageItemList);
        //adapter
        ListView listView = (ListView)findViewById(R.id.Message_list);
        listView.setAdapter(adapter);
        //ListView中各子项的事件,点击的时候跳转到聊天界面
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent,View view,int position,long id){
                MessageItem messageItem = MessageItemList.get(position); //获得了那个子项

                //跳转的时候，将下一个界面需要的数据都传递过去
                Intent intent = new Intent("qilu.help.ACTION_CHAT_START");
                intent.putExtra("his Name",messageItem.getMessage_item_name());
                intent.putExtra("his Content",messageItem.getMessage_item_content());
                intent.putExtra("his UserId",messageItem.getMessage_item_heId());
                ifIHelpOther = messageItem.getIfIhelpOther();
                ClickPictureName = messageItem.getMessage_item_touxiang();

                messageItem.setIfFirstClick(false);
                startActivity(intent);
                finish();
            }
        });
    }

    //往“消息大厅中添加记录”,这应该是服务器向我发送的信息
    static public void addRecord(String name,String content,
                                 String time,String heId,String imageid){
        MessageItem message = new MessageItem();
        message.setMessage_item_name(name);
        message.setMessage_item_time(time);
        message.setMessage_item_content(content);
        message.setMessage_item_touxiang(imageid);
        if(heId==DataBaseTools.queryData("userId")){
            message.setIfIhelpOther(false);
        }else{
            message.setIfIhelpOther(true);
        }
        message.setMessage_item_heId(heId);
        MessageItemList.add(message);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.Message_To_Message:
                break;
            case R.id.Message_To_Community:
                Intent intent_Message_To_Community = new Intent(MessageActivity.this,HelpCommunity.class);
                startActivity(intent_Message_To_Community);
                break;
            case R.id.Message_To_Record:
                Intent intent_Message_To_Record = new Intent(MessageActivity.this,RecordActivity.class);
                startActivity(intent_Message_To_Record);
                break;
            case R.id.Message_back:
                Intent intent = new Intent(MessageActivity.this,HelpCommunity.class);
                startActivity(intent);
                break;
        }
    }
}
