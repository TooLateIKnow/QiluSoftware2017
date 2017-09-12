package qilu.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Y481 on 2017/9/11.
 */

public class MessageAdapter extends ArrayAdapter<MessageItem> {
    private int resourceId;
    public MessageAdapter(Context context, int textVieWResourceId, List<MessageItem> objects){
        super(context,textVieWResourceId,objects);
        resourceId = textVieWResourceId;
    }
    @Override
    public View getView(int position, View converView, ViewGroup parent){
        MessageItem messageItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        TextView Message_username = (TextView)view.findViewById(R.id.Message_username);
        TextView Message_content = (TextView)view.findViewById(R.id.Message_content);
        Message_username.setText("用户"+messageItem.getMessage_item_name());
        Message_content.setText(messageItem.getMessage_item_content()+"将给予您帮助");

        return view;
    }
}
