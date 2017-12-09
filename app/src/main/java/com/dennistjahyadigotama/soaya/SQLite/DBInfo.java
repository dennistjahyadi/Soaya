package com.dennistjahyadigotama.soaya.SQLite;

/**
 * Created by Denn on 7/30/2016.
 */
public class DBInfo {
    public static final String ROOM_TABLE_NAME = "chatroom";
    public static final String ROOM_COLUMN_ID = "id";
    public static final String ROOM_COLUMN_ROOMID = "roomid";
    public static final String ROOM_COLUMN_ROOMNAME = "roomname";
    public static final String ROOM_COLUMN_LASTMESSAGEDATE = "lastmessagedate";
    public static final String ROOM_COLUMN_USERS = "users";
    public static final String ROOM_COLUMN_MESSAGES = "messages";
    public static final String ROOM_COLUMN_TYPE="type";
    public static final String ROOM_COLUMN_SENDER="sender";
    public static final String ROOM_COLUMN_PROFILEPIC="profilepic";
    public static final String ROOM_COLUMN_JUMLAHUSER="jumlahuser";
    public static final String ROOM_COLUMN_TOTALNEWMESSAGE="totalnewmessage";
    public static final String ROOM_COLUMN_ROOMNAME2="roomname2";


    public static final String CREATE_ROOM_TABLE = "create table if not exists "+ROOM_TABLE_NAME+
            "("+ROOM_COLUMN_ID+" integer primary key,"+ROOM_COLUMN_ROOMID+" text,"+ROOM_COLUMN_ROOMNAME+" text,"
            + ROOM_COLUMN_LASTMESSAGEDATE+" text,"+ROOM_COLUMN_USERS+" text,"+ROOM_COLUMN_MESSAGES+" text,"+ROOM_COLUMN_TYPE+" text,"+ROOM_COLUMN_SENDER+" text,"
            +ROOM_COLUMN_PROFILEPIC+" text,"+ROOM_COLUMN_JUMLAHUSER+" integer,"+ROOM_COLUMN_TOTALNEWMESSAGE+" integer,"+ROOM_COLUMN_ROOMNAME2+" text)";


    public static final String CHAT_TABLE_NAME = "chattext";
    public static final String CHAT_COLUMN_ID = "id";
    public static final String CHAT_COLUMN_ROOMID = "roomid";
    public static final String CHAT_COLUMN_MESSAGES = "messages";
    public static final String CHAT_COLUMN_TYPE = "type";
    public static final String CHAT_COLUMN_THEDATE = "thedate";
    public static final String CHAT_COLUMN_SENDER = "sender";
    public static final String CHAT_COLUMN_PROFILEPIC = "profilepic";
    public static final String CHAT_COLUMN_THETIME = "thetime";

    public static final String CREATE_CHAT_TABLE = "create table if not exists "+CHAT_TABLE_NAME+"("+CHAT_COLUMN_ID+" integer primary key,"+CHAT_COLUMN_ROOMID+" text,"
            +CHAT_COLUMN_MESSAGES+" text,"+CHAT_COLUMN_TYPE+" text, "+CHAT_COLUMN_THEDATE+" text, "+CHAT_COLUMN_SENDER+" text,"+CHAT_COLUMN_THETIME+" text,"+CHAT_COLUMN_PROFILEPIC+" text)";


}
