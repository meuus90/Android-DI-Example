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

package com.dependency_injection.sample.repository.item

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dependency_injection.base.network.NetworkBoundResource
import com.dependency_injection.base.network.Resource
import com.dependency_injection.base.utility.Query
import com.dependency_injection.base.utility.SingleLiveEvent
import com.dependency_injection.sample.datasource.ItemListDataSource
import com.dependency_injection.sample.datasource.dao.item.ItemDao
import com.dependency_injection.sample.datasource.model.item.Item
import com.dependency_injection.sample.repository.BaseRepository
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ItemListRepository
@Inject
constructor(private val dao: ItemDao) : BaseRepository<Query>() {
    override suspend fun work(liveData: MutableLiveData<Query>): SingleLiveEvent<Resource> {
        return object :
                NetworkBoundResource<PagedList<Item>, MutableList<Item>>(liveData.value?.boundType!!) {
            override suspend fun workToCache(item: MutableList<Item>) {
                clearCache()
                dao.insert(item)
            }

            override suspend fun loadFromCache(
                    isLatest: Boolean,
                    itemCount: Int,
                    pages: Int
            ): LiveData<PagedList<Item>> {
                val config = PagedList.Config.Builder()
                        .setInitialLoadSizeHint(20)
                        .setPageSize(itemCount)
                        .setPrefetchDistance(10)
                        .setEnablePlaceholders(true)
                        .build()

                return LivePagedListBuilder(object :
                        DataSource.Factory<Int, Item>() {
                    override fun create(): DataSource<Int, Item> {

                        val list = dao.getAssets()
                        return ItemListDataSource(list)
                    }
                }, /* PageList Config */ config).build()
            }

            override suspend fun doNetworkJob() =
                    serverAPI.getItems(liveData.value!!.params[0] as String)

            override fun onNetworkError(errorMessage: String?, errorCode: Int) =
                    Timber.e("Network-Error: $errorMessage")

            override fun onFetchFailed(failedMessage: String?) =
                    Timber.e("Fetch-Failed: $failedMessage")
        }.getAsSingleLiveEvent()
    }
}