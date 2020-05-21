package com.example.a44261.bysj102.IM.db.dao;

import android.database.sqlite.SQLiteDatabase;


import com.example.a44261.bysj102.IM.db.NewFriend;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

public class DaoSession extends AbstractDaoSession {

    private final DaoConfig newFriendDaoConfig;

    private final NewFriendDao newFriendDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        newFriendDaoConfig = daoConfigMap.get(NewFriendDao.class).clone();
        newFriendDaoConfig.initIdentityScope(type);

        newFriendDao = new NewFriendDao(newFriendDaoConfig, this);

        registerDao(NewFriend.class, newFriendDao);
    }
    
    public void clear() {
        newFriendDaoConfig.getIdentityScope().clear();
    }

    public NewFriendDao getNewFriendDao() {
        return newFriendDao;
    }

}
