package qilu.help;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sf.json.JSON;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelpCommunity extends AppCompatActivity implements View.OnClickListener{
    private static final int GET_DATA = 1;

    private Button Community_back;
    private Button Community_puthelp;

    //获取对话框中的控件
    private TextView put_help_Dialog_username;
    private TextView put_help_Dialog_location;
    private TextView put_help_Dialog_date;
    private TextView put_help_Dialog_incident;

    //定义进度条
    private ProgressDialog progressDialog;

    //获得PopupWindow中的控件
    private Button PopupWindow_update;
    private Button PopupWindow_delete;

    //界面下面的三个按钮
    private FloatingActionButton Community_To_Message;
    private FloatingActionButton Community_To_Community;
    private FloatingActionButton Community_To_Record;

    static String helpDate;//用于存储当前时间

    //传输头像(到时候在考虑)
    static public String  imageID;
    static public String helpname;
    static public String heId;

    ////
    static public Context context;

    //JSON数据
    private JSONObject jsonObject;

    //定义适配器
    HelpCommunityAdapter adapter;
    private List<HelpItem> helpItemList = new ArrayList<>();//定义列表

    //异步消息处理机制，方便在子线程中更改UI
    private Handler handler = new Handler(){
        public void handleMessage(Message msg){
            switch (msg.what){
                case GET_DATA:
                    String resultData = (String)msg.obj;
                    dismissProgressDialog();//对话框消失
                    initHelpItem(resultData);//解析JSON并且添加进入list中
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
        setContentView(R.layout.community_activity);
        //读取当前系统时间
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        helpDate = formatter.format(curDate);
        //net.sf.json.JSONObject jsonData = net.sf.json.JSONObject.fromObject(curDate);
        init();
        addListToAdapter();
        showProgressDialog();
        connectToServer(helpDate);//连接服务器
    }
    @Override
    protected void onStart(){
        super.onStart();
    }
    @Override
    protected void onStop(){
        super.onStop();
    }
    @Override
    protected void onResume(){
        super.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        helpItemList.clear();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
    }

    public void showProgressDialog(){//显示进度条对话框
        progressDialog.setTitle("正在获取内容！");
        progressDialog.setMessage("请稍后...");
        progressDialog.setCancelable(true);
        progressDialog.show();
    }
    public void dismissProgressDialog(){//对话框消失
        progressDialog.dismiss();
    }

    //连接服务器
    public void connectToServer(final String data){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    //String urlStr="http://192.168.43.193:50000/HelpServer/loginingservlet";//设置路径
                    String urlStr="http://192.168.43.49:8080/RequestHallServlet";
                    if(!data.equals(helpDate)){//设置路径
                        urlStr="http://192.168.43.49:8080/PublishServlet";
                    }
                    //下面是写入服务器中的数据
                    String params = "help=" + data;
                    String resultData=HttpUtil.HttpPostMethod(urlStr,params);
                    System.out.println(resultData);
                    if (resultData!=null) {
                        System.out.println(resultData);
                        Message message = new Message();//异步机制
                        message.what = GET_DATA;
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
    //将list映射到适配器中，然后将适配器添加给ListView
    public void addListToAdapter(){
        //将ListView中的数据初始化适配器
        adapter = new HelpCommunityAdapter(HelpCommunity.this, R.layout.help_item,helpItemList);
        //adapter
        ListView listView = (ListView)findViewById(R.id.listview_help_item);
        listView.setAdapter(adapter);
        //ListView中各子项的事件
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?>parent,View view,int position,long id){
                HelpItem helpItem = helpItemList.get(position); //获得了那个子项
                if(helpItem.getIfMine()){
                    initPopup(view,position,helpItem);
                }else{
                    showInfo(helpItem.getHelp_item_username(),helpItem.getHelp_item_content(),
                            helpItem.getTouxiang(),helpItem.getReqId(),helpItem.getUserId());
                }

            }
        });
    }

    public void init(){
        Community_To_Message  = (FloatingActionButton)findViewById(R.id.Community_To_Message);
        Community_To_Community = (FloatingActionButton)findViewById(R.id.Community_To_Community);
        Community_To_Record = (FloatingActionButton)findViewById(R.id.Community_To_Record);

        Community_To_Message.setOnClickListener(this);
        Community_To_Community.setOnClickListener(this);
        Community_To_Record.setOnClickListener(this);

        Community_back = (Button)findViewById(R.id.Community_back);
        Community_puthelp = (Button)findViewById(R.id.Community_puthelp);
        Community_back.setOnClickListener(this);
        Community_puthelp.setOnClickListener(this);

        context = getApplicationContext();

        progressDialog = new ProgressDialog(HelpCommunity.this);

    }

    public void initHelpItem(String resultData){
        try{
            JSONArray jsonArray = new JSONArray(resultData);
            for(int i = 0;i<jsonArray.length();i++){
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                HelpItem TangHelp = new HelpItem();
                TangHelp.setUserId(jsonObject.getString("userId"));//发布求助信息的userId
                TangHelp.setHelp_item_username(jsonObject.getString("username"));//发布求助信息的名字
                TangHelp.setHelp_item_location(jsonObject.getString("location"));//求助信息中的地点
                TangHelp.setHelp_item_content(jsonObject.getString("reqInfo"));//求助信息中的事件
                TangHelp.setTouxiang(jsonObject.getString("picnum"));//发布求助信息的图片
                TangHelp.setHelp_item_time(jsonObject.getString("time"));//求助信息中的时间
                TangHelp.setReqId(jsonObject.getString("reqId"));//标记该求助信息的id

                //判断是否是当前用户自己发送的
                if(TangHelp.getUserId().equals(DataBaseTools.queryData("userId"))){//表示是自己发送的
                    TangHelp.setIfMine(true);
                }else{
                    TangHelp.setIfMine(false);
                }
                helpItemList.add(TangHelp);
            }
        }catch(JSONException e){
            e.printStackTrace();
        }
    }

    //这个方法完成的功能是，用户点击“发送请求”的时候，能往helpItemList中添加子项
    //其实这个方法设置的不好
    public void addToHelpItem(String name,String location,String date,String incident,boolean ifmine){
        //为了测试。先把时间地点和时间放在一个字符串中
        HelpItem helpItem_new = new HelpItem();
        helpItem_new.setHelp_item_username(name);
        helpItem_new.setHelp_item_time(date);
        helpItem_new.setHelp_item_location(location);
        helpItem_new.setHelp_item_content(incident);
        helpItem_new.setTouxiang("i");
        helpItem_new.setUserId(DataBaseTools.queryData("userId"));
        helpItem_new.setIfMine(ifmine);
        helpItemList.add(0,helpItem_new);//每次都让数据添加在第一行
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.Community_To_Message:
                Intent Community_To_MessageIntent = new Intent(this,MessageActivity.class);
                startActivity(Community_To_MessageIntent);
                break;
            case R.id.Community_To_Community:
                break;
            case R.id.Community_To_Record:
                Intent Community_To_RecordIntent = new Intent(this,RecordActivity.class);
                startActivity(Community_To_RecordIntent);
                break;
            case R.id.Community_back:
                Intent intent = new Intent(HelpCommunity.this,MainActivity.class);
                startActivity(intent);
                break;
            case R.id.Community_puthelp:

                //弹出对话框
                final TableLayout putHelpDialog = (TableLayout) getLayoutInflater().inflate(R.layout.put_help_dialog, null);
                //对话框的控件添加信息
                put_help_Dialog_username = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_username);
                put_help_Dialog_location = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_location);
                put_help_Dialog_date = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_date);
                put_help_Dialog_incident = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_incident);

                put_help_Dialog_username.setText(DataBaseTools.queryData("name"));//用户名
                put_help_Dialog_location.setText(MainActivity.sendingCurrentposition);//事件
                put_help_Dialog_date.setText(helpDate);//时间

                AlertDialog.Builder dialog = new AlertDialog.Builder(this);
                //.setIcon()
                dialog.setTitle("发布求助信息")
                        .setView(putHelpDialog)
                        .setCancelable(true)
                        .setPositiveButton("发送", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //将求助信息发送出去,发送给addToHelpItem
                                addToHelpItem(put_help_Dialog_username.getText().toString()
                                        , put_help_Dialog_location.getText().toString()
                                        , put_help_Dialog_date.getText().toString()
                                        , put_help_Dialog_incident.getText().toString()
                                        , true);
                                try{
                                    jsonObject = new JSONObject();
                                    jsonObject.put("username", put_help_Dialog_username.getText().toString());
                                    jsonObject.put("location", put_help_Dialog_location.getText().toString());
                                    jsonObject.put("time",put_help_Dialog_date.getText().toString());
                                    jsonObject.put("reqInfo",put_help_Dialog_incident.getText().toString());
                                    jsonObject.put("picnum","i");
                                    jsonObject.put("userId",DataBaseTools.queryData("userId"));
                                }catch (JSONException e){
                                    e.printStackTrace();
                                }
                                //上传到服务器
                                connectToServer(jsonObject.toString());
                                //这里需要刷新页面
                                addListToAdapter();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
                break;
        }
    }

    //ListView 中各子项的事件1：不是用户自己发送的
    public void showInfo(final String helpusername,String helpcontent,final String helptouxiang
                        ,final String helpreqId,final String userId){
        imageID = helptouxiang;
        helpname = helpusername;

        new AlertDialog.Builder(this)
                .setTitle("求救详情")
                .setMessage(helpusername+"   "+helpcontent)//可以加上性别
                .setPositiveButton("给予帮助", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //点击“给予帮助”后，会向服务器发送一条通知，该通知用来通知另一方
                        connectToServer("helpreqId");
                        //点击“给予帮助”,将会向服务器发送请求，但是不会跳转到聊天界面。
                        //聊天界面通过点击消息中的list跳转。
                        Toast.makeText(HelpCommunity.this,"已向对方发送消息！正在等待对方回应",Toast.LENGTH_SHORT).show();
                        //需要把userId：标志点击的那条记录是谁发送的
                        heId = userId;
                        //此处应该打开一个服务，用来监听对方的回应
                        Intent intentserver = new Intent(HelpCommunity.this,
                                                  GiveHelpIntentService.class);
                        intentserver.putExtra("touxiang",helptouxiang);
                        startService(intentserver);
                        //向服务器发送数据
                        try{
                            JSONObject jsonMsg = new JSONObject();
                            jsonMsg.put("Msg","我可以帮助你");
                            jsonMsg.put("userAFHId",DataBaseTools.queryData("userId"));
                            jsonMsg.put("userPHId",userId);
                            jsonMsg.put("picnumAFH","我的头像");
                            jsonMsg.put("picnumPH",helptouxiang);
                            jsonMsg.put("unmAFH",DataBaseTools.queryData("name"));
                            jsonMsg.put("unmPH",helpusername);
                            socketService.send(jsonMsg.toString());
                        }catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                }).show();
    }
    //ListView 中各子项的事件2：是用户自己发送的
    public void initPopup(View v,final int position,final HelpItem helpItem){
        View view= LayoutInflater.from(HelpCommunity.this).inflate(R.layout.popupwindow_layout, null, false);
        PopupWindow_update = (Button)view.findViewById(R.id.PopupWindow_update);
        PopupWindow_delete = (Button)view.findViewById(R.id.PopupWindow_delete);
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画
        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效
        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 300, 0);
        //设置popupWindow里的按钮的事件
        PopupWindow_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //弹出对话框警告用户
                new android.support.v7.app.AlertDialog.Builder(HelpCommunity.this).setTitle("确定删除该条求助！")
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setPositiveButton("确认删除",new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //删除选中的子项
                                helpItemList.remove(position);
                                adapter.notifyDataSetChanged();//适配器自适应
                            }}).show();
                popWindow.dismiss();
            }
        });
        PopupWindow_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //修改选中的子项,
                //弹出对话框
                final TableLayout putHelpDialog = (TableLayout) getLayoutInflater().inflate(R.layout.put_help_dialog, null);
                //对话框的控件添加信息
                put_help_Dialog_username = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_username);
                put_help_Dialog_location = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_location);
                put_help_Dialog_date = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_date);
                put_help_Dialog_incident = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_incident);
                put_help_Dialog_username.setHint("输入用户名");
                put_help_Dialog_location.setHint("输入求助的位置");
                put_help_Dialog_date.setHint("输入求助的地点");
                //设置对话框
                AlertDialog.Builder dialog = new AlertDialog.Builder(HelpCommunity.this);
                //.setIcon()
                dialog.setTitle("修改求助信息")
                        .setView(putHelpDialog)
                        .setCancelable(true)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                //弹出一个对话框要求用户确认
                                new android.support.v7.app.AlertDialog.Builder(HelpCommunity.this).setTitle("确定修改该条求助！")
                                        .setIcon(android.R.drawable.ic_dialog_info)
                                        .setPositiveButton("确认修改",new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                //将修改的内容同步到列表中
                                                helpItem.setHelp_item_username(put_help_Dialog_username.getText().toString());
                                                StringBuilder helpcontent = new StringBuilder();
                                                helpcontent.append(put_help_Dialog_location.getText().toString())
                                                        .append(put_help_Dialog_date.getText().toString())
                                                        .append(put_help_Dialog_incident.getText().toString());
                                                helpItem.setHelp_item_content(helpcontent.toString());
                                                //这里需要刷新页面
                                                //addListToAdapter();
                                            }}).show();
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();
            }
        });

    }
}
