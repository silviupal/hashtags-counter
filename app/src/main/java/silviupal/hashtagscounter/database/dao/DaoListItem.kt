package silviupal.hashtagscounter.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import silviupal.hashtagscounter.database.entities.ListItemEntity

/**
 * Created by Silviu Pal on 4/7/2019.
 */
@Dao
interface DaoListItem {
    @Insert
    fun insertOneItem(listItem: ListItemEntity)

    @Insert
    fun insertMultipleItems(listItems: List<ListItemEntity>)

    @Update
    fun updateOneItem(listItem: ListItemEntity)

    @Delete
    fun deleteOneItem(listItem: ListItemEntity)

    @Query("SELECT * FROM ListItemEntity WHERE id = :itemId")
    fun getListItemById(itemId: Int): ListItemEntity?
}