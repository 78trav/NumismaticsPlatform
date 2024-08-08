package ru.numismatics.platform.libs.cor.test

data class TestContext(
    var status: CorStatuses = CorStatuses.NONE,
    var counter: Int = 0,
    var history: String = "",
) {
    enum class CorStatuses {
        NONE,
        RUNNING,
        FAILING,
        ERROR
    }
}
