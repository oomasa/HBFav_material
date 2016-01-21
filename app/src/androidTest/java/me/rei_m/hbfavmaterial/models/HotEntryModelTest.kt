package me.rei_m.hbfavmaterial.models

import com.squareup.otto.Subscribe
import junit.framework.TestCase
import me.rei_m.hbfavmaterial.events.EventBusHolder
import me.rei_m.hbfavmaterial.events.network.HotEntryLoadedEvent
import me.rei_m.hbfavmaterial.events.network.LoadedEventStatus
import me.rei_m.hbfavmaterial.repositories.MockEntryErrorRepository
import me.rei_m.hbfavmaterial.repositories.MockEntryRepository
import me.rei_m.hbfavmaterial.utils.BookmarkUtil
import org.hamcrest.CoreMatchers.`is`
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

class HotEntryModelTest : TestCase() {

    lateinit private var entryRepository: MockEntryRepository
    lateinit private var entryErrorRepository: MockEntryErrorRepository

    @Before
    public override fun setUp() {
        super.setUp()
        entryRepository = MockEntryRepository()
        entryErrorRepository = MockEntryErrorRepository()
    }

    @After
    public override fun tearDown() {

    }

    @Test
    fun testFetch() {

        val eventCatcher = EventCatcher()

        EventBusHolder.EVENT_BUS.register(eventCatcher)

        val hotEntryModel = HotEntryModel(entryRepository)

        // 最初は0件
        Assert.assertThat(hotEntryModel.entryList.size, `is`(0))

        // Busyでない
        Assert.assertThat(hotEntryModel.isBusy, `is`(false))

        // 取得開始
        eventCatcher.initCountDown()
        hotEntryModel.fetch(BookmarkUtil.Companion.EntryType.ALL)

        // リクエスト中はBusy
        Assert.assertThat(hotEntryModel.isBusy, `is`(true))
        eventCatcher.startCountDown()

        // 取得したあとは25件取れている
        Assert.assertThat(eventCatcher.event.status, `is`(LoadedEventStatus.OK))
        Assert.assertThat(hotEntryModel.entryList.size, `is`(25))
        Assert.assertThat(hotEntryModel.entryType, `is`(BookmarkUtil.Companion.EntryType.ALL))

        // 違うカテゴリで取得し直す
        eventCatcher.initCountDown()
        hotEntryModel.fetch(BookmarkUtil.Companion.EntryType.ENTERTAINMENT)
        eventCatcher.startCountDown()

        Assert.assertThat(eventCatcher.event.status, `is`(LoadedEventStatus.OK))
        Assert.assertThat(hotEntryModel.entryList.size, `is`(25))
        Assert.assertThat(hotEntryModel.entryType, `is`(BookmarkUtil.Companion.EntryType.ENTERTAINMENT))

        EventBusHolder.EVENT_BUS.unregister(eventCatcher)
    }

    @Test
    fun testFetchError() {

        val eventCatcher = EventCatcher()

        EventBusHolder.EVENT_BUS.register(eventCatcher)

        val hotEntryModel = HotEntryModel(entryErrorRepository)

        // エラーの場合
        eventCatcher.initCountDown()
        hotEntryModel.fetch(BookmarkUtil.Companion.EntryType.ALL)
        eventCatcher.startCountDown()
        Assert.assertThat(eventCatcher.event.status, `is`(LoadedEventStatus.ERROR))

        EventBusHolder.EVENT_BUS.unregister(eventCatcher)
    }

    private class EventCatcher() {

        lateinit var event: HotEntryLoadedEvent

        lateinit private var countDownLatch: CountDownLatch

        fun initCountDown() {
            countDownLatch = CountDownLatch(1)
        }

        fun startCountDown() {
            countDownLatch.await(10, TimeUnit.SECONDS)
        }

        @Subscribe
        fun subscribe(e: HotEntryLoadedEvent) {
            event = e
            countDownLatch.countDown()
        }
    }
}