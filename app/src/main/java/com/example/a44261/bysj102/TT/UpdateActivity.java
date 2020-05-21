package com.example.a44261.bysj102.TT;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


import cn.bmob.v3.datatype.BmobDate;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

public class UpdateActivity extends AppCompatActivity {
    private TextView et_actname,et_actplace,et_acttime,et_advisetime,et_content;
    private String id,name,content,place,time,advisetime;
    private String actname,acttime,actplace,actcontent,addate,adtime;
    private Button btn_back,btn_act_update,oplandate,oplantime;
    private int mYear,mMonth,mDay,mHour,mMinute;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        Intent intent1=getIntent();
        Bundle bundle=intent1.getExtras();
        id=bundle.getString("actid");
        name=bundle.getString("actname");
        place=bundle.getString("actplace");
        time=bundle.getString("acttime");
        content=bundle.getString("actcontent");

        et_actname=(EditText)findViewById(R.id.et_actname);
        et_actplace=(EditText)findViewById(R.id.et_actplace);
        et_acttime=(EditText)findViewById(R.id.et_acttime);
        et_content=(EditText)findViewById(R.id.et_actcontent);

        et_actname.setText(name);
        et_actplace.setText(place);
        et_acttime.setText(time);
        et_content.setText(content);

        oplandate=(Button)findViewById(R.id.oplandate);
        oplantime=(Button)findViewById(R.id.oplantime);
        btn_back=(Button)findViewById(R.id.btn_back);
        btn_act_update=(Button)findViewById(R.id.btn_act_update);

        setDate();
        setTime();

        calendar=Calendar.getInstance();


        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                UpdateActivity.this.finish();
            }
        });

        btn_act_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actname=et_actname.getText().toString();
                actcontent=et_content.getText().toString();
                actplace=et_actplace.getText().toString();
                acttime=et_acttime.getText().toString();
                addate=oplandate.getText().toString();
                adtime=oplantime.getText().toString();

                if(TextUtils.isEmpty(actname)){
                    Toast.makeText(UpdateActivity.this, "请输入活动名称", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(acttime)){
                    Toast.makeText(UpdateActivity.this, "请输入活动时间", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(TextUtils.isEmpty(actplace)){
                    Toast.makeText(UpdateActivity.this, "请输入活动地点", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(addate.equals("设置日期")){
                    Toast.makeText(UpdateActivity.this, "请设置日期", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(adtime.equals("设置时间")){
                    Toast.makeText(UpdateActivity.this, "请设置时间", Toast.LENGTH_SHORT).show();
                    return;
                }

                String createdAt = addate+" "+adtime;
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date createdAtDate = new Date();
                try {
                    createdAtDate = sdf.parse(createdAt);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                BmobDate bmobCreatedAtDate = new BmobDate(createdAtDate);

                Act act=new Act();
                act.setActname(actname);
                act.setActtime(acttime);
                act.setActplace(actplace);
                act.setActcontent(actcontent);
                act.setActadvisetime(bmobCreatedAtDate);
                act.update(id, new UpdateListener() {
                    @Override
                    public void done(BmobException e) {
                        if (e==null){
                            Toast.makeText(UpdateActivity.this, "修改成功", Toast.LENGTH_SHORT).show();
                            UpdateActivity.this.finish();

                        }
                    }
                });

            }
        });
    }

    public void setTime(){
        //点击"时间"按钮布局 设置时间
        oplantime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //自定义控件
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
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
                AlertDialog.Builder builder = new AlertDialog.Builder(UpdateActivity.this);
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
