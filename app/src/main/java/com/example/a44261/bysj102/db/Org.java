package com.example.a44261.bysj102.db;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;

public class Org extends BmobObject {
    private String orgname;
    private MyUser organizer;
    private String orger_name;
    private String school;
    private BmobRelation member;
    private BmobRelation myacyivity;
    private BmobRelation manager;

    public BmobRelation getManager() {
        return manager;
    }

    public void setManager(BmobRelation manager) {
        this.manager = manager;
    }

    public BmobRelation getMyacyivity() {
        return myacyivity;
    }

    public void setMyacyivity(BmobRelation myacyivity) {
        this.myacyivity = myacyivity;
    }

    public Org() {
        this.orgname = orgname;
        this.organizer = organizer;
        this.orger_name=orger_name;
        this.school = school;
        this.member = member;
        this.myacyivity=myacyivity;
        this.manager=manager;

    }


    public String getOrgname() {
        return orgname;
    }

    public Org setOrgname(String orgname) {
        this.orgname = orgname;
        return this;
    }

    public MyUser getOrganizer() {
        return organizer;
    }

    public Org setOrganizer(MyUser organizer) {
        this.organizer = organizer;
        return this;

    }

    public String getOrger_name() {
        return orger_name;
    }

    public void setOrger_name(String org_name) {
        this.orger_name = org_name;
    }

    public String getSchool() {
        return school;
    }

    public Org setSchool(String school) {
        this.school = school;
        return this;

    }

    public BmobRelation getMember() {
        return member;
    }

    public Org setMember(BmobRelation member) {
        this.member = member;
        return this;

    }
}
