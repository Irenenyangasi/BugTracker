package com.example.bugtracker

import android.app.Application
import androidx.room.Room
import com.example.bugtracker.data.BugTrackerDatabase
import com.example.bugtracker.data.IssueApi
import com.example.bugtracker.data.IssueRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class BugTrackerApplication : Application() {

    val database by lazy {
        Room.databaseBuilder(
            applicationContext,
            BugTrackerDatabase::class.java,
            "bug_tracker_database"
        ).build()
    }

    val api: IssueApi by lazy {
        Retrofit.Builder()
            .baseUrl("https://example.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(IssueApi::class.java)
    }

    val repository by lazy {
        IssueRepository(
            database.issueDao(),
            api
        )
    }
}