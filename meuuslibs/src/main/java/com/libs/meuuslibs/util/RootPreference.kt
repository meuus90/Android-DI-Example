package com.libs.meuuslibs.util

import android.app.Activity
import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException

class RootPreference(val name: String) {
    fun getDefaultValue(context: Context, key: String): Any? {
        val pref = context.getSharedPreferences(name, Activity.MODE_PRIVATE)
        val g = Gson()
        val j = pref.getString(key, "")
        try {
            return g.fromJson(j, Any::class.java)
        } catch (e: JsonSyntaxException) {
            e.printStackTrace()
        }
        return null
    }

    fun setDefaultValue(context: Context, key: String, value: Any) {
        val pref = context.getSharedPreferences(name, Activity.MODE_PRIVATE)
        val editor = pref.edit()
        val g = Gson()
        val j = g.toJson(value)
        editor.putString(key, j)
        editor.apply()
    }
}