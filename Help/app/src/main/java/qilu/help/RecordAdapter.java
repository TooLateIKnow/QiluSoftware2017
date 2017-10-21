package qilu.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static qilu.help.MainActivity.imageUri;
import static qilu.help.R.id.Recorc_item_date;

/**
 * Created by tang on 2017/9/9.
 */

public class RecordAdapter extends ArrayAdapter<RecordItem> {
    private int resourceId;
    public RecordAdapter(Context context, int textVieWResourceId, List<RecordItem> objects){
        super(context,textVieWResourceId,objects);
        resourceId = textVieWResourceId;
    }
    @Override
    public View getView(int position, View converView, ViewGroup parent){
        RecordItem recordItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        TextView Record_item_date = (TextView)view.findViewById(Recorc_item_date);
        TextView Record_item_location = (TextView)view.findViewById(R.id.Record_item_location);
        TextView Record_item_incident = (TextView)view.findViewById(R.id.Record_item_incident);
        TextView Record_item_left_name = (TextView)view.findViewById(R.id.Record_leftName);
        TextView Record_item_right_name = (TextView)view.findViewById(R.id.Record_rightName);
        Record_item_date.setText(recordItem.getRecord_item_date());
        Record_item_location.setText(recordItem.getRecord_item_location());
        Record_item_incident.setText(recordItem.getRecord_item_incident());
        Record_item_left_name.setText(recordItem.getRecord_left_name());
        Record_item_right_name.setText(recordItem.getRecord_right_name());

        //头像控件
        CircleImageView lefttouxiang = (CircleImageView)view.findViewById(R.id.Record_Lefttouxiang);
        CircleImageView righttouxiang = (CircleImageView)view.findViewById(R.id.Record_Righttouxiang);
        if(recordItem.getRecord_item_ifIhelpOther()){//TODO:如果我帮助了别人
            //设置左边的头像是我的头像
            lefttouxiang.setImageURI(imageUri);
            //设置右边的头像是对方的头像
            righttouxiang.setImageResource(getImageResourceId(recordItem.getRecord_right_touxiang()));
        }else{//TODO:如果别人帮助了我
            //设置左边的头像是对方的头像
            lefttouxiang.setImageResource(getImageResourceId(recordItem.getRecord_left_touxiang()));
            //设置右边的头像是我的头像
            righttouxiang.setImageURI(imageUri);
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
