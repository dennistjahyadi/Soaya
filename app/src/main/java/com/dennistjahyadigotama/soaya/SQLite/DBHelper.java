package com.dennistjahyadigotama.soaya.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.dennistjahyadigotama.soaya.QuickstartPreferences;
import com.dennistjahyadigotama.soaya.User;

import org.json.JSONArray;
import org.json.JSONException;


/**
 * Created by Denn on 7/30/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "App3.db";


    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SharedPreferences sharedPreferences = context.getSharedPreferences("prefs", Context.MODE_PRIVATE);
        User.username=sharedPreferences.getString(QuickstartPreferences.USERNAME,null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DBInfo.CREATE_ROOM_TABLE);

        db.execSQL(DBInfo.CREATE_CHAT_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean InsertRoom  (String roomid, String roomname, String lastmessagedate, String users, String messages, String type, String sender, String profilepic, int jumlahuser, String roomname2)
    {
        SQLiteDatabase dbb = this.getReadableDatabase();
        Cursor res =  dbb.rawQuery("select * from "+ DBInfo.ROOM_TABLE_NAME+" where roomid="+roomid,null);

        if(res.getCount()==0);
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBInfo.ROOM_COLUMN_LASTMESSAGEDATE, lastmessagedate);
            contentValues.put(DBInfo.ROOM_COLUMN_ROOMID, roomid);
            contentValues.put(DBInfo.ROOM_COLUMN_ROOMNAME, roomname);
            contentValues.put(DBInfo.ROOM_COLUMN_USERS, users);
            contentValues.put(DBInfo.ROOM_COLUMN_MESSAGES, messages);
            contentValues.put(DBInfo.ROOM_COLUMN_TYPE, type);
            contentValues.put(DBInfo.ROOM_COLUMN_SENDER, sender);
            contentValues.put(DBInfo.ROOM_COLUMN_PROFILEPIC, profilepic);
            contentValues.put(DBInfo.ROOM_COLUMN_JUMLAHUSER, jumlahuser);
            contentValues.put(DBInfo.ROOM_COLUMN_TOTALNEWMESSAGE, 0);
            contentValues.put(DBInfo.ROOM_COLUMN_ROOMNAME2, roomname2);

            db.insert(DBInfo.ROOM_TABLE_NAME, null, contentValues);
            db.close();

        }
        res.close();
        dbb.close();
        return true;
    }

    public void UpdateRoomInfo(String roomid, String lastmessagedate, String messages, String type, String sender, String users, int con, String roomname2)
    {

        int totalMessage=0;
        SQLiteDatabase dbb = this.getReadableDatabase();
        Cursor res =  dbb.rawQuery("select * from "+ DBInfo.ROOM_TABLE_NAME+" where roomid="+roomid,null);

            if (res.moveToFirst()) {
                do {
                    totalMessage = res.getInt(res.getColumnIndex(DBInfo.ROOM_COLUMN_TOTALNEWMESSAGE));

                } while (res.moveToNext());
            }


        res.close();

        totalMessage += con;

        String roomName=null;
        try {

            JSONArray json = new JSONArray(users);
            for(int a=0;a<json.length();a++)
            {
                if(a==0) {
                    roomName = json.getString(a);
                }else
                {
                    roomName = roomName+", "+json.getString(a);
                }
            }
            if(json.length()==2)
            {
                if(json.getString(0).equals(User.username))
                {
                    roomName=json.getString(1);
                }else
                {
                    roomName=json.getString(0);

                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }



        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBInfo.ROOM_COLUMN_LASTMESSAGEDATE, lastmessagedate);
        contentValues.put(DBInfo.ROOM_COLUMN_MESSAGES, messages);
        contentValues.put(DBInfo.ROOM_COLUMN_TYPE, type);
        contentValues.put(DBInfo.ROOM_COLUMN_ROOMNAME, roomName);
        contentValues.put(DBInfo.ROOM_COLUMN_SENDER, sender);
        contentValues.put(DBInfo.ROOM_COLUMN_TOTALNEWMESSAGE,totalMessage);
        contentValues.put(DBInfo.ROOM_COLUMN_ROOMNAME2,roomname2);

        db.update(DBInfo.ROOM_TABLE_NAME, contentValues, "roomid="+roomid,null );
    }

    public void UpdateRoomNewMessage(String roomid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBInfo.ROOM_COLUMN_TOTALNEWMESSAGE,0);

        db.update(DBInfo.ROOM_TABLE_NAME, contentValues, "roomid="+roomid,null );
    }

    public Cursor GetRoomData ()
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+ DBInfo.ROOM_TABLE_NAME,null);

        return res;

    }
    public Cursor GetRoomDataId (String roomid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+ DBInfo.ROOM_TABLE_NAME+" where roomid="+roomid,null);

        return res;

    }
    public void DeleteRoomById(String roomid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBInfo.ROOM_TABLE_NAME,DBInfo.ROOM_COLUMN_ROOMID+"="+roomid,null);
    }

    public void DeleteAllRoom()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBInfo.ROOM_TABLE_NAME,null,null);
    }


    //----------------------------------------------------------------------------------------------

    public boolean InsertChat  (String id, String roomid, String messages, String type, String thedate, String sender,String thetime,String profilepic)
    {
        SQLiteDatabase dbb = this.getReadableDatabase();
        Cursor res =  dbb.rawQuery("select * from "+ DBInfo.CHAT_TABLE_NAME+" where id="+id,null);
        if(res.getCount()==0);
        {
            SQLiteDatabase db = this.getWritableDatabase();
            ContentValues contentValues = new ContentValues();
            contentValues.put(DBInfo.CHAT_COLUMN_ID, id);
            contentValues.put(DBInfo.CHAT_COLUMN_ROOMID, roomid);
            contentValues.put(DBInfo.CHAT_COLUMN_MESSAGES, messages);
            contentValues.put(DBInfo.CHAT_COLUMN_TYPE, type);
            contentValues.put(DBInfo.CHAT_COLUMN_THEDATE, thedate);
            contentValues.put(DBInfo.CHAT_COLUMN_SENDER, sender);
            contentValues.put(DBInfo.CHAT_COLUMN_THETIME,thetime);
            contentValues.put(DBInfo.CHAT_COLUMN_PROFILEPIC,profilepic);



            db.insert(DBInfo.CHAT_TABLE_NAME, null, contentValues);
        }
        res.close();
        dbb.close();
        dbb.close();
        return true;
    }
    public Cursor GetChatData (String roomid)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+ DBInfo.CHAT_TABLE_NAME+" where roomid="+roomid+" order by id desc ",null);

        return res;

    }
    public Cursor GetChatData2 (String roomid,int rrr)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery("select * from "+ DBInfo.CHAT_TABLE_NAME+" where roomid="+roomid+" order by id desc limit 15 offset "+rrr,null);

        return res;

    }
    public void DeleteChatText(String roomid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBInfo.CHAT_TABLE_NAME, DBInfo.CHAT_COLUMN_ROOMID+"="+roomid,null);
    }

    public void DeleteAllText()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DBInfo.CHAT_TABLE_NAME,null,null);
    }

}
