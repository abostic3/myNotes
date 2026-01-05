package com.consumer.notesapp

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey

class EncryptionHelper(private val context: Context) {
    private var prefs: SharedPreferences? = null
    companion object { private const val PREF_NAME = "secret_notes_prefs" }

    private fun ensurePrefs() {
        if (prefs != null) return
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            prefs = EncryptedSharedPreferences.create(context, PREF_NAME, masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM)
        } catch (e: Exception) {
            e.printStackTrace()
            prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        }
    }

    fun put(key: String, value: String) { ensurePrefs(); prefs?.edit()?.putString(key, value)?.apply() }
    fun get(key: String): String { ensurePrefs(); return prefs?.getString(key, "") ?: "" }
    fun remove(key: String) { ensurePrefs(); prefs?.edit()?.remove(key)?.apply() }
}
