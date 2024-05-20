package com.app.claquetaTfg.domain.generatorId


import java.util.concurrent.atomic.AtomicInteger


private var atomicInteger = AtomicInteger(0)
private val epoch = 1027231200000L
private val maxIncrement = 4096
private val maxWordId = 1024

fun generateUniqueId(fword: String, sword: String): String {
    val currentTs = System.currentTimeMillis() - epoch
    val binaryTimestamp =
        String.format("%42s", java.lang.Long.toBinaryString(currentTs)).replace(' ', '0')
    val maxIncrement = maxIncrement - 1

    if (atomicInteger.get() >= maxIncrement) {
        atomicInteger.set(0)
    }
    val increment = atomicInteger.incrementAndGet()

    val binaryIncrement = String.format("%12s", Integer.toBinaryString(increment)).replace(' ', '0')
    val binaryUniqueId =
        String.format("%10s", Integer.toBinaryString(generateUniqueTwoWordsId(fword, sword)))
            .replace(' ', '0')

    val long1 = java.lang.Long.parseLong(binaryTimestamp, 2)
    val long2 = java.lang.Long.parseLong(binaryUniqueId, 2)
    val long3 = java.lang.Long.parseLong(binaryIncrement, 2)

    val concatenatedLong =
        (long1 shl (binaryUniqueId.length + binaryIncrement.length)) or (long2 shl binaryIncrement.length) or long3

    return concatenatedLong.toString()

}

private fun generateUniqueTwoWordsId(fword: String, sword: String): Int {
    // Combina dos palabras por ejemplo el nombre de la película y el nombre del director
    val fwordId = fword.hashCode() and (maxWordId - 1)
    val swordId = sword.hashCode() and (maxWordId - 1)
    val combinedId = (fwordId shl 10) or swordId
    val tenBitMask = 0x3FF
    return combinedId and tenBitMask
}