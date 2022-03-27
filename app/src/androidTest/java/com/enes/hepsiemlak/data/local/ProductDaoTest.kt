package com.enes.hepsiemlak.data.local


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.enes.hepsiemlak.db.AppDatabase
import com.enes.hepsiemlak.db.dao.ProductDao
import com.enes.hepsiemlak.db.entity.ProductEntity
import com.enes.hepsiemlak.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@HiltAndroidTest
@SmallTest
class ProductDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("product_db")
    lateinit var database: AppDatabase
    private lateinit var dao: ProductDao

    @Before
    fun setup() {
        hiltRule.inject()
        dao = database.productDao()
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertProductItem() = runBlockingTest {
        val productItem = ProductEntity(1, "TL", "url", "Diş Macunu","23.40",1)
        dao.insert(productItem)
        val allProductItems = dao.getProductList().getOrAwaitValue()
        assertThat(allProductItems).contains(productItem)
    }

    @Test
    fun deleteProductItem() = runBlockingTest {
        val productItem = ProductEntity(1, "TL", "url", "Diş Macunu","23.40",1)
        dao.insert(productItem)
        dao.delete(productItem)

        val allProductItems = dao.getProductList().getOrAwaitValue()
        assertThat(allProductItems).doesNotContain(productItem)
    }

    @Test
    fun updateProductItem() = runBlockingTest {
        val productItem = ProductEntity(1, "TL", "url", "Diş Macunu","23.40",1)
        val productItem2 = ProductEntity(1, "TL", "url", "Diş Macunu","23.40",5)
        dao.insert(productItem)
        dao.insert(productItem2)
        val allProductItems = dao.getProductList().getOrAwaitValue()
        assertThat(allProductItems[0].amount).isEqualTo(5)
    }

    @Test
    fun searchProductItem() = runBlockingTest {
        val productItem = ProductEntity(1, "TL", "url", "Diş Macunu","23.40",1)
        dao.insert(productItem)
        val searchProductItems = dao.getSearchProduct(productItem.productId).getOrAwaitValue()
        assertThat(searchProductItems[0].productId).isEqualTo(productItem.productId)

    }
}