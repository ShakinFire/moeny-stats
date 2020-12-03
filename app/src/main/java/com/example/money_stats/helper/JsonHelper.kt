package com.example.money_stats.helper

import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.util.*


object JsonHelper {
    @Throws(JSONException::class)
    fun toJSON(`object`: Any?): Any? {
        return if (`object` is Map<*, *>) {
            val json = JSONObject()
            val map = `object`
            for (key in map.keys) {
                json.put(key.toString(), toJSON(map[key]))
            }
            json
        } else if (`object` is Iterable<*>) {
            val json = JSONArray()
            for (value in `object`) {
                json.put(value)
            }
            json
        } else {
            `object`
        }
    }

    fun isEmptyObject(`object`: JSONObject): Boolean {
        return `object`.names() == null
    }

    @Throws(JSONException::class)
    fun getMap(
        `object`: JSONObject,
        key: String?
    ): Map<String?, Any?> {
        return toMap(`object`.getJSONObject(key))
    }

    @Throws(JSONException::class)
    fun toMap(`object`: JSONObject): Map<String?, Any?> {
        val map: MutableMap<String?, Any?> =
            HashMap()
        val keys: Iterator<*> = `object`.keys()
        while (keys.hasNext()) {
            val key = keys.next() as String
            map[key] = fromJson(`object`[key])
        }
        return map
    }

    @Throws(JSONException::class)
    fun toList(array: JSONArray): List<*> {
        val list = mutableListOf<Any?>()
        for (i in 0 until array.length()) {
            list.add(fromJson(array[i]))
        }
        return list
    }

    @Throws(JSONException::class)
    private fun fromJson(json: Any): Any? {
        return if (json === JSONObject.NULL) {
            null
        } else if (json is JSONObject) {
            toMap(json)
        } else if (json is JSONArray) {
            toList(json)
        } else {
            json
        }
    }
}