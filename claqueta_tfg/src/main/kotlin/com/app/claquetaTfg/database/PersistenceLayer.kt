package com.app.claquetaTfg.database

interface PersistenceLayer {
    fun saveData(data: Any)
    fun loadData(): Any
}