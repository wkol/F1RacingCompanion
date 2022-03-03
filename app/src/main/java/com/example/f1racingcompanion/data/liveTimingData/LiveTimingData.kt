package com.example.f1racingcompanion.data.liveTimingData

import java.util.Date

// Currently using Date (in future change to Java.time if sdk >= 26 or ThreeTenBP else)
class LiveTimingData<T>(val name: String?, val data: T?, val date: Date?)
