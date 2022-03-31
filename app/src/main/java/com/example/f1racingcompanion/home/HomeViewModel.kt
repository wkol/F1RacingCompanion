package com.example.f1racingcompanion.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1racingcompanion.data.Formula1Repository
import com.example.f1racingcompanion.utils.Result
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject

class HomeViewModel @Inject constructor(private val repository: Formula1Repository) : ViewModel() {

    private val _isActive: MutableState<RaceStatusState> = mutableStateOf(RaceStatusState())
    val isActive: State<RaceStatusState> = _isActive

    private var connectionToken: String? = ""

    init {
        checkRacingStatus()
    }

    private fun checkRacingStatus() {
        repository.checkForActiveSession().onEach {
            when (it) {
                is Result.Loading -> _isActive.value = RaceStatusState(null, true)
                is Result.Success -> _isActive.value = RaceStatusState(it.data, false)
                is Result.Error -> _isActive.value = RaceStatusState(null, false, it.msg)
            }
        }.launchIn(viewModelScope)
    }

    private fun negotiateConnection() {
        repository.getConnectionToken().onEach {
            connectionToken = when (it) {
                is Result.Loading -> null
                is Result.Success -> it.data
                is Result.Error -> null
            }
        }.launchIn(viewModelScope)
    }
}
