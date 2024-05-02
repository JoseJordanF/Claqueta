package com.app.claquetaTfg.logs

import com.app.claquetaTfg.domain.Film
import com.app.claquetaTfg.domain.Review
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.JsonArray
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put


object ManagerLogger {

    private var logInMemory: MutableList<Pair<String, String>> = mutableListOf()

    fun addLog(log: Pair<String, String>) {
        logInMemory.add(log)
    }

    fun getLogsInMemory(): MutableList<Pair<String, String>> {
        return logInMemory
    }

    fun dataLogs(msg: String): MutableMap<String, String> {
        val rightMsg = msg.split("=")[1]
        val jsonObject = Json.parseToJsonElement(rightMsg).jsonObject
        val resultMap = mutableMapOf<String, String>()

        jsonObject.forEach { (key, jsonElement) ->
            //resultMap[key] = jsonElement.jsonPrimitive.content
            val content = when {
                jsonElement is JsonPrimitive -> jsonElement.content
                jsonElement is JsonArray -> jsonElement.joinToString(",")
                else -> "Unsupported type"
            }
            resultMap[key] = content
        }
        return resultMap
    }

    fun createLog(info: Any, extraInfo: List<Pair<String, String>> = listOf()): String {
        val jsonPrettyFormat = Json { prettyPrint = true }

        var log = when (info) {
            is Review -> jsonPrettyFormat.encodeToString(info as Review)
            is Film -> jsonPrettyFormat.encodeToString(info as Film)
            else -> "Unsupported type"
        }

        if (extraInfo.isNotEmpty()) {
            val jsonObject = jsonPrettyFormat.parseToJsonElement(log).jsonObject

            val updatedJson = buildJsonObject {
                jsonObject.entries.forEach { (key, value) ->
                    put(key, value)
                }
                extraInfo.forEach { (key, value) ->
                    put(key, value)
                }
            }
            log = jsonPrettyFormat.encodeToString(updatedJson)
        }
        return log
    }
}