package com.example.a44261.bysj102;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a44261.bysj102.db.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdatePSDActivity extends AppCompatActivity {
    private TextView tv_back,tv_main_title;
    private EditText et_oldpsd,et_newpsd1,et_newpsd2;
    private String oldpsd,newpsd1,newpsd2;
    private Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_psd);
        init();
    }

    private void init() {
        tv_back = findViewById(R.id.tv_back);
        tv_main_title = findViewById(R.id.tv_main_title);
        et_oldpsd=findViewById(R.id.update_psd_et_oldpsd);
        et_newpsd1=findViewById(R.id.update_psd_et_newpsd1);
        et_newpsd2=findViewById(R.id.update_psd_et_newpsd2);
        btn_submit=findViewById(R.id.update_psd_btn_submit);

        tv_back.setText("返回");
        tv_main_title.setText("修改密码");

        tv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //返回键
                UpdatePSDActivity.this.finish();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oldpsd=et_oldpsd.getText().toString().trim();
                newpsd1=et_newpsd1.getText().toString().trim();
                newpsd2=et_newpsd2.getText().toString().trim();

                if (TextUtils.isEmpty(oldpsd)) {
                    Toast.makeText(UpdatePSDActivity.this, "请输入旧密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(newpsd1)) {
                    Toast.makeText(UpdatePSDActivity.this, "请输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (TextUtils.isEmpty(newpsd2)) {
                    Toast.makeText(UpdatePSDActivity.this, "请再次输入新密码", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!newpsd2.equals(newpsd1)) {
                    Toast.makeText(UpdatePSDActivity.this, "两次输入的密码不一样", Toast.LENGTH_SHORT).show();
                    return;
                }else {
                    MyUser.updateCurrentUserPassword(oldpsd, newpsd1, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if(e==null){
                                Toast.makeText(UpdatePSDActivity.this, "密码修改成功，可以用新密码进行登录啦", Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(UpdatePSDActivity.this,LoginActivity.class);
                                startActivity(intent);
                                UpdatePSDActivity.this.finish();
                            }else{
                                Toast.makeText(UpdatePSDActivity.this, "密码修改失败:" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        }

                    });
                }
            }
        });
    }
}
