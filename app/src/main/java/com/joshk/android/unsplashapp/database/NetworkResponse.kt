package com.joshk.android.unsplashapp.database

sealed class NetworkResponse<out T> {
    class Success<out T>(val data:T): NetworkResponse<T>()
    class Error(val message: String) : NetworkResponse<Nothing>()
    object Loading : NetworkResponse<Nothing>()
}
