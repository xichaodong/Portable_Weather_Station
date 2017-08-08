package com.sara.com.portable_weather_station.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by 17682 on 2017/2/28.
 */

public class DBCreate extends SQLiteOpenHelper {
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public DBCreate(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql1 = "create table sstqqk (CSBM  VARCHAR(16) NOT NULL,SJRQ DATE NOT NULL,ZGWD VARCHAR(8) DEFAULT '0', ZDWD VARCHAR(8) DEFAULT '0',SSWD VARCHAR(8) DEFAULT '0',SSYQ VARCHAR(8) DEFAULT '0',XDWD VARCHAR(8) DEFAULT '0',SSAPI VARCHAR(8) DEFAULT '0',PM10 VARCHAR(8) DEFAULT '0' ,PM2_5 VARCHAR(8) DEFAULT '0',CO VARCHAR(8) DEFAULT '0',NO2 VARCHAR(8) DEFAULT '0', SO2 VARCHAR(8) DEFAULT '0',QYQK VARCHAR(8) DEFAULT '0',LNQK VARCHAR(8) DEFAULT'0',NLRQ VARCHAR(8) DEFAULT '0' ,YJSY VARCHAR(24) DEFAULT '0',DSJY VARCHAR(64) DEFAULT '0',CYJY VARCHAR(24) DEFAULT '0', XCJY VARCHAR(24) DEFAULT '0',ZWXJY VARCHAR(24) DEFAULT '0',DRSJ DATE NOT NULL,PRIMARY KEY(CSBM,SJRQ,DRSJ));";


        String sql2 = "create table sdtqqk(CSBM VARCHAR(100) NOT NULL,SJSK DATE NOT NULL,WD VARCHAR(24) DEFAULT '0'" +
                ",FLFX VARCHAR(24) DEFAULT '0',SSAPI VARCHAR(24) DEFAULT '0',DRSJ DATE NOT NULL,PRIMARY KEY(CSBM,SJSK,DRSJ))";

       String sql3 = "create table wltqqk(CSBM VARCHAR(100) NOT NULL,SJRQ DATE NOT NULL,ZGWD VARCHAR(24) DEFAULT '0'," +
               "ZDWD VARCHAR(24) DEFAULT '0',FLFX VARCHAR(24) DEFAULT '0',DRSJ DATE NOT NULL,PRIMARY KEY(CSBM,SJRQ,DRSJ))";
        db.execSQL(sql1);
        db.execSQL(sql2);
        db.execSQL(sql3);
    }
}
