package com.tatuas.ghsv.data.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.tatuas.ghsv.data.api.GitHubListUser

@Entity(tableName = User.Constant.TABLE_NAME,
        indices = [(Index(value = [(User.Column.ID)], unique = true))])
data class User(
        @PrimaryKey @ColumnInfo(name = Column.ID) val id: Long,
        @ColumnInfo(name = Column.NAME) val name: String,
        @ColumnInfo(name = Column.IMAGE_URL) val imageUrl: String) {
    companion object {
        fun from(gitHubListUser: GitHubListUser) = User(
                gitHubListUser.id,
                gitHubListUser.login,
                gitHubListUser.avatar_url)
    }

    object Constant {
        const val TABLE_NAME = "users"
    }

    object Column {
        const val ID = "id"
        const val NAME = "login"
        const val IMAGE_URL = "image_url"
    }
}
