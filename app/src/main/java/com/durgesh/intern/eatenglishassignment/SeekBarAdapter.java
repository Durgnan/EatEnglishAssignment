package com.durgesh.intern.eatenglishassignment;

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
        db.execSQL("create table speed (speed1 integer,speed2 integer) ");
        db.execSQL("insert into speed values(200,50);");
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
    public boolean update(int speed1,int speed2)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("delete from speed");
        db.execSQL("insert into speed values("+speed1+","+speed2+")");
        return true;
    }

}
