package com.example.f1racingcompanion.utils.coroutineadapter

import com.tinder.scarlet.Stream
import com.tinder.scarlet.StreamAdapter
import kotlinx.coroutines.channels.ReceiveChannel

class ReceiveChannelStreamAdapter<T>(private val buffer: Int) : StreamAdapter<T, ReceiveChannel<T>> {

    override fun adapt(stream: Stream<T>): ReceiveChannel<T> {
        val channelForwarder = ChannelForwarder<T>(buffer)
        return channelForwarder.start(stream)
    }
}
