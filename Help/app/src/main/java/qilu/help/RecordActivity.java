package qilu.help;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecordActivity extends AppCompatActivity implements View.OnClickListener{
    private static final int GET_RECORD = 1;
    //每次进入聊天室都会读取数据
    static private List<RecordItem> recordItemList = new ArrayList<>();
    //界面跳转按钮
    private FloatingActionButton Record_To_Message;
    private FloatingActionButton Record_To_Community;
    private FloatingActionButton Record_To_Record;

    //工具条上的按钮
    public Button Record_back;

    //异步消息处理机制，方便在子线程中更改UI
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case GET_RECORD:
                    String resultData = (String)msg.obj;
                    initRecordItem(resultData);//解析JSON并且添加进入list中
                    addListToAdapter();
                    break;
                default:
                    break;
            }

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.record_layout);
        init();
        addListToAdapter();
        connectToServer(DataBaseTools.queryData("userId"));//连接服务器读取个人的历史纪录
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        recordItemList.clear();//每一次退出就清空列表
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
    //添加适配器
    public void addListToAdapter(){
        //将ListView中的数据初始化适配器
        RecordAdapter adapter = new RecordAdapter(RecordActivity.this, R.layout.record_item,recordItemList);
        //adapter
        ListView recordList = (ListView)findViewById(R.id.Record_list);
        recordList.setAdapter(adapter);
    }
    //将读取的数据显示。
    public void initRecordItem(String resultData){
        //把json的数据解析出来添加在这里
        System.out.println("+++++++++++++++++++"+resultData);
        try{
            JSONArray jsonArray = new JSONArray(resultData);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                RecordItem recordItem = new RecordItem();
                recordItem.setRecord_item_AFHuserId(jsonObject.getString("userAFHId"));
                System.out.println("============================"+DataBaseTools.queryData("userId"));
                recordItem.setRecord_item_PHuserId(jsonObject.getString("userPHId"));
                if(recordItem.getRecord_item_AFHuserId()==DataBaseTools.queryData("userId")){
                    recordItem.setRecord_item_ifIhelpOther(true);
                    recordItem.setRecord_left_name(jsonObject.getString("unmAFH"));
                    recordItem.setRecord_right_name(jsonObject.getString("unmPH"));
                    recordItem.setRecord_left_touxiang(jsonObject.getString("picnumAFH"));//我的头像
                    recordItem.setRecord_right_touxiang(jsonObject.getString("picnumPH"));
                }else{
                    //如果是别人帮助了我
                    recordItem.setRecord_item_ifIhelpOther(false);
                    recordItem.setRecord_left_name(jsonObject.getString("unmAFH"));
                    recordItem.setRecord_right_name(jsonObject.getString("unmPH"));
                    recordItem.setRecord_left_touxiang(jsonObject.getString("picnumAFH"));
                    recordItem.setRecord_right_touxiang(jsonObject.getString("picnumPH"));//我的头像
                }
                recordItem.setRecord_item_location(jsonObject.getString("location"));
                recordItem.setRecord_item_incident(jsonObject.getString("helpWhat"));
                recordItem.setRecord_item_date(jsonObject.getString("time"));
                recordItemList.add(recordItem);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    //连接服务器
    public void connectToServer(final String data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //String urlStr="http://192.168.43.193:50000/HelpServer/loginingservlet";//设置路径
                    String urlStr="http://192.168.43.49:8080/ShowHistoryServlet";
                    //下面是写入服务器中的数据
                    String params = "getRecord=" + data;//给服务器发送一个消息，内容待定
                    String resultData=HttpUtil.HttpPostMethod(urlStr,params);
                    System.out.println("=================="+resultData);
                    if (!(resultData==null)) {
                        Message message = new Message();//异步机制
                        message.what = GET_RECORD;
                        message.obj = resultData;//将服务器返回的json字符串传递出去
                        handler.sendMessage(message);//子线程中修改UI
                    }else{
                    }
                }catch(IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.Record_back:
                finish();
                break;
            case R.id.Record_To_Message:
                Intent intent_Record_To_Message = new Intent(RecordActivity.this, MessageActivity.class);
                startActivity(intent_Record_To_Message);
                break;
            case R.id.Record_To_Community:
                Intent intent_Record_To_Community = new Intent(RecordActivity.this, HelpCommunity.class);
                startActivity(intent_Record_To_Community);
                break;
            case R.id.Record_To_Record:
                break;
        }
    }
}
