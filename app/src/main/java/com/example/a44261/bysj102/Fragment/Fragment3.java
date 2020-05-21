package com.example.a44261.bysj102.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a44261.bysj102.IM.ui.SearchUserActivity;
import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.TT.UserActivity;
import com.example.a44261.bysj102.TYQ.EditTYQActivity;
import com.example.a44261.bysj102.TYQ.EditTYQAdapter;
import com.example.a44261.bysj102.TYQ.TYQActivity;
import com.example.a44261.bysj102.db.MyUser;
import com.example.a44261.bysj102.db.TYQPublish;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;


public class Fragment3 extends Fragment {

    private TextView tv_right;
    private MyUser myUser;
    private EditTYQAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment3,container,false);
        myUser=BmobUser.getCurrentUser(MyUser.class);
        BmobQuery<TYQPublish> query1 = new BmobQuery<TYQPublish>();
        query1.addWhereEqualTo("school", myUser.getSchool());
        query1.order("-createdAt");
        query1.findObjects(new FindListener<TYQPublish>() {
            @Override
            public void done(List<TYQPublish> list, BmobException e) {
                if (e == null) {

                    RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.f3_rc_view);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter=new EditTYQAdapter(list);
                    recyclerView.setAdapter(adapter);
                }

            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        tv_right=(TextView)getActivity().findViewById(R.id.f3_tv_right);
        tv_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),EditTYQActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        BmobQuery<TYQPublish> query1 = new BmobQuery<TYQPublish>();
        query1.addWhereEqualTo("school", myUser.getSchool());
        query1.order("-createdAt");
        query1.findObjects(new FindListener<TYQPublish>() {
            @Override
            public void done(List<TYQPublish> list, BmobException e) {
                if (e == null) {

                    RecyclerView recyclerView=(RecyclerView)getActivity().findViewById(R.id.f3_rc_view);
                    LinearLayoutManager linearLayoutManager=new LinearLayoutManager(getActivity());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter=new EditTYQAdapter(list);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }

            }
        });
    }
}
