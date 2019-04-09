package com.durgesh.intern.eatenglishassignment.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SeekBarAdapter extends SQLiteOpenHelper {
        public SeekBarAdapter(Context context)
    {
        super(context,"speeds.db",null,1);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table speed (speed1 integer,speed2 integer,sound integer,speakSound integer,qsound integer,asound integer,choice integer) ");
        db.execSQL("insert into speed values(250,50,1,1,1,1,0);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS speed");
        onCreate(db);
    }
    public Cursor getData( )
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from speed",null);
        return res;
    }
    public boolean update(int speed1,int speed2,boolean v1,boolean v2,boolean v3,boolean v4,int v5)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        int a = v1?1:0;
        int b = v2?1:0;
        int c = v3?1:0;
        int d = v4?1:0;
        db.execSQL("delete from speed");
        db.execSQL("insert into speed values("+speed1+","+speed2+","+a+","+b+","+c+","+d+","+v5+")");
        return true;
    }

}
