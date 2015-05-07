package com.stone.customview.db;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import android.database.sqlite.SQLiteDatabase;

public class DBHelper {
	
	private ReadWriteLock lock = new ReentrantReadWriteLock(true);
	private Lock readLock  = lock.readLock();
	private Lock writeLock   = lock.writeLock();
	
	public SQLiteDatabase getReadableDataBase(String databaseDirPath,String databaseFileName) {

		readLock.lock();
		
		try {
			String databasePath = databaseDirPath.concat(databaseFileName);
			
			return SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		}finally{
			readLock.unlock();
		}
	}
	
	public SQLiteDatabase getWritableDataBase(String databaseDirPath,String databaseFileName) {
		
		writeLock.lock();
		
		try {
			String databasePath = databaseDirPath.concat(databaseFileName);
			
			return SQLiteDatabase.openDatabase(databasePath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		} finally{
			writeLock.unlock();
		}
	}
}
