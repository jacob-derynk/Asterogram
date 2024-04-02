package dev.jacobderynk.asterogram.data.datastore

import dev.jacobderynk.asterogram.data.datastore.util.DarkmodeValues
import kotlinx.coroutines.flow.Flow

interface IDatastoreRepository {

    suspend fun putShowOnboarding(show: Boolean)

    suspend fun getShowOnboarding(): Boolean

    suspend fun putUsername(newUsername: String)

    fun getUsername(): Flow<String>

    suspend fun putDarkmode(key: DarkmodeValues)

    fun getDarkmode(): Flow<DarkmodeValues>

}