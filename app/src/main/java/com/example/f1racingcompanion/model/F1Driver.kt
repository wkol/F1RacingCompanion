package com.example.f1racingcompanion.model

open class F1Driver (
    val firstName: String,
    val lastName: String,
    val carNumber: Int,
    val shortcut: String,
    val team: String,
    val teamColor: TeamColor,
) {
    constructor(): this("", "", -1, "", "", TeamColor.WILLIAMS)
    companion object {
        fun getDriverByNumber(num: Int): F1Driver {
            // @TODO convert to get drivers from drivers list
            return F1Driver()
        }
    }
}
