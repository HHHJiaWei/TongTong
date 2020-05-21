package com.example.a44261.bysj102;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a44261.bysj102.db.MyUser;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * 查看个人信息
 */
public class MyInformationActivity extends AppCompatActivity {

    private String my_id,username,nickname,sex,age,school,email;
    private TextView tv_back, tv_main_title,tv_username,tv_nickname,tv_sex,tv_age,tv_school,tv_email;
    private LinearLayout ll_infor;
    private MyUser myUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_information);

        myUser=BmobUser.getCurrentUser(MyUser.class);

        my_id=myUser.getObjectId();

        init();
    }

    private void init() {
        tv_back = findViewById(R.id.tv_back);
        tv_main_title = findViewById(R.id.tv_main_title);
        ll_infor=findViewById(R.id.my_infor_ll_infor);
        tv_age=findViewById(R.id.my_infor_tv_age);
        tv_email=findViewById(R.id.my_infor_tv_email);
        tv_nickname=findViewById(R.id.my_infor_tv_nickname);
        tv_school=findViewById(R.id.my_infor_tv_school);
        tv_username=findViewById(R.id.my_infor_tv_username);
        tv_sex=findViewById(R.id.my_infor_tv_sex);

        username=myUser.getUsername();
        nickname=myUser.getNickname();
        sex=myUser.getSex();
        age=myUser.getAge();
        school=myUser.getSchool();
        email=myUser.getEmail();

        tv_back.setText("返回");
        tv_main_title.setText("个人信息");
        tv_sex.setText(sex);
        tv_username.setText(username);
        tv_school.setText(school);
        tv_nickname.setText(nickname);
        tv_email.setText(email);
        tv_age.setText(age);

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回键
                MyInformationActivity.this.finish();
            }
        });
        ll_infor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(MyInformationActivity.this,UpdateInformationActivity.class);
                startActivity(intent);
            }
        });
    }


}
