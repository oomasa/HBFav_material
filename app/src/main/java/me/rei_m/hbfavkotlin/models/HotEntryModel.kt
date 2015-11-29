package me.rei_m.hbfavkotlin.models

import me.rei_m.hbfavkotlin.entities.EntryEntity
import me.rei_m.hbfavkotlin.events.EventBusHolder
import me.rei_m.hbfavkotlin.events.HotEntryLoadedEvent
import me.rei_m.hbfavkotlin.network.HotEntryRss
import me.rei_m.hbfavkotlin.utils.BookmarkUtil.Companion.EntryType
import rx.Observer
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import java.util.*
import me.rei_m.hbfavkotlin.events.HotEntryLoadedEvent.Companion.Type as EventType

public class HotEntryModel {

    public var isBusy = false
        private set

    public val entryList = ArrayList<EntryEntity>()

    public var entryType = EntryType.ALL
        private set

    public fun fetch(entryType: EntryType) {

        if (isBusy) {
            return
        }

        isBusy = true

        val listFromRss = ArrayList<EntryEntity>()

        val observer = object : Observer<EntryEntity> {
            override fun onNext(t: EntryEntity?) {
                listFromRss.add(t!!)
            }

            override fun onCompleted() {
                entryList.clear()
                entryList.addAll(listFromRss)
                this@HotEntryModel.entryType = entryType
                EventBusHolder.EVENT_BUS.post(HotEntryLoadedEvent(EventType.COMPLETE))
            }

            override fun onError(e: Throwable?) {
                EventBusHolder.EVENT_BUS.post(HotEntryLoadedEvent(EventType.ERROR))
            }
        }

        HotEntryRss().request(entryType)
                .onBackpressureBuffer()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .finallyDo({
                    isBusy = false
                })
                .subscribe(observer)
    }
}