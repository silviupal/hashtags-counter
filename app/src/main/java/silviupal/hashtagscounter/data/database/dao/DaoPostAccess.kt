package silviupal.hashtagscounter.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import silviupal.hashtagscounter.data.database.entities.PostEntity

/**
 * Created by Silviu Pal on 4/7/2019.
 */
@Dao
interface DaoPostAccess {
    @Insert
    fun insertOneItem(post: PostEntity)

    @Update
    fun updateOneItem(post: PostEntity)

    @Delete
    fun deleteOneItem(post: PostEntity)

    @Query("SELECT * FROM PostEntity WHERE id = :itemId")
    fun getListItemById(itemId: Int): PostEntity?

    @Query("SELECT * FROM PostEntity")
    fun getAllItems(): List<PostEntity>
}