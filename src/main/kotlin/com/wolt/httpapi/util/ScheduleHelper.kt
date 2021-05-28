package com.wolt.httpapi.util

import com.wolt.httpapi.models.OpeningHours
import com.wolt.httpapi.models.Schedule
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.*

/**
 * This Helper class contains static methods for building and converting data
 */
class ScheduleHelper {
    companion object {
        /**
         * Generates a map that represents schedule table of the week
         */
        @JvmStatic
        fun generateScheduleTable(schedule: Schedule): MutableMap<String, List<OpeningHours>> {

            val dayOfWeekMap = mutableMapOf<String, List<OpeningHours>>()

            dayOfWeekMap[schedule::monday.name] = schedule.monday.sortedWith(compareBy { it.value })
            dayOfWeekMap[schedule::tuesday.name] = schedule.tuesday.sortedWith(compareBy { it.value })
            dayOfWeekMap[schedule::wednesday.name] = schedule.wednesday.sortedWith(compareBy { it.value })
            dayOfWeekMap[schedule::thursday.name] = schedule.thursday.sortedWith(compareBy { it.value })
            dayOfWeekMap[schedule::friday.name] = schedule.friday.sortedWith(compareBy { it.value })
            dayOfWeekMap[schedule::saturday.name] = schedule.saturday.sortedWith(compareBy { it.value })
            dayOfWeekMap[schedule::sunday.name] = schedule.sunday.sortedWith(compareBy { it.value })

            return dayOfWeekMap
        }

        /**
         * Converts seconds to time AM/PM format. The function uses Locale and UTC zone offset
         */
        @JvmStatic
        fun covertSecondsToTimeAMPM(seconds: Int): String {
            val secondsLong: Long = seconds.toLong()
            val dateTime = LocalDateTime.ofEpochSecond(secondsLong, 0, ZoneOffset.UTC)
            val formatter = DateTimeFormatter.ofPattern("h.mm a", Locale.ENGLISH)
            val formattedDate = dateTime.format(formatter)
            return formattedDate.replace(".00", "")
        }
    }
}