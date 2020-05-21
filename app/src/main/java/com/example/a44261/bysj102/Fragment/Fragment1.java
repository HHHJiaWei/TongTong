package com.example.a44261.bysj102.Fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.a44261.bysj102.R;
import com.example.a44261.bysj102.TT.FoundPage;
import com.example.a44261.bysj102.TT.HomePage;
import com.example.a44261.bysj102.TT.JoinPage;
import com.example.a44261.bysj102.TT.UserActivity;
import com.example.a44261.bysj102.db.MyUser;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

public class Fragment1 extends Fragment {
    private Button button;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment1,container,false);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Button btn_found = (Button) getActivity().findViewById(R.id.btn_found);
        Button btn_join = (Button) getActivity().findViewById(R.id.btn_join);
        Button btn_home = (Button) getActivity().findViewById(R.id.btn_home);
        Button btn_org_list=(Button)getActivity().findViewById(R.id.btn_org_list);

        btn_found.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FoundPage.class);
                startActivity(intent);
            }
        });

        btn_join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), JoinPage.class);
                startActivity(intent);
            }
        });
        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), HomePage.class);
                startActivity(intent);
            }
        });
        btn_org_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(getActivity(),UserActivity.class);
                startActivity(intent);
            }
        });

    }
}