package com.dependency_injection.sample.storage

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.dependency_injection.sample.model.DataUser
import com.google.gson.Gson

class LocalStorage constructor(context: Context) : APILocal {
    companion object {
        const val key_user = "user"
    }

    private val PREF_File = "User"
    private val pref: SharedPreferences = context.getSharedPreferences(PREF_File, Activity.MODE_PRIVATE)

    var mUser: DataUser? = null
    override fun setUser(dataUser: DataUser) {
        mUser = dataUser
        val editor = pref.edit()
        val gson = Gson()
        val json = gson.toJson(mUser)
        editor.putString(key_user, json)
        editor.apply()
    }

    override fun getUser(): DataUser? {
        if (mUser != null)
            return mUser

        val gson = Gson()
        val json = pref.getString(key_user, "")

        mUser = gson.fromJson(json, DataUser::class.java)
        return mUser
    }

}