package generatorId


import java.util.concurrent.atomic.AtomicInteger

class SnowFlakeForFilm {
    private val atomicInteger = AtomicInteger(0)
    private val epoch = 1027231200000L
    private val maxIncrement = 4096    //12 bits
    private val maxFdirectorId = 1024 // 10 bits

    init {
        if (maxFdirectorId < 1) {
            throw IllegalArgumentException("maxFdirectorId must be at least 1")
        }
    }

    fun generateUniqueId(title: String, director: String): Long {
        val currentTs = System.currentTimeMillis() - epoch
        val binaryTimestamp = String.format("%42s", java.lang.Long.toBinaryString(currentTs)).replace(' ', '0')
        val maxIncrement = maxIncrement - 1

        if (atomicInteger.get() >= maxIncrement) {
            atomicInteger.set(0)
        }
        val increment = atomicInteger.incrementAndGet()

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
        val titleId = title.hashCode() and (maxFdirectorId - 1)
        val directorId = director.hashCode() and (maxFdirectorId - 1)
        val combinedId = (titleId shl 10) or directorId
        val tenBitMask = 0x3FF
        return combinedId and tenBitMask
    }
}