package com.example.a44261.bysj102.TT;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.TT.Adapter.OrgActItemAdapter;
import com.example.a44261.bysj102.db.Act;
import com.example.a44261.bysj102.db.AppSQLiteData;
import com.example.a44261.bysj102.db.Have;
import com.example.a44261.bysj102.db.MyUser;

import java.util.Calendar;
import java.util.List;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobPointer;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

import static cn.bmob.v3.datatype.BmobDate.getTimeStamp;

public class HomePage extends AppCompatActivity {

    private AppSQLiteData mRemindSQL;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        Bmob.initialize(this, "7ee361ae2183671d9c529b40ebb1ea56");
        MyUser user = BmobUser.getCurrentUser(MyUser.class);

            mRemindSQL = new AppSQLiteData(this, "Remind.db", null, 1);
            mRemindSQL.getWritableDatabase();

            Button btn_found = (Button) findViewById(R.id.btn_found);
            Button btn_join = (Button) findViewById(R.id.btn_join);

            btn_found.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomePage.this, FoundPage.class);
                    startActivity(intent);
                }
            });

            btn_join.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(HomePage.this, JoinPage.class);
                    startActivity(intent);
                }
            });

            final BmobQuery<Act> query = new BmobQuery<Act>();
            BmobQuery<Have> query1 = new BmobQuery<Have>();
            query1.addWhereEqualTo("userid", user.getObjectId());
            query1.findObjects(new FindListener<Have>() {
                @Override
                public void done(List<Have> list, BmobException e) {
                    if (e == null) {
                        for (Have have : list) {
                            query.addWhereRelatedTo("actid", new BmobPointer(have));
                            query.order("actadvisetime");
                            query.findObjects(new FindListener<Act>() {
                                @Override
                                public void done(final List<Act> object, BmobException e) {
                                    if (e == null) {
                                        ListView listView = (ListView) findViewById(R.id.lv_my_org_activity_item);
                                        listView.setAdapter(new OrgActItemAdapter(HomePage.this, object));
                                        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent intent = new Intent(HomePage.this, SeeActivity.class);
                                                Act act = object.get(i);
                                                Bundle bundle = new Bundle();
                                                bundle.putString("actid", act.getObjectId());
                                                bundle.putString("actname", act.getActname());
                                                bundle.putString("actcontent", act.getActcontent());
                                                bundle.putString("acttime", act.getActtime());
                                                bundle.putString("actplace", act.getActplace());
                                                bundle.putString("advisetime", act.getActadvisetime().getDate());
                                                intent.putExtras(bundle);
                                                startActivity(intent);
                                            }
                                        });


                                                Calendar c = Calendar.getInstance();//获取当前时间，为了判断添加时间是否已经过时
                                                Log.e("NowTime:", "" + c.getTimeInMillis());
                                                SQLiteDatabase db = mRemindSQL.getWritableDatabase();
                                                db.execSQL("delete from Remind_data");
                                                ContentValues values = new ContentValues();
                                                for (Act data : object) {
                                                    if (!data.getActadvisetime().equals("") && getTimeStamp(data.getActadvisetime().getDate()) > c.getTimeInMillis()) {
                                                        values.put("remindTime", getTimeStamp(data.getActadvisetime().getDate()));
                                                        values.put("orgname", data.getOrganization());
                                                        values.put("title", data.getActname());
                                                        values.put("content", data.getActtime()+"  "+data.getActplace());
                                                        db.insert("Remind_data", null, values);
                                                    }
                                                    values.clear();
                                                }


                                    }
                                }
                            });
                        }
                    }
                }
            });

            SQLiteDatabase db1 = mRemindSQL.getWritableDatabase();
            Cursor cursor = db1.query("Remind_data", null, null, null, null, null, null);
            Log.e("Cursor:", "" + cursor.getCount());
            if (cursor.moveToFirst()) { //遍历数据库的表，拿出一条，选择最近的时间赋值，作为第一条提醒数据。
                do {
                    Log.e("Time:", "" + cursor.getLong(cursor.getColumnIndex("remindTime")));
                    Log.e("Title:", cursor.getString(cursor.getColumnIndex("title")));
                    Log.e("Content:", cursor.getString(cursor.getColumnIndex("content")));
                } while (cursor.moveToNext());
            }
            cursor.close();

            Intent intent1 = new Intent(this, AlarmService.class);
            if(Build.VERSION.SDK_INT>=26){
                startForegroundService (intent1);
            }else{
                startService (intent1);
            }





    }

}
