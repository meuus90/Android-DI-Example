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

package com.dependency_injection.base.view

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * A value holder that automatically clears the reference if the Fragment's view is destroyed.
 * @param <T>
</T> */
class AutoClearedValue<T>(fragment: Fragment, private var value: T?) {
    init {
        val fragmentManager = fragment.parentFragmentManager
        fragmentManager.registerFragmentLifecycleCallbacks(
            object : FragmentManager.FragmentLifecycleCallbacks() {
                override fun onFragmentViewDestroyed(fm: FragmentManager, f: Fragment) {
                    this@AutoClearedValue.value = null
                    fragmentManager.unregisterFragmentLifecycleCallbacks(this)
                }
            }, false)
    }

    fun get(): T? {
        return value
    }
}