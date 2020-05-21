package com.example.a44261.bysj102.TT;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.TT.Adapter.OrgItemAdapter;
import com.example.a44261.bysj102.db.MyUser;
import com.example.a44261.bysj102.db.Org;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class JoinPage extends AppCompatActivity {

    private boolean run = false;
    private final Handler handler = new Handler();
    private String school_search;
    private EditText et_school_search;
    private Button btn_search,btn_back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_page);

        btn_search=(Button)findViewById(R.id.btn_search);
        btn_back=(Button)findViewById(R.id.btn_back);
        //一秒刷新一次

        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                run = true;
                handler.postDelayed(task, 2000);
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JoinPage.this.finish();
            }
        });

    }

    private final Runnable task = new Runnable() {
        @Override
        public void run() {
            // TODO Auto-generated method stub
            if (run) {
                et_school_search=(EditText)findViewById(R.id.et_school_search);
                school_search= et_school_search.getText().toString();

                BmobQuery<Org> query1= new BmobQuery<Org>();
                query1.addWhereEqualTo("school",school_search);
                BmobQuery<Org> query2= new BmobQuery<Org>();
                query2.addWhereEqualTo("orgname",school_search);
                List<BmobQuery<Org>> orQuerys = new ArrayList<BmobQuery<Org>>();
                orQuerys.add(query1);
                orQuerys.add(query2);

                BmobQuery<Org> query= new BmobQuery<Org>();
                query.or(orQuerys);
                query.setLimit(50);
                query.order("school,orgname");
                query.findObjects(new FindListener<Org>() {
                    @Override
                    public void done(final List<Org> list, BmobException e) {
                        if (e == null) {

                            ListView listView=(ListView)findViewById(R.id.lv_org_item_jioning);
                            listView.setAdapter(new OrgItemAdapter(JoinPage.this,list));
                            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                                    Org org_list=list.get(position);
                                    MyUser user = BmobUser.getCurrentUser(MyUser.class);
                                    BmobRelation relation = new BmobRelation();
                                    relation.add(org_list);
                                    user.setMyorg(relation);
                                    user.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e==null){
                                                Toast.makeText(getApplication(), "成功加入", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });

                                    Org org1=new Org();
                                    //关联当前org(s为当前org的id)
                                    org1.setObjectId(org_list.getObjectId());
                                    //将当前用户添加到Org表中的member字段值中，表明当前用户喜欢该帖子
                                    BmobRelation relation1 = new BmobRelation();
                                    //将当前用户添加到多对多关联中
                                    relation1.add(BmobUser.getCurrentUser(MyUser.class));
                                    //多对多关联指向`org`的`member`字段
                                    org1.setMember(relation1);
                                    org1.update(new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if(e==null){
                                                Toast.makeText(getApplication(), "成功加入", Toast.LENGTH_SHORT).show();
                                           }else{
                                                Log.i("bmob","失败："+e.getMessage());
                                            }
                                        }

                                   });
                                }
                            });

                        }
                    }


                });
                handler.postDelayed(this, 2000);
            }
        }
    };
}
