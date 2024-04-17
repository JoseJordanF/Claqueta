package com.app.claquetaTfg.config


import com.app.jconfiglibrary.JConfig
import java.io.File

class Configurator private constructor() {
    private lateinit var config: JConfig

    companion object{
        private lateinit var instance: Configurator

        fun instance(path: String="src/test/resources/configuration/config.env"): Configurator {
            instance = Configurator()
            instance.config = JConfig()

            if (instance.config.envVarFilter("CLAQUETA_")){
            }else{
                instance.config.parseFile(File(path))
            }

            return instance
        }
    }

    fun getConfig(key: String): String? {
        return config.getString(key)
    }
}
