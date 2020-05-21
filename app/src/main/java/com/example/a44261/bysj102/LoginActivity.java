package com.example.a44261.bysj102;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a44261.bysj102.db.MyUser;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class LoginActivity extends AppCompatActivity {

    private TextView tv_back,tv_main_title;
    private EditText et_username,et_psd;
    private String username,psd;
    private Button btn_lg,btn_reg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Bmob.initialize(this, "7ee361ae2183671d9c529b40ebb1ea56");

        init();

    }

    private void init() {
        tv_back=findViewById(R.id.tv_back);
        tv_main_title=findViewById(R.id.tv_main_title);
        et_username=findViewById(R.id.lg_et_username);
        et_psd=findViewById(R.id.lg_et_psd);
        btn_lg=findViewById(R.id.lg_btn_denglu);
        btn_reg=findViewById(R.id.lg_btn_zhuce);

        tv_main_title.setText("登录");

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(LoginActivity.this,RegActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        btn_lg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                username=et_username.getText().toString().trim();
                psd=et_psd.getText().toString().trim();

                if(TextUtils.isEmpty(username)){
                    Toast.makeText(LoginActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                }else if(TextUtils.isEmpty(psd)){
                    Toast.makeText(LoginActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                    // md5Psw.equals(); 判断，输入的密码加密后，是否与保存在SharedPreferences中一致
                }else{
                    MyUser user=new MyUser();
                    user.setUsername(username);
                    user.setPassword(psd);
                    user.login(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser bmobUser, BmobException e) {
                            if (e == null) {
                              //  MyUser myUser1 = BmobUser.getCurrentUser(MyUser.class);
                                Toast.makeText(LoginActivity.this, "登录成功" , Toast.LENGTH_SHORT).show();

                                if(TextUtils.isEmpty(bmobUser.getSchool())){
                                    Intent intent=new Intent(LoginActivity.this,InformationActivity.class);
                                    startActivity(intent);
                                }else {
                                    Intent intent=new Intent(LoginActivity.this,FragmentActivity.class);
                                    startActivity(intent);

                                }
                                LoginActivity.this.finish();

                            } else {
                                Toast.makeText(LoginActivity.this, "登录失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }

    //显示数据， onActivityResult
    //startActivityForResult(intent, 1); 从注册界面中获取数据
    //int requestCode , int resultCode , Intent data
    // LoginActivity -> startActivityForResult -> onActivityResult();
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        super.onActivityResult(requestCode, resultCode, data);
        if(data!=null){
            //是获取注册界面回传过来的用户名
            // getExtra().getString("***");
            String userName=data.getStringExtra("userName");
            String Psd=data.getStringExtra("Psd");
            if(!TextUtils.isEmpty(userName)){
                //设置用户名到 et_user_name 控件
                et_username.setText(userName);
                et_psd.setText(Psd);
                //et_user_name控件的setSelection()方法来设置光标位置
                et_username.setSelection(userName.length());
            }
        }
    }
}
