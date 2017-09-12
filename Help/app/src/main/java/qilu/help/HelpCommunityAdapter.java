package qilu.help;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.LabeledIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

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
        TextView help_item_time = (TextView)view.findViewById(R.id.help_item_time);
        TextView help_item_location = (TextView)view.findViewById(R.id.help_item_location);
        help_item_username.setText(helpItem.getHelp_item_username());
        help_item_content.setText(helpItem.getHelp_item_content());
        help_item_time.setText(helpItem.getHelp_item_time());
        help_item_location.setText(helpItem.getHelp_item_location());

        //匹配头像控件
        CircleImageView touxiang = (CircleImageView)view.findViewById(R.id.help_item_touxiang);
        if(helpItem.getHelp_item_username().equals("木土土的")){
            touxiang.setImageResource(R.drawable.tang);
        }else if(helpItem.getHelp_item_username().equals("蓦然飞跃")){
            touxiang.setImageResource(R.drawable.tang1);
        }else if(helpItem.getHelp_item_username().equals("荡荡")){
            touxiang.setImageResource(R.drawable.tang2);
        }else if(helpItem.getHelp_item_username().equals("高富帅")){
            touxiang.setImageResource(R.drawable.tang3);
        }else if(helpItem.getHelp_item_username().equals("印团")){
            touxiang.setImageResource(R.drawable.tang4);
        }

        ImageView help_item_tongzhi = (ImageView)view.findViewById(R.id.help_item_tongzhi);
        help_item_tongzhi.setVisibility(View.INVISIBLE);

        return view;
    }
}
