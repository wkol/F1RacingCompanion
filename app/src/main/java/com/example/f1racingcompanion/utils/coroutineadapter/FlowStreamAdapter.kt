package com.example.f1racingcompanion.utils.coroutineadapter

import com.tinder.scarlet.Stream
import com.tinder.scarlet.StreamAdapter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class FlowStreamAdapter<T>(private val buffer: Int) : StreamAdapter<T, Flow<T>> {

    override fun adapt(stream: Stream<T>): Flow<T> {
        return ChannelForwarder<T>(buffer).start(stream).receiveAsFlow()
    }
}
