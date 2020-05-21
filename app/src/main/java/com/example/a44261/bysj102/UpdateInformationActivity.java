package com.example.a44261.bysj102;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a44261.bysj102.db.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdateInformationActivity extends AppCompatActivity {

    private TextView tv_back, tv_main_title;
    private MyUser myUser;
    private String nickname,sex,age,school,email;
    private EditText et_nickname,et_sex,et_age,et_school,et_email;
    private Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_information);

        myUser=BmobUser.getCurrentUser(MyUser.class);

        tv_back = findViewById(R.id.tv_back);
        tv_main_title = findViewById(R.id.tv_main_title);
        btn_submit=findViewById(R.id.up_infor_btn_submit);
        et_age=findViewById(R.id.up_infor_et_age);
        et_email=findViewById(R.id.up_infor_et_email);
        et_nickname=findViewById(R.id.up_infor_et_nickname);
        et_school=findViewById(R.id.up_infor_et_school);
        et_sex=findViewById(R.id.up_infor_et_sex);

        tv_back.setText("返回");
        tv_main_title.setText("修改个人信息");

        show();

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回键
                UpdateInformationActivity.this.finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                update();
            }
        });

    }

    private void show() {
        nickname=myUser.getNickname();
        sex=myUser.getSex();
        age=myUser.getAge();
        school=myUser.getSchool();
        email=myUser.getEmail();

        et_sex.setText(sex);
        et_school.setText(school);
        et_nickname.setText(nickname);
        et_email.setText(email);
        et_age.setText(age);
    }

    private void update() {
        nickname=et_nickname.getText().toString().trim();
        sex=et_sex.getText().toString().trim();
        age=et_age.getText().toString().trim();
        school=et_school.getText().toString().trim();
        email=et_email.getText().toString().trim();

        if (TextUtils.isEmpty(nickname)) {
            Toast.makeText(getApplication(), "请输入昵称", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(sex)) {
            Toast.makeText(getApplication(), "请输入性别", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(age)) {
            Toast.makeText(getApplication(), "请输入年龄", Toast.LENGTH_SHORT).show();
            return;
        } else if (TextUtils.isEmpty(school)) {
            Toast.makeText(getApplication(), "请输入所在学校", Toast.LENGTH_SHORT).show();
            return;
        }else if (TextUtils.isEmpty(email)) {
            Toast.makeText(getApplication(), "请输入电子邮箱", Toast.LENGTH_SHORT).show();
            return;
        }else {

            MyUser user=new MyUser();
            user.setNickname(nickname);
            user.setAge(age);
            user.setSex(sex);
            user.setSchool(school);
            user.setEmail(email);
            user.update(myUser.getObjectId(), new UpdateListener() {
                @Override
                public void done(BmobException e) {
                    if(e==null){
                        Toast.makeText(getApplication(), "提交成功", Toast.LENGTH_SHORT).show();

                        UpdateInformationActivity.this.finish();
                    }else{
                        Toast.makeText(getApplication(), "提交失败"+e.toString(), Toast.LENGTH_SHORT).show();
                        Log.e("bomb",e.toString());
                    }
                }
            });
        }
    }
}
