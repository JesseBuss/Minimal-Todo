package com.example.avjindersinghsekhon.minimaltodo.Main.model

import com.example.avjindersinghsekhon.minimaltodo.Utility.StoreRetrieveData
import com.example.avjindersinghsekhon.minimaltodo.Utility.ToDoItem
import org.json.JSONException
import java.io.IOException
import java.util.*

object StoreHelper {
    const val FILENAME = "todoitems.json"

    @Deprecated("Use room instead")
    @JvmStatic
    fun getLocallyStoredData(storeRetrieveData: StoreRetrieveData): ArrayList<ToDoItem?>? {
        var items: ArrayList<ToDoItem?>? = null
        try {
            items = storeRetrieveData.loadFromFile()
        } catch (e: IOException) {
            e.printStackTrace()
        } catch (e: JSONException) {
            e.printStackTrace()
        }
        if (items == null) {
            items = ArrayList()
        }
        return items
    }
}