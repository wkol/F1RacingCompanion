package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.data.f1driverlistelementdto.F1DriverListElementDto

open class F1Driver(
    val firstName: String,
    val lastName: String,
    val carNumber: Int,
    val shortcut: String,
    val team: String,
    val teamColor: Long,
) {

    constructor(driverInfo: F1DriverListElementDto): this(driverInfo.firstName, driverInfo.lastName, driverInfo.carNumber, driverInfo.shortcut, driverInfo.team, driverInfo.teamColor)

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
