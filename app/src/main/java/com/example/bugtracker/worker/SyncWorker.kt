package com.example.bugtracker.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.bugtracker.BugTrackerApplication
import java.util.concurrent.TimeUnit

class SyncWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {

        return try {

            val application =
                applicationContext as BugTrackerApplication

            val repository = application.repository

            val pendingIssues = repository.getPendingIssues()

            for (issue in pendingIssues) {
                repository.syncIssue(issue)
            }

            Result.success()

        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {

        fun scheduleSync(context: Context) {

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .build()

            val syncRequest =
                OneTimeWorkRequestBuilder<SyncWorker>()
                    .setConstraints(constraints)
                    .setBackoffCriteria(
                        BackoffPolicy.EXPONENTIAL,
                        15,
                        TimeUnit.SECONDS
                    )
                    .build()

            WorkManager.getInstance(context)
                .enqueue(syncRequest)
        }
    }
}