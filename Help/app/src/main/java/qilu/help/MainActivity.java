package qilu.help;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import net.sf.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener {

    public static  boolean ifLogin = false;
    public static  boolean ifSign = false;
    private boolean ifupdata = false;

    private boolean fabOpened = false;
    private MapView mapView;
    public LocationClient mLocationClient; //
    private BaiduMap baiduMap;
    private boolean isFirstLocate = true; //判断是不是第一次定位

    public static final int TAKE_PHOTO = 1;
    public static final int CHOOSE_PHOTO = 2;
    private CircleImageView picture;

    static public Uri imageUri;//存放拍照后的图片地址00

    static private TextView name;    static public String sendingname = null;//用来传递用户名
    static private TextView tel;
    static private TextView mail;

    private FloatingActionButton OpenPhone;
    private Button OpenShare;
    private Button OpenShortMessage;


    private StringBuilder currentPosition;  static public String sendingCurrentposition = null;
    private String sosText; //用来生成求助文本

    private boolean ifLayerOpen = false;
    private Button school;
    private Button carmer;
    private Button hospital;
    private Button station;
    private Button forest;
    private Button market;
    private TextView layerName;
    private TextView layerContent;
    private ImageView layerPicture;

    private JSONObject updataJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getApplicationContext());//用于显示地图
        setContentView(R.layout.activity_main);

        //百度地图控件
        mapView = (MapView)findViewById(R.id.bmapView);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);

        //自定义工具条
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //权限申请
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(new MyLocationListener());//注册了一个定位监听器
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.INTERNET);
        }
        if (!permissionList.isEmpty()){
            String[]permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(MainActivity.this,permissions,1);
        }else{
            requestLocation();
        }

        //这一块是悬浮按钮事件
        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!fabOpened){
                    openMenu(view);
                }
                else{
                    closeMenu(view);
                }
            }
        });
        //获取滑动窗口
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        //获取Navigation以及上面的控件
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headView = navigationView.inflateHeaderView(R.layout.nav_header_main);
        picture = (CircleImageView) headView.findViewById(R.id.head_CircleImageView);
        name = (TextView) headView.findViewById(R.id.name);
        sendingname = name.getText().toString();//用来将用户的用户名传递
        tel = (TextView) headView.findViewById(R.id.tel);
        mail = (TextView) headView.findViewById(R.id.mail);
        navigationView.setNavigationItemSelectedListener(this);

        //获取三个控件
        OpenPhone = (FloatingActionButton)findViewById(R.id.OpenPhone);
        OpenPhone.setOnClickListener(this);
        OpenShare = (Button)findViewById(R.id.OpenShare);
        OpenShare.setOnClickListener(this);
        OpenShortMessage = (Button)findViewById(R.id.OpenShortMessage);
        OpenShortMessage.setOnClickListener(this);

        //读取文件
        save();
        load();
        //readFromDatabase();//将数据库中的数据读出来

        school = (Button)findViewById(R.id.School);
        carmer = (Button)findViewById(R.id.Carmer);
        hospital = (Button)findViewById(R.id.Hospital);
        station = (Button)findViewById(R.id.Station);
        forest = (Button)findViewById(R.id.Forest);
        market = (Button)findViewById(R.id.Market);

        school.setOnClickListener(this);
        carmer.setOnClickListener(this);
        hospital.setOnClickListener(this);
        station.setOnClickListener(this);
        forest.setOnClickListener(this);
        market.setOnClickListener(this);

        updataJsonObject = new JSONObject();//当修改数据的时候，通过JSON的方式修改
    }

    //实现实时定位
    private void requestLocation(){
        LocationClientOption option = new LocationClientOption();//这四句是实现实时定位
        option.setScanSpan(5000);
        option.setIsNeedAddress(true);
        mLocationClient.setLocOption(option);

        mLocationClient.start();
    }

    @Override//申请权限的方法
    public void onRequestPermissionsResult(int requestCode,String[]permissions,int[]grantResults){
        switch(requestCode){
            case 1:
                if(grantResults.length>0){
                    for(int result:grantResults){
                        if(result!=PackageManager.PERMISSION_GRANTED){
                            Toast.makeText(this,"同意授权才能正常使用功能",Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }else {
                            openAlbum();
                        }
                    }
                    requestLocation();
                }else{
                    Toast.makeText(this,"发生未知错误",Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    //显示地图和当前位置
    public class MyLocationListener implements BDLocationListener {//定位监听器，需要先注册才能使用方法
        @Override
        public void onReceiveLocation(BDLocation location){
            //获得当前详细方位
            getCurrebtPosition(location);

            if(location.getLocType()==BDLocation.TypeGpsLocation||
                    location.getLocType()==BDLocation.TypeNetWorkLocation){
                if (isFirstLocate) {
                    LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
                    MapStatusUpdate update = MapStatusUpdateFactory.newLatLng(ll);
                    baiduMap.animateMapStatus(update);
                    isFirstLocate = false;
                }
                MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
                locationBuilder.latitude(location.getLatitude());
                locationBuilder.longitude(location.getLongitude());
                MyLocationData locationData = locationBuilder.build();
                baiduMap.setMyLocationData(locationData);
            }
        }
    }

    //获取当前位置
    public void getCurrebtPosition(BDLocation location){
        //存储当前详细方位
        currentPosition = new StringBuilder();
        currentPosition.append("纬度：").append(location.getLatitude()).append("   ");
        currentPosition.append("经度：").append(location.getLongitude()).append("\n");
        currentPosition.append(location.getAddrStr()).append("\n");
        sendingCurrentposition = currentPosition.toString();//将当前位置信息发送出去
        sosText = currentPosition.toString()+"事件是：  ";
    }

    @Override
    protected void onResume(){
        super.onResume();
        mapView.onResume();
    }
    @Override
    protected void onPause(){
        super.onPause();
        mapView.onPause();
    }
    @Override
    protected void onDestroy(){
        super.onDestroy();
        mLocationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
        save();
    }
    @Override
    protected void onRestart(){
        super.onRestart();
        //readFromDatabase();
    }

    @Override//点击背景
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override//创建工具条菜单
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        MenuItem item = menu.findItem(R.id.action_login).setChecked(true);
        try{
            //设置自定义图标可见
            item.getClass().getDeclaredMethod("setOptionalIconsVisible",Boolean.TYPE);
        }catch(Exception e){
            e.printStackTrace();
        }

        if(ifLogin){
            item.setTitle("已登录");
            item.setEnabled(false);
        }else{
            item.setTitle("账号登录");
            item.setEnabled(true);
        }
        return true;
    }

    @Override//工具条上的菜单事件
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_login) {//跳转到登录界面
            Intent intent = new  Intent(this,Login.class);
            startActivity(intent);
            return true;
        }
        if(id==R.id.action_exit){
            ifLogin = false;
        }
        if(id==R.id.action_exitApp){
            finish();
        }
        if(id==R.id.action_open_close){
            View view = getWindow().getDecorView();
            if(!ifLayerOpen){
                addLayer(view);
            }else{
                closeLayer(view);
            }

        }
        if(id==R.id.action_guide){
            View view = getWindow().getDecorView();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override//Nagegation中的菜单事件
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if(ifLogin){//如果登录了，这些工具都可以操作
            if (id == R.id.nav_selectByPhoto) {
                File outputImage = new File(getExternalCacheDir(),"output_image.jpg");
                try{
                    if(outputImage.exists()){
                        outputImage.delete();
                    }
                    outputImage.createNewFile();
                }catch(IOException e){
                    e.printStackTrace();
                }
                if(Build.VERSION.SDK_INT>=24){
                    imageUri = FileProvider.getUriForFile(MainActivity.this,
                            "qilu.help.fileprovider",outputImage);
                }else{
                    imageUri = Uri.fromFile(outputImage);
                }

                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                startActivityForResult(intent,TAKE_PHOTO);

            } else if (id == R.id.nav_selectByAlbum) {
                if(ContextCompat.checkSelfPermission(MainActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                    ActivityCompat.requestPermissions(MainActivity.this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);
                }else{
                    openAlbum();
                }

            } else if (id == R.id.nav_setTel) {//若干个对话框
                inputTitleDialog("输入手机号：","确定",R.id.nav_setTel);
                tel.setSelected(false);
            } else if (id == R.id.nav_setMail) {
                inputTitleDialog("输入邮箱：","确定",R.id.nav_setMail);
                mail.setSelected(false);
            } else if (id == R.id.nav_setPersonal) {
                inputTitleDialog("输入昵称：","确定",R.id.nav_setPersonal);
                name.setSelected(false);
            } else if (id == R.id.nav_space) {
                Intent intent = new Intent(MainActivity.this,HelpCommunity.class);
                startActivity(intent);
            } else if (id == R.id.nav_record) {
                Intent intent = new Intent(MainActivity.this,RecordActivity.class);
                startActivity(intent);
            } else if (id == R.id.nav_message){
                //跳转到消息界面
                Intent intent = new Intent(MainActivity.this,MessageActivity.class);
                startActivity(intent);
            }
        }else{
            Toast.makeText(this,"请先登录",Toast.LENGTH_SHORT).show();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
    //打开照相机或者相册
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
            switch(requestCode){
                case TAKE_PHOTO:
                    if(resultCode==RESULT_OK){
                        try{
                            Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().
                                    openInputStream(imageUri));
                            picture.setImageBitmap(bitmap);
                        }catch(FileNotFoundException e){
                            e.printStackTrace();
                        }
                    }
                    break;
                case CHOOSE_PHOTO:
                    if(resultCode==RESULT_OK){
                        if(Build.VERSION.SDK_INT>=19){
                            handleImageOnKitKat(data);
                        }else{
                            handleImageBeforeKitKat(data);
                        }
                    }
                    break;
                default:
                    break;
            }
    }
    //打开相册
    private void openAlbum(){
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,CHOOSE_PHOTO);
    }
    //4.4版本以后打开相册
    @TargetApi(19)
    private void handleImageOnKitKat(Intent data){
        String imagePath = null;
        Uri uri = data.getData();
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID +"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
        }else if("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(uri,null);
        }else if("file".equalsIgnoreCase(uri.getScheme())){
            imagePath= uri.getPath();
        }
        displayImage(imagePath,data);
        imageUri = uri;//这个imageuri是用来传递图片的
    }
    //4.4版本以前打开相册
    private void handleImageBeforeKitKat(Intent data){
        Uri uri = data.getData();
        String imagePath = getImagePath(uri,null);
        displayImage(imagePath,data);
        imageUri = uri;//这个imageuri是用来传递图片的
    }
    //获取相册图片的真实路径
    private String getImagePath(Uri uri,String selection){
        String path = null;
        Cursor cursor = getContentResolver().query(uri,null,selection,null,null);
        if(cursor!=null){
            if(cursor.moveToFirst()){
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    //显示相册图片
    private void displayImage(String imagePath,Intent data){
        if(imagePath!=null){
            //Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            Bitmap bm = compressBitmap(null, null, this,data.getData() , 4, false);
            picture.setImageBitmap(bm);
        }else{
            Toast.makeText(this,"failed to get image",Toast.LENGTH_SHORT).show();
        }
    }

    //打开悬浮按钮
    public void openMenu(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",0,-155,-135);
        animator.setDuration(500);
        animator.start();
        OpenPhone.setVisibility(view.VISIBLE);
        OpenShare.setVisibility(View.VISIBLE);
        OpenShortMessage.setVisibility(View.VISIBLE);
        AlphaAnimation alphaAnimation = new AlphaAnimation(0,0.7f);
        alphaAnimation.setDuration(500);
        alphaAnimation.setFillAfter(true);
        fabOpened = true;
    }
    //关闭悬浮按钮
    public void closeMenu(View view){
        ObjectAnimator animator = ObjectAnimator.ofFloat(view,"rotation",-135,20,0);
        animator.setDuration(500);
        animator.start();

        OpenPhone.setVisibility(view.GONE);
        OpenShare.setVisibility(View.GONE);
        OpenShortMessage.setVisibility(View.GONE);

        AlphaAnimation alphaAnimation = new AlphaAnimation(0.7f,0);
        alphaAnimation.setDuration(500);
        fabOpened = false;
    }

    //弹出对话框
    private void inputTitleDialog(String title, String buttonName, final int ItemID) {

        final EditText inputServer = new EditText(this);//对话框中的一个输入框
        inputServer.setFocusable(true);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title).setView(inputServer);
        builder.setPositiveButton(buttonName,
                new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        switch(ItemID){
                            case R.id.nav_setTel:
                                if (isMobile(inputServer.getText().toString())) {
                                    updataJsonObject.put("mobilephone",inputServer.getText().toString());
                                    connectToServer();
                                    if(ifupdata){
                                        tel.setText(inputServer.getText().toString());
                                    }else{
                                        Toast.makeText(MainActivity.this,"发生未知错误，修改失败！",Toast.LENGTH_SHORT).show();
                                    }
                                } else{
                                    Toast.makeText(MainActivity.this,"请输入正确的手机号码",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.nav_setMail:
                                if (isEmail(inputServer.getText().toString())) {
                                    updataJsonObject.put("usermail",inputServer.getText().toString());
                                    connectToServer();
                                    if(ifupdata){
                                        mail.setText(inputServer.getText().toString());
                                    }else{
                                        Toast.makeText(MainActivity.this,"发生未知错误，修改失败！",Toast.LENGTH_SHORT).show();
                                    }
                                } else{
                                    Toast.makeText(MainActivity.this,"请输入正确的邮箱",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case R.id.nav_setPersonal:
                                if(inputServer.getText().toString()!=null){
                                    sendingname = name.getText().toString();
                                    updataJsonObject.put("username",inputServer.getText().toString());
                                    connectToServer();
                                    if(ifupdata){
                                        name.setText(inputServer.getText().toString());
                                    }else{
                                        Toast.makeText(MainActivity.this,"发生未知错误，修改失败！",Toast.LENGTH_SHORT).show();
                                    }
                                }else{
                                    Toast.makeText(MainActivity.this,"用户名无效",Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                });
        builder.show();
    }
    //将修改的数据上传到服务器
    public void connectToServer(){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String urlStr="http://192.168.43.193:50000/HelpServer/loginingservlet";//设置路径
                    //下面是写入服务器中的数据
                    String params = "updata=" + updataJsonObject.toString();
                    String resultData=HttpUtil.HttpPostMethod(urlStr,params);
                    if (resultData.equals("登录成功")) {//这里的服务器需要把注册信息发送给客户端
                        System.out.println("++++++++++++++++++++++修改好像成功了");
                        ifupdata = true;
                    }else{
                        ifupdata = false;
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }

    //验证手机号
    public static boolean isMobile(String number) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）
    总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
    */
        //"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        String num = "[1][3578]\\d{9}";
        if (TextUtils.isEmpty(number)) {
            return false;
        } else {
            //matches():字符串是否在给定的正则表达式匹配
            return number.matches(num);
        }
    }
    //验证邮箱
    public static boolean isEmail(String strEmail) {
        String strPattern = "^[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]@[a-zA-Z0-9][\\w\\.-]*[a-zA-Z0-9]\\.[a-zA-Z][a-zA-Z\\.]*[a-zA-Z]$";
        if (TextUtils.isEmpty(strPattern)) {
            return false;
        } else {
            return strEmail.matches(strPattern);
        }
    }

    //为微信，qq，电话，短信，邮件  添加事件
    @Override
    public void onClick(View view){
        switch(view.getId()){
            case R.id.OpenPhone:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"));
                startActivity(intent);
                break;
            case R.id.OpenShare:
                final EditText inputServer = new EditText(this);
                inputServer.setMaxLines(5);
                inputServer.setText(sosText);
                inputServer.setFocusable(true);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("求助的内容").setView(inputServer);
                builder.setPositiveButton("确认",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
                                // 文本格式
                                emailIntent.setType("text/plain");
                                // 邮件文本内容
                                emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, inputServer.getText().toString());
                                startActivity(Intent.createChooser(emailIntent, "求助信息发送到"));
                            }
                        });
                builder.show();
                break;
            case R.id.OpenShortMessage:
                Uri smsToUri = Uri.parse("smsto:");// 联系人地址
                Intent Intent = new Intent(android.content.Intent.ACTION_SENDTO, smsToUri);
                Intent.putExtra("sms_body", sosText);// 短信内容
                startActivity(Intent);
                break;
            case R.id.School:
                StringBuilder layerSchool = new StringBuilder();
                layerSchool.append("类型：小学").append("\n")
                            .append("面积：500亩").append("\n");
                initPopup(view,"希望小学","school",layerSchool.toString());
                break;
            case R.id.Carmer:
                StringBuilder layerCarmer = new StringBuilder();
                layerCarmer.append("类型：电影院").append("\n")
                        .append("面积：2亩").append("\n");
                initPopup(view,"中山影城","carmer",layerCarmer.toString());
                break;
            case R.id.Hospital:
                StringBuilder layerHospital = new StringBuilder();
                layerHospital.append("类型：医院").append("\n")
                        .append("面积：30亩").append("\n");
                initPopup(view,"人民医院","hospital",layerHospital.toString());
                break;
            case R.id.Station:
                StringBuilder layerStation = new StringBuilder();
                layerStation.append("类型：车站").append("\n")
                        .append("面积：50亩").append("\n");
                initPopup(view,"火车站","station",layerStation.toString());
                break;
            case R.id.Forest:
                StringBuilder layerForest = new StringBuilder();
                layerForest.append("类型：森林").append("\n")
                        .append("面积：4000亩").append("\n");
                initPopup(view,"原始森林","forest",layerForest.toString());
                break;
            case R.id.Market:
                StringBuilder layerMarket = new StringBuilder();
                layerMarket.append("类型：超市").append("\n")
                        .append("面积：10亩").append("\n");
                initPopup(view,"连锁超市","market",layerMarket.toString());
                break;
            default:
                break;
        }
    }

    public void save(){//打开文件，将是否登录的信息存进去
        FileOutputStream out = null;
        BufferedWriter writer= null;
        try{
            out = openFileOutput("Login", Context.MODE_PRIVATE);
            writer = new BufferedWriter(new OutputStreamWriter(out));
            if(ifLogin){
                writer.write("1");
            }else{
                writer.write("0");
            }
        }catch(IOException e){
            e.printStackTrace();
        }finally {
            try{
                if(writer!=null){
                    writer.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }
    protected void load(){ //打开文件，读取登录信息,确认账号是登录还是离线
        FileInputStream in = null;
        BufferedReader reader = null;
        String number = "";
        try{
            in = openFileInput("Login");
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while((line=reader.readLine())!=null){
                number = line;
            }
            if(number.equals("1")){
                ifLogin = true;
            }else if(number.equals("0")){
                ifLogin = false;
            }
        }catch (IOException e){
            e.printStackTrace();
        } finally {
            try{
                reader.close();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    /**图片压缩处理，size参数为压缩比，比如size为2，则压缩为1/4**/
    static public Bitmap compressBitmap(String path, byte[] data, Context context, Uri uri, int size, boolean width) {
        BitmapFactory.Options options = null;
        if (size > 0) {
            BitmapFactory.Options info = new BitmapFactory.Options();
             /**如果设置true的时候，decode时候Bitmap返回的为数据将空*/
            info.inJustDecodeBounds = false;
            decodeBitmap(path, data, context, uri, info);
            int dim = info.outWidth;
            if (!width) dim = Math.max(dim, info.outHeight);
            options = new BitmapFactory.Options();
            /**把图片宽高读取放在Options里*/
            options.inSampleSize = size;
        }
        Bitmap bm = null;
        try {
            bm = decodeBitmap(path, data, context, uri, options);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return bm;
    }
    /**把byte数据解析成图片*/
    static public Bitmap decodeBitmap(String path, byte[] data, Context context, Uri uri, BitmapFactory.Options options) {
        Bitmap result = null;
        if (path != null) {
            result = BitmapFactory.decodeFile(path, options);
        }
        else if (data != null) {
            result = BitmapFactory.decodeByteArray(data, 0, data.length, options);
        }
        else if (uri != null) {
            ContentResolver cr = context.getContentResolver();
            InputStream inputStream = null;
            try {
                inputStream = cr.openInputStream(uri);
                result = BitmapFactory.decodeStream(inputStream, null, options);
                inputStream.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    //为了测试写的方法
    static public void readFromDatabase(MyDatabaseHelper dbHelper){
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor cursor = db.query("user",null,null,null,null,null,null);
        if(cursor.moveToFirst()){
            do{
                name.setText(cursor.getString(cursor.getColumnIndex("name")));
            }while(cursor.moveToNext());
        }
        cursor.close();
        tel.setText("17854212445");
        mail.setText("258132@163.com");
    }
    //为了测试写的方法
    static public void ceshifangfa(){
        name.setText("tangqiuyi");
        tel.setText("tel: "+"17854212445");
        mail.setText("mail: "+"258312@163.com");

    }

    //添加覆盖物
    public void addLayer(View view){
        school.setVisibility(view.VISIBLE);
        carmer.setVisibility(view.VISIBLE);
        hospital.setVisibility(view.VISIBLE);
        station.setVisibility(view.VISIBLE);
        forest.setVisibility(view.VISIBLE);
        market.setVisibility(view.VISIBLE);
        ifLayerOpen = true;
    }
    //去除覆盖物
    public void closeLayer(View view){
        school.setVisibility(view.GONE);
        carmer.setVisibility(view.GONE);
        hospital.setVisibility(view.GONE);
        station.setVisibility(view.GONE);
        forest.setVisibility(view.GONE);
        market.setVisibility(view.GONE);
        ifLayerOpen = false;
    }
    public void initPopup(View v,String name,String type,String content){
        View view= LayoutInflater.from(MainActivity.this).inflate(R.layout.popupwindow_layer_layout, null, false);
        layerName = (TextView) view.findViewById(R.id.layer_name);
        layerContent = (TextView) view.findViewById(R.id.layer_content);
        layerPicture = (ImageView) view.findViewById(R.id.layer_picture);
        layerName.setText(name);
        layerContent.setText(content);
        if(type.equals("school")){
            layerPicture.setImageResource(R.drawable.xiwang);
        }else if(type.equals("carmer")){
            layerPicture.setImageResource(R.drawable.zhongshan);
        }else if(type.equals("hospital")){
            layerPicture.setImageResource(R.drawable.renminyiyuan);
        }else if(type.equals("station")){
            layerPicture.setImageResource(R.drawable.chezhan);
        }else if(type.equals("forest")){
            layerPicture.setImageResource(R.drawable.senlin);
        }else if(type.equals("market")){
            layerPicture.setImageResource(R.drawable.chaoshi);
        }
        //1.构造一个PopupWindow，参数依次是加载的View，宽高
        final PopupWindow popWindow = new PopupWindow(view,
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popWindow.setAnimationStyle(R.anim.anim_pop);  //设置加载动画
        //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
        //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
        //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
        popWindow.setTouchable(true);
        popWindow.setTouchInterceptor(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
                // 这里如果返回true的话，touch事件将被拦截
                // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
            }
        });
        popWindow.setBackgroundDrawable(new ColorDrawable(0x00000000));    //要为popWindow设置一个背景才有效
        //设置popupWindow显示的位置，参数依次是参照View，x轴的偏移量，y轴的偏移量
        popWindow.showAsDropDown(v, 100, 0);
    }
}
