package com.app.claquetaTfg.database

import com.app.claquetaTfg.domain.Film
import com.app.claquetaTfg.domain.Review
import kotlinx.serialization.KSerializer
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.buildJsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.add
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
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

    override fun loadData(): Any {
        val content = file.readText()
        if (content.isEmpty()) return hashMapOf<Any, Any>()
        when (classType) {
            Film::class -> {
                val list = jsonPrettyFormat.decodeFromString(ListSerializer(serializer), content)
                val map: HashMap<String, Film> = hashMapOf()
                for (film: Film in list as List<Film>) {
                    map.getOrPut(film.id) { film }
                }
                return map
            }

            Review::class -> {
                val list = jsonPrettyFormat.decodeFromString(ListSerializer(serializer), content)
                val map: HashMap<String, HashSet<Review>> = hashMapOf()
                for (review: Review in list as List<Review>) {
                    map.getOrPut(review.filmId) { HashSet() }.add(review)
                    map.getOrPut(review.userName) { HashSet() }.add(review)
                }
                return map
            }

            else -> return hashMapOf<Any, Any>()
        }
    }
}

class RecomendationsDatabasePersistenceLayer(
    fileJson: String,
) : PersistenceLayer {
    private val jsonPrettyFormat = Json { prettyPrint = true }
    private val file = File(fileJson)

    override fun saveData(data: Any) {
        if (!file.exists()) {
            file.createNewFile()
        }
        val map = data as HashMap<String, List<String>>

        val list = buildJsonArray {
            map.forEach { (first, second) ->
                add(
                    buildJsonObject {
                        put("first", first)
                        put("second", buildJsonArray { second.forEach { add(it) } })
                    }
                )
            }
        }

        val json = jsonPrettyFormat.encodeToString(list)
        file.writeText(json)
    }

    override fun loadData(): HashMap<String, List<String>> {
        val content = file.readText()
        if (content.isEmpty()) return hashMapOf()
        val list = jsonPrettyFormat.parseToJsonElement(content).jsonArray
        val map = hashMapOf<String, List<String>>()
        list.forEach {
            val jobject = it.jsonObject
            val first = jobject["first"].toString().trim('"')
            val second = jobject["second"]?.jsonArray?.map { it.toString().trim('"') }?.toList()
            if (second != null) {
                map[first] = second
            }
        }
        return map
    }
}

class UsersDatabasePersistenceLayer(
    fileJson: String,
) : PersistenceLayer {
    private val jsonPrettyFormat = Json { prettyPrint = true }
    private val file = File(fileJson)

    override fun saveData(data: Any) {
        if (!file.exists()) {
            file.createNewFile()
        }
        val json = jsonPrettyFormat.encodeToString(data as String)
        val isNewFile = file.length() == 0L
        if (isNewFile) {
            file.appendText("[\n$json\n]")
        } else {
            val content = file.readText()
            val contentWithoutClosingBracket = content.removeSuffix("]")
            file.writeText("$contentWithoutClosingBracket,$json\n]")
        }
    }

    override fun loadData(): HashSet<String> {
        val content = file.readText()
        if (content.isEmpty()) return hashSetOf()
        val list = jsonPrettyFormat.parseToJsonElement(content).jsonArray
        val userList = hashSetOf<String>()
        list.forEach {
            val jobject = it.toString()
            userList.add(jobject.trim('"'))
        }
        return userList
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