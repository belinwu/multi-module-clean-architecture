package org.kumnan.aos.apps.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.kumnan.aos.apps.data.entity.UnsplashResponse
import org.kumnan.aos.apps.data.network.UnsplashService
import org.kumnan.aos.apps.data.repository.datasource.KtorUnsplashPagingSource
import org.kumnan.aos.apps.domain.entity.UnsplashPhoto
import org.kumnan.aos.apps.domain.entity.status.Result
import org.kumnan.aos.apps.domain.repository.UnsplashRepository
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class KtorUnsplashRepositoryImpl @Inject constructor(
    private val unsplashService: UnsplashService,
    private val responseToPhotoList: (Result<UnsplashResponse>) -> Result<List<UnsplashPhoto>>
) : UnsplashRepository {

    override fun getSearchResultOfPage(
        query: String,
        page: Int
    ): Flow<Result<List<UnsplashPhoto>>> = flow {
        val response = unsplashService.searchPhotos(query, page)
        emit(responseToPhotoList(response))
    }

    override fun <T> getSearchResult(query: String): Flow<T> =
        Pager(
            config = PagingConfig(
                pageSize = 20,
                maxSize = 100,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { KtorUnsplashPagingSource(unsplashService, query) }
        ).flow as Flow<T>
}