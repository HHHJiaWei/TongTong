package com.example.a44261.bysj102.db;

import cn.bmob.v3.BmobObject;

public class Join extends BmobObject {
    private String userid;
    private String orgid;

    public String getOrgid() {
        return orgid;
    }

    public void setOrgid(String orgid) {
        this.orgid = orgid;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }


}
