package com.app.claquetaTfg.database

import java.io.File
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue

class DatabasePersistenceLayer(
    jsonFileName: String
) : PersistenceLayer {
    private val objectMapper = jacksonObjectMapper()
    private val file = File(jsonFileName)
    private var data: HashMap<String,Any> = hashMapOf()

    init {
        if (!file.exists()) {
            throw FileNotExistException("The file in $jsonFileName does not exist")
        }else if (file.length()<=0){
            file.writeText(objectMapper.writeValueAsString(hashMapOf<String,Any>()))
        }else{
            data = objectMapper.readValue(file)
        }
    }

    override fun saveData(data: Any) {
        if (file.length()<=0){this.data = objectMapper.readValue(file)}
        val dataAux =  data as Pair<String, Any>
        this.data[dataAux.first]=dataAux.second
        file.writeText(objectMapper.writeValueAsString(this.data))
    }

    override fun loadData(): HashMap<String,Any> {
        if (data.isNotEmpty()){return this.data}
        return hashMapOf()
    }

    class FileNotExistException(message: String) : RuntimeException(message)

}