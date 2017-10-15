package qilu.help;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Y481 on 2017/9/26.
 */

public class Info implements Serializable
{
    private static final long serialVersionUID = -758459502806858414L;
    /**
     * 精度
     */
    private double latitude;
    /**
     * 纬度
     */
    private double longitude;
    /**
     * 图片ID，真实项目中可能是图片路径
     */
    private int imgId;
    /**
     * 商家名称
     */
    private String name;
    /**
     * 距离
     */
    private String distance;
    /**
     * 赞数量
     */
    private int zan;

    public static List<Info> infos = new ArrayList<Info>();

    static
    {
        infos.add(new Info(34.242652, 108.971171, R.drawable.button_shape, "人民医院",
                "距离209米", 1456));
        infos.add(new Info(34.242952, 108.972171, R.drawable.ic_menu_camera, "连锁超市",
                "距离897米", 456));
        infos.add(new Info(34.242852, 108.973171, R.drawable.ic_menu_gallery, "希望小学",
                "距离249米", 1456));
        infos.add(new Info(34.242152, 108.971971, R.drawable.ic_menu_send, "原始森林",
                "距离679米", 1456));
        infos.add(new Info(34.242152, 108.971971, R.drawable.ic_menu_send, "中山影院",
                "距离679米", 1456));
    }

    public Info()
    {
    }

    public Info(double latitude, double longitude, int imgId, String name,
                String distance, int zan)
    {
        super();
        this.latitude = latitude;
        this.longitude = longitude;
        this.imgId = imgId;
        this.name = name;
        this.distance = distance;
        this.zan = zan;
    }

    public double getLatitude()
    {
        return latitude;
    }

    public void setLatitude(double latitude)
    {
        this.latitude = latitude;
    }

    public double getLongitude()
    {
        return longitude;
    }

    public void setLongitude(double longitude)
    {
        this.longitude = longitude;
    }

    public String getName()
    {
        return name;
    }

    public int getImgId()
    {
        return imgId;
    }

    public void setImgId(int imgId)
    {
        this.imgId = imgId;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getDistance()
    {
        return distance;
    }

    public void setDistance(String distance)
    {
        this.distance = distance;
    }

    public int getZan()
    {
        return zan;
    }

    public void setZan(int zan)
    {
        this.zan = zan;
    }

}


