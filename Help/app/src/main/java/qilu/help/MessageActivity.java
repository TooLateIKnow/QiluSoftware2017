package qilu.help;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_activity);

        init();
        initMessageItem();//这个方法是将数据添加近list中
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
    }

    //为了演示需要。提前准备好一些数据
    public void initMessageItem(){
        MessageItem item_one = new MessageItem();
        item_one.setMessage_item_name("汤求毅");
        item_one.setMessage_item_content("在石油大学");
        MessageItemList.add(item_one);

        MessageItem item_two = new MessageItem();
        item_two.setMessage_item_name("雍振东");
        item_two.setMessage_item_content("在石油大学");
        MessageItemList.add(item_two);

    }
    //将list映射到适配器中，然后将适配器添加给ListView
    public void addListToAdapter(){
        //将ListView中的数据初始化适配器
        adapter = new MessageAdapter(MessageActivity.this, R.layout.message_item,MessageItemList);
        //adapter
        ListView listView = (ListView)findViewById(R.id.Message_list);
        listView.setAdapter(adapter);
        //ListView中各子项的事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent,View view,int position,long id){
                MessageItem messageItem = MessageItemList.get(position); //获得了那个子项
                Intent intent = new Intent("qilu.help.ACTION_CHAT_START");
                intent.putExtra("extra_data",messageItem.getMessage_item_name());
                startActivity(intent);
            }
        });
    }

    //往“消息大厅中添加记录”
    static public void addRecord(String name,String content){
        MessageItem message = new MessageItem();
        message.setMessage_item_name(name);
        message.setMessage_item_content(content);
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
                finish();
                break;
        }
    }
}
