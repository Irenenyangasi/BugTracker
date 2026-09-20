package com.example.bugtracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "issues")
data class Issue(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    val title: String,

    val description: String,

    val priority: String,

    val status: String,

    val creationDate: String,

    // Tracks whether the issue has been synchronized with the remote server.
    val syncStatus: String = "PENDING"
)