package net.sharermax.mword.database;
/********************
 * 数据库Adapter 
 * author: SharerMax
 * create: 2014.06.04
 * modify: 2014.06.04
 */

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

public class DBAdapter {
	
	private static final String TEXT_TYPE = " TEXT NOT NULL";
	private static final String COMMA_SEP = ",";
	private static final String SQL_CREATE_TABLE =
		    "CREATE TABLE " + MyBaseColumns.TABLE_NAME + " (" +
		    MyBaseColumns._ID + " INTEGER PRIMARY KEY," +
		    MyBaseColumns.WORD_SPELLING + TEXT_TYPE + COMMA_SEP +
		    MyBaseColumns.WORD_EXPLANATION + TEXT_TYPE + ")"; 
	private static final String SQL_DELETE_TABLE =
	    "DROP TABLE IF EXISTS " + MyBaseColumns.TABLE_NAME;
	
	private Context context;
	private SQLiteDatabase dbDatabase;
	private MySQLiteOpenHelper dbOpenHelper;
	private boolean mIsOpen;
	

	public DBAdapter(Context _context) {
		this.context = _context;
	}
	
	//official recommend call getWritableDabase() or get ReadableDatabase()
	//in background thread because they can be long-running
	public void open() {
		dbOpenHelper = new MySQLiteOpenHelper(context);
		try {
			dbDatabase = dbOpenHelper.getWritableDatabase();
		} catch (SQLiteException e) {
			// TODO: handle exception
			dbDatabase = dbOpenHelper.getReadableDatabase();
		}
		mIsOpen = true;
	}
	
	//close database
	public void close() {
		if(null != dbDatabase) {
			dbDatabase.close();
			dbDatabase = null;
		}
		mIsOpen = false;
	}
	
	public boolean isClose() {
		return mIsOpen;
	}
	
	//insert one record into database
	public long insert(Word word) {
		ContentValues cValues = new ContentValues();
		cValues.put(MyBaseColumns.WORD_SPELLING, word.spelling);
		cValues.put(MyBaseColumns.WORD_EXPLANATION, word.explanation);
		
		return dbDatabase.insert(MyBaseColumns.TABLE_NAME, null, cValues);
	}
	//delete one record in database
	public int deleteOneData(int _id) {
		return dbDatabase.delete(MyBaseColumns.TABLE_NAME, "_id = " + _id, null);
	}
	//delete all record in the database
	public int deleteAllData() {
		return dbDatabase.delete(MyBaseColumns.TABLE_NAME, null, null);
	}
	//modify(update) one record in the database
	public int update(Word word, int _id) {
		ContentValues cv = new ContentValues();
		cv.put(MyBaseColumns.WORD_SPELLING, word.spelling);
		cv.put(MyBaseColumns.WORD_EXPLANATION, word.explanation);
		
		return dbDatabase.update(MyBaseColumns.TABLE_NAME, cv, "_id =" + _id, null);
	}
	//select all data
	public Word[] queryAllData() {
		String[] projection = {
				MyBaseColumns._ID,
				MyBaseColumns.WORD_SPELLING,
				MyBaseColumns.WORD_EXPLANATION,
		};
		Cursor result = dbDatabase.query(MyBaseColumns.TABLE_NAME, projection,
				null, null, null, null, null);
		return convertToWord(result);
	}
	
	//select one record from all data
	public Word queryOneData(int _id) {
		String[] projection = {
				MyBaseColumns._ID,
				MyBaseColumns.WORD_SPELLING,
				MyBaseColumns.WORD_EXPLANATION,
		};
		Cursor result = dbDatabase.query(MyBaseColumns.TABLE_NAME, projection, "_id = " + _id, null, null, null, null);
		return convertToWord(result)[0];
	}
	//cursor convert to Student
	private Word[] convertToWord(Cursor cursor) {
		int resultCounts = cursor.getCount();
		if((0 == resultCounts) || !cursor.moveToFirst()) {
			return null;
		}
		Word[] words = new Word[resultCounts];
		for(int i=0; i<resultCounts; i++) {
			words[i] = new Word();
			words[i]._id = cursor.getInt(cursor.getColumnIndex(MyBaseColumns._ID));
			words[i].spelling = cursor.getString(cursor.getColumnIndex(MyBaseColumns.WORD_SPELLING));
			words[i].explanation = cursor.getString(cursor.getColumnIndex(MyBaseColumns.WORD_EXPLANATION));
			cursor.moveToNext();
		}
		return words;
	}
	public final class MyBaseColumns implements BaseColumns {
		public MyBaseColumns() {
			// TODO Auto-generated constructor stub
		}
		
		public static final String TABLE_NAME = "words";
		public static final String WORD_SPELLING = "spelling";
		public static final String WORD_EXPLANATION = "explanation";
		
	}
	
	public class MySQLiteOpenHelper extends SQLiteOpenHelper {
		
		public static final int DATABASE_VERSION = 1;
	    public static final String DATABASE_NAME = "words.db";
	    
		public MySQLiteOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			db.execSQL(SQL_CREATE_TABLE);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			db.execSQL(SQL_DELETE_TABLE);
			onCreate(db);
		}

		@Override
		public void onDowngrade(SQLiteDatabase db, int oldVersion,
				int newVersion) {
			// TODO Auto-generated method stub
			onUpgrade(db, oldVersion, newVersion);
		}
		
	}
}
