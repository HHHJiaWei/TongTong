package com.example.a44261.bysj102.TT;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.db.MyUser;
import com.example.a44261.bysj102.db.Org;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class FoundPage extends AppCompatActivity {
    private String username_now;
    private Button btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_page);

        SharedPreferences sps = getSharedPreferences("NamePwd", Context.MODE_PRIVATE);
        username_now=BmobUser.getCurrentUser(MyUser.class).getUsername();

        Button btn_found=(Button)findViewById(R.id.btn_org_found);
        final EditText et_orgname=(EditText)findViewById(R.id.et_orgname);
        final EditText et_school=(EditText)findViewById(R.id.et_school);
        TextView tv_organizer=(TextView) findViewById(R.id.tv_organizer);
        btn_back=(Button)findViewById(R.id.btn_back);

        tv_organizer.setText(username_now);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FoundPage.this.finish();
            }
        });

        btn_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String orgname=et_orgname.getText().toString().trim();
                String school=et_school.getText().toString().trim();

                if(TextUtils.isEmpty(orgname)){
                    Toast.makeText(FoundPage.this, "请输入团队名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(school)){
                    Toast.makeText(FoundPage.this, "请输入所属学校", Toast.LENGTH_SHORT).show();
                    return;
                }

                final Org org = new Org();
                org.setOrgname(orgname);
                org.setSchool(school);
                org.setOrger_name(username_now);
                //添加一对一关联，用户关联帖子
                org.setOrganizer(BmobUser.getCurrentUser(MyUser.class));
                org.save(new SaveListener<String>() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
//                            Toast.makeText(getApplication(), "创建成功", Toast.LENGTH_SHORT).show();

                            MyUser user = BmobUser.getCurrentUser(MyUser.class);

                            //关联当前org(s为当前org的id)
                            org.setObjectId(s);
                            //将当前org添加到User表的myorg字段值中，表明当前用户加入该组织
                            BmobRelation relation = new BmobRelation();
                            //将当前org添加到多对多关联中
                            relation.add(org);
                            //多对多关联指向`user`的`myorg`字段
                            user.setMyorg(relation);
                            user.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        Toast.makeText(getApplication(), "创建成功", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                            BmobRelation relation1=new BmobRelation();
                            relation1.add(user);
                            org.setMember(relation1);
                            org.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        Toast.makeText(getApplication(), "创建成功", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Log.e("BMOB", e.toString());
                            Toast.makeText(getApplication(), "创建失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                FoundPage.this.finish();
            }
        });
    }
}
