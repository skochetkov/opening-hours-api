package com.wolt.httpapi.util

/**
 * The Validator is a container for status information which is used as return object.
 * It consists of status of action and info message.
 */
data class Validator(val ok: Boolean, val message: String)
