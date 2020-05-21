package com.example.a44261.bysj102;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextUtils;
import android.text.method.DigitsKeyListener;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a44261.bysj102.db.Have;
import com.example.a44261.bysj102.db.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class RegActivity extends AppCompatActivity {

    private TextView tv_back, tv_main_title;
    private EditText et_nickname,et_username, et_psd1, et_psd2;
    private Button btn_reg;
    private String nickname,username, psd1, psd2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reg);

        init();
    }

    private void init() {
        tv_back = findViewById(R.id.tv_back);
        tv_main_title = findViewById(R.id.tv_main_title);
        et_nickname=findViewById(R.id.reg_et_nickname);
        et_username = findViewById(R.id.reg_et_username);
        et_psd1 = findViewById(R.id.reg_et_psd1);
        et_psd2 = findViewById(R.id.reg_et_psd2);
        btn_reg = findViewById(R.id.reg_btn_reg);

        tv_back.setText("返回");
        tv_main_title.setText("注册");

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回键
                RegActivity.this.finish();
            }
        });

        btn_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nickname=et_nickname.getText().toString().trim();
                username = et_username.getText().toString().trim();//.trim()去除两边空格
                psd1 = et_psd1.getText().toString().trim();
                psd2 = et_psd2.getText().toString().trim();

                if (TextUtils.isEmpty(username)) {
                    Toast.makeText(RegActivity.this, "请输入用户名", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(nickname)) {
                    Toast.makeText(RegActivity.this, "请输入昵称", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(psd1)) {
                    Toast.makeText(RegActivity.this, "请输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(psd2)) {
                    Toast.makeText(RegActivity.this, "请再次输入密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!psd1.equals(psd2)) {
                    Toast.makeText(RegActivity.this, "输入两次的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    MyUser myUser = new MyUser();
                    myUser.setNickname(nickname);
                    myUser.setUsername(username);
                    myUser.setPassword(psd1);
                    myUser.signUp(new SaveListener<MyUser>() {
                        @Override
                        public void done(MyUser myUser, BmobException e) {

                            if (e == null) {
                                Toast.makeText(getApplication(), "注册成功，返回objectId为：" + myUser.getObjectId(), Toast.LENGTH_SHORT).show();

                                Have have=new Have();
                                have.setUserid(myUser.getObjectId());
                                have.save(new SaveListener<String>() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e==null){
                                            Log.e("Bmob",s);
                                        }else {
                                            Log.e("Bmob",e.getMessage());
                                        }
                                    }
                                });

                                Intent data = new Intent();
                                data.putExtra("userName", username);
                                data.putExtra("Psd",psd1);
                                setResult(RESULT_OK, data);

                                RegActivity.this.finish();;
                            } else {
                                Toast.makeText(getApplication(), "注册失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                Log.e("bomb", "注册失败：" + e.getMessage());
                                return;
                            }
                        }
                    });
                }
            }
        });

    }
}
