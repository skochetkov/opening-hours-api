package com.wolt.httpapi.models

import kotlinx.serialization.Serializable
import org.jetbrains.annotations.NotNull

@Serializable
data class Schedule(@NotNull val monday: List<OpeningHours>,
                    @NotNull val tuesday: List<OpeningHours>,
                    @NotNull val wednesday: List<OpeningHours>,
                    @NotNull val thursday: List<OpeningHours>,
                    @NotNull val friday: List<OpeningHours>,
                    @NotNull val saturday: List<OpeningHours>,
                    @NotNull val sunday: List<OpeningHours>
)