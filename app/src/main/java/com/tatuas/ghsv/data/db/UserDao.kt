package com.tatuas.ghsv.data.db

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query


@Dao
interface UserDao {
    @Query("SELECT * FROM ${User.Constant.TABLE_NAME} ORDER BY ${User.Column.ID} ASC")
    fun getUserList(): DataSource.Factory<Int, User>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(userList: List<User>): List<Long>

    @Query("DELETE FROM ${User.Constant.TABLE_NAME}")
    fun clear()
}
