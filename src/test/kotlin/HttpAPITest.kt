import com.google.gson.Gson
import com.wolt.httpapi.models.OpeningHours
import com.wolt.httpapi.models.Schedule
import com.wolt.httpapi.module
import io.ktor.http.*
import io.ktor.server.testing.*
import org.junit.Test
import kotlin.test.assertEquals

class ScheduleHoursAppTest {
    @Test
    fun testGetMethod() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Get, "/api/v1/merchants/hours").apply {
                assertEquals(
                    "An endpoint that accepts JSON-formatted opening hours of a" +
                            "restaurant as an input and returns the rendered human readable format as a text output",
                    response.content
                )
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }

    @Test
    fun testEmptyPUTMethod() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Put, "/api/v1/merchants/hours").apply {
                assertEquals(HttpStatusCode.UnsupportedMediaType, response.status())
            }
        }
    }

    @Test
    fun testPUTMethodWithNormalHours() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Put, "/api/v1/merchants/hours"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("Accept", "application/json")
                var gson = Gson()
                var monday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var tuesday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var wednesday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var thursday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var friday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var saturday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var sunday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))

                var schedule = Schedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
                var jsonString:String = gson.toJson(schedule)

                setBody(jsonString)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("A restaurant is open:\n" +
                        "monday: 10 AM - 6 PM\n" +
                        "tuesday: 10 AM - 6 PM\n" +
                        "wednesday: 10 AM - 6 PM\n" +
                        "thursday: 10 AM - 6 PM\n" +
                        "friday: 10 AM - 6 PM\n" +
                        "saturday: 10 AM - 6 PM\n" +
                        "sunday: 10 AM - 6 PM\n", response.content)
            }
        }
    }

    @Test
    fun testPUTMethodWithRestaurantOpenClosedMultipleTimesDuringSameDay() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Put, "/api/v1/merchants/hours"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("Accept", "application/json")
                var gson = Gson()
                var monday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 46000),
                                                         OpeningHours("open", 56000), OpeningHours("close", 64800))
                var tuesday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var wednesday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var thursday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var friday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var saturday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var sunday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))

                var schedule = Schedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
                var jsonString:String = gson.toJson(schedule)

                setBody(jsonString)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals("A restaurant is open:\n" +
                        "monday: 10 AM - 12.46 PM, 3.33 PM - 6 PM\n" +
                        "tuesday: 10 AM - 6 PM\n" +
                        "wednesday: 10 AM - 6 PM\n" +
                        "thursday: 10 AM - 6 PM\n" +
                        "friday: 10 AM - 6 PM\n" +
                        "saturday: 10 AM - 6 PM\n" +
                        "sunday: 10 AM - 6 PM\n", response.content)
            }
        }
    }

    @Test
    fun testPUTMethodWithMissingClosingHoursForOneOfTheDays() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Put, "/api/v1/merchants/hours"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("Accept", "application/json")
                var gson = Gson()
                var monday: List<OpeningHours>  = listOf(OpeningHours("open", 36000))
                var tuesday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var wednesday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var thursday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var friday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var saturday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var sunday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))

                var schedule = Schedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
                var jsonString:String = gson.toJson(schedule)

                setBody(jsonString)
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("tuesday: There is no closing hours for overnight", response.content)
            }
        }
    }

    @Test
    fun testPUTMethodWithOvernightHours() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Put, "/api/v1/merchants/hours") {
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("Accept", "application/json")
                var gson = Gson()
                var monday: List<OpeningHours> = listOf(OpeningHours("open", 36000))
                var tuesday: List<OpeningHours> =
                    listOf(OpeningHours("close", 3600), OpeningHours("open", 36000), OpeningHours("close", 64800))
                var wednesday: List<OpeningHours> = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var thursday: List<OpeningHours> = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var friday: List<OpeningHours> = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var saturday: List<OpeningHours> = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var sunday: List<OpeningHours> = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))

                var schedule = Schedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
                var jsonString: String = gson.toJson(schedule)

                setBody(jsonString)
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
                assertEquals(
                    "A restaurant is open:\n" +
                            "monday: 10 AM - 1 AM\n" +
                            "tuesday: 10 AM - 6 PM\n" +
                            "wednesday: 10 AM - 6 PM\n" +
                            "thursday: 10 AM - 6 PM\n" +
                            "friday: 10 AM - 6 PM\n" +
                            "saturday: 10 AM - 6 PM\n" +
                            "sunday: 10 AM - 6 PM\n", response.content
                )
            }
        }
    }

    @Test
    fun testPUTMethodWithOvernightHoursAndOneClosedDay() {
            withTestApplication({ module(testing = true) }) {
                handleRequest(HttpMethod.Put, "/api/v1/merchants/hours") {
                    addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                    addHeader("Accept", "application/json")
                    var gson = Gson()
                    var monday: List<OpeningHours> = listOf(OpeningHours("open", 36000))
                    var tuesday: List<OpeningHours> =
                        listOf(OpeningHours("close", 3600), OpeningHours("open", 36000), OpeningHours("close", 64800))
                    var wednesday: List<OpeningHours> =
                        listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                    var thursday: List<OpeningHours> = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                    var friday: List<OpeningHours> = listOf()
                    var saturday: List<OpeningHours> = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                    var sunday: List<OpeningHours> = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))

                    var schedule = Schedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
                    var jsonString: String = gson.toJson(schedule)

                    setBody(jsonString)
                }.apply {
                    assertEquals(
                        "A restaurant is open:\n" +
                                "monday: 10 AM - 1 AM\n" +
                                "tuesday: 10 AM - 6 PM\n" +
                                "wednesday: 10 AM - 6 PM\n" +
                                "thursday: 10 AM - 6 PM\n" +
                                "friday: Closed\n" +
                                "saturday: 10 AM - 6 PM\n" +
                                "sunday: 10 AM - 6 PM\n", response.content
                    )
                    assertEquals(HttpStatusCode.OK, response.status())

                }
            }
    }

    @Test
    fun testPUTMethodWithIncorrectSequenceOfHours1() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Put, "/api/v1/merchants/hours"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("Accept", "application/json")
                var gson = Gson()
                var monday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var tuesday: List<OpeningHours>  = listOf(OpeningHours("close", 36000), OpeningHours("close", 64800))
                var wednesday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var thursday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var friday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var saturday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var sunday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))

                var schedule = Schedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
                var jsonString:String = gson.toJson(schedule)

                setBody(jsonString)
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("tuesday: The hours are out of sequence", response.content)
            }
        }
    }

    @Test
    fun testPUTMethodWithIncorrectSequenceOfHours2() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Put, "/api/v1/merchants/hours"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("Accept", "application/json")
                var gson = Gson()
                var monday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var tuesday: List<OpeningHours>  = listOf(OpeningHours("close", 36000), OpeningHours("open", 64800))
                var wednesday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var thursday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var friday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var saturday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var sunday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))

                var schedule = Schedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
                var jsonString:String = gson.toJson(schedule)

                setBody(jsonString)
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("tuesday: The hours are out of sequence", response.content)
            }
        }
    }

    @Test
    fun testPUTMethodWithIncorrectSequenceOfHours3() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Put, "/api/v1/merchants/hours"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("Accept", "application/json")
                var gson = Gson()
                var monday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var tuesday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("open", 64800), OpeningHours("open", 64800))
                var wednesday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var thursday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var friday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var saturday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var sunday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))

                var schedule = Schedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
                var jsonString:String = gson.toJson(schedule)

                setBody(jsonString)
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("tuesday: The hours are out of sequence", response.content)
            }
        }
    }

    @Test
    fun testPUTMethodWithTimeOutOfRange() {
        withTestApplication({ module(testing = true) }) {
            handleRequest(HttpMethod.Put, "/api/v1/merchants/hours"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.Json.toString())
                addHeader("Accept", "application/json")
                var gson = Gson()
                var monday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 648000))
                var tuesday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var wednesday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var thursday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var friday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var saturday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))
                var sunday: List<OpeningHours>  = listOf(OpeningHours("open", 36000), OpeningHours("close", 64800))

                var schedule = Schedule(monday, tuesday, wednesday, thursday, friday, saturday, sunday)
                var jsonString:String = gson.toJson(schedule)

                setBody(jsonString)
            }.apply {
                assertEquals(HttpStatusCode.BadRequest, response.status())
                assertEquals("monday: The time value is out of range", response.content)
            }
        }
    }
}