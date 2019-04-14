package silviupal.hashtagscounter.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import silviupal.hashtagscounter.database.entities.HashtagEntity

/**
 * Created by Silviu Pal on 4/12/2019.
 */
@Dao
interface DaoHashtagAccess {
    @Insert
    fun insertOneItem(hashtag: HashtagEntity)

    @Update
    fun updateOneItem(hashtag: HashtagEntity)

    @Delete
    fun deleteOneItem(hashtag: HashtagEntity)

    @Query("SELECT * FROM HashtagEntity WHERE id = :itemId")
    fun getHashtagById(itemId: Int): HashtagEntity?

    @Query("SELECT * FROM HashtagEntity")
    fun getAllItems(): List<HashtagEntity>
}