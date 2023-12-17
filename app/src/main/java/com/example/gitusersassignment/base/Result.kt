package com.example.gitusersassignment.base

/**
 * Sealed class to represent the status of the API execution
 */
sealed class ResultStatus {
    data object Loading : ResultStatus()
    data object Success : ResultStatus()
    data object Error : ResultStatus()
}

/**
 * Result wrapper class to wrap the api responses in a structured way
 */
class Result<T> private constructor(
    val status: ResultStatus,
    val data: T?,
    val error: Throwable?,

    ) {

    companion object {
        fun <T> loading(): Result<T> = Result(ResultStatus.Loading, null, null)
        fun <T> success(data: T): Result<T> = Result(ResultStatus.Success, data, null)
        fun <T> error(e: Throwable): Result<T> = Result(ResultStatus.Error, null, e)
    }

}