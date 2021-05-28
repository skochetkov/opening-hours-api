package com.wolt.httpapi.routes

import com.wolt.httpapi.models.OpeningHours
import com.wolt.httpapi.models.Schedule
import com.wolt.httpapi.util.ScheduleHelper
import com.wolt.httpapi.util.ScheduleValidator
import com.wolt.httpapi.util.Validator
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

/**
 * The class handles routing to the endpoint.
 */
fun Route.routing() {
    route("/api/v1/merchants/hours") {
        get {
            call.respond("An endpoint that accepts JSON-formatted opening hours of a" +
                    "restaurant as an input and returns the rendered human readable format as a text output")
        }
        put {
            val schedule = call.receive<Schedule>()

            var validatorResult = processSchedule(schedule)

            if(validatorResult.ok)
                call.respond(validatorResult.message)
            else
                call.respond(TextContent("${validatorResult.message}",
                    ContentType.Text.Plain.withCharset(Charsets.UTF_8), HttpStatusCode.BadRequest))
        }
    }
}

/**
 * Validates and processes input schedule
 *
 * @return Human readable 12-hour clock format or error message
 */
fun processSchedule(schedule: Schedule): Validator {
    var validatorResult  = ScheduleValidator.hasMissingDays(schedule)
    if(!validatorResult.ok) return validatorResult

    var dayOfWeekMap = ScheduleHelper.generateScheduleTable(schedule)

    validatorResult = ScheduleValidator.isScheduleTimeSequenceValid(dayOfWeekMap)
    if(!validatorResult.ok) return validatorResult

    return generateHumanReadableFormat(dayOfWeekMap)
}

/**
 * Generates Human readable format out of week timetable
 *
 * @return generated readable string in 12-hour clock format
 */
fun generateHumanReadableFormat(dayOfWeekMap: MutableMap<String, List<OpeningHours>>): Validator {
    val builder = StringBuilder()
    builder.append("A restaurant is open:\n")
    var isOvernight = false
    var lastEventOfTheDay: Int

    for ((key, value) in dayOfWeekMap.entries) {
        val iterator = value.iterator()

        // handle scenario when starts with close related to previous day
        if(isOvernight) {
            val seconds: Int = iterator.next().value
            val time = ScheduleHelper.covertSecondsToTimeAMPM(seconds)
            builder.append(time)
            builder.append("\n")
            isOvernight = false
        }

        // check if the last event of the day is open, which means we expect next day close
        if(iterator.hasNext()){
            lastEventOfTheDay = value.size - 1
            isOvernight = ScheduleValidator.isOpen(value[lastEventOfTheDay].type)
        }

        builder.append(key)
        builder.append(": ")
        // if this is the last event of the day
        if(!iterator.hasNext()) {
            builder.append("Closed\n")
            continue
        }

        while(iterator.hasNext()) {
            val hours = iterator.next()
            val seconds: Int = hours.value
            val time = ScheduleHelper.covertSecondsToTimeAMPM(seconds)
            builder.append(time)
            if(ScheduleValidator.isOpen(hours.type)) {
                builder.append(" - ")
            }
            else {
                if(iterator.hasNext()) builder.append(", ")
            }
        }
        if(!isOvernight) builder.append("\n")
    }
    return Validator(true, builder.toString())
}

fun Application.registerRoutes() {
    routing {
        routing()
    }
}