package ru.numismatics.backend.common.models.core

enum class EntityType(private val description: String) {
    UNDEFINED("Не определен"),
    COUNTRY("Страна"),
    MATERIAL("Материал"),
    SECTION("Раздел"),
    LOT("Лот"),
    MARKET_PRICE("Рыночная цена");

    fun description() = description
}