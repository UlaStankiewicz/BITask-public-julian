package io.github.sp0rk.bitask.feature.commit.domain

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import io.github.sp0rk.data.db.entity.Commit
import io.github.sp0rk.data.db.entity.Repository
import retrofit2.HttpException
import java.io.IOException
import java.io.InvalidObjectException

@OptIn(ExperimentalPagingApi::class)
class CommitRemoteMediator(
    private val getCommitsUseCase: GetCommitsUseCase,
    private val upsertCommitsForPageUseCase: UpsertCommitsForPageUseCase,
    private val getClosestRemoteKeyUseCase: GetClosestRemoteKeyUseCase,
    private val getRemoteKeyForLastItemUseCase: GetRemoteKeyForLastItemUseCase,
    private val repository: Repository,
) : RemoteMediator<Int, Commit>() {

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Commit>): MediatorResult {
        return try {
            val page = calculatePage(loadType, state) ?: return MediatorResult.Success(true)

            val response = getCommitsUseCase(repository.path, page, state.config.pageSize)
            val commits = response.body()?.map { it.toEntity(repository.id) }

            commits?.let {
                val endOfPaginationReached = upsertCommitsForPageUseCase(
                    commits,
                    state.config.pageSize,
                    page,
                    loadType == LoadType.REFRESH,
                    repository.id
                )
                MediatorResult.Success(endOfPaginationReached)
            } ?: MediatorResult.Error(IOException(response.code().toString()))
        } catch (e: IOException) {
            MediatorResult.Error(e)
        } catch (e: HttpException) {
            MediatorResult.Error(e)
        }
    }

    /**
     * @return page to load, null, if no page is needed
     */
    private suspend fun calculatePage(loadType: LoadType, state: PagingState<Int, Commit>): Int? =
        when (loadType) {
            LoadType.REFRESH -> {
                val remoteKeys = getClosestRemoteKeyUseCase(state)
                remoteKeys?.nextKey?.minus(1) ?: 0
            }

            LoadType.APPEND -> {
                val remoteKeys = getRemoteKeyForLastItemUseCase(state)
                    ?: throw InvalidObjectException("Result is empty")
                remoteKeys.nextKey
            }

            LoadType.PREPEND -> {
                null
            }
        }
}
