package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.previousdata.DriverInfoDto

open class F1Driver(
    val firstName: String,
    val lastName: String,
    val carNumber: Int,
    val shortcut: String,
    val team: String,
    val teamColor: Long,
) {
    constructor(driverInfo: DriverInfoDto) : this(
        driverInfo.firstName,
        driverInfo.lastName, driverInfo.racingNumber,
        driverInfo.tla,
        driverInfo.teamName, driverInfo.teamColour.toLong(16)
    )

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is F1Driver) return false
        if (carNumber != other.carNumber) return false
        return true
    }

    override fun hashCode(): Int {
        var result = carNumber
        result = 31 * result + shortcut.hashCode()
        result = 31 * result + team.hashCode()
        return result
    }
}
