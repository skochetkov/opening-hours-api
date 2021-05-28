package com.wolt.httpapi.util

import com.wolt.httpapi.models.OpeningHours
import com.wolt.httpapi.models.Schedule

/**
 * This class contains various static methods for input and data format validations.
 * TODO - Static messages needs to be pulled to a config file
 */
class ScheduleValidator {

    companion object {
        private const val maxTimeInSecond: Int = 86399

        /**
         * Validates if the schedule is missing one or more days
         */
        @JvmStatic
        fun hasMissingDays(schedule: Schedule): Validator {
            // if one of the days is missing
            if(schedule.monday === null ||
                schedule.tuesday == null ||
                schedule.wednesday == null ||
                schedule.thursday == null ||
                schedule.friday == null ||
                schedule.saturday == null
            ) return Validator(false, "One or more of the days are missing")
            return Validator(true, "")
        }

        /**
         * Function to validate if sequence of open/close events is valid.
         * @param schedule of week days with sorted list of events (open/close) by time - this gives us proper time sequence
         */
        @JvmStatic
        fun isScheduleTimeSequenceValid(schedule: MutableMap<String, List<OpeningHours>>): Validator {
            var prevBoolean = isOpen("close")
            var isOvernight = false

            for ((key, value) in schedule.entries) {
                if(isOvernight && (value.isEmpty())) {
                    return Validator(false, "$key: There is no closing hours for overnight as restaurant is closed")
                }
                for((index, hours) in value.withIndex()) {
                    if (isOvernight && (index == 0 && isOpen(hours.type))) {
                        return Validator(false, "$key: There is no closing hours for overnight")
                    } else if(isOvernight) {
                        isOvernight = false //reset
                    }
                    if(!isTimeInSecondsValid(hours.value))
                        return Validator(false, "$key: The time value is out of range")

                    if(isOpen(hours.type) == prevBoolean || !isTimeInSecondsValid(hours.value))
                        return Validator(false, "$key: The hours are out of sequence")
                    prevBoolean = isOpen(hours.type)

                    // check if the last event of the day is open, which means we expect next day close
                    if(value.isNotEmpty() && index == value.size - 1){
                        isOvernight = isOpen(hours.type)
                    }
                }
            }
            return return Validator(true, "")
        }

        @JvmStatic
        fun isTimeInSecondsValid(timeInSeconds: Int): Boolean {
            return !(timeInSeconds < 0 || timeInSeconds > maxTimeInSecond)
        }

        @JvmStatic
        fun isOpen(value: String): Boolean {
            return value == "open"
        }
    }
}