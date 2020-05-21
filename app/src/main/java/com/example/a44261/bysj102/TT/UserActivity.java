package com.example.a44261.bysj102.TT;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.TT.Adapter.OrgItemAdapter;
import com.example.a44261.bysj102.db.MyUser;
import com.example.a44261.bysj102.db.Org;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class UserActivity extends AppCompatActivity {
    private ImageButton ibtn_aitem,ibtn_pcenter;
    private TextView tv_username_now;
    private Button logout;
    private ImageView iv_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        final MyUser user = BmobUser.getCurrentUser(MyUser.class);
        String username_now=user.getUsername();
        String userobject=user.getObjectId();

        tv_username_now=(TextView)findViewById(R.id.username_now);

        tv_username_now.setText(username_now);

        BmobQuery<Org> query= new BmobQuery<Org>();
        query.addWhereEqualTo("organizer", userobject);
        query.setLimit(50);
        query.include("username");
        query.order("school,orgname");
        query.findObjects(new FindListener<Org>() {
            @Override
            public void done(final List<Org> list, BmobException e) {
                if (e == null) {
                    //listView.setAdapter(new MyAdapter(MainActivity.this , list));
                    ListView listView=(ListView)findViewById(R.id.lv_org_item_found);
                    listView.setAdapter(new OrgItemAdapter(UserActivity.this,list));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            Org org=list.get(position);
                            Intent intent=new Intent(UserActivity.this,OrganizationActivity.class);
                            Bundle bundle=new Bundle();
                            bundle.putString("id", org.getObjectId());
                            bundle.putString("orgname",org.getOrgname());
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                    });

                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int num, long l) {
                            AlertDialog.Builder builder=new AlertDialog.Builder(UserActivity.this);
                            builder.setMessage("确定解散该团队?");
                            builder.setTitle("提示");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getBaseContext(), "成功解散该团队", Toast.LENGTH_SHORT).show();

                                }
                            });
                            //添加AlertDialog.Builder对象的setNegativeButton()方法
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            builder.create().show();
                            return true;
                        }
                    });

                }
            }

        });

        BmobQuery<Org> query1= new BmobQuery<Org>();
        MyUser user1=new MyUser();
        user1.setObjectId(user.getObjectId());
        query1.addWhereRelatedTo("myorg",new BmobPointer(user1));
        query1.setLimit(50);
        query1.include("username");
        query1.order("school,orgname");
        query1.findObjects(new FindListener<Org>() {
            @Override
            public void done(final List<Org> list1, BmobException e) {
                if (e == null) {
                    //listView.setAdapter(new MyAdapter(MainActivity.this , list));
                    ListView listView=(ListView)findViewById(R.id.lv_org_item_jion);
                    listView.setAdapter(new OrgItemAdapter(UserActivity.this,list1));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            final Org org1=list1.get(position);
                            BmobQuery<MyUser> query1 = new BmobQuery<MyUser>();
                            query1.addWhereRelatedTo("manager", new BmobPointer(org1));
                            query1.findObjects(new FindListener<MyUser>() {
                                @Override
                                public void done(List<MyUser> object, BmobException e) {
                                    if(e==null){
                                        for(MyUser user1 : object){
                                            if (user.getObjectId().equals(user1.getObjectId())){
                                                Intent intent=new Intent(UserActivity.this,OrganizationActivity.class);
                                                Bundle bundle=new Bundle();
                                                bundle.putString("id", org1.getObjectId());
                                                bundle.putString("orgname",org1.getOrgname());
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                            else {
                                            }
                                        }
                                    }else{
                                        Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }

                            });

                        }
                    });
                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int num, long l) {
                            AlertDialog.Builder builder=new AlertDialog.Builder(UserActivity.this);
                            builder.setMessage("确定退出该团队?");
                            builder.setTitle("提示");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Toast.makeText(getBaseContext(), "成功退出该团队", Toast.LENGTH_SHORT).show();

                                }
                            });
                            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            });
                            builder.create().show();
                            return true;
                        }
                    });
                }
            }

        });

    }


}
