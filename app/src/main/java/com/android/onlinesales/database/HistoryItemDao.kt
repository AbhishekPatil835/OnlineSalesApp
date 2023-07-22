package com.android.onlinesales.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.android.onlinesales.repository.HistoryItem

@Dao
interface HistoryItemDao {
    @Insert
    fun insertHistoryItem(historyItem: HistoryItem): Long

    @Query("SELECT * FROM history_items ORDER BY submission_date DESC")
    fun getAllHistoryItems(): List<HistoryItem>
}
