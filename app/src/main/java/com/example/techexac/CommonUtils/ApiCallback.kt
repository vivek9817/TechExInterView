package com.example.techexac.CommonUtils


/**
 * Will be used for getting callbacks from api calls
 */
data class ApiCallback<out T>(
    val status: Utlis.ApiStatus, val data: T?, val message: String?
) {
    /* every state-wise ui handling needs to be done only in the views, i.e activities or fragments */
    companion object {

        /**
         * On success state callback
         */
        fun <T> onSuccess(data: T): ApiCallback<T> = ApiCallback(
            status = Utlis.ApiStatus.SUCCESS, data = data, message = null
        )

        /**
         * On failure/ error state callback
         */
        fun <T> onFailure(data: T?, message: String? = "Something went wrong"): ApiCallback<T> =
            ApiCallback(
                status = Utlis.ApiStatus.FAILURE, data = data, message = message
            )

        /**
         * loading state callback
         */
        fun <T> onLoading(data: T): ApiCallback<T> = ApiCallback(
            status = Utlis.ApiStatus.LOADING, data = null, message = null
        )
    }
}