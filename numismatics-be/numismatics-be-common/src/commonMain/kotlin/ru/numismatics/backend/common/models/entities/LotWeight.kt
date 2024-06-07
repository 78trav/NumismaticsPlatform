package ru.numismatics.backend.common.models.entities

data class LotWeight(
    val value: Float,
    val material: Material
) {

    fun isEmpty() = (this == EMPTY)

    companion object {
        val EMPTY = LotWeight(0f, Material.EMPTY)
    }
}
