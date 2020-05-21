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

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * 初次提交个人信息
 */
public class InformationActivity extends AppCompatActivity {

    private TextView tv_main_title;
    private EditText et_sex,et_age,et_school,et_email;
    private Button btn_submit;
    private String sex,age,school,email;
    MyUser myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);
        myUser=MyUser.getCurrentUser(MyUser.class);
        init();
    }

    private void init() {
        tv_main_title = findViewById(R.id.tv_main_title);
        et_age=findViewById(R.id.infor_et_age);
        et_email=findViewById(R.id.infor_et_email);
        et_school=findViewById(R.id.infor_et_school);
        et_sex=findViewById(R.id.infor_et_sex);
        btn_submit=findViewById(R.id.infor_btn_submit);

        tv_main_title.setText("完善个人资料");


        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                school=et_school.getText().toString().trim();
                sex=et_sex.getText().toString().trim();
                age=et_age.getText().toString().trim();
                email=et_email.getText().toString().trim();
                if (TextUtils.isEmpty(sex)) {
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
                    user.setAge(age);
                    user.setSex(sex);
                    user.setSchool(school);
                    user.setEmail(email);
                    user.update(myUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(getApplication(), "提交成功", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(InformationActivity.this,FragmentActivity.class);
                                startActivity(intent);
                                InformationActivity.this.finish();
                            }else{
                                Toast.makeText(getApplication(), "提交失败"+e.toString(), Toast.LENGTH_SHORT).show();
                                Log.e("bomb",e.toString());
                            }
                        }
                    });


                }
            }
        });
    }

}
