package silviupal.hashtagscounter.database

import androidx.room.RoomDatabase
import androidx.room.Database
import androidx.room.Room
import silviupal.hashtagscounter.App
import silviupal.hashtagscounter.database.dao.DaoListItem
import silviupal.hashtagscounter.database.entities.ListItemEntity
import silviupal.hashtagscounter.database.entities.ListItemModel

/**
 * Created by Silviu Pal on 4/7/2019.
 */
@Database(entities = [ListItemEntity::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun daoListItem(): DaoListItem

    companion object {
        val DATABASE_NAME: String = "hashtags_manager_db"
        val database = Room.databaseBuilder(App.instance.applicationContext, MyDatabase::class.java, DATABASE_NAME)
            .build()

    }
}