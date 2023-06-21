package io.github.sp0rk.bitask

import android.app.Application
import io.github.sp0rk.bitask.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class BITaskApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@BITaskApplication)
            modules(appModule())
        }
    }
}
