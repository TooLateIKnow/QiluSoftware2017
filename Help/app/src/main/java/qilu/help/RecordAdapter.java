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
 * Created by Y481 on 2017/9/9.
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
        if(recordItem.getRecord_item_ifIhelpOther()){
            //设置左边的头像是我的头像
            lefttouxiang.setImageURI(imageUri);
            //设置右边的头像是我帮助的人的头像
            righttouxiang.setImageResource(R.drawable.tang);
        }else{
            //设置左边的头像是帮助我的人的头像
            lefttouxiang.setImageResource(R.drawable.helpi);
            //设置右边的头像是我的头像
            righttouxiang.setImageURI(imageUri);
        }
        return view;
    }
}
