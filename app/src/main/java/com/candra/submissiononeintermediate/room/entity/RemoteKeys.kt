package com.candra.submissiononeintermediate.room.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "remote_keys")
data class RemoteKeys(
    @PrimaryKey val id: String,
    val prefKey: Int?,
    val nextKey: Int?
)