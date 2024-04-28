package com.app.claquetaTfg.logs

object ManagerLogger {

    private var logInMemory: MutableList<Pair<String, String>> = mutableListOf()

    fun addLog(log: Pair<String,String>){
        logInMemory.add(log)
    }

    fun getLogsInMemory(): MutableList<Pair<String, String>> {
        return logInMemory
    }

    fun dataLogs(msg: String, splitRow: String, splitData: String): MutableMap<String,String> {
        val rowList: MutableList<String> = msg.split(splitRow).toMutableList()
        rowList.removeAt(0)
        var dataMap = mutableMapOf<String,String>()
        for(row in rowList){
            val data = row.split(splitData)
            dataMap.put(data[0].trim(),data[1].trim())
        }
        return dataMap
    }
}