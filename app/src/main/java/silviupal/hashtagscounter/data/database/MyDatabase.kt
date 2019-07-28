package silviupal.hashtagscounter.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import silviupal.hashtagscounter.MyApp
import silviupal.hashtagscounter.data.database.dao.DaoHashtagAccess
import silviupal.hashtagscounter.data.database.dao.DaoPostAccess
import silviupal.hashtagscounter.data.database.entities.HashtagEntity
import silviupal.hashtagscounter.data.database.entities.PostEntity

/**
 * Created by Silviu Pal on 4/7/2019.
 */
@Database(entities = [PostEntity::class, HashtagEntity::class], version = 1, exportSchema = false)
abstract class MyDatabase : RoomDatabase() {
    abstract fun daoPost(): DaoPostAccess

    abstract fun daoHashtag(): DaoHashtagAccess

    companion object {
        private const val DATABASE_NAME: String = "hashtags_manager_db"

        /*
        Example of Migration - https://medium.com/androiddevelopers/understanding-migrations-with-room-f01e04b07929
        val migration12 = object: Migration(1,2){
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE ListItemEntity RENAME TO PostEntity")
            }
        }*/

        val database = Room.databaseBuilder(MyApp.instance.applicationContext, MyDatabase::class.java,
            DATABASE_NAME)
            //.addMigrations(migration12)
            .build()

    }
}