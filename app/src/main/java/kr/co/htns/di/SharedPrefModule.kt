package  kr.co.htns.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import kr.co.htns.PREFERENCES_FILE_KEY
import kr.co.htns.util.ShardPrefService
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module

val SharedPrefModule = module {
   single { provideSettingsPreferences(androidApplication()) }
   single { ShardPrefService(get()) }
}



private fun provideSettingsPreferences(app: Application): SharedPreferences =
    app.getSharedPreferences(PREFERENCES_FILE_KEY, Context.MODE_PRIVATE)
