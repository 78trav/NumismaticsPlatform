package ru.numismatics.backend.common

import kotlin.math.max

fun year(value: Int?) = max(value ?: 0, 0).toUInt()

fun quantity(value: Int?) = max(value ?: 1, 1).toUInt()