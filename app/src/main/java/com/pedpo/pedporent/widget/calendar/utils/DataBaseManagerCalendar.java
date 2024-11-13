package com.pedpo.pedporent.widget.calendar.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pedpo.pedporent.widget.calendar.to.MarkTO;

import java.util.ArrayList;

public class DataBaseManagerCalendar extends SQLiteOpenHelper {

    private static final String NAME_TABLE = "cal";
    private static final String DATE = "date";
    private static final String COLOR = "color";

    private SQLiteDatabase sqLiteDatabase ;

    public DataBaseManagerCalendar(Context context)
    {
        super(context,"calendar",null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL("create table if not exists "+NAME_TABLE+" (ID INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL UNIQUE , "+DATE+" varchar , "+COLOR+" int )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists "+NAME_TABLE+"");
    }

    public void open()
    {
        sqLiteDatabase = this.getWritableDatabase();
    }
    public void close()
    {
        sqLiteDatabase.close();
    }
    public void insert(MarkTO markTO )
    {
        sqLiteDatabase.execSQL("insert into "+NAME_TABLE+" ("+DATE+" , "+COLOR+") values ( '"+markTO.getDate()+"' , "+markTO.getColor()+" )");
    }

    public ArrayList<MarkTO> getSelect()
    {
        ArrayList<MarkTO> arrayList = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from "+NAME_TABLE+"",null);
        String context = "";

        while (cursor.moveToNext())
        {
            MarkTO markTO = new MarkTO();
            markTO.setDate(cursor.getString(cursor.getColumnIndex(DATE)));
            markTO.setColor(cursor.getInt(cursor.getColumnIndex(COLOR)));
            arrayList.add(markTO);
        }
        return arrayList ;
    }


}
