package com.example.sahu.addloc;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Message;

import java.sql.SQLDataException;
import java.sql.SQLException;

/**
 * Created by Sahu on 10/30/2014.
 */
public class DbHelperAdapter {

    DbHelper helper;
    SQLiteDatabase sqLiteDatabase;
    public DbHelperAdapter(Context context)
    {
        helper=new DbHelper(context);
    }


    public long insertData(String name,String number,double lat,double lon)
    {
        sqLiteDatabase=helper.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DbHelper.Name,name);
        contentValues.put(DbHelper.Number,number);
        contentValues.put(DbHelper.Lati,lat);
        contentValues.put(DbHelper.Longi,lon);
        long id= sqLiteDatabase.insert(DbHelper.Table_Name,null,contentValues);
        return id;

    }

    public Cursor all() {
        sqLiteDatabase=helper.getWritableDatabase();
        Cursor cursor = sqLiteDatabase.query(
                DbHelper.Table_Name, //Table Name
                new String[]{DbHelper.Name, DbHelper.Number, DbHelper.Lati, DbHelper.Longi},
                null,//where
                null,//Where param
                null, //groupBy
                null, //having
                null //orderBy
        );

        return cursor;

    }

    public Cursor query(String name){
        sqLiteDatabase=helper.getReadableDatabase();
        String query="SELECT Name,Number,Lati,Longi FROM MyFriends WHERE Name='"+name+"';";
        Cursor c=sqLiteDatabase.rawQuery(query,new String[]{});
        return c;
    }

    public void Delete(String name){
        sqLiteDatabase=helper.getReadableDatabase();
        String query="DELETE FROM MyFriends WHERE Name='"+name+"';";
        sqLiteDatabase.delete("MyFriends", "Name" + " = ?", new String[] { name });
    }

    static class DbHelper extends SQLiteOpenHelper {
       private static final String Db_Name="Database1";
       private static final int Db_Version=1;
       private static final String Table_Name="MyFriends";
       private static final String Name="Name";
       private static final String Number="Number";
       private static final String Lati="Lati";
       private static final String Longi="Longi";
       private static final String Create="CREATE TABLE "+Table_Name+" ("+Name+" VARCHAR(255),"+Number+" VARCHAR(255),"+Lati+" DOUBLE,"+Longi+" DOUBLE)";
       private static final String Drop="DROP TABLE IF EXISTS "+Table_Name;
       private Context context;

       public DbHelper(Context context)
       {
           super(context,Db_Name,null,Db_Version);
           this.context=context;
           //MyMessage.message(context,"Constructor is called");
       }

       @Override
       public void onCreate(SQLiteDatabase sqLiteDatabase) {

           try
           {
               sqLiteDatabase.execSQL(Create);
               MyMessage.message(context,"on Create is called");
           }
           catch (android.database.SQLException e)
           {
               MyMessage.message(context, "Error!!"+e);
           }
       }
                    @Override
       public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
           try
           {
               sqLiteDatabase.execSQL(Drop);
               onCreate(sqLiteDatabase);
               MyMessage.message(context,"on Upgrade is called");
           }
           catch (android.database.SQLException e)
           {
               MyMessage.message(context, "Error!!"+e);
           }
       }

   }

    }
