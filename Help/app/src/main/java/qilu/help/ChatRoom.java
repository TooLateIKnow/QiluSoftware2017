package qilu.help;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class ChatRoom extends AppCompatActivity implements View.OnClickListener{

    private Button ChatRoom_back;
    private Button ChatRoom_sure;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chatroom_activity);

        //显示中间的名字
        TextView chatroonusername = (TextView)findViewById(R.id.ChatRoom_username);
        Intent intent = getIntent();
        String username = intent.getStringExtra("extra_data");
        chatroonusername.setText(username);

        init();

    }
    public void init(){
        ChatRoom_back = (Button)findViewById(R.id.ChatRoom_back);
        ChatRoom_sure = (Button)findViewById(R.id.ChatRoom_sure);
        ChatRoom_back.setOnClickListener(this);
        ChatRoom_sure.setOnClickListener(this);
    }
    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.ChatRoom_back:
                finish();
                break;
            case R.id.ChatRoom_sure:
                //为了测试，自定义一些记录
                RecordActivity.addRecord("2010-12-1","青岛市黄岛区石油大学","车胎爆了");
                break;
        }
    }
}
