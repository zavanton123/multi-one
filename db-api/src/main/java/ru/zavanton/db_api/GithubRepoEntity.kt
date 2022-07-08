package ru.zavanton.db_api

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "repository")
data class GithubRepoEntity(
    @PrimaryKey
    var id: Int? = null,

    var name: String? = null,
)
