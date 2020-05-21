package com.example.a44261.bysj102.TT;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.db.Act;
import com.example.a44261.bysj102.db.Have;
import com.example.a44261.bysj102.db.MyUser;
import com.example.a44261.bysj102.db.Org;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class AddActivity extends AppCompatActivity {
    private Button oplandate,oplantime,btn_org_add,btn_back;
    private EditText et_actname,et_acttime,et_actplace,et_actcontent;
    private TextView tv_username;
    private Calendar calendar;
    private int mYear,mMonth,mDay,mHour,mMinute;
    private String id,orgname;
    private BmobRelation relation1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        Bmob.initialize(this,"7ee361ae2183671d9c529b40ebb1ea56");
        MyUser user = BmobUser.getCurrentUser(MyUser.class);

        Intent intent=getIntent();
        Bundle bundle1=intent.getExtras();
        id=bundle1.getString("id");
        orgname=bundle1.getString("orgname");

        calendar=Calendar.getInstance();

        btn_back=(Button) findViewById(R.id.btn_back);
        oplandate=(Button)findViewById(R.id.oplandate);
        oplantime=(Button)findViewById(R.id.oplantime);
        btn_org_add=(Button)findViewById(R.id.btn_org_add);
        et_actname=(EditText)findViewById(R.id.et_actname);
        et_acttime=(EditText)findViewById(R.id.et_acttime);
        et_actplace=(EditText)findViewById(R.id.et_actplace);
        et_actcontent=(EditText)findViewById(R.id.et_actcontent);
        tv_username=(TextView)findViewById(R.id.tv_username);

        tv_username.setText(user.getUsername());

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddActivity.this.finish();
            }
        });

        setDate();
        setTime();

        btn_org_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String actname,acttime,actplace,actcontent,date,time;
                actname=et_actname.getText().toString();
                acttime=et_acttime.getText().toString();
                actplace=et_actplace.getText().toString();
                actcontent=et_actcontent.getText().toString();
                date=oplandate.getText().toString();
                time=oplantime.getText().toString();

                if(TextUtils.isEmpty(actname)){
                    Toast.makeText(AddActivity.this, "请输入活动名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(acttime)){
                    Toast.makeText(AddActivity.this, "请输入活动时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(actplace)){
                    Toast.makeText(AddActivity.this, "请输入活动地点", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(date.equals("设置日期")){
                    Toast.makeText(AddActivity.this, "请设置日期", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(time.equals("设置时间")){
                    Toast.makeText(AddActivity.this, "请设置时间", Toast.LENGTH_SHORT).show();
                    return;
                }

                String createdAt = date+" "+time;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date createdAtDate = new Date();
                try {
                    createdAtDate = sdf.parse(createdAt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);

                final Act act=new Act();
                act.setActname(actname);
                act.setActtime(acttime);
                act.setActplace(actplace);
                act.setActcontent(actcontent);
                act.setActadvisetime(bmobCreatedAtDate);
                act.setOrganization(orgname);
                act.save(new SaveListener<String>() {
                    @Override
                    public void done(final String s, BmobException e) {
                        if (e == null) {
                            act.setObjectId(s);
                            Org org=new Org();
                            org.setObjectId(id);
                            BmobRelation relation = new BmobRelation();
                            relation.add(act);
                            org.setMyacyivity(relation);
                            org.update(new UpdateListener() {
                                @Override
                                public void done(BmobException e) {
                                    if (e==null){
                                        Log.e("Bmob","myactivity添加成功");
//                                        Toast.makeText(getApplication(), "创建成功", Toast.LENGTH_SHORT).show();
                                    }else {
                                        Log.e("Bmob","myactivity添加失败"+e.getMessage());
                                        Toast.makeText(getApplication(), "创建活动失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                            BmobQuery<MyUser> query = new BmobQuery<MyUser>();
                            Org org1=new Org();
                            org1.setObjectId(id);
                            query.addWhereRelatedTo("member", new BmobPointer(org1));
                            query.findObjects(new FindListener<MyUser>() {
                                @Override
                                public void done(List<MyUser> object, BmobException e) {
                                    if(e==null){
                                        Log.e("BMOB", "member："+object.size());

                                        relation1 = new BmobRelation();
                                        relation1.add(act);

                                        for(int i=0;i<object.size();i++){
                                            BmobQuery<Have> query1=new BmobQuery<Have>();
                                            query1.addWhereEqualTo("userid",object.get(i).getObjectId());
                                            query1.findObjects(new FindListener<Have>() {
                                                @Override
                                                public void done(List<Have> list, BmobException e) {
                                                    if (e==null){
                                                        for (Have have:list){
                                                            have.setActid(relation1);
                                                            have.update(new UpdateListener() {
                                                                @Override
                                                                public void done(BmobException e) {
                                                                    if(e==null){
                                                                        Log.e("bmob","多对多关联添加成功");
                                                                    }else{
                                                                        Log.e("bmob","失败："+e.getMessage());
                                                                    }
                                                                }
                                                            });
                                                        }
                                                    }
                                                }
                                            });
                                        }

                                    }else{
                                        Log.e("bmob","member查询失败"+e.getMessage());
                                        Toast.makeText(getApplication(), "创建活动失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                        } else {
                            Log.e("bmob","act创建失败"+e.getMessage());
                            Toast.makeText(getApplication(), "创建活动失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                AddActivity.this.finish();

            }
        });
    }

    public void setTime(){
        //点击"时间"按钮布局 设置时间
        oplantime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义控件
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                View view = (LinearLayout) getLayoutInflater().inflate(R.layout.time_dialog, null);
                final TimePicker timePicker = (TimePicker) view.findViewById(R.id.time_picker);
                //初始化时间
                calendar.setTimeInMillis(System.currentTimeMillis());
                timePicker.setIs24HourView(true);
                timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
                //timePicker.setCurrentMinute(Calendar.MINUTE);
                //设置time布局
                builder.setView(view);
                builder.setTitle("设置时间信息");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mHour = timePicker.getCurrentHour();
                        mMinute = timePicker.getCurrentMinute();
                        //时间小于10的数字 前面补0 如01:12:00
                        oplantime.setText(new StringBuilder().append(mHour < 10 ? "0" + mHour : mHour).append(":")
                                .append(mMinute < 10 ? "0" + mMinute : mMinute).append(":00") );
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }

    public void setDate(){
        //点击"日期"按钮布局 设置日期
        oplandate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //通过自定义控件AlertDialog实现
                AlertDialog.Builder builder = new AlertDialog.Builder(AddActivity.this);
                View view = (LinearLayout) getLayoutInflater().inflate(R.layout.date_dialog, null);
                final DatePicker datePicker = (DatePicker) view.findViewById(R.id.date_picker);
                //设置日期简略显示 否则详细显示 包括:星期\周
                datePicker.setCalendarViewShown(false);
                //初始化当前日期
                calendar.setTimeInMillis(System.currentTimeMillis());
                datePicker.init(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH), null);
                //设置date布局
                builder.setView(view);
                builder.setTitle("设置日期信息");
                builder.setPositiveButton("确  定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //日期格式
                        StringBuffer sb = new StringBuffer();
                        sb.append(String.format("%d-%02d-%02d",
                                datePicker.getYear(),
                                datePicker.getMonth() + 1,
                                datePicker.getDayOfMonth()));
                        oplandate.setText(sb);
                        //赋值后面闹钟使用
                        mYear = datePicker.getYear();
                        mMonth = datePicker.getMonth();
                        mDay = datePicker.getDayOfMonth();
                        dialog.cancel();
                    }
                });
                builder.setNegativeButton("取  消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.create().show();
            }
        });
    }


}
