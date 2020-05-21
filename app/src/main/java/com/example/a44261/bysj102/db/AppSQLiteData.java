package com.example.a44261.bysj102.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class AppSQLiteData extends SQLiteOpenHelper {

    public static String Remind_data="create table Remind_data("

            +"id integer primary key autoincrement, "

            +"remindTime long,"

            +"orgname text,"

            +"content text,"

            +"title text"+")";

    private Context mContext;

    public AppSQLiteData(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {

        super(context,name,factory,version);

        mContext= context;

    }

    @Override

    public void onCreate(SQLiteDatabase db) {

        db.execSQL(Remind_data);

    }


    @Override

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

}
