package com.cyan.notylight;

import java.sql.ResultSet;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class Table {
	public static final String TABLE_NAME = "noty_light_table";
	private static final String c_id = "id";
	public static final String c_name = "c_name";
	public static final String c_pkg = "c_pkg";
	public static final String c_statu = "c_statu";

	public static void createTable(SQLiteDatabase db) {
		StringBuffer sb = new StringBuffer();
		sb.append("CREATE TABLE ").append(TABLE_NAME).append(" (");
		sb.append(c_id).append(" INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, ");// AUTOINCREMENT
		sb.append(c_name).append(" varchar(50) NOT NULL , ");
		sb.append(c_pkg).append(" varchar(100) NOT NULL , ");
		sb.append(c_statu).append(" int NOT NULL ");
		sb.append(");");
		db.execSQL(sb.toString());
	}

	public static void insert(ContentValues cv, Context ctx) {
		SQLiteDatabase db = DB.getInstance(ctx).getWritableDatabase();
		if (show(ctx).contains(cv.getAsString(c_name)) == false) {
			db.insert(TABLE_NAME, null, cv);
			// db.close();
		} else {
			db.delete(TABLE_NAME, c_name + " = ?",
					new String[] { cv.getAsString(c_name), });
			db.insert(TABLE_NAME, null, cv);
			// db.close();
		}
	}

	public static void delete(String name, Context ctx) {
		SQLiteDatabase db = DB.getInstance(ctx).getWritableDatabase();
		db.delete(TABLE_NAME, c_name + " = ?", new String[] { name });
		// db.close();
	}

	public static String show(Context ctx) {
		StringBuilder sb = new StringBuilder();
		SQLiteDatabase db = DB.getInstance(ctx).getReadableDatabase();
		Cursor cs = null;
		try {
			cs = db.query(TABLE_NAME, null, null, null, null, null, null);
			while (cs.moveToNext()) {
				sb.append(cs.getString(cs.getColumnIndex(c_name)));
				sb.append(" _ ");
				sb.append(cs.getInt(cs.getColumnIndex(c_statu)));
				sb.append("\n");
				sb.append(cs.getString(cs.getColumnIndex(c_pkg)));
				sb.append("\n\n");

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cs != null && !cs.isClosed()) {
				cs.close();
			}
			// db.close();
		}
		return sb.toString();
	}

	public static int action(String pkg, Context ctx) {
		Cursor cs = null;
		int flag = 0;
		SQLiteDatabase db = DB.getInstance(ctx).getReadableDatabase();
		try {
			cs = db.query(TABLE_NAME, new String[] { c_statu }, c_pkg + " = ?",
					new String[] { pkg }, null, null, null);
			while (cs.moveToNext()) {
				flag = cs.getInt(cs.getColumnIndex(c_statu));
			}
		} catch (Exception e) {
		} finally {
			if (cs != null && !cs.isClosed()) {
				cs.close();
			}
			// db.close();
		}
		return flag;
	}

}
