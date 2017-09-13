package qilu.help;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.json.JSONObject;

import java.io.IOException;

//import net.sf.json.JSONObject;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener{


    public Button Sign_sure;//所有的控件
    public Button Sign_cannel;
    public EditText Sign_name_editText = null;
    public EditText Sign_phone_editText = null;
    public EditText Sign_mail_editText = null;
    public RadioGroup Sign_sex = null;
    public RadioButton Sign_man_radioButton = null;
    public RadioButton Sign_woman_radioButton = null;
    public EditText Sign_password_textView = null;
    public EditText Sign_Repassword_textView = null;
    public CheckBox Sign_checkBox = null;

    private String username;//定义用户名
    private String mobilephone;//定义手机
    private String usermail;//定义邮箱
    private String usersex;//定义性别
    private String password;//定义密码
    private String repassword;//再一次确认密码

    public User user;//一个JavaBean
    public JSONObject jsonObject;

    private boolean ifOk = false;//判断输入是否合法
    private String resultData = null;//存储服务器返回值

    private boolean ifExist = false;//判断用户名是否存在

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_layout);

        //获取所有的注册信息
        init();
    }

    public void init(){
        Sign_sure   = (Button)findViewById(R.id.Sign_sure_button);
        Sign_cannel = (Button)findViewById(R.id.Sign_cancel_button);
        Sign_name_editText     = (EditText)findViewById(R.id.Sign_name_editText);
        Sign_phone_editText    = (EditText)findViewById(R.id.Sign_phone_editText);
        Sign_mail_editText     = (EditText)findViewById(R.id.Sign_mail_editText);
        Sign_sex               = (RadioGroup)findViewById(R.id.Sign_sex);
        Sign_man_radioButton   = (RadioButton)findViewById(R.id.Sign_man_radioButton);
        Sign_woman_radioButton = (RadioButton)findViewById(R.id.Sign_woman_radioButton);
        Sign_password_textView = (EditText)findViewById(R.id.Sign_password_textView);
        Sign_Repassword_textView = (EditText)findViewById(R.id.Sign_Repassword_textView);
        Sign_checkBox          = (CheckBox)findViewById(R.id.Sign_checkBox);

        user = new User();

        Sign_sure.setOnClickListener(this);
        Sign_cannel.setOnClickListener(this);
        Sign_man_radioButton.setOnClickListener(this);
        Sign_woman_radioButton.setOnClickListener(this);
        Sign_checkBox.setOnClickListener(this);

        Sign_sex.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch(checkedId){
                    case R.id.Sign_man_radioButton:
                        usersex = "man";
                        break;
                    case R.id.Sign_woman_radioButton:
                        usersex = "woman";
                        break;
                }
            }
        });
    }

    @Override
    public void onClick(View v){
        switch(v.getId()){
            case R.id.Sign_sure_button:
                username = Sign_name_editText.getText().toString().trim();
                mobilephone = Sign_phone_editText.getText().toString();
                usermail = Sign_mail_editText.getText().toString().trim();
                //usersex 已经赋值完毕了
                password = Sign_password_textView.getText().toString();
                repassword= Sign_Repassword_textView.getText().toString().trim();
                //调用方法，判断输入是否合法，合法就是ifOK。
                JudgeIegal(username,mobilephone,usermail,password,repassword,usersex);
                if(ifOk) {
                    //上传数据
                    DataToBeanUser();//传入Bean
                    connectToServer();//传入服务器
                }


                //为了测试，不管是不是注册成功，都显示成功.
                //照理说应该是根据服务器返回的消息进行判断的
                MainActivity.ifSign = true;
                //然后将这些注册信息放入表中
                try{
                    MyDatabaseHelper dbHelper;
                    dbHelper = new MyDatabaseHelper(this,"USERS.db",null,2);//创建数据库;
                    dbHelper.getWritableDatabase();
                    //向数据库中添加数据
                    SQLiteDatabase db = dbHelper.getWritableDatabase();
                    ContentValues values = new ContentValues();
                    values.put("phone",Integer.getInteger(mobilephone));
                    values.put("name",username);
                    values.put("mail",usermail);
                    values.put("sex",usersex);
                    values.put("password",Integer.getInteger(password));
                    db.insert("user",null,values);

                    MainActivity.readFromDatabase(dbHelper);
                }catch (Exception e){
                    e.printStackTrace();
                }
                /////////////////////////////////////////

                try {
                    Thread.currentThread().sleep(1000);//阻断1秒
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                if(MainActivity.ifSign){
                    Toast.makeText(SignInActivity.this,"注册成功！快去登录吧!!",Toast.LENGTH_SHORT).show();
                    try {
                        Thread.currentThread().sleep(2000);//阻断2秒
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(SignInActivity.this,Login.class);
                    intent.putExtra("phone","17854212445");
                    startActivity(intent);
                }
                if(ifExist){
                    Toast.makeText(SignInActivity.this,"用户名已经存在！",Toast.LENGTH_SHORT).show();
                }

                break;
            case R.id.Sign_cancel_button:
                Intent intent_cancel = new Intent(SignInActivity.this,Login.class);
                startActivity(intent_cancel);
                break;
            case R.id.Sign_man_radioButton:
                break;
            case R.id.Sign_woman_radioButton:
                break;
            case R.id.Sign_checkBox:
                break;
            default:
                break;
        }
    }

    //判断注册信息的输入是否合法
    public void JudgeIegal(String name,String phone,String mail,String password,String repassword,String usersex){
        if(name.equals("")){
            Toast.makeText(this, "用户名不能为空", Toast.LENGTH_SHORT).show();
        }else{
            if(phone.equals("")){
                Toast.makeText(this, "请输入手机号", Toast.LENGTH_SHORT).show();
            }else{
                if(mail.equals("")){
                    Toast.makeText(this, "请输入邮箱", Toast.LENGTH_SHORT).show();
                }else{
                    if(password.equals("")){
                        Toast.makeText(this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }else{
                        if(repassword.equals("")){
                            Toast.makeText(this, "请确认密码", Toast.LENGTH_SHORT).show();
                        }else if(!repassword.equals(password)){
                            Toast.makeText(this, "两次密码输入不一致", Toast.LENGTH_SHORT).show();
                        }else{
                            if(Sign_checkBox.isChecked()){
                                ifOk = true;
                            }else{
                                Toast.makeText(this, "需要接受条款", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                }
            }
        }
    }

    //注册信息写入bean
    public void DataToBeanUser(){
        user.setUsername(username);
        user.setMobilephone(mobilephone);
        user.setUsermail(usermail);
        user.setUsersex(usersex);
        user.setPassword(password);
    }
    //注册信息上传
    public void connectToServer(){

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String urlStr="http://172.24.237.151:50000/HelpServer/loginservlet";//设置路径
                    //下面是写入服务器中的数据
                    String params="username=" + username
                               + "&mobilephone=" + mobilephone
                               + "&usermail=" + usermail
                               + "&usersex=" + usersex
                               + "&password=" + password;
                    resultData=HttpUtil.HttpPostMethod(urlStr,params);
                    if(resultData.equals("注册成功")){
                        MainActivity.ifSign = true;
                        ifExist = false;
                    }else if(resultData.equals("用户名已经存在")) {
                        ifExist = true;
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
