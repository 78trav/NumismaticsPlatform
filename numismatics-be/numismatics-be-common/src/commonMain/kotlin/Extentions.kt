package ru.numismatics.backend.common

fun year(value: Int?) = (value ?: 0).toUInt()

fun quantity(value: Int?) = (value ?: 1).toUInt()
