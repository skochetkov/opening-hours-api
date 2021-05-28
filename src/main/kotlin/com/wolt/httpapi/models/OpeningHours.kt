package com.wolt.httpapi.models

import kotlinx.serialization.Serializable

@Serializable
data class OpeningHours (val type: String, val value: Int)