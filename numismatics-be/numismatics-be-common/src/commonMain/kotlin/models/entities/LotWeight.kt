package ru.numismatics.backend.common.models.entities

data class LotWeight(
    val value: Float,
    val material: Material
) {
    companion object {
        val EMPTY = LotWeight(0f, Material.EMPTY)
    }
}

inline fun LotWeight.isEmpty(): Boolean = (this == LotWeight.EMPTY)

inline fun LotWeight.isNotEmpty(): Boolean = !isEmpty()
