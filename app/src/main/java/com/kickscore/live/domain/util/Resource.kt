/*
 * Concept derived from reference project (permission dated 2025-01-13).
 * Implementation © 2025 Kev. See NOTICE.md for details.
 */

package com.kickscore.live.domain.util

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null) : Resource<T>(data)

    val isLoading: Boolean get() = this is Loading
    val isSuccess: Boolean get() = this is Success
    val isError: Boolean get() = this is Error
}

// Extension functions for easier usage
inline fun <T> Resource<T>.onSuccess(action: (value: T) -> Unit): Resource<T> {
    if (this is Resource.Success) action(data!!)
    return this
}

inline fun <T> Resource<T>.onError(action: (message: String?) -> Unit): Resource<T> {
    if (this is Resource.Error) action(message)
    return this
}

inline fun <T> Resource<T>.onLoading(action: () -> Unit): Resource<T> {
    if (this is Resource.Loading) action()
    return this
}