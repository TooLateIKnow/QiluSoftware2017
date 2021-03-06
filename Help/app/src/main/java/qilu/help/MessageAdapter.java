package qilu.help;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static qilu.help.MainActivity.imageUri;

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

        //初始化控件
        TextView Message_username = (TextView)view.findViewById(R.id.Message_username);
        TextView Message_content = (TextView)view.findViewById(R.id.Message_content);
        TextView Message_time = (TextView)view.findViewById(R.id.Message_time);
        Message_username.setText(messageItem.getMessage_item_name());
        Message_content.setText(messageItem.getMessage_item_content());
        Message_time.setText(messageItem.getMessage_item_time());

        //匹配头像
        //其实这个设置头像是需要使用item中的方法的。这里暂且不用，为了测试
        CircleImageView touxiang = (CircleImageView)view.findViewById(R.id.Message_touxiang);
        if(!messageItem.getIfIhelpOther()) {
            if(messageItem.getMessage_item_name().equals("孤独的茶")){
                touxiang.setImageResource(R.drawable.message);
            }else if(messageItem.getMessage_item_name().equals("苏打水")){
                touxiang.setImageResource(R.drawable.message1);
            }else if(messageItem.getMessage_item_name().equals("锦城江风")){
                touxiang.setImageResource(R.drawable.message2);
            }else if(messageItem.getMessage_item_name().equals("盒子先生")){
                touxiang.setImageResource(R.drawable.message3);
            }else{
                touxiang.setImageResource(R.drawable.helpi);
            }
        }else{
            touxiang.setImageResource(getImageResourceId(messageItem.getMessage_item_touxiang()));
        }


        //如果不是第一次点开这个消息，那么就不会有标志
        if(!messageItem.getIfFirstClick()){
            ImageView help_item_tongzhi = (ImageView)view.findViewById(R.id.Message_tongzhi);
            help_item_tongzhi.setVisibility(View.INVISIBLE);
        }

        return view;
    }


    //获取图片的真是地址
    public int getImageResourceId(String name) {
        R.drawable drawables=new R.drawable();
        //默认的id
        int resId=0x7f02000b;
        try {
            //根据字符串字段名，取字段//根据资源的ID的变量名获得Field的对象,使用反射机制来实现的
            java.lang.reflect.Field field=R.drawable.class.getField(name);
            //取值
            resId=(Integer)field.get(drawables);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resId;
    }
}
