package com.tatuas.ghsv.data.db

import android.arch.paging.DataSource
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy
import android.arch.persistence.room.Query

@Dao
interface UserDao {
    @Query("SELECT * FROM ${User.Constant.TABLE_NAME} ORDER BY ${User.Column.ID} ASC")
    fun getUserList(): DataSource.Factory<Int, User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(userList: List<User>): List<Long>

    @Query("DELETE FROM ${User.Constant.TABLE_NAME}")
    fun clear()
}
