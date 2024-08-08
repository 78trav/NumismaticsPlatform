package ru.numismatics.backend.common

import kotlinx.datetime.Clock
import ru.numismatics.backend.common.context.AppContext
import ru.numismatics.backend.common.context.NumismaticsPlatformContext
import ru.numismatics.backend.common.helpers.toError
import ru.numismatics.backend.common.models.core.Command
import ru.numismatics.backend.common.models.core.State
import ru.numismatics.backend.common.models.entities.Lot
import kotlin.reflect.KClass

suspend inline fun AppContext<Lot>.controllerHelper(
    crossinline getRequest: suspend NumismaticsPlatformContext<Lot>.() -> Unit,
    crossinline toResponse: suspend NumismaticsPlatformContext<Lot>.() -> Unit,
    clazz: KClass<*>,
    logId: String
) {
    println("$clazz $logId")
//    val logger = corSettings.loggerProvider.logger(clazz)
    val context = NumismaticsPlatformContext(
        entityRequest = Lot.EMPTY,
        timeStart = Clock.System.now()
    )
    try {
        context.getRequest()
//        logger.info(
//            msg = "Request $logId started for ${clazz.simpleName}",
//            marker = "BIZ",
//            data = ctx.toLog(logId)
//        )
//        println(5)
        processor.exec(context)
//        println(51)
//        logger.info(
//            msg = "Request $logId processed for ${clazz.simpleName}",
//            marker = "BIZ",
//            data = ctx.toLog(logId)
//        )
        context.toResponse()
    } catch (e: Throwable) {
//        logger.error(
//            msg = "Request $logId failed for ${clazz.simpleName}",
//            marker = "BIZ",
//            data = ctx.toLog(logId),
//            e = e,
//        )
//        println(6)
        val er = e.toError()
        println(er)
        context.state = State.FAILING
        context.errors.add(er)
        processor.exec(context)
        if (context.command == Command.NONE) {
            context.command = Command.READ
        }
        context.toResponse()
    }
}