package com.ProgrammerYuan.PKUEater.utils;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import com.ProgrammerYuan.PKUEater.PKUApplication;
import com.ProgrammerYuan.PKUEater.model.Canteen;
import com.ProgrammerYuan.PKUEater.model.DBEntry;
import com.ProgrammerYuan.PKUEater.model.Dish;
import studio.archangel.toolkitv2.util.Logger;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Michael on 2014/9/25.
 */
public class EaterDB {


    //    static DbUtils db;
    static SQLiteDatabase db;

    static DBHelper helper;

    public static void saveEntry(DBEntry e) {
        try {
            String sql = e.getSavingSql().replace("'null'", "null");
            Logger.out(sql);
            getDb().execSQL(sql);
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    public static void releaseDB() {
        if (db != null) {
            db.close();
            db = null;
        }
        if (helper != null) {
            helper.close();
            helper = null;
        }

    }

    public static SQLiteDatabase getDb() {
        if (db == null) {
            if (helper == null) {
                helper = new DBHelper(PKUApplication.instance);
            }
            db = helper.getWritableDatabase();
        }
        return db;
    }

    public static void init(Context context) {
        db = getDb();
        for (String s : helper.database_create) {
            db.execSQL(s);
        }
        Cursor cursor = db.rawQuery("select tbl_name from sqlite_master ", null);
        int rows_num = cursor.getCount();    //取得資料表列數

        if (rows_num != 0) {
            cursor.moveToFirst();            //將指標移至第一筆資料
            for (int i = 0; i < rows_num; i++) {
                String name = cursor.getString(cursor.getColumnIndex("tbl_name"));
                Logger.out(name);
                cursor.moveToNext();        //將指標移至下一筆資料
            }
        }
        cursor.close();
        if(!PKUApplication.db_inited)
            PKUApplication.db_inited = true;
        Logger.out("dbHelper.database_create:" + helper.database_create);
    }

    public static void deleteEntry(DBEntry e) {
        getDb().execSQL(e.getDeletingSql());
    }

    public static ArrayList<Canteen> getCanteens(){
        ArrayList<Canteen> canteens = new ArrayList<>();
        Cursor cursor = query("canteens",null,null,null,null,null);
        int row = cursor.getCount();
        if(row > 0) {
            Logger.out(row);
            cursor.moveToFirst();
            for (int i = 0; i < row; i++) {
                canteens.add(new Canteen(cursor));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return canteens;
    }

    public static ArrayList<Dish> getMyFavoriteDishes(){
        ArrayList<Dish> dishes = new ArrayList<>();
        Cursor cursor = query("`dishes`",null,"liked = 1",null,null,null);
        int row = cursor.getCount();
        if(row > 0){
            cursor.moveToFirst();
            for(int i = 0;i<row;i++){
                dishes.add(new Dish(cursor));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return dishes;
    }

    public static void likeDish(Dish dish,boolean like){
        int like_int = like ? 1:0;
        String sql = "UPDATE `dishes` set liked = " + like_int + " where id = " + dish.getId();
        getDb().execSQL(sql);
        return;
    }

    public static ArrayList<Dish> getDishesOfCanteen(int canteen_id,int offset,int limit){
        ArrayList<Dish> dishes = new ArrayList<>();
        Cursor cursor = query("dishes",null,"canteen_id == " + canteen_id,null,null,offset + "," + limit);
        int row = cursor.getCount();
        if(row > 0){
            Logger.out(row);
            cursor.moveToFirst();
            for(int i = 0;i<row;i++){
                dishes.add(new Dish(cursor));
                cursor.moveToNext();
            }
        }
        cursor.close();
        return dishes;
    }

    static Cursor query(String table, String[] cols, String where, String[] where_args, String orderby, String limit) {
        Cursor mCursor = getDb().query(true, table, cols, where
                , where_args, null, null, orderby, limit);
        return mCursor; // iterate to get each value.
    }
}

