package com.example.a44261.bysj102.db;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class Have extends BmobObject {
    private String userid;
    private BmobRelation actid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public BmobRelation getActid() {
        return actid;
    }

    public void setActid(BmobRelation actid) {
        this.actid = actid;
    }
}
