package qilu.help;

import android.app.AlertDialog;
import android.content.Context;
import android.content.pm.LabeledIntent;
import android.graphics.Bitmap;
import android.net.Uri;
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

import static qilu.help.MainActivity.imageUri;

/**
 * Created by tang on 2017/9/9.
 */

public class HelpCommunityAdapter extends ArrayAdapter<HelpItem> {
    private int resourceId;

    static public Uri HisHeadImageUri;//标记对方的头像

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
        if(helpItem.getTouxiang().equals("a")){
            touxiang.setImageResource(R.drawable.tang);
        }else if(helpItem.getTouxiang().equals("b")){
            touxiang.setImageResource(R.drawable.tang1);
        }else if(helpItem.getTouxiang().equals("c")){
            touxiang.setImageResource(R.drawable.tang2);
        }else if(helpItem.getTouxiang().equals("d")){
            touxiang.setImageResource(R.drawable.tang3);
        }else if(helpItem.getTouxiang().equals("i")){
            Bitmap bm = MainActivity.compressBitmap(null, null, HelpCommunity.context,imageUri, 4, false);
            touxiang.setImageBitmap(bm);
        }

        ImageView help_item_tongzhi = (ImageView)view.findViewById(R.id.help_item_tongzhi);//通知，这个界面其实不需要
        help_item_tongzhi.setVisibility(View.INVISIBLE);

        return view;
    }
    public int getImageResourceId(String name) {//获得图片源
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
