package qilu.help;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

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
        TextView Recorc_item_date = (TextView)view.findViewById(R.id.Recorc_item_date);
        TextView Record_item_location = (TextView)view.findViewById(R.id.Record_item_location);
        TextView Record_item_incident = (TextView)view.findViewById(R.id.Record_item_incident);

        Recorc_item_date.setText("时间："+recordItem.getRecord_item_date());
        Record_item_location.setText("地点："+recordItem.getRecord_item_location());
        Record_item_incident.setText("事件："+recordItem.getRecord_item_incident());

        return view;
    }
}
