package me.rei_m.hbfavkotlin.events

import me.rei_m.hbfavkotlin.utils.BookmarkUtil

public class EntryCategoryChangedEvent(val type: BookmarkUtil.Companion.EntryType,
                                       val target: EntryCategoryChangedEvent.Companion.Target) {
    companion object {
        public enum class Target {
            HOT,
            NEW
        }
    }
}