package com.example.bugtracker.data

import kotlinx.coroutines.flow.Flow

class IssueRepository(
    private val dao: IssueDao,
    private val api: IssueApi
) {

    fun getAllIssues(): Flow<List<Issue>> {
        return dao.getAllIssues()
    }

    suspend fun createIssue(issue: Issue) {
        dao.insertIssue(issue)
    }

    suspend fun updateIssue(issue: Issue) {
        dao.updateIssue(issue)
    }

    suspend fun deleteIssue(issue: Issue) {
        dao.deleteIssue(issue)
    }

    suspend fun getPendingIssues(): List<Issue> {
        return dao.getPendingIssues()
    }

    suspend fun syncIssue(issue: Issue) {
        try {
            val remoteIssue = api.createIssue(issue)

            dao.updateIssue(
                issue.copy(
                    syncStatus = "SYNCED"
                )
            )
        } catch (e: Exception) {
            throw e
        }
    }
}