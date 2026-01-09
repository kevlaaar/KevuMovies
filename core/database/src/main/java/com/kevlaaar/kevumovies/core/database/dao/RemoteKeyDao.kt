package com.kevlaaar.kevumovies.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.kevlaaar.kevumovies.core.database.entity.RemoteKeyEntity

@Dao
interface RemoteKeyDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRemoteKey(remoteKey: RemoteKeyEntity)

    @Query("SELECT * FROM remote_keys WHERE category = :category")
    suspend fun getRemoteKeyByCategory(category: String): RemoteKeyEntity?

    @Query("DELETE FROM remote_keys WHERE category = :category")
    suspend fun deleteRemoteKeyByCategory(category: String)

    @Query("DELETE FROM remote_keys")
    suspend fun clearAllRemoteKeys()
}