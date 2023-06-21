package io.github.sp0rk.bitask.test

import androidx.room.RoomDatabase
import androidx.room.withTransaction
import io.github.sp0rk.data.db.entity.CommitRemoteKeys
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coInvoke
import io.mockk.mockkStatic

fun mockTransactionsInDb(testCase: Any, db: RoomDatabase) {
    MockKAnnotations.init(testCase)

    mockkStatic(
        "androidx.room.RoomDatabaseKt"
    )

    coEvery {
        db.withTransaction(captureCoroutine<suspend () -> CommitRemoteKeys?>())
    } coAnswers {
        coroutine<suspend () -> CommitRemoteKeys?>().coInvoke()
    }
}
