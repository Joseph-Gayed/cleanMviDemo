package com.jo.core.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import java.util.*

class LocaleManager {
    companion object {
        private const val LANGUAGE_KEY = "CHOOSE_LANGUAGE"
        const val LANGUAGE_AR = "ar"
        const val LANGUAGE_EN = "en"
        val deviceLanguage by lazy { Locale.getDefault().language }

        fun setLocale(c: Context, prefs: SharedPreferences): Context {
            val savedLanguage = getLanguage(c, prefs)
            return updateResources(c, savedLanguage)
        }

        fun setNewLocale(c: Context, language: String, prefs: SharedPreferences): Context {
            persistLanguage(c, language, prefs)
            return updateResources(c, language)
        }

        fun setNewLocale(c: Context, newLocale: Locale, prefs: SharedPreferences): Context {
            persistLanguage(c, newLocale.toString(), prefs)
            return updateResources(c, newLocale)
        }

        fun getLanguage(c: Context, prefs: SharedPreferences): String {
            return if (prefs.contains(LANGUAGE_KEY)) {
                prefs.getString(LANGUAGE_KEY, deviceLanguage) ?: deviceLanguage
            } else deviceLanguage
        }

        @SuppressLint("ApplySharedPref", "CommitPrefEdits")
        private fun persistLanguage(c: Context, language: String, prefs: SharedPreferences) {
            prefs.edit().putString(LANGUAGE_KEY, language).commit()
        }

        private fun updateResources(context: Context, language: String): Context {
            val locale = Locale(language)
            Locale.setDefault(locale)
            return updateResources(context, locale)
        }


        private fun updateResources(context: Context, locale: Locale): Context {
            val res = context.resources
            val config = Configuration(res.configuration)
            config.setLocale(locale)
            return context.createConfigurationContext(config)
        }

        fun getLocale(res: Resources): Locale {
            val config = res.configuration
            return if (Build.VERSION.SDK_INT >= 24) config.locales.get(0) else config.locale
        }

        fun getSavedLocale(c: Context, prefs: SharedPreferences): Locale {
            val savedLanguage = getLanguage(c, prefs)
            return Locale(savedLanguage)
        }
    }
}