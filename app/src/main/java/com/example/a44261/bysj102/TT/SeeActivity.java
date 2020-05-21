package com.example.a44261.bysj102.TT;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.a44261.bysj102.R;

public class SeeActivity extends AppCompatActivity {
    private TextView tv_actname,tv_actplace,tv_acttime,tv_advisetime,tv_actcontent;
    private String name,content,place,time,advisetime;
    private Button btn_back,btn_read;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see);

        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        name=bundle.getString("actname");
        place=bundle.getString("actplace");
        time=bundle.getString("acttime");
        advisetime=bundle.getString("advisetime");
        content=bundle.getString("actcontent");

        tv_actname=(TextView)findViewById(R.id.tv_actname);
        tv_actplace=(TextView)findViewById(R.id.tv_actplace);
        tv_acttime=(TextView)findViewById(R.id.tv_acttime);
        tv_advisetime=(TextView)findViewById(R.id.tv_advisetime);
        tv_actcontent=(TextView)findViewById(R.id.tv_actcontent);

        tv_actname.setText(name);
        tv_actplace.setText(place);
        tv_acttime.setText(time);
        tv_advisetime.setText(advisetime);
        tv_actcontent.setText(content);

        btn_back=(Button)findViewById(R.id.btn_back);
        btn_read=(Button)findViewById(R.id.btn_read);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SeeActivity.this.finish();
            }
        });
        btn_read.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}
