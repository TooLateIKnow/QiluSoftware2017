package qilu.help;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener{

    //在聊天室点击确定才会把数据添加进来
    static private List<RecordItem> recordItemList = new ArrayList<>();

    //界面跳转按钮
    private FloatingActionButton Record_To_Message;
    private FloatingActionButton Record_To_Community;
    private FloatingActionButton Record_To_Record;

    //工具条上的按钮
    public Button Record_back;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);

        init();

        RecordAdapter adapter = new RecordAdapter(RecordActivity.this,
                R.layout.record_item,recordItemList);

        ListView recordList = (ListView)findViewById(R.id.Record_list);
        recordList.setAdapter(adapter);
    }
    static public void addRecord(String date,String location,String incident){
        RecordItem record = new RecordItem();
        record.setRecord_item_date(date);
        record.setRecord_item_location(location);
        record.setRecord_item_incident(incident);
        recordItemList.add(record);
    }

    public void init(){
        Record_To_Message = (FloatingActionButton)findViewById(R.id.Record_To_Message);
        Record_To_Community = (FloatingActionButton)findViewById(R.id.Record_To_Community);
        Record_To_Record = (FloatingActionButton)findViewById(R.id.Record_To_Record);
        Record_To_Message.setOnClickListener(this);
        Record_To_Community.setOnClickListener(this);
        Record_To_Record.setOnClickListener(this);

        Record_back = (Button)findViewById(R.id.Record_back);
        Record_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.Record_back:
                finish();
                break;
            case R.id.Record_To_Message:
                Intent intent_Record_To_Message = new Intent(RecordActivity.this,MessageActivity.class);
                startActivity(intent_Record_To_Message);
                break;
            case R.id.Record_To_Community:
                Intent intent_Record_To_Community = new Intent(RecordActivity.this,HelpCommunity.class);
                startActivity(intent_Record_To_Community);
                break;
            case R.id.Record_To_Record:
                break;
        }
    }

    /*public void addToList(){
        RecordAdapter adapter = new RecordAdapter(RecordActivity.this,
                R.layout.record_item,recordItem);

        ListView recordList = (ListView)findViewById(R.id.Record_list);
        recordList.setAdapter(adapter);
    }*/
}
