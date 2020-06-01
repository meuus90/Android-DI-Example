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

package com.dependency_injection.sample.datasource.network

import androidx.lifecycle.LiveData
import com.dependency_injection.base.network.ApiResponse
import com.dependency_injection.sample.datasource.model.item.Item
import retrofit2.http.GET
import retrofit2.http.Query

interface ServerAPI {
    @GET("search")
    fun getItems(@Query("part") part: String): LiveData<ApiResponse<MutableList<Item>>>
}