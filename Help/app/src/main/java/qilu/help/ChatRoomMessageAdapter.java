package qilu.help;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Y481 on 2017/9/11.
 */

public class ChatRoomMessageAdapter extends RecyclerView.Adapter<ChatRoomMessageAdapter.ViewHolder>{
    private List<ChatRoomMessage> mMsgList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        LinearLayout leftLayout;
        LinearLayout rightLayout;
        TextView leftMsg;
        TextView rightMsg;

        //表示的是聊天窗口上的用户名
        private TextView myName;
        private TextView heName;

        public ViewHolder(View view){
            super(view);
            leftLayout = (LinearLayout)view.findViewById(R.id.left_message);
            rightLayout = (LinearLayout)view.findViewById(R.id.Right_message);
            leftMsg = (TextView)view.findViewById(R.id.ChatRoom_left_message_I_send);
            rightMsg = (TextView)view.findViewById(R.id.ChatRoom_right_message_He_send);

            myName = (TextView)view.findViewById(R.id.ChatRoom_myName);
            heName = (TextView)view.findViewById(R.id.ChatRoom_heName);
            myName.setText("我:");
            heName.setText(ChatRoom.chatusername+":");
        }
    }

    public ChatRoomMessageAdapter(List<ChatRoomMessage> msgList){
        mMsgList = msgList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent,int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chatroom_message_item_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder,int position){
        ChatRoomMessage msg = mMsgList.get(position);
        if(msg.getType() == ChatRoomMessage.TYPE_RECEIVED){
            //表示收到消息，显示右边的布局
            holder.rightLayout.setVisibility(View.VISIBLE);
            holder.leftLayout.setVisibility(View.GONE);
            holder.rightMsg.setText(msg.getContent());
        }else if(msg.getType() == ChatRoomMessage.TYPE_SENT){
            //表示发送消息，显示左边的布局
            holder.leftLayout.setVisibility(View.VISIBLE);
            holder.rightLayout.setVisibility(View.GONE);
            holder.leftMsg.setText(msg.getContent());
        }
    }

    @Override
    public int getItemCount(){
        return mMsgList.size();
    }
}
