package io.github.sp0rk.bitask.feature.commit.domain.model

/**
 * Simple Result implementation as Kotlin's Result implementation
 * being an inline class messes with mocks in tests
 */
sealed class Result<T> {
    data class Success<T>(val value: T) : Result<T>()
    data class Failure<T>(val error: Throwable) : Result<T>()
}
