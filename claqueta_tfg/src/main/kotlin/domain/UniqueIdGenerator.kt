package domain

interface UniqueIdGenerator {
    fun generateUniqueId(obj: Any): Long
}