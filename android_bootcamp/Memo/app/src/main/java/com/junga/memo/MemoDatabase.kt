package com.junga.memo

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [MemoEntity::class], version = 1)
abstract class MemoDatabase : RoomDatabase() {
    abstract fun memoDao() : MemoDAO

    companion object {
        var INSTANCE : MemoDatabase? = null

        fun getInstance(context : Context): MemoDatabase? {
            if (INSTANCE == null) {
                synchronized(MemoDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, MemoDatabase::class.java, "memo.db")
                        .fallbackToDestructiveMigration()  // version uprade시 table을 모두 삭제하고 다시 생성한다.
                        .build()
                }
            }

            return INSTANCE
        }

    }
}