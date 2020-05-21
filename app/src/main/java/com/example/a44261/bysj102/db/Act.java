package com.example.a44261.bysj102.db;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobDate;

public class Act extends BmobObject {
    private String actname;
    private String actcontent;
    private String actplace;
    private String acttime;
    private BmobDate actadvisetime;
    private String organization;

    public String getActname() {
        return actname;
    }

    public void setActname(String actname) {
        this.actname = actname;
    }

    public String getActcontent() {
        return actcontent;
    }

    public void setActcontent(String actcontent) {
        this.actcontent = actcontent;
    }

    public String getActplace() {
        return actplace;
    }

    public void setActplace(String actplace) {
        this.actplace = actplace;
    }

    public String getActtime() {
        return acttime;
    }

    public void setActtime(String acttime) {
        this.acttime = acttime;
    }

    public BmobDate getActadvisetime() {
        return actadvisetime;
    }

    public void setActadvisetime(BmobDate actadvisetime) {
        this.actadvisetime = actadvisetime;
    }

    public String getOrganization() {
        return organization;
    }

    public void setOrganization(String organization) {
        this.organization = organization;
    }

    public Act() {
        this.actname = actname;
        this.actcontent = actcontent;
        this.actplace = actplace;
        this.acttime = acttime;
        this.actadvisetime = actadvisetime;
        this.organization = organization;
    }
}
