package qilu.help;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.LoaderManager.LoaderCallbacks;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import org.w3c.dom.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_CONTACTS;
import static qilu.help.MainActivity.ceshifangfa;

public class Login extends AppCompatActivity implements LoaderCallbacks<Cursor> {

    private static final int REQUEST_READ_CONTACTS = 0;

    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    private UserLoginTask mAuthTask = null;

    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;

    boolean cancel = false;//标志输入是否合法

    private String email;//存储用来登录的手机号和密码
    private String password;
    private JSONObject loginJsonObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Set up the login form.
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        populateAutoComplete();

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    email = mEmailView.getText().toString();//获得邮箱
                    password = mPasswordView.getText().toString();//获得密码
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        //点击登录Login_in按钮
        Button mEmailSignInButton = (Button) findViewById(R.id.login_in);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                email = mEmailView.getText().toString();//获得邮箱
                password = mPasswordView.getText().toString();//获得密码
                attemptLogin();
                if(!cancel){
                    //上传数据到服务器
                    connectToServer(email,password,view);
                }
            }
        });
        //点击注册Sign_in按钮
        Button btnSignIn = (Button)findViewById(R.id.sign_in);
        btnSignIn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {//跳转到注册界面
                Intent intent = new Intent(Login.this,SignInActivity.class);
                startActivity(intent);
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        Intent intent = getIntent();
        String data = intent.getStringExtra("phone");
        mEmailView.setText(data);

        loginJsonObject = new JSONObject();//JSON类型的数据
    }

    private void populateAutoComplete() {
        if (!mayRequestContacts()) {
            return;
        }

        getLoaderManager().initLoader(0, null, this);
    }

    private boolean mayRequestContacts() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }
        if (checkSelfPermission(READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            return true;
        }
        if (shouldShowRequestPermissionRationale(READ_CONTACTS)) {
            Snackbar.make(mEmailView, R.string.permission_rationale, Snackbar.LENGTH_INDEFINITE)
                    .setAction(android.R.string.ok, new View.OnClickListener() {
                        @Override
                        @TargetApi(Build.VERSION_CODES.M)
                        public void onClick(View v) {
                            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
                        }
                    });
        } else {
            requestPermissions(new String[]{READ_CONTACTS}, REQUEST_READ_CONTACTS);
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READ_CONTACTS) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                populateAutoComplete();
            }
        }
    }

    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mEmailView.setError(null);
        mPasswordView.setError(null);
        View focusView = null;

        // 检查密码
        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
            mPasswordView.setError("注册时的密码大于四位");
            focusView = mPasswordView;
            cancel = true;
        }

        // 检查邮箱地址
        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("请输入注册使用的手机号码");
            focusView = mEmailView;
            cancel = true;
        } else if (!isPhoneValid(email)) {
            mEmailView.setError("手机号码需要十一位");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {//如果检查不合格
            focusView.requestFocus();
        } else {//如果检查合格
            showProgress(true);
            mAuthTask = new UserLoginTask(email, password);
            mAuthTask.execute((Void) null);

            //使用JSON类型保存数据
            try{
                loginJsonObject.put("mobilephone",email);
                loginJsonObject.put("password",password);
            }catch(JSONException e){
                e.printStackTrace();
            }
        }
    }
    //上传数据到服务器
    public void connectToServer(final String mobilephone,final String password,final View view){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    String urlStr="http://192.168.43.193:50000/HelpServer/loginingservlet";//设置路径
                    //下面是写入服务器中的数据
                    String params = "login=" + loginJsonObject.toString();
                    String resultData=HttpUtil.HttpPostMethod(urlStr,params);
                    if (resultData.equals("登录成功")) {//这里的服务器需要把注册信息发送给客户端
                        //接收登录的广播
                        Intent intentLogin = new Intent("HAS.LOGINED.IN.SUCCEED");
                        sendBroadcast(intentLogin);
                        //登录之后，需要启动一个服务，该服务每隔一段时间向服务器发送一次当前的
                    }else{
                        Snackbar.make(view,"注册失败，请修改用户名重新注册！",Snackbar.LENGTH_SHORT)
                                .show();
                    }
                }catch(IOException e){
                    e.printStackTrace();
                }
            }
        }).start();

    }
    static public boolean isEmailValid(String email) {
        //TODO: 判断邮箱中有没有@
        return email.contains("@");
    }

    static public boolean isPhoneValid(String phone){
        //TODO: 判断手机号有没有四位
        return phone.length()==11;
    }

    static public boolean isPasswordValid(String password) {
        //TODO: 要求密码长度大于四
        return password.length() > 4;
    }


    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {//进度条
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return new CursorLoader(this,
                // Retrieve data rows for the device user's 'profile' contact.
                Uri.withAppendedPath(ContactsContract.Profile.CONTENT_URI,
                        ContactsContract.Contacts.Data.CONTENT_DIRECTORY), ProfileQuery.PROJECTION,

                // Select only email addresses.
                ContactsContract.Contacts.Data.MIMETYPE +
                        " = ?", new String[]{ContactsContract.CommonDataKinds.Email
                .CONTENT_ITEM_TYPE},

                // Show primary email addresses first. Note that there won't be
                // a primary email address if the user hasn't specified one.
                ContactsContract.Contacts.Data.IS_PRIMARY + " DESC");
    }

    @Override
    public void onLoadFinished(Loader<Cursor> cursorLoader, Cursor cursor) {
        List<String> emails = new ArrayList<>();
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            emails.add(cursor.getString(ProfileQuery.ADDRESS));
            cursor.moveToNext();
        }

        addEmailsToAutoComplete(emails);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> cursorLoader) {

    }

    private void addEmailsToAutoComplete(List<String> emailAddressCollection) {
        //Create adapter to tell the AutoCompleteTextView what to show in its dropdown list.
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(Login.this,
                        android.R.layout.simple_dropdown_item_1line, emailAddressCollection);

        mEmailView.setAdapter(adapter);
    }


    private interface ProfileQuery {
        String[] PROJECTION = {
                ContactsContract.CommonDataKinds.Email.ADDRESS,
                ContactsContract.CommonDataKinds.Email.IS_PRIMARY,
        };

        int ADDRESS = 0;
        int IS_PRIMARY = 1;
    }

    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mEmail;
        private final String mPassword;

        UserLoginTask(String email, String password) {
            mEmail = email;
            mPassword = password;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            try {
                // Simulate network access.
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                return false;
            }

            for (String credential : DUMMY_CREDENTIALS) {
                String[] pieces = credential.split(":");
                if (pieces[0].equals(mEmail)) {
                    // Account exists, return true if the password matches.
                    return pieces[1].equals(mPassword);
                }
            }

            // TODO: register the new account here.
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            if (success) {
                if(MainActivity.ifLogin){
                    showProgress(true);//表示进度条的界面会消失
                    finish();
                }else{
                    showProgress(false);//表示进度条的界面不会消失
                }
            } else {
                mPasswordView.setError("密码错误！");
                mPasswordView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false);
        }
    }
}
