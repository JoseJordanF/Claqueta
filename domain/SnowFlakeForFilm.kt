package com.app.claquetatfg_2.domain


import android.util.Log
import java.util.concurrent.atomic.AtomicInteger
import kotlin.math.log

class SnowFlakeForFilm {
    private val ATOMIC_INCREMENT = AtomicInteger(0)
    private val EPOCH = 1027231200000L
    private val MAX_INCREMENT = 4096    //12 bits
    private val MAX_FDIRECTOR_ID = 1024 // 10 bits

    init {
        if (MAX_FDIRECTOR_ID < 1) {
            throw IllegalArgumentException("MAX_FDIRECTOR_ID must be at least 1")
        }
    }

    fun generateUniqueId(title: String, director: String): Long {
        val currentTs = System.currentTimeMillis() - EPOCH
        val binaryTimestamp = String.format("%42s", java.lang.Long.toBinaryString(currentTs)).replace(' ', '0')
        val maxIncrement = MAX_INCREMENT - 1

        if (ATOMIC_INCREMENT.get() >= maxIncrement) {
            ATOMIC_INCREMENT.set(0)
        }
        val increment = ATOMIC_INCREMENT.incrementAndGet()

        val binaryIncrement = String.format("%12s", Integer.toBinaryString(increment)).replace(' ', '0')
        val binaryUniqueId =
            String.format("%10s", Integer.toBinaryString(generateUniqueFilmDirectorId(title, director)))
                .replace(' ', '0')

        val long1 = java.lang.Long.parseLong(binaryTimestamp, 2)
        val long2 = java.lang.Long.parseLong(binaryUniqueId, 2)
        val long3 = java.lang.Long.parseLong(binaryIncrement, 2)

        val concatenatedLong =
            (long1 shl (binaryUniqueId.length + binaryIncrement.length)) or (long2 shl binaryIncrement.length) or long3

        return concatenatedLong

    }

    private fun generateUniqueFilmDirectorId(title: String, director: String): Int {
        // Combina el nombre de la película y el nombre del director
        val titleId = title.hashCode() and (MAX_FDIRECTOR_ID - 1)
        val directorId = director.hashCode() and (MAX_FDIRECTOR_ID - 1)
        val combinedId = (titleId shl 10) or directorId
        val tenBitMask = 0x3FF
        return combinedId and tenBitMask
    }
}