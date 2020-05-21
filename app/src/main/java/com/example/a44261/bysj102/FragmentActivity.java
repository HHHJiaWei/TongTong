package com.example.a44261.bysj102;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.a44261.bysj102.Fragment.Fragment1;
import com.example.a44261.bysj102.Fragment.Fragment2;
import com.example.a44261.bysj102.Fragment.Fragment3;
import com.example.a44261.bysj102.Fragment.Fragment4;
import com.example.a44261.bysj102.db.MyUser;



import cn.bmob.newim.BmobIM;
import cn.bmob.newim.core.ConnectionStatus;
import cn.bmob.newim.event.MessageEvent;
import cn.bmob.newim.event.OfflineMessageEvent;
import cn.bmob.newim.listener.ConnectListener;
import cn.bmob.newim.listener.ConnectStatusChangeListener;
import cn.bmob.newim.notification.BmobNotificationManager;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;

public class FragmentActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    private Fragment1 fragment1;
    private Fragment2 fragment2;
    private Fragment3 fragment3;
    private Fragment4 fragment4;
    private Fragment[] fragments;
    private int lastfragment;//用于记录上个选择的Fragment
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        Bmob.initialize(this, "7ee361ae2183671d9c529b40ebb1ea56");
        MyUser myUser=BmobUser.getCurrentUser(MyUser.class);
        if (null == myUser) {
            Intent intent=new Intent(FragmentActivity.this,LoginActivity.class);
            startActivity(intent);
            FragmentActivity.this.finish();

        } else {
            Toast.makeText(getApplication(), "当前用户："+myUser.getNickname() , Toast.LENGTH_SHORT).show();
            initFragment();
            //TODO 连接：3.1、登录成功、注册成功或处于登录状态重新打开应用后执行连接IM服务器的操作
            if (!TextUtils.isEmpty(myUser.getObjectId())) {
                BmobIM.connect(myUser.getObjectId(), new ConnectListener() {
                    @Override
                    public void done(String uid, BmobException e) {
                        if (e == null) {
                            //连接成功
                        } else {
                            //连接失败
                            Toast.makeText(getApplication(), e.getMessage() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
            //TODO 连接：3.3、监听连接状态，可通过BmobIM.getInstance().getCurrentStatus()来获取当前的长连接状态
            BmobIM.getInstance().setOnConnectStatusChangeListener(new ConnectStatusChangeListener() {
                @Override
                public void onChange(ConnectionStatus status) {
                    Toast.makeText(getApplication(), "" + status.getMsg() , Toast.LENGTH_SHORT).show();
                }
            });
            //解决leancanary提示InputMethodManager内存泄露的问题
            //IMMLeaks.fixFocusedViewLeak(getApplication());
        }

    }
    //初始化fragment和fragment数组,初始化了4个fragment 和bottomNaviationView 和一个初始显示的fragment
    private void initFragment()
    {

        fragment1 = new Fragment1();
        fragment2 = new Fragment2();
        fragment3 = new Fragment3();
        fragment4 = new Fragment4();
        fragments = new Fragment[]{fragment1,fragment2,fragment3,fragment4};
        lastfragment=0;
        getSupportFragmentManager().beginTransaction().replace(R.id.mainview,fragment1).show(fragment1).commit();
        bottomNavigationView=(BottomNavigationView)findViewById(R.id.bnv);

        bottomNavigationView.setOnNavigationItemSelectedListener(changeFragment);
    }
    //判断选择的菜单
    private BottomNavigationView.OnNavigationItemSelectedListener changeFragment= new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
    //对点击的item的id做了判断 然后通过switchFragment函数来进行界面的操作，lastfragment是表示上个被选中的导航栏item
            switch (item.getItemId())
            {
                case R.id.navigation_first:
                {
                    if(lastfragment!=0)
                    {
                        switchFragment(lastfragment,0);
                        lastfragment=0;

                    }

                    return true;
                }
                case R.id.navigation_second:
                {
                    if(lastfragment!=1)
                    {
                        switchFragment(lastfragment,1);
                        lastfragment=1;

                    }

                    return true;
                }
                case R.id.navigation_third:
                {
                    if(lastfragment!=2)
                    {
                        switchFragment(lastfragment,2);
                        lastfragment=2;

                    }

                    return true;
                }
                case R.id.navigation_fourth:
                {
                    if(lastfragment!=3)
                    {
                        switchFragment(lastfragment,3);
                        lastfragment=3;

                    }

                    return true;
                }


            }


            return false;
        }
    };
    //切换Fragment
    private void switchFragment(int lastfragment,int index)
    {
        FragmentTransaction transaction =getSupportFragmentManager().beginTransaction();
        transaction.hide(fragments[lastfragment]);//隐藏上个Fragment
        if(fragments[index].isAdded()==false)
        {
            transaction.add(R.id.mainview,fragments[index]);


        }
        transaction.show(fragments[index]).commitAllowingStateLoss();


    }

}
