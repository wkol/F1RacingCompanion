package com.example.f1racingcompanion.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.f1racingcompanion.data.ErgastRepository
import com.example.f1racingcompanion.data.LiveTimingFormula1Repository
import com.example.f1racingcompanion.utils.Result
import com.example.f1racingcompanion.utils.toNextSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.time.ZonedDateTime
import java.time.temporal.ChronoUnit
import javax.inject.Inject
import kotlin.math.absoluteValue

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repositoryLiveTiming: LiveTimingFormula1Repository,
    private val ergastRepository: ErgastRepository
) : ViewModel() {

    private val _uiState: MutableStateFlow<RaceStatusState> =
        MutableStateFlow(RaceStatusState(null, true))
    val uiState: StateFlow<RaceStatusState>
        get() = _uiState

    init {
        checkRacingStatus()
    }

    private fun checkRacingStatus() {
        repositoryLiveTiming.checkForActiveSession().onEach {
            when (it) {
                is Result.Loading -> _uiState.value = RaceStatusState(isActive = null, isLoading = true)
                is Result.Success -> {
                    it.data?.let { isActive ->
                        retrieveNextSession(isActive = isActive)
                    }
                }
                is Result.Error -> _uiState.value = RaceStatusState(isActive = null, isLoading = false, error = it.msg)
            }
        }.launchIn(viewModelScope)
    }

    private fun retrieveNextSession(isActive: Boolean) {
        ergastRepository.getNextSession().onEach {
            when (it) {
                is Result.Loading -> _uiState.value = RaceStatusState(null, true)
                is Result.Success -> {
                    val session = it.data!!.toNextSession()
                    _uiState.value = RaceStatusState(
                        isActive = isActive, isLoading = false,
                        nextSession = session.copy(
                            schedule = session.schedule.sortedBy { item ->
                                if (isActive)
                                    ChronoUnit.MINUTES.between(
                                        ZonedDateTime.now(),
                                        item.zonedStartTime
                                    ).absoluteValue
                                else ChronoUnit.MINUTES.between(
                                    item.zonedStartTime,
                                    item.zonedStartTime
                                )
                            }
                        )
                    )
                }
                is Result.Error ->
                    _uiState.value =
                        _uiState.value.copy(error = it.msg, isLoading = false)
            }
        }.launchIn(viewModelScope)
    }
}
