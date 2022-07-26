package com.example.f1racingcompanion.timing

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.example.f1racingcompanion.data.timingdatadto.SectorValue
import com.example.f1racingcompanion.utils.Constants
import kotlinx.collections.immutable.ImmutableMap
import kotlinx.collections.immutable.persistentMapOf
import kotlinx.collections.immutable.toImmutableMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@Composable
fun rememberTelemetryState(
    viewModel: TimingViewModel,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
): TelemetryState =
    remember(viewModel) {
        TelemetryState(coroutineScope, viewModel)
    }

@Stable
data class Telemetry(
    val driverStr: String?,
    val speed: Int,
    val rpm: Int,
    val gear: Int,
    val brake: Animatable<Float, AnimationVector1D>,
    val throttle: Animatable<Float, AnimationVector1D>,
    val isDrs: Boolean,
    val sectors: ImmutableMap<String, SectorValue>
)

class TelemetryState(
    private val coroutineScope: CoroutineScope,
    private val viewModel: TimingViewModel
) {

    var telemetry by mutableStateOf(
        Telemetry(
            null,
            0,
            0,
            0,
            Animatable(0F),
            Animatable(0F),
            false,
            persistentMapOf()
        )
    )
        private set

    var isOpen by mutableStateOf(false)
        private set

    init {
        viewModel.driverTelemetry.onEach {
            telemetry = telemetry.copy(
                driverStr = it.driverStr,
                gear = it.currentGear.toInt(),
                rpm = it.currentRPMValue,
                speed = it.currentSpeed,
                isDrs = it.isDRSEnabled,
                sectors = it.sectors.toImmutableMap()
            )
            coroutineScope.launch {
                launch {
                    telemetry.brake.animateTo(
                        it.currentBrakeValue.toFloat() / Constants.MAXIMUM_BRAKE_VALUE,
                        tween(durationMillis = it.delayInMilis.toInt())
                    )
                }
                launch {
                    telemetry.throttle.animateTo(
                        it.currentThrottleValue.toFloat() / Constants.MAXIMUM_THROTTLE_VALUE,
                        tween(durationMillis = it.delayInMilis.toInt())
                    )
                }
            }
        }.launchIn(coroutineScope)
    }

    fun openTelemetry(driverNum: Int) {
        viewModel.startDriverTelemetry(driverNum)
        isOpen = true
    }

    fun closeTelemetry() {
        viewModel.stopDriverTelemetry()
        isOpen = false
        telemetry = telemetry.copy(driverStr = null)
    }
}
