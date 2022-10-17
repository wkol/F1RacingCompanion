package com.example.f1racingcompanion.timing

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Stable
import androidx.compose.runtime.mutableStateOf
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.toPersistentHashMap
import java.io.Serializable

@Stable
class ExpandedState(carNumbers: List<Int>) : Serializable {
    var expandedState: MutableState<ImmutableMap<Int, Boolean>> = mutableStateOf(
        carNumbers.associateBy({ it }, { false }).toPersistentHashMap()
    )
        private set

    fun onExpand(carNumber: Int) {
        expandedState.value = expandedState.value.mapValues {
            if (carNumber == it.key) !it.value else it.value
        }.toPersistentHashMap()
    }
}
