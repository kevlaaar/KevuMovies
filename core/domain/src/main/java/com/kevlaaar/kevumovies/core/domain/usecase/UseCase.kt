package com.kevlaaar.kevumovies.core.domain.usecase

import com.kevlaaar.kevumovies.core.common.result.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn

abstract class FlowUseCase<in P, R>(private val dispatcher: CoroutineDispatcher) {
    operator fun invoke(parameters: P): Flow<Result<R>> = execute(parameters)
        .catch { e -> emit(Result.Error(e))}
        .flowOn(dispatcher)

    protected abstract fun execute (parameters: P): Flow<Result<R>>
}

abstract class NoParamFlowUseCase<R>(private val dispatcher: CoroutineDispatcher) {
    operator fun invoke(): Flow<Result<R>> = execute()
        .catch { e -> emit(Result.Error(e)) }
        .flowOn(dispatcher)

    protected abstract fun execute(): Flow<Result<R>>
}