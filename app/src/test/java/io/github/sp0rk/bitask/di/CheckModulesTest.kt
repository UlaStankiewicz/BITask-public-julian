package io.github.sp0rk.bitask.di

import io.mockk.mockk
import org.junit.Test
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.koinApplication
import org.koin.test.KoinTest
import org.koin.test.check.checkModules

class CheckModulesTest : KoinTest {
    @Test
    fun `verify Koin app`() {
        koinApplication {
            androidContext(mockk(relaxed = true))
            modules(appModule())
            checkModules()
        }
    }
}
