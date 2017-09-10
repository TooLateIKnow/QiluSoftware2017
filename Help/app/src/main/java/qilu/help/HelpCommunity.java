package qilu.help;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class HelpCommunity extends AppCompatActivity implements View.OnClickListener{
    private Button Community_back;
    private Button Community_puthelp;

    //获取对话框中的控件
    private TextView put_help_Dialog_username;
    private TextView put_help_Dialog_location;
    private TextView put_help_Dialog_date;
    private TextView put_help_Dialog_incident;

    //定义适配器
    HelpCommunityAdapter adapter;

    String helpDate = null;//用于存储当前时间

    static private List<HelpItem> helpItemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity);
        init();
        initHelpItem();//这个方法是将数据添加近list中
        addListToAdapter();
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
                    showInfoMe(view,position,helpItem);
                }else{
                    showInfo(helpItem.getHelp_item_username(),helpItem.getHelp_item_content());
                }

            }
    });
    }

    public void init(){
        Community_back = (Button)findViewById(R.id.Community_back);
        Community_puthelp = (Button)findViewById(R.id.Community_puthelp);
        Community_back.setOnClickListener(this);
        Community_puthelp.setOnClickListener(this);
    }

    //为了演示需要。提前准备好一些数据
    public void initHelpItem(){
        HelpItem TangHelp = new HelpItem();
        TangHelp.setHelp_item_username("汤求毅");
        TangHelp.setHelp_item_content("在石油大学");
        helpItemList.add(TangHelp);

        HelpItem TangHelp1 = new HelpItem();
        TangHelp1.setHelp_item_username("雍振东");
        TangHelp1.setHelp_item_content("在石油大学");
        helpItemList.add(TangHelp1);

    }

    //这个方法完成的功能是，用户点击“发送请求”的时候，能往helpItemList中添加子项
    public void addToHelpItem(String name,String location,String date,String incident,boolean ifmine){
        //为了测试。先把时间地点和时间放在一个字符串中
        StringBuilder helpcontent = new StringBuilder();
        helpcontent.append(location).append(date).append(incident);
        HelpItem helpItem_new = new HelpItem();
        helpItem_new.setHelp_item_username(name);
        helpItem_new.setHelp_item_content(helpcontent.toString());
        helpItem_new.setIfMine(ifmine);
        helpItemList.add(0,helpItem_new);//每次都让数据添加在第一行
    }

    @Override
    public void onClick(final View v) {
        switch (v.getId()) {
            case R.id.Community_back:
                finish();
                break;
            case R.id.Community_puthelp:
                //读取当前系统时间
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日HH:mm:ss");
                Date curDate = new Date(System.currentTimeMillis());//获取当前时间
                helpDate = formatter.format(curDate);

                //弹出对话框
                final TableLayout putHelpDialog = (TableLayout) getLayoutInflater().inflate(R.layout.put_help_dialog, null);
                //对话框的控件添加信息
                put_help_Dialog_username = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_username);
                put_help_Dialog_location = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_location);
                put_help_Dialog_date = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_date);
                put_help_Dialog_incident = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_incident);

                put_help_Dialog_username.setText(MainActivity.sendingname);
                put_help_Dialog_location.setText(MainActivity.sendingCurrentposition);
                put_help_Dialog_date.setText(helpDate);

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
    public void showInfo(final String helpusername,String helpcontent){
        new AlertDialog.Builder(this)
                .setTitle("求救详情")
                .setMessage(helpusername+"   "+helpcontent)//可以加上性别
                .setPositiveButton("给予帮助", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent("qilu.help.ACTION_CHAT_START");
                        intent.putExtra("extra_data",helpusername);
                        startActivity(intent);
                    }
                }).show();
    }
    //ListView 中各子项的事件2：是用户自己发送的
    public void showInfoMe(View view,final int position,final HelpItem helpItem){
        PopupMenu popup = new PopupMenu(HelpCommunity.this,view);
        getMenuInflater().inflate(R.menu.popup_item,popup.getMenu());
        popup.setOnMenuItemClickListener(
                new PopupMenu.OnMenuItemClickListener(){
                    @Override
                    public boolean onMenuItemClick(MenuItem item){
                        switch (item.getItemId()){
                            case R.id.Popup_delete:
                                //删除选中的子项
                                helpItemList.remove(position);
                                adapter.notifyDataSetChanged();//适配器自适应
                                break;
                            case R.id.Popup_update:
                                //修改选中的子项,
                                //弹出对话框
                                final TableLayout putHelpDialog = (TableLayout) getLayoutInflater().inflate(R.layout.put_help_dialog, null);
                                //对话框的控件添加信息
                                put_help_Dialog_username = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_username);
                                put_help_Dialog_location = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_location);
                                put_help_Dialog_date = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_date);
                                put_help_Dialog_incident = (TextView) putHelpDialog.findViewById(R.id.put_help_Dialog_incident);

                                //设置对话框
                                AlertDialog.Builder dialog = new AlertDialog.Builder(HelpCommunity.this);
                                //.setIcon()
                                dialog.setTitle("修改求助信息")
                                        .setView(putHelpDialog)
                                        .setCancelable(true)
                                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
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
                            default:
                                break;
                        }
                        return true;
                    }
                }
        );
        popup.show();
    }
}
