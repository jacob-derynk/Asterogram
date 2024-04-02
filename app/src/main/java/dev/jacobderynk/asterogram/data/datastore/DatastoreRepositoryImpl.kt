package dev.jacobderynk.asterogram.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import dev.jacobderynk.asterogram.data.datastore.util.DarkmodeValues
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

const val ONBOARDING_KEY = "onboarding"
const val USERNAME_KEY = "username"
const val DARKMODE_KEY = "darkmode"

class DatastoreRepositoryImpl(
    private val context: Context
) : IDatastoreRepository {

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "asterogram_ds")
    override suspend fun putShowOnboarding(show: Boolean) {
        val key = booleanPreferencesKey(ONBOARDING_KEY)
        context.dataStore.edit { onboarding ->
            onboarding[key] = show
        }
    }

    override suspend fun getShowOnboarding(): Boolean {
        val key = booleanPreferencesKey(ONBOARDING_KEY)
        return context.dataStore.data
            .map { onboarding ->
                onboarding[key] ?: true
            }
            .first()
    }

    override suspend fun putUsername(newUsername: String) {
        val key = stringPreferencesKey(USERNAME_KEY)
        context.dataStore.edit { username ->
            username[key] = newUsername
        }
    }

    override fun getUsername(): Flow<String> {
        val key = stringPreferencesKey(USERNAME_KEY)
        val usernameFlow: Flow<String> = context.dataStore.data
            .map { preferences ->
                preferences[key] ?: ""
            }

        return usernameFlow
    }

    override suspend fun putDarkmode(key: DarkmodeValues) {
        TODO("Not yet implemented")
    }

    override fun getDarkmode(): Flow<DarkmodeValues> {
        TODO("Not yet implemented")
    }
}