package com.example.a44261.bysj102.TT;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.TT.Adapter.OrgActItemAdapter;
import com.example.a44261.bysj102.TT.Adapter.OrgmemberAdapter;
import com.example.a44261.bysj102.db.Act;
import com.example.a44261.bysj102.db.MyUser;
import com.example.a44261.bysj102.db.Org;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.UpdateListener;

public class OrganizationActivity extends AppCompatActivity {
    private TextView tv_main_title;
    private Button btn_add_activity,btn_back;
    private String id,orgname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_organization);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        id=bundle.getString("id");
        orgname=bundle.getString("orgname");

        tv_main_title=(TextView)findViewById(R.id.tv_main_title);
        btn_add_activity=(Button)findViewById(R.id.btn_add_activity);
        btn_back=(Button)findViewById(R.id.btn_back);

        tv_main_title.setText(orgname);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                OrganizationActivity.this.finish();
            }
        });

        btn_add_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(OrganizationActivity.this,AddActivity.class);
                Bundle bundle1=new Bundle();
                bundle1.putString("id", id);
                bundle1.putString("orgname",orgname);
                intent.putExtras(bundle1);
                startActivity(intent);
            }
        });

        BmobQuery<Act> query= new BmobQuery<Act>();
        Org org=new Org();
        org.setObjectId(id);
        query.addWhereRelatedTo("myacyivity",new BmobPointer(org));
//        query.addWhereEqualTo("organizer", userobject);
        query.setLimit(50);
        query.order("school,orgname");
        query.findObjects(new FindListener<Act>() {
            @Override
            public void done(final List<Act> list, BmobException e) {
                if (e == null) {
                    //listView.setAdapter(new MyAdapter(MainActivity.this , list));
                    ListView listView=(ListView)findViewById(R.id.lv_org_activity_item);
                    listView.setAdapter(new OrgActItemAdapter(OrganizationActivity.this,list));
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            Intent intent1=new Intent(OrganizationActivity.this,UpdateActivity.class);
                            Act act=list.get(i);
                            Bundle bundle=new Bundle();
                            bundle.putString("actid",act.getObjectId());
                            bundle.putString("actname",act.getActname());
                            bundle.putString("actcontent",act.getActcontent());
                            bundle.putString("acttime",act.getActtime());
                            bundle.putString("actplace",act.getActplace());
                            bundle.putString("advisetime",act.getActadvisetime().toString());
                            intent1.putExtras(bundle);
                            startActivity(intent1);
                        }
                    });

                    listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int num, long l) {
                            AlertDialog.Builder builder=new AlertDialog.Builder(OrganizationActivity.this);
                            builder.setMessage("确定删除该活动?");
                            builder.setTitle("提示");
                            builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    Act act=new Act();
                                    act.delete(list.get(num).getObjectId(), new UpdateListener() {
                                        @Override
                                        public void done(BmobException e) {
                                            if (e == null) {
                                                Toast.makeText(getBaseContext(), "删除活动成功", Toast.LENGTH_SHORT).show();
                                            } else {
                                                Log.e("BMOB", e.toString());
                                                Toast.makeText(getBaseContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
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

        Org org1=new Org();
        org1.setObjectId(id);
        BmobQuery<MyUser> query1 = new BmobQuery<MyUser>();
        query1.addWhereRelatedTo("member", new BmobPointer(org1));
        query1.findObjects(new FindListener<MyUser>() {

            @Override
            public void done(List<MyUser> object, BmobException e) {
                if(e==null){
                    ListView listView1=(ListView)findViewById(R.id.lv_org_member);
                    listView1.setAdapter(new OrgmemberAdapter(OrganizationActivity.this,object));
                    registerForContextMenu(listView1);
                }else{
                    Toast.makeText(getApplication(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.setHeaderTitle("管理");
        getMenuInflater().inflate(R.menu.member, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch ( item.getItemId() ) {
            case R.id.manager:
                manager(item);
                return true;
            case R.id.delete :
                delete(item);
                return true;
            default:  return false;
        }
    }

    public void manager(MenuItem item){
        AdapterView.AdapterContextMenuInfo info1 = (AdapterView.AdapterContextMenuInfo)item.getMenuInfo();
        TextView mn=(TextView)info1.targetView.findViewById(R.id.org_member);
        String membername=mn.getText().toString();
        BmobQuery<MyUser> query1 = new BmobQuery<MyUser>();
        query1.addWhereEqualTo("username",membername);
        query1.findObjects(new FindListener<MyUser>() {
            @Override
            public void done(List<MyUser> list, BmobException e) {
                if (e==null){
                    for(MyUser user : list){
                        Org org1=new Org();
                        org1.setObjectId(id);
                        BmobRelation relation1 = new BmobRelation();
                        relation1.add(user);
                        org1.setManager(relation1);
                        org1.update(new UpdateListener() {
                            @Override
                            public void done(BmobException e) {
                                if(e==null){
                                    Toast.makeText(getApplication(), "设置成功", Toast.LENGTH_SHORT).show();
                                }else{
                                    Log.i("bmob","失败："+e.getMessage());
                                }
                            }

                        });
                    }
                }
            }
        });
    }

    public void delete(MenuItem item){
        AlertDialog.Builder builder=new AlertDialog.Builder(OrganizationActivity.this);
        builder.setMessage("确定将该成员移出团队?");
        builder.setTitle("提示");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(getBaseContext(), "成功将该成员移出团队", Toast.LENGTH_SHORT).show();

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        builder.create().show();
    }
}
