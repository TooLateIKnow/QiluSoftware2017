package qilu.help;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity {

    //在聊天室点击确定才会把数据添加进来
    static private List<RecordItem> recordItemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);
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

    /*public void addToList(){
        RecordAdapter adapter = new RecordAdapter(RecordActivity.this,
                R.layout.record_item,recordItem);

        ListView recordList = (ListView)findViewById(R.id.Record_list);
        recordList.setAdapter(adapter);
    }*/
}
