package qilu.help;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class HelpCommunity extends AppCompatActivity implements View.OnClickListener{
    private Button Community_back;
    private Button Community_puthelp;

    private List<HelpItem> helpItemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.community_activity);

        init();
        initHelpItem();

        HelpCommunityAdapter adapter = new HelpCommunityAdapter(HelpCommunity.this, R.layout.help_item,helpItemList);
        ListView listView = (ListView)findViewById(R.id.listview_help_item);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
           @Override
            public void onItemClick(AdapterView<?>parent,View view,int position,long id){
               HelpItem helpItem = helpItemList.get(position);
               showInfo(helpItem.getHelp_item_username(),helpItem.getHelp_item_content());
           }
        });
    }

    public void init(){
        Community_back = (Button)findViewById(R.id.Community_back);
        Community_puthelp = (Button)findViewById(R.id.Community_puthelp);

        Community_back.setOnClickListener(this);
        Community_puthelp.setOnClickListener(this);
    }

    public void initHelpItem(){//为了演示需要。提前准备好一些数据
        for(int i = 0;i<3;i++){
            HelpItem TangHelp = new HelpItem();
            TangHelp.setHelp_item_username("汤求毅");
            TangHelp.setHelp_item_content("在石油大学");
            helpItemList.add(TangHelp);

            HelpItem TangHelp1 = new HelpItem();
            TangHelp1.setHelp_item_username("雍振东");
            TangHelp1.setHelp_item_content("在石油大学");
            helpItemList.add(TangHelp1);

            HelpItem TangHelp2 = new HelpItem();
            TangHelp2.setHelp_item_username("龙强");
            TangHelp2.setHelp_item_content("在石油大学");
            helpItemList.add(TangHelp2);

            HelpItem TangHelp3 = new HelpItem();
            TangHelp3.setHelp_item_username("汪小琪");
            TangHelp3.setHelp_item_content("在石油大学");
            helpItemList.add(TangHelp3);

            HelpItem TangHelp4 = new HelpItem();
            TangHelp4.setHelp_item_username("伍程斌");
            TangHelp4.setHelp_item_content("在石油大学");
            helpItemList.add(TangHelp4);

            HelpItem TangHelp5 = new HelpItem();
            TangHelp5.setHelp_item_username("栾小康");
            TangHelp5.setHelp_item_content("在石油大学");
            helpItemList.add(TangHelp5);
        }

    }

    @Override
    public void onClick(View v){
        switch (v.getId()){
            case R.id.Community_back:
                finish();
                break;
            case R.id.Community_puthelp:
                //弹出自定义对话框
                break;
        }
    }

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
}
