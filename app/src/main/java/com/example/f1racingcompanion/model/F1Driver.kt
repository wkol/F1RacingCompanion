package com.example.f1racingcompanion.model

import com.example.f1racingcompanion.utils.Constants

open class F1Driver (
    val firstName: String,
    val lastName: String,
    val carNumber: Int,
    val shortcut: String,
    val team: String,
    val teamColor: TeamColor,
) {

    constructor(driver: F1Driver): this(driver.firstName, driver.lastName, driver.carNumber, driver.shortcut, driver.team, driver.teamColor)




    companion object {
        fun getDriverByNumber(num: Int): F1Driver {
            return Constants.DRIVERS[num] ?: F1Driver("", "", -1, "", "", TeamColor.WILLIAMS)
        }
    }

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
