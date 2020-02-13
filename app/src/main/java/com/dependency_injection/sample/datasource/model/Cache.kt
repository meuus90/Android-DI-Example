/*
 * Copyright (C)  2020 Blue-Ocean
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

package com.dependency_injection.sample.datasource.model

import androidx.room.Database
import androidx.room.RoomDatabase
import com.dependency_injection.sample.datasource.dao.item.ItemDao
import com.dependency_injection.sample.datasource.model.item.Item

/**
 * Main cache description.
 */
@Database(
        entities = [
            Item::class
        ], exportSchema = false, version = 1
)
//@TypeConverters(BigDecimalTypeConverter::class, StringListTypeConverter::class)
abstract class Cache : RoomDatabase() {
    abstract fun itemDao(): ItemDao
}