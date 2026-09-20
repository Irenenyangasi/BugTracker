package com.example.bugtracker.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.bugtracker.data.Issue
import com.example.bugtracker.data.IssueRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class IssueViewModel(
    private val repository: IssueRepository
) : ViewModel() {

    val issues: StateFlow<List<Issue>> =
        repository.getAllIssues()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = emptyList()
            )

    fun createIssue(issue: Issue) {
        viewModelScope.launch {
            repository.createIssue(issue)
        }
    }

    fun updateIssue(issue: Issue) {
        viewModelScope.launch {
            repository.updateIssue(issue)
        }
    }

    fun deleteIssue(issue: Issue) {
        viewModelScope.launch {
            repository.deleteIssue(issue)
        }
    }
}