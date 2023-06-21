package io.github.sp0rk.bitask.feature.commit.domain

import androidx.paging.PagingSource
import io.github.sp0rk.data.db.dao.CommitDao
import io.github.sp0rk.data.db.entity.Commit
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.Test
import kotlin.test.assertEquals

class CommitPagingSourceFactoryTest {
    private val repositoryId = 0L

    private val pagingSource = mockk<PagingSource<Int, Commit>>()
    private val dao = mockk<CommitDao> {
        every { pagingSource(any()) } returns pagingSource
    }

    private val sut = CommitPagingSourceFactory(dao)

    @Test
    fun `WHEN invoked THEN call dao`() {
        val result = sut.invoke(repositoryId)

        assertEquals(pagingSource, result)
        verify { dao.pagingSource(repositoryId) }
    }
}
