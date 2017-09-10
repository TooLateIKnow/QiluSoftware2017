package qilu.help;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Y481 on 2017/9/9.
 */

public class HelpCommunityAdapter extends ArrayAdapter<HelpItem> {
    private int resourceId;
    public HelpCommunityAdapter(Context context,int textVieWResourceId,List<HelpItem> objects){
        super(context,textVieWResourceId,objects);
        resourceId = textVieWResourceId;
    }
    @Override
    public View getView(int position,View converView,ViewGroup parent){
        HelpItem helpItem = getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId,parent,false);

        TextView help_item_username = (TextView)view.findViewById(R.id.help_item_username);
        TextView help_item_content = (TextView)view.findViewById(R.id.help_item_content);

        help_item_username.setText(helpItem.getHelp_item_username());
        help_item_content.setText(helpItem.getHelp_item_content());
        return view;
    }
}
