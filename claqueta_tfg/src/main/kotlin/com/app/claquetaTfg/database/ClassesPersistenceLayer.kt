package com.app.claquetaTfg.database

import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json
import java.io.File
import kotlin.reflect.KClass
import kotlin.reflect.cast


class DatabasePersistenceLayer<T : Any>(
    fileJson: String,
    private val serializer: KSerializer<T>,
    private val classType: KClass<T>,
) : PersistenceLayer {
    private val jsonPrettyFormat = Json { prettyPrint = true }
    private val file = File(fileJson)

    override fun saveData(data: Any) {
        if (!file.exists()) {
            file.createNewFile()
        }

        if (classType.isInstance(data)) {
            saveOneByOneData(classType.cast(data))
        } else if (data is MutableCollection<*>) {
            saveSetData(data)
        } else {
            throw IllegalArgumentException("Unsupported data type")
        }
    }

    private fun saveOneByOneData(data: T) {
        var json: String = jsonPrettyFormat.encodeToString(serializer, data)
        val isNewFile = file.length() == 0L
        if (isNewFile) {
            file.appendText("[\n$json\n]")
        } else {
            val content = file.readText()
            val contentWithoutClosingBracket = content.removeSuffix("]")
            file.writeText("$contentWithoutClosingBracket,$json\n]")
        }
    }

    private fun saveSetData(data: MutableCollection<*>) {
        data.forEach {
            val item = it as T
            saveOneByOneData(item)
        }
    }

    override fun loadData(): List<Any> {
        val content = file.readText()
		if(content.isEmpty()) return emptyList()
        return jsonPrettyFormat.decodeFromString(ListSerializer(serializer), content)
    }
}


class DataManager(private val persistenceLayer: PersistenceLayer) {
    fun saveData(data: Any) {
        persistenceLayer.saveData(data)
    }

    fun loadData(): Any {
        return persistenceLayer.loadData()
    }
}