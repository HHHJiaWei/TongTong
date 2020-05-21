package com.example.a44261.bysj102.IM.model.i;


import com.example.a44261.bysj102.db.MyUser;
import cn.bmob.newim.listener.BmobListener1;
import cn.bmob.v3.exception.BmobException;

public abstract class QueryUserListener extends BmobListener1<MyUser> {

    public abstract void done(MyUser s, BmobException e);

    @Override
    protected void postDone(MyUser o, BmobException e) {
        done(o, e);
    }
}
