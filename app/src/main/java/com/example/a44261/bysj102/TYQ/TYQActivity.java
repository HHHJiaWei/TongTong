package com.example.a44261.bysj102.TYQ;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.db.MyUser;
import com.example.a44261.bysj102.db.TYQPublish;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

public class TYQActivity extends AppCompatActivity {
    private List<TYQPublish> studentList=new ArrayList<>();
    private MyUser myUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tyq);
        myUser=BmobUser.getCurrentUser(MyUser.class);

        BmobQuery<TYQPublish> query1 = new BmobQuery<TYQPublish>();
        query1.addWhereEqualTo("school", myUser.getSchool());
        query1.order("-updateAt");
        query1.findObjects(new FindListener<TYQPublish>() {
            @Override
            public void done(List<TYQPublish> list, BmobException e) {
                if (e == null) {

                    RecyclerView recyclerView=findViewById(R.id.tyq_rc_view);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getApplication());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    EditTYQAdapter studentAdapter=new EditTYQAdapter(list);
                    recyclerView.setAdapter(studentAdapter);
                }

            }
        });
    }
}
