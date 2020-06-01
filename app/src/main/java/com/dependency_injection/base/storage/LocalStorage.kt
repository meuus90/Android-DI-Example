/*
 * Copyright (C)  2020 MeUuS90
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dependency_injection.base.storage

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalStorage
@Inject
constructor(val context: Context) {
    companion object {
        internal const val USER_FILE = "User"

        const val key_user_email = "user_email"
    }

    private val pref: SharedPreferences =
            context.getSharedPreferences(USER_FILE, Activity.MODE_PRIVATE)

    var userEmailAddress: String? = null
    internal fun getUserEmail(): String? {
        val gson = Gson()
        val json = pref.getString(key_user_email, "")

        userEmailAddress = gson.fromJson(json, String::class.java)
        return userEmailAddress
    }

    internal fun setUserEmail(email: String?) {
        userEmailAddress = email
        val editor = pref.edit()
        val gson = Gson()
        val json = gson.toJson(email)
        editor.putString(key_user_email, json)
        editor.apply()
    }
}