package de.sambalmueslie.openevent


import org.apache.poi.ss.formula.functions.T
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import kotlin.system.measureTimeMillis

inline fun <T> Iterable<T>.forEachWithTryCatch(action: (T) -> Unit) {
    for (element in this) {
        try {
            action(element)
        } catch (e: Exception) {
            LoggerFactory.getLogger("Iterable").error("Exception occurred", e)
        }
    }
}


fun String?.nullIfBlank(): String? {
    return if (isNullOrBlank()) null else this
}

public inline fun <T> measureTimeMillisWithValue(block: () -> T): Pair<Long, T> {
    val start = System.currentTimeMillis()
    val result = block()
    val duration = System.currentTimeMillis() - start
    return Pair(duration, result)
}

public inline fun <T> Logger.logTimeMillisWithValue(msg: String, block: () -> T): T {
    val (duration, result) = measureTimeMillisWithValue(block)
    info("$msg within $duration ms")
    return result
}

public inline fun Logger.logTimeMillis(msg: String, block: () -> Unit) {
    val duration = measureTimeMillis(block)
    info("$msg within $duration ms")
}