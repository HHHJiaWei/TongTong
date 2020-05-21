package com.example.a44261.bysj102.db;

import com.example.a44261.bysj102.IM.db.NewFriend;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.datatype.BmobRelation;

public class MyUser extends BmobUser {

    private String nickname;
    private String sex;
    private String age;
    private String school;
    private BmobFile image;
    private String photo;
    private String avatar;
    private BmobRelation myorg;
    private BmobRelation myact;

    public BmobRelation getMyact() {
        return myact;
    }

    public void setMyact(BmobRelation myact) {
        this.myact = myact;
    }

    public BmobRelation getMyorg() {
        return myorg;
    }

    public void setMyorg(BmobRelation myorg) {
        this.myorg = myorg;
    }

    public MyUser(){}

    public MyUser(NewFriend friend){
        setObjectId(friend.getUid());
        setUsername(friend.getName());
        setAvatar(friend.getAvatar());
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public BmobFile getImage() {
        return image;
    }

    public void setImage(BmobFile image) {
        this.image = image;
    }


}
