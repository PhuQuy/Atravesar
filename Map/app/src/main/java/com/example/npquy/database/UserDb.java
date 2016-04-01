package com.example.npquy.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.example.npquy.entity.User;

/**
 * Created by npquy on 3/22/2016.
 */
public class UserDb extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "database_user.db";
    public static final String USER_TABLE_NAME = "Users";
    public static final String USER_CUS_ID = "id";
    public static final String USER_COLUMN_NAME = "name";
    public static final String USER_COLUMN_MOBILE = "mobile";
    public static final String USER_COLUMN_EMAIL = "email";
    public static final String USER_COLUMN_DEVICE_ID = "device_id";

    public UserDb(Context context) {
        super(context, UserDb.DATABASE_NAME, null, 1);
    }

    public UserDb(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(
                "create TABLE IF NOT EXISTS " + UserDb.USER_TABLE_NAME + "("
                        + UserDb.USER_CUS_ID + "VARCHAR(255),"
                        + UserDb.USER_COLUMN_NAME + " VARCHAR(255), "
                        + UserDb.USER_COLUMN_MOBILE + " VARCHAR(255), "
                        + UserDb.USER_COLUMN_EMAIL + " VARCHAR(255), "
                        + UserDb.USER_COLUMN_DEVICE_ID + " VARCHAR(255) "
                        + ")"
        );

        Log.e("Database", "Create database " + USER_TABLE_NAME + " succeed", null);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + UserDb.USER_TABLE_NAME);
        onCreate(db);
    }

    public boolean saveUser(User user) {
        SQLiteDatabase db = this.getWritableDatabase();
/*        Cursor cursor = getData(user.getEmail());
        if(cursor.getColumnCount() != 0 ){
            cursor.moveToFirst();
            deleteUser(cursor.getInt(0));
        }*/
        ContentValues values = new ContentValues();
        values.put(UserDb.USER_CUS_ID, user.getCusID());
        values.put(UserDb.USER_COLUMN_NAME, user.getName());
        values.put(UserDb.USER_COLUMN_MOBILE, user.getMobile());
        values.put(UserDb.USER_COLUMN_EMAIL, user.getEmail());
        values.put(UserDb.USER_COLUMN_DEVICE_ID, user.getDeviceID());

        if (db.insert(UserDb.USER_TABLE_NAME, null, values) == -1) {
            db.close();
            return false;
        }
        db.close();
        return true;
    }

    public Cursor getData(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from " + UserDb.USER_TABLE_NAME + " where " + UserDb.USER_COLUMN_EMAIL + " = '" + email + "'", null);
        db.close();
        return res;
    }

    public Integer deleteUser(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result =  db.delete(UserDb.USER_TABLE_NAME,
                "id = ? ",
                new String[]{Integer.toString(id)});
        db.close();
        return result;
    }

    public void clearDataUserDb() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ UserDb.USER_TABLE_NAME );
        db.close();
    }

    public User getCurrentUser() {
        SQLiteDatabase db = this.getReadableDatabase();
        User user = null;
        Cursor cursor = null;
        if (db != null) {
            try {
                cursor = db.query(UserDb.USER_TABLE_NAME, null, null, null, null, null, null);
                if(cursor.getCount() != 0) {
                    cursor.moveToFirst();
                    user = new User();
                    user.setCusID(cursor.getString(0));
                    user.setName(cursor.getString(1));
                    user.setMobile(cursor.getString(2));
                    user.setEmail(cursor.getString(3));
                    user.setDeviceID(cursor.getString(4));
                }
            } catch (Exception ex) {
                Log.e("Error", ex.getLocalizedMessage(), ex);
            }finally {
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        }
        db.close();
        return user;
    }

    public User login(User user) {
     //   clearDataUserDb();
        saveUser(user);
        return user;
    }
}
